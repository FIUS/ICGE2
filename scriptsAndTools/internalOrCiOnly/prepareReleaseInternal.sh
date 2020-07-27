#!/usr/bin/env bash

# This is used by prepareRelase.sh
# $1 must be --i-know-what-i-am-doing
# $2 is the version to bump to.
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

if ! git checkout -b "$branchName" ;then
  fail "Could not checkout new branch" 21
fi

if ! bash "$dir/doVersionBumpInPoms.sh" "--i-know-what-i-am-doing" "$version" ;then
  fail "Could not bump version" 22
fi

if ! git commit -m "Bump version to $version" "**/pom.xml" ;then
  fail "Could not commit" 23
fi

if ! git tag "$version" ;then
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

if ! git commit -m "Prepare for development on $version" "**/pom.xml" ;then
  fail "Could not commit the second tome" 23
fi
