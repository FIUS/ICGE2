#!/usr/bin/env bash

# This script checks that all java files have the correct file comment.
# No arguments allowed.

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

script="$dir/internalOrCiOnly/checkSingleJavaFileComment.sh"

correctHeaderFile="$dir/correctJavaFileComment.txt"

if ! [ -e "$correctHeaderFile" ] ;then
  fail "Cannot find the file with the correct header: $correctHeaderFile"
fi

failedFile="$dir/headerFailed"
rm -f "$failedFile"

#Check all headers
find . -iname "*.java" -not -path "./.*" -exec sh -c "if ! \"$script\" \"--i-know-what-i-am-doing\" \"{}\" \"$correctHeaderFile\" ;then touch \"$failedFile\" ;fi"  \;

if [ -e "$failedFile" ] ;then
  rm "$failedFile"
  fail "Not all headers correct" 11
fi
