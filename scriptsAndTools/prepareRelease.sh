#!/usr/bin/env bash

# This script prepares a release of the project.
# This script is interactive. No arguments allowed.
# This script changes the version in all poms to the requested version and commits and tags it.
# Then it bumps the version again. This time to the next version after the requested appended with -Snapshot.

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

function confirm() {
    # call with a prompt string or use a default
    read -r -p "${1:-Are you sure? [Y/N]} " response
    case "$response" in
        [yY][eE][sS]|[yY]|[jJ]) 
            true
            ;;
        [nN][oO]|[nN])
            false
            ;;
        *)
            confirm "$@"
    esac
}


if [ $# -ne 0 ] ; then
  fail "No argument expected." 2
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 3
fi

if [ "$(git status -s)" != "" ] ;then
  fail "Detected uncomitted changes" 4
fi

if [ "$(git branch --show-current)" != "master" ] ;then
  fail "Not on master branch." 5
fi

git fetch >/dev/null 2>&1

if ! git diff HEAD FETCH_HEAD --exit-code > /dev/null ;then
  fail "Branch is not up to date." 6
fi

if ! [ -e "pom.xml" ] ;then
  fail "No pom detected, Are you in root of repo?" 7
fi

lastRelease="$(git tag --list | head -n 1)"
currentVersion="$(xmlstarlet sel -t -v "/_:project/_:version" "pom.xml")"

expectedNextVersion=""

if [[ "$currentVersion" == *"-Snapshot" ]] ;then
  expectedNextVersion="${currentVersion%-Snapshot}"
fi

prompt="Please enter the version to prepare the release for"

echo "The last release seems to be $lastRelease"
echo "The current version is $currentVersion"

if [ "$expectedNextVersion" != "" ] ;then
  echo "The expected version to prepare the release for is: $expectedNextVersion"
  prompt="$prompt [$expectedNextVersion]"
else
  echo "Could not determine which version to bump to."
fi

read -p "$prompt: " version
version=${version:-$expectedNextVersion}

echo "Will prepare to release version $version."
echo "This will checkout a new branch, do some changes and commit them."

confirm "Start? [Y/N]" || fail "Aborting." 11

push="false"
delete="false"

confirm "Should the new branch be pushed?" && push="true"
confirm "Should master be checked out and the branch be deleted?" || delete="true"

bash "$dir/internalOrCiOnly/prepareReleaseInternal.sh" "--i-know-what-i-am-doing" "$version" "$push" "$delete"







