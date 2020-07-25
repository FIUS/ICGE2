#!/usr/bin/env bash

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

