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

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  fail "Cannot version bump on a PR" 3
fi

echo "Checking if need to do version bump."

version="$(xmlstarlet sel -t -v "/_:project/_:version" "pom.xml")"
msg="$(git log -1 --pretty=%B)"

branchC="true"
versionC="true"
msgC="true"
msgF="false"

if [[ "$TRAVIS_BRANCH" != "versionBump/"* ]] ;then
  branchC="false"
fi
if [[ "$version" == *"Snapshot" ]] ;then
  versionC="false"
fi
if [[ "$msg" != "Bump version to "* ]] ;then
  msgC="false"
fi
if [[ "$msg" == "Prepare for develoment on"* ]];then
  msgF="true"
fi

if [ "$branchC" == "true" ] && [ "$versionC" == "true" ] && [ "$msgC" == "true" ] ;then
  echo "Doing version bump."
elif [ "$branchC" == "false" ] && [ "$versionC" == "false" ] && [ "$msgC" == "false" ] ;then
  echo "Not doing version bump."
  exit 0
elif [ "$branchC" == "true" ] && [ "$versionC" == "false" ] && [ "$msgF" == "true" ] ;then
  echo "Detected finished version bump. Not doing it again."
  exit 0
else
  echo "Version bump indicators don't match!"
  echo "Branch name: $TRAVIS_BRANCH"
  echo "  -> $branchC"
  echo "Artifact version: $version"
  echo "  -> $versionC"
  echo "Last commit msg: $msg"
  echo "  -> $msgC"
  fail "Not doing version bump." 5
fi

versionFirstPart="${version%.*}"
versionLastPart="${version##*.}"
versionLastPartIncreased="$(expr $versionLastPart + 1)"
newVersion="$versionFirstPart.$versionLastPartIncreased-Snapshot"

mvn -s "$dir/m2settings.xml" deploy -Drepo.login=$REPO_LOGIN -Drepo.pwd=$REPO_PWD

set -x

git tag "$version"

"$dir/versionBumpLocal.sh" "$newVersion"
git commit -a -m "Prepare for develoment on $newVersion"

source "$dir/prepareGitForPush.sh"

git push origin "HEAD:$TRAVIS_BRANCH"
git push origin "$version"

