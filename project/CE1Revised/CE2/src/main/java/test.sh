#!/bin/bash

javac TextBuddy.java &&
java TextBuddy $1 < testinput.txt > output.txt &&
cmp expected.txt output.txt


