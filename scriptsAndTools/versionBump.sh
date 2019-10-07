#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 1 ] ; then
  fail "Need the new version" 2
fi

if [ "$(git status -s)" != "" ] ;then
  fail "Detected uncomitted changes" 3
fi

if [ "$(git branch --show-current)" != "master" ] ;then
  fail "Not on master branch." 4
fi

if ! [ -e "pom.xml" ] ;then
  fail "No pom detcted, Are you in root of repo?" 5
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 6
fi

branchName="versionBump/$1"

if ! git checkout -b "$branchName" ;then
  fail "Could not checkout new branch" 7
fi

if ! "$dir/versionBumpLocal.sh" "$1" ;then
  fail "Could not bump verison" 8
fi

git commit -a -m "Bump version to $1"
git push --set-upstream origin "$branchName"
git checkout master
