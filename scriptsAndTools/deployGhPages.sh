#!/usr/bin/env bash
dir="$(dirname "$(realpath "$0")")"

source "$dir/prepareGitForPush.sh"

git clone --depth 1 -b gh-pages git@github.com:FIUS/JVK-2019.git gh-pages

mvn javadoc:aggregate -pl '!ICGE-build-tools,!ICGE-ManualStart'
cp -r target/site/apidocs/ gh-pages

cd gh-pages
git add --all
git commit -m "Update apidoc"
git push
