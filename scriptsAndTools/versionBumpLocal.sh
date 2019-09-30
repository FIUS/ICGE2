#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 1 ] ; then
  fail "Need the new version" 2
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 3
fi

#In the main pom update the main project version
xmlstarlet edit -P -L -u "/_:project/_:version" -v "$1" "pom.xml"

#In sub projects and the template update the parent version
find -iname "pom.xml" -ipath "./ICGE*" -not -samefile "./ICGE-archetype-module/pom.xml" -exec xmlstarlet edit -P -L -u "/_:project/_:parent/_:version" -v "$1" "{}" \;

#In all poms update icge.version
find . -iname "pom.xml" -exec xmlstarlet edit -P -L -u "/_:project/_:properties/_:icge.version" -v "$1" "{}" \;
