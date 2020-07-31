#!/usr/bin/env bash

# This script checks the format of all poms.
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

if ! which diff >/dev/null 2>&1 ;then
  fail "Need program diff" 4
fi

failedFile="$dir/pomFailed"
rm -f "$failedFile"
operation='diff "{}" "{}.2" || touch "'$failedFile'" ; rm "{}.2"'

bash "$dir/internalOrCiOnly/formatPomsAndRunOperation.sh" "--i-know-what-i-am-doing" "$operation"

if [ -e "$failedFile" ] ;then
  rm "$failedFile"
  fail "The format of at least one pom file is wrong." 21
fi

echo "Pom format looking good"
