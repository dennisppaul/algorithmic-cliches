#!/bin/sh

LIB_NAME=$1
ROOT=$(pwd)

SRC=$ROOT/../../../netbeans/dist/algorithmiccliches.jar
DST=$ROOT/../processing-library/algorithmiccliches/library

if [ -d "$DST" ]; then
	rm -rf "$DST"
fi
mkdir -p "$DST"

cp "$SRC" "$DST"
