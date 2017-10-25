#!/bin/sh
cd $(cd -P -- "$(dirname -- "$0")" && pwd -P)
rm "$HMR_PARSER_FILENAME".java "$HMR_SEMANTICS_FILENAME".java
java -cp generator.jar:. mouse.Generate -r "$HMR_RUNTIME_NAME" -G "$HMR_GRAMMAR_PATH" -D "$HMR_TARGET_DIR" -S "$HMR_SEMANTICS_FILENAME" -p "$HMR_PACKAGE_NAME" "$@" -P "$HMR_PARSER_FILENAME" 
