#!/bin/sh

LIB_NAME=$1
ROOT=$(pwd)

SRC=$1/../../../netbeans/dist/algorithmiccliches.jar
DST=$1/../processing-library/algorithmiccliches/library

SRC=/Users/dennisppaul/dev/tools/netbeans/tools/dist/tools.jar
DST=$ROOT/../processing-library/$LIB_NAME/library/

if [ -d "$DST" ]; then
	rm -rf "$DST"
fi
mkdir -p "$DST"

cp "$SRC" "$DST"
