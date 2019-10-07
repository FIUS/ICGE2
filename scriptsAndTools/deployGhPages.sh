#!/usr/bin/env bash
dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [[ "$TRAVIS_BRANCH" != "master" ]] ;then
  fail "Not on master" 1
fi

source "$dir/prepareGitForPush.sh"

git clone --depth 1 -b gh-pages git@github.com:FIUS/ICGE2.git gh-pages

mvn javadoc:aggregate -pl '!ICGE-build-tools,!ICGE-ManualStart'
cp -r target/site/apidocs/ gh-pages

cd gh-pages
git add --all
git commit -m "Update apidoc"
git push
