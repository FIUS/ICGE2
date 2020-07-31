#!/usr/bin/env bash

# This is used by prepareRelaseInternal.sh
# $1 must be --i-know-what-i-am-doing
# $2 is the version to bump to.

dir="$(dirname "$(realpath "$0")")"

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

#In the main pom update the main project version
xmlstarlet edit -P -L -u "/_:project/_:version" -v "$2" "pom.xml"

#In sub projects and the template update the parent version
find -iname "pom.xml" -ipath "./ICGE*" -not -samefile "./ICGE-archetype-module/pom.xml" -exec xmlstarlet edit -P -L -u "/_:project/_:parent/_:version" -v "$2" "{}" \;

#In all poms update icge.version
find . -iname "pom.xml" -exec xmlstarlet edit -P -L -u "/_:project/_:properties/_:icge.version" -v "$2" "{}" \;
