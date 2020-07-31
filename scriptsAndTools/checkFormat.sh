#!/usr/bin/env bash

# This is the main check script which runs all other checks in this directory.
# The CI runs this script and additionally mvn javadoc:validate
#
# Usually there should not be any arguments.
# There may be one single argument -v. In this case the output of the maven commands is not voided.
#
# The script has some required programs, which it checks for.
# As this script calls checkJavaFileComments the file with the correct comment must exist.
# (This is currently not checked by this script)


dir="$(dirname "$(realpath "$0")")"

mvn="$dir/../mvnw"

function fail {
  echo $1
  exit $2
}

if [ ! "$1" == "-v" ] && [ $# -ne 0 ] ; then
  fail "No argument expected (or single -v)" 2
fi

if ! [ -x "$mvn" ] ;then
  fail "Mavenwrapper missing" 3
fi

if ! which xmlstarlet >/dev/null 2>&1 ;then
  fail "Need program xmlstarlet" 5
fi

if ! which diff >/dev/null 2>&1 ;then
  fail "Need program diff" 6
fi

if ! "$dir/checkPomFormat.sh" ;then
  fail "The format of a pom is wrong." 21
fi

if [ "$1" == "-v" ] ;then
  "$mvn" -B -f ICGE-build-tools install

  if ! "$mvn" -B formatter:validate ;then
    fail "The format of a java file is wrong. See mvn formatter:validate" 22
  fi
else
  "$mvn" -B -f ICGE-build-tools install > /dev/null

  if ! "$mvn" -B formatter:validate >/dev/null ;then
    fail "The format of a java file is wrong. See mvn formatter:validate" 22
  fi
fi

if ! "$dir/checkJavaFileComments.sh" ;then
  fail "A source file header is wrong" 23
fi

echo "All looking good."
