#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

operation="$2"

#Do for all poms
find . -iname "pom.xml" -exec sh -c 'xmlstarlet fo -s 4 -e utf8 "{}" > "{}.2" ;'"$operation" \;

