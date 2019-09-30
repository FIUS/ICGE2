#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

#Fix all headers
find . -iname "*.java" -not -path "./.*" -exec "$dir/fixHeader.sh" "{}"  \;
