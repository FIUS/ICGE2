#!/usr/bin/env bash

# This is used by checkPomFormat and fixPomFormat.
# $1 must be --i-know-what-i-am-doing
# $2 is an operation to perform after formatting.
# This script searches all pom.xml files and formats them.
# The result of formatting is not saved to the same file but to <filename>.2 instead.
# Then the given operation is run. The operation is run in a find -exec context, so {} is the filename.
# One operation might be to move the .2 file back to the original file.

dir="$(dirname "$(realpath "$0")")"

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

operation="$2"

#Do for all poms
find . -iname "pom.xml" -exec sh -c 'xmlstarlet fo -s 4 -e utf8 "{}" > "{}.2" ;'"$operation" \;

