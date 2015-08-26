#!/bin/bash

javac TextBuddy.java &&
java TextBuddy experimental.txt < testinput.txt > output.txt &&
cmp expected.txt output.txt


