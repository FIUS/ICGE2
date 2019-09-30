#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 3
fi

#Format all poms
find . -iname "pom.xml" -exec sh -c 'xmlstarlet fo -s 4 -e utf8 "{}" > "{}.2" ;mv "{}.2" "{}"' \;
