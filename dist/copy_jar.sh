#!/bin/sh

SRC=$1/../../../netbeans/dist/algorithmiccliches.jar
DST=$1/../processing-library/algorithmiccliches/library

if [ -d "$DST" ]; then
	rm -rf "$DST"
fi
mkdir -p "$DST"

cp "$SRC" "$DST"