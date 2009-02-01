#!bin/bash
scp ../doc/*.html ${1}@shell.sourceforge.net:/home/groups/a/an/antforms/htdocs/
scp ../doc/images/*.* ${1}@shell.sourceforge.net:/home/groups/a/an/antforms/htdocs/images
scp ../doc/style/*.css ${1}@shell.sourceforge.net:/home/groups/a/an/antforms/htdocs/style

