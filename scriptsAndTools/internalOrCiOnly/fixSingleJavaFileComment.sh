#!/usr/bin/env

# This is used by fixJavaFileComments.
# $1 must be --i-know-what-i-am-doing
# $2 is the file to fix.
# $3 must be a file with the correct header
# Fixes the header of the given file. Does only work if one of the above seperators is in the file.
# Replaces everything above the seperator with the correct header.
# Fails with exit code 1 if no seperator is found.


SEPERATOR_P="package de.unistuttgart.informatik.fius.icge"
SEPERATOR_M="module de.unistuttgart.informatik.fius.icge"
SEPERATOR_OM="open module de.unistuttgart.informatik.fius.icge"

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if ! [ "$1" == "--i-know-what-i-am-doing" ] ;then
  exit 91
fi

replace=$(cat "$3")

searchP="*$SEPERATOR_P"
replaceP=$(printf "$replace\n$SEPERATOR_P")

searchM="*$SEPERATOR_M"
replaceM=$(printf "$replace\n$SEPERATOR_M")

searchOM="*$SEPERATOR_OM"
replaceOM=$(printf "$replace\n$SEPERATOR_OM")

buffer=$(cat "$2")
bufLen=${#buffer}

bufferP="${buffer##$searchP}"
bufLenP=${#bufferP}

bufferM="${buffer##$searchM}"
bufLenM=${#bufferM}

bufferOM="${buffer##$searchOM}"
bufLenOM=${#bufferOM}


result=""

if [ $bufLenP -lt $bufLen ] ;then
  result="$replaceP$bufferP"
elif [ $bufLenOM -lt $bufLen ] ;then
  result="$replaceOM$bufferOM" 
elif [ $bufLenM -lt $bufLen ] ;then
  result="$replaceM$bufferM"
else
  fail "Could not find sperator in $2" 1
fi

echo "$result" > "$2"
