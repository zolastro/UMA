#!/bin/bash

while getopt h opt; do
	case $opt in
	-h|--help) echo "script5(1)				Assignment on Bourne Shell data processing				script5(1)

NAME
       script5 - adds the number of lines of code in each file of a given directory

SYNOPSIS
       script5 [OPTION]... [DIRECTORY]...

DESCRIPTION
       Echoes the number of lines of code in each file from a given directory.
       -e EXTENSION
		changes the extension searched
       -h
		displays this help window

       -n
		displays each .h file in the current directory along with its number of lines
" | less; shift ;;
	-n) for file in $(find `pwd` -type f -name "*.h"); do wc -l $file; done | less;;
	-e) SUM=0; for line in $(for file in $(find /usr/include -type f -name "*.$2"); do wc -l $file; done | awk '{print $1}'); do SUM=$((SUM+line)); done && echo $SUM;;
	esac
done
SUM=0; for line in $(for file in `find $1 -type f -name "*.h"`; do wc -l $file; done | awk '{print $1}'); do SUM=$((SUM+line)); done && echo $SUM


