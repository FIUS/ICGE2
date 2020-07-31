# Scripts and Tools

All of the scripts expect to be run from the root directory of this repository. \
 **So always cd to the git root first and use `./scriptsAndTools/<script>.sh`!**

All non-internal scripts will fail if an requirement or argument is missing

If there are any unexpected maven errors, try `mvn clean install`  and then run the script again. 


## Scripts to be used by developers
You can use the following scripts during your development.
Only use the scripts in `internalOrCiOnly` if you know what you are doing.

| Name | Description | Requirements | Arguments | Script Saftey
| --- | --- | --- |  --- | --- |
| `checkFormat.sh` | Checks all basic format requirements. Does not check javadoc validity (use `mvn javadoc:javadoc` for that) | `xmlstarlet` and `diff` | none or single `-v` | read only
| `checkJavaFileComments.sh` | Checks the file comments of all java files. Included in `checkFormat.sh` | none | none | read only
| `checkPomFormat.sh` | Checks the format of the poms. Included in `checkFormat.sh` | `xmlstarlet` and `diff` | none | writes temporary files in working dir and deletes them. Uses existing file names with `.2` appended for the tmp files.
| `fixJavaFileComments.sh` | Replaces the first part of each java file in the repo with the correctHeader. | none | none | writes to working dir
| `fixPomFormat.sh` | Formats the poms according to the requirements. | `xmlstarlet` | none | writes to working dir
| `prepareRelease.sh` | Prepares a new release by increasing the version, tagging and increasing the version to the next snapshot-version. | `xmlstarlet` and `git`; no uncommited changes, beeing on an up to
date master branch; being able to interact with the script | none | writes to working dir and commits. Optionally pushes and deletes branch.

## Examples

Checking the format:
```
/path/to/repo $ ./scriptsAndTools/checkFormat.sh
Pom format looking good
Java file comments looking good.
All looking good.
/path/to/repo $ 
```

Fixing the pom format:
```
/path/to/repo $ ./scriptsAndTools/fixPomFormat.sh
/path/to/repo $
```

Preparing a release:
```
/path/to/repo $ ./scriptsAndTools/prepareRelease.sh
The last release seems to be 2.3.1
The current version is 2.3.2-Snapshot
The expected version to prepare the release for is: 2.3.2
Please enter the version to prepare the release for [2.3.2]: 
Will prepare to release version 2.3.2.
This will checkout a new branch, do some changes and commit them.
Should the new branch and the tag be pushed? [Y/N] y
Should master be checked out and the branch be deleted? [Y/N] y
Start? [Y/N] y
Switched to a new branch 'versionBump/2.3.2'
Switched to branch 'master'
```

## Release workflow

To release a version of this project follow these steps:
 1. Checkout master
 2. Pull
 3. Make sure there are no uncomitted changes
 4. Run the script `prepareRelease` (see example above)
    1. If you didn't choose to autmatically push, push the new branch and the tags
 5. Create a pull request, have someone review it and merge the pull request
 6. Create a Github Release from the new tag.
 7. Wait for the Github Worflow to run.

### Explanation
The script changes the version in all poms to the version to release. Then it commits and tags this state.
Then it changes the version in all poms again. This time to the next version with the `-Snapshot` suffix. 
This way the development can safley continue afterwards. This is also committed.
When the Github Release is created, a Github Worflow is started, which deploys the project at the state of the tag to a maven repository.