#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

script="$dir/internalOrCiOnly/checkSingleJavaFileComment.sh"

correctHeaderFile="$dir/correctHeader.txt"

failedFile="$dir/headerFailed"
rm -f "$failedFile"

#Check all headers
find . -iname "*.java" -not -path "./.*" -exec sh -c "if ! \"$script\" \"--i-know-what-i-am-doing\" \"{}\" \"$correctHeaderFile\" ;then touch \"$failedFile\" ;fi"  \;

if [ -e "$failedFile" ] ;then
  rm "$failedFile"
  fail "Not all headers correct" 11
fi
