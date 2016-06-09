# Context project by goto fail;
[![Build Status](https://travis-ci.org/bartdejonge1996/goto-fail.png)](https://travis-ci.org/bartdejonge1996/goto-fail) [![Coverage Status](https://coveralls.io/repos/github/bartdejonge1996/goto-fail/badge.svg?branch=coveralls)](https://coveralls.io/github/bartdejonge1996/goto-fail?branch=coveralls)

This is the Context project of the team 'goto fail;'. This project aims to improve the workflow and overall productivity of the scripting and production at 'Polycast'.

## Other repository
This repository contains the scripting application of the project, the webserver/webapp part can be found at `https://github.com/bartdejonge1996/goto-fail-webserver`.

## Tests, checkstyle and coverage
Our tests can be run using the maven command `mvn test`. Checkstyle can be run using the maven command `mvn validate`. We have an integration for coveralls.io, this calculates the coverage percentage of the repository. Coverage can also be calculated using EclEmma, running `mvn jacoco:report` or the built-in coverage tool in IntelliJ.

## Starting the application
The application can be started by running the `MainClass.java`.

## Lombok and annotation processing
This project uses lombok, an annotation processor for java. This annotation processor needs to be enabled in your settings. This can be done using the following steps:

### IntelliJ
In IntelliJ, this can be done in your settings under: `Build, Execution, Deployment > Compiler > Annotation Processors`. You only need to click the checkbox at the top of the page. The setting is named: `Enable annotation processing`. For the final step, you will need to install the IntelliJ lombok plugin. This can be done under: `Settings > Plugins > Browse repositories...`.

### Eclipse
In Eclipse this can also be done. If you are interested, please let us know. We'll happily provide instructions if this is necessary.

## JRE Version
The minimum required JRE version is 1.8.0_40

## Git hooks
To add git hooks necessary for this project run:
```shell
chmod +x bin/git/init-hooks.sh
bin/git/init-hooks.sh
```

## Branch naming conventions
As for the branch names we will be using during the project, the branch names are supposed to not have capital letters and no wierd punctuation or signs. The branch names are supposed to be simple and clear. The name should be descriptive of what will be implemented, added or fixed in the branch. Every word in the branch should be seperated by a '-'. Some examples of good names for branches would be: `feature-timeline-autoscroll`, `fix-timeline-word-wrap`.
