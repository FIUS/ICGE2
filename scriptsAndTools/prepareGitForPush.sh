#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

eval "$(ssh-agent -s)"
ssh-add "$dir/deployKey"

git remote set-url origin git@github.com:FIUS/ICGE2.git
