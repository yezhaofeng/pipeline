#! /bin/bash
find . "(" -name "*.java" -or -name "*.xml" -or -name "*.properties" ")" -print | xargs wc -l
