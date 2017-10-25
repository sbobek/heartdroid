#!/bin/sh
cd $(cd -P -- "$(dirname -- "$0")" && pwd -P)
PARSER_FILENAME="HMRParser"
SEMANTICS_FILENAME="HMRSemantics"
GRAMMAR_PATH="HMRGrammar.peg"
java -cp generator.jar:.  mouse.Generate -G "$GRAMMAR_PATH" -S "$SEMANTICS_FILENAME" -P "$PARSER_FILENAME" 
javac -cp generator.jar:. "$PARSER_FILENAME.java" "$SEMANTICS_FILENAME.java"
java -cp generator.jar:. mouse.TryParser -P "$PARSER_FILENAME"