#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 3
fi

if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  fail "Not a PR" 3
fi

version="$(xmlstarlet sel -t -v "/_:project/_:version" "../pom.xml")"
msg="$(git log -1 --pretty=%B)"

if [ "$version" == "*-Snapshot" ] || [ "$msg" == "Bump version to *" ] ;then
  #No version bump
  exit 0
fi

echo "Doing version bump."

versionFirstPart="${version%.*}"
versionLastPart="${version##*.}"
eval "versionLastPartIncreased=$versionLastPart + 1"
newVersion="$versionFirstPart.$versionLastPartIncreased-Snapshot"

mvn deploy -Drepo.login=$REPO_LOGIN -Drepo.pwd=$REPO_PWD

git tag "$version"

"$dir/versionBumpLocal.sh" "$newVersion"
git commit -a -m "Bump version to $newVersion"

git push
git push "$version"
