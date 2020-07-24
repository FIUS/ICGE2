#!/usr/bin/env bash

dir="$(dirname "$(realpath "$0")")"

function fail {
  echo $1
  exit $2
}

if [ $# -ne 0 ] ; then
  fail "No argument expected" 2
fi

if ! which git >/dev/null 2>&1 ;then
  fail "Need program git" 3
fi

if ! which mvn >/dev/null 2>&1 ;then
  fail "Need program mvn" 4
fi

if ! "$dir/formatPoms.sh" ;then
  fail "Failed to format poms" 5
fi

if [ ! "$(git status -s **/pom.xml)" == "" ] ;then
  git diff
  fail "The format of a pom is wrong." 11
fi

if ! mvn compile formatter:validate ;then
  fail "The format of a java file is wrong" 12
fi

if ! "$dir/verifySourceFileHeaders.sh" ;then
  fail "The source file headers are wrong" 13
fi
