#!/usr/bin/env bash

# This script fixes the format of all poms.
# No arguments allowed

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

operation='mv "{}.2" "{}"'

bash "$dir/internalOrCiOnly/formatPomsAndRunOperation.sh" "--i-know-what-i-am-doing" "$operation"
