#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ ! "$1" == "-v" ] && [ $# -ne 0 ] ; then
  fail "No argument expected (or single -v)" 2
fi

if ! which git >/dev/null 2>&1 ;then
  fail "Need program git" 3
fi

if ! which mvn >/dev/null 2>&1 ;then
  fail "Need program mvn" 4
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 5
fi

if ! which diff >/dev/null 2>&1 ;then
  fail "Need preogram diff" 6
fi

if ! "$dir/checkPomFormat.sh" ;then
  fail "The format of a pom is wrong." 21
fi

if [ "$1" == "-v" ] ;then
  mvn -B -f ICGE-build-tools install

  if ! mvn -B formatter:validate ;then
    fail "The format of a java file is wrong. See mvn formatter:validate" 22
  fi
else
  mvn -B -f ICGE-build-tools install > /dev/null

  if ! mvn -B formatter:validate >/dev/null ;then
    fail "The format of a java file is wrong. See mvn formatter:validate" 22
  fi
fi

if ! "$dir/checkJavaFileComments.sh" ;then
  fail "A source file header is wrong" 23
fi
