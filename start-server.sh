#!/bin/sh

JARPATH=`find lib -name '*.jar'`
MYCLASSPATH=""
for FILE in $JARPATH; do 
  MYCLASSPATH=$MYCLASSPATH:$FILE
done
MYCLASSPATH=$MYCLASSPATH:`pwd`/target/classes
java -cp "$MYCLASSPATH" se.umu.cs.umume.rest.Main $@
