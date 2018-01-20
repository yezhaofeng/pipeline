#! /bin/bash
find . "(" -name "*.java" -or -name "*.xml" -or -name "*.properties" -or -name "*.js" -or -name "*.css" -or -name "*.jsp"  -or -name "*.html" ")" -print | xargs wc -l
