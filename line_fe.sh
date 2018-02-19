#! /bin/bash
find . "("  -name "*.js" -or -name "*.css" -or -name "*.jsp"  -or -name "*.html" ")" -print | xargs wc -l
