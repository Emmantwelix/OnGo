# The Incredible Coding Standards

**This document lists the programming standards of The Incredibles group work.**

## Writing Code

1. Each of our interface files will contain a comment block like this:
```java
//-----------------------------------------
// NAME: who wrote this code?
//
// REMARKS: What is the purpose of this interface?
//
//-----------------------------------------
```
2. Use CamelCase for wording.

3. Declare all variables at the beginning of a method.

4. Use constant identifiers for magic numbers and be all uppercase:
```java
PST_RATE = 1.07.
```
5. Avoid duplication of code. Every job should be implemented in one place.

6. Use indentation properly, and align else with if:
```java
if-while-else-etc 
{  
   stuff-inside   
   stuff-inside
}
```
7. Use meaningful but reasonable variable and method names. If a variable name does not completely describe the data it stoes, add a comment to its declaration with additional information
```java
calculateWorkoutDuration()
```
8. Layout Files (XML) will use ‘snake_case’ 
```bash
activity_main.xml
```

## Git and Branching
1. Branch shold follow this naming standard
```
feature/issue#-short-description
```
2. Commit messages shoud reference its issue number
```bash
git commit -m "WorkoutBuilder#15"
```
3. Require at least one Peer Review i.e. should be reviewed by at least one other person

4. Before merging to main make sure no dead code, remove unused imports or unnecessary comments, no unfinished TODOs, unit test must pass, and no warnings should be triggering

### Sample Git Workflow
1. Pull latest changes
```bash
git pull origin main
```
 
2. Create a branch: Name is based on the issueID from GitLab
```bash
git checkout -b feature/12-stub-workout
```
3. "To the point" commits
```bash
git commit -m “Implement getExercise in Stub #12”
```
4. Open a merge request to merge with main branch so it can be reviewed by another team member. Update the time and status of the repective issue on GitLab

## Architecture
1. Strictly forbid UI classes from accessing Database (Stub) directly. All data must pass through Logic/Domain layer

2. Packages should be organized by layer or feature
```
com.exergen.persistence
com.exergen.ui
```
 