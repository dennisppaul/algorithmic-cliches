#!/bin/sh

source config.build

ROOT=$(pwd)

printJob()
{
	echo ""
	echo "################################"
	echo "# "$1
	echo "################################"
}

printJob "create folder"
sh $ROOT/create-folder.sh $LIB_NAME
printJob "copying jar"
sh $ROOT/copy_jar.sh $LIB_NAME
printJob "copying src"
sh $ROOT/copy_src.sh $LIB_NAME
printJob "copying README"
sh $ROOT/copy_readme.sh $LIB_NAME
printJob "creating processing sketches"
sh $ROOT/create-processing-sketches.sh $LIB_NAME
printJob "packing zip"
sh $ROOT/pack-zip.sh $LIB_NAME
printJob "done"


# TODO
# 
# * get rid off ROOT in other shellscripts
# * add more variables for 'creating-processing-...'