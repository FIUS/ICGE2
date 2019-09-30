#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 1 ] ; then
  fail "Need the file to check" 2
fi
headerSize="$(stat --printf="%s" "$dir/correctHeader.txt")"
if ! cmp -n "$headerSize" "$dir/correctHeader.txt" $1 >>/dev/null ;then
  fail "Header of $1 not correct." 11
fi

