# Scripts and Tools

Many of these scripts expect to be run from the root directory of this repository.
So use `$ ./scriptsAndTools/<script>.sh`

Most scripts will fail if an argument is missing

If there are any unexpected maven errors, try `mvn clean install` then run the script again. 


## Scripts to be used by developers
Only use the other scripts if you know what you are doing.

| Name | Description |
| --- | --- |
| `checkFormat.sh` | Checks all basic format requirements. Does not check javadoc validity (use `mvn javadoc:javadoc` for that)
| `checkjavaFileComments.sh` | Checks the file comments of all java files. Included in `checkFormat.sh`
| `checkPomFormat.sh` | Checks the format of the poms. Included in `checkFormat.sh`
| `fixJavaFileComments.sh` | Replaces the first part of each java file in the repo with the correctHeader.
| `fixPomFormat.sh` | Formats the poms according to the requirements.
| `prepareRelease.sh` | Prepares a new release by increasing the version, tagging and increasing the version to the next snapshot-version.
