#!/usr/bin/env bash

# This is used by checkJavaFileComments.
# $1 must be --i-know-what-i-am-doing
# $2 is the file to check.
# $3 must be a file with the correct header
# If the file to check does not start with the correct header this script fails with exit code 11

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

headerSize="$(stat --printf="%s" "$3")"
if ! cmp -n "$headerSize" $3 $2 >>/dev/null ;then
  fail "Header of $2 not correct." 11
fi

