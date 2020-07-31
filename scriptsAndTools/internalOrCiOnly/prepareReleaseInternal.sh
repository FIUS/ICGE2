#!/usr/bin/env bash

# This is used by prepareRelase.sh
# $1 must be --i-know-what-i-am-doing
# $2 is the version to bump to.
# $3 should the branch be pushed (string true or false)
# $4 should master be checked out and the branch be deleted (string true or false)
# This file does not do many checks and calls git commit. Be really careful when using it manually!

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

version="$2"
branchName="versionBump/$version"

if ! git checkout -b "$branchName" >/dev/null ;then
  fail "Could not checkout new branch" 21
fi

if ! bash "$dir/doVersionBumpInPoms.sh" "--i-know-what-i-am-doing" "$version" ;then
  fail "Could not bump version" 22
fi

if ! git commit -m "Bump version to $version" "**pom.xml" >/dev/null ;then
  fail "Could not commit" 23
fi

if ! git tag "$version" >/dev/null ;then
  fail "Could not tag" 24
fi

# Calculate new version after release
versionLastPart="${version##*.}"
versionLastPartIncreased="$(expr $versionLastPart + 1)"
versionWithoutLastPart="${version%$versionLastPart}"
newVersion="$versionWithoutLastPart$versionLastPartIncreased-Snapshot"

if ! bash "$dir/doVersionBumpInPoms.sh" "--i-know-what-i-am-doing" "$newVersion" ;then
  fail "Could not bump version the second time" 25
fi

if ! git commit -m "Prepare for development on $newVersion" "**pom.xml" >/dev/null ;then
  fail "Could not commit the second tome" 26
fi

if [ "$3" == "true" ] ;then
  git push --set-upstream origin "$branchName" >/dev/null 2>&1
  git push origin "$version" >/dev/null 2>&1

  if [ "$4" == "true" ] ;then
    git checkout master >/dev/null
    git branch -D "$branchName" >/dev/null
  fi
fi
