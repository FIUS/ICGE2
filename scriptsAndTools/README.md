# Scripts and Tools

Many of these scripts expect to be run from the root directory of this folder.
So use `$ ./scriptsAndTools/<script>.sh`

Most scripts will fail if an argument is missing

## Scripts to be used by developers
Don't use the other scripts, except if you know what you are doing.

| Name | Description |
| --- | --- |
| `verifyFormat.sh` | Checks all format requirements. `mvn clean install` before using it.
| `fixSourceFileHeaders.sh` | Replaces the first part of each java file in the repo with the correctHeader.
| `formatPoms.sh` | Formats the poms according to the requirements.
| `versionBump.sh` | Initiate a version bump and release.
