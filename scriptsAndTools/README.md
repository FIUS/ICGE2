# Scripts and Tools
We use some tools and various scripts to help with the development process.

## Tools
### Maven
The most important tool we use is [maven](https://maven.apache.org/). It helps us define projects, their dependencies, how to build them and much more.

Instead of using the command `mvn` we use the maven wrapper `mvnw` in the root of the repository. To use it make sure you are in the root of the repo and run `./mvnw`.
In this documentation the command `mvn` is used, because it is shorter, but you can and should use the maven wrapper instead.

Useful maven commands:
| Command | Description |
| --- | --- |
| `mvn clean install` | Cleans, builds and installs the project to the local maven repository.
| `mvn javadoc:javadoc` | Check if all rules for the javadoc comments are respected.
| `mvn formatter:format` | Format the java source code.

## Scripts to be used by developers
You can use the following scripts during your development.
Only use the scripts in `internalOrCiOnly` if you know what you are doing.

These scripts are written for bash and only tested with bash.

All non-internal scripts will fail if a requirement or argument is missing
If there are any unexpected maven errors, try `mvn clean install`  and then run the script again. 

All of the scripts expect to be run from the root directory of this repository. \
 **So always cd to the git root first and use `./scriptsAndTools/<script>.sh`!**


| Name | Description | Requirements | Arguments | Script Saftey
| --- | --- | --- |  --- | --- |
| `checkFormat.sh` | Checks all basic format requirements. Does not check javadoc validity (use `mvn javadoc:javadoc` for that) | `xmlstarlet` and `diff` | none or single `-v` | read only
| `checkJavaFileComments.sh` | Checks the file comments of all java files. Included in `checkFormat.sh` | none | none | read only
| `checkPomFormat.sh` | Checks the format of the poms. Included in `checkFormat.sh` | `xmlstarlet` and `diff` | none | writes temporary files in working dir and deletes them. Uses existing file names with `.2` appended for the tmp files.
| `fixJavaFileComments.sh` | Replaces the first part of each java file in the repo with the correctHeader. | none | none | writes to working dir
| `fixPomFormat.sh` | Formats poms to match the correct format used for poms by this project. | `xmlstarlet` | none | writes to working dir
| `prepareRelease.sh` | Prepares a new release by increasing the version, tagging and increasing the version to the next snapshot-version. | `xmlstarlet` and `git`; no uncommited changes, beeing on an up to date master branch; being able to interact with the script | none | writes to working dir and commits. Optionally pushes and deletes branch.

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
This way the development can safeley continue afterwards. This is also committed.
When the Github Release is created, a Github Worflow is started, which deploys the project at the state of the tag to a maven repository.