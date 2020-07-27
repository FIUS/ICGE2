#!/usr/bin/env bash

# This script fixes all java file comments
# No arguments allowed

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

script="$dir/internalOrCiOnly/fixSingleJavaFileComment.sh"

correctHeaderFile="$dir/correctHeader.txt"

#Fix all headers
find . -iname "*.java" -not -path "./.*" -exec "\"$script\" \"--i-know-what-i-am-doing\" \"{}\" \"$correctHeaderFile\""  \;
