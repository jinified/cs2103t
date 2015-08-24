#!/bin/bash

javac CityConnect.java &&
java CityConnect < ../bin/input.txt > ../bin/output.txt &&
cmp ../bin/expected.txt ../bin/output.txt


