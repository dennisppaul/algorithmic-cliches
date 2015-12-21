#!/bin/sh

ROOT=$(pwd)
LIB_NAME=$1

find $ROOT/.. -name ".DS_Store" -print0 | xargs -0 rm -f
cd $ROOT/../processing-library/
zip --quiet -r $ROOT/../$LIB_NAME.zip ./$LIB_NAME
cd $ROOT/../dist/