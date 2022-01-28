# RefactoringAwareMergingEvaluation

In RefactoringAwareMergingEvaluation, we perform a quantitative comparison between operation-based refactoring-aware merging (implemented in RefMerge) and 
graph-based refactoring-aware merging (implemented in IntelliMerge). Afterwards, we dive deeper into the results by manually sampling 50 merge scenarios and 
investigate the conflicts that Git, RefMerge, and IntelliMerge report as well as what their causes are. This evaluation was used in the paper, "A Systematic Comparison of Two Refactoring-aware Merging Techniques" (http://arxiv.org/abs/2112.10370).

## System requirements
* Linux
* git
* Java 11
* IntelliJ (Community Addition) Version 2020.1.2

## How to run

### 1. Clone and build RefactoringMiner 
Use `Git Clone https://github.com/tsantalis/RefactoringMiner.git` to clone RefactoringMiner. 
Then build RefactoringMiner with `./gradlew distzip`. It will be under build/distributions.

### 2. Add RefactoringMiner to your local maven repository
You will need to add RefactoringMiner to your local maven repository to
use it in the build.gradle. You can use `mvn install:install-file -Dfile=<path-to-file>`
to add it to your local maven repository. You can verify that it's been installed 
by checking the path `/home/username/.m2/repository/org/refactoringminer`.

### 3. Populate databases
Use the refactoring analysis dump found [here](https://github.com/ualberta-smr/refactoring-analysis-results)
to populate the original_analysis database. Use the database/intelliMerge_data1
sql dump to populate intelliMerge_data1 database. Use the database/refactoringAwareMerging_dataset
to populate refactoringAwareMerging_dataset database. A description of how to populate the databases can be found 
[here](https://github.com/ualberta-smr/RefactoringAwareMergingEvaluation/wiki/Datasets).

## IntelliMerge Replication

### Using IntelliMerge

This project comes with the version of IntelliMerge that we used to run the IntelliMerge replication, 
found in our fork of IntelliMerge, https://github.com/max-ellis/IntelliMerge.git, at commit `5966f75`.

### Get IntelliMerge replication commits
Run `python intelliMerge_data_resolver` to get the IntelliMerge commits used in
the IntelliMerge replication. 

### Edit configuration
Edit the configuration tasks to have `:runIde` and include set `-Pmode=` to `replication`. 

## RefactoringAwareMerging Comparison

### Using IntelliMerge and RefMerge

This project comes with the versions of IntelliMerge and RefMerge used in the paper. The source code for 
the version of IntelliMerge we used is the same as that in our IntelliMerge replication.
 If you would like to use a different version of IntelliMerge, build the respective IntelliMerge version 
and copy and paste that version into the lib folder.

RefMerge's history can be found here: https://github.com/ualberta-smr/RefMerge.git. The
version used within this evaluation is release 1.0.0 in commit `adb13ff`.
If you would like to use a different version of
RefMerge, you first need to clone RefMerge. After you clone RefMerge, copy the code in
`ca.ualberta.cs.smr.refmerge` and replace the code in the same package within this project.

### Edit configuration
Edit the configuration tasks to have `:runIde` and include set `-Pmode=` to `comparison`.
Then, set `-PevaluationProject=` to the project that you want to evaluate on. For example,
it would look like `-PevaluationProject=error-prone` if you want to evaluate on error-prone.


### Running RQ1 experiment

To replicate RQ1, first run `python project_sampler` to get the additional 10 projects used 
in the evaluation. Then, run `python refMerge_data_resolver` to get the commits with
refactoring-related conflicts.

Put the `refMerge_evaluation_commits` file in the resources folder and the first file to
the configuration, for example `-PevaluationProject=error-prone`. Clone that project
and open it with the IntelliJ IDEA. Wait for IntelliJ to build the project, close it, and
run the evaluation. Repeat this for each project. 

Finally, use the scripts in evaluation_data_plotter to get tables and plots from the data.

In the event that the evaluation pipeline crashes, rerunning it will continue from the 
place it crashed at. 

### Running RQ2 experiment

First, use stats/evaluation_data_resolver to get the list of merge commit ids to sample. 
Then use the query `SELECT * FROM merge_commit WHERE id = x;` to get the parent commits.
Use `git merge-base p1 p2` to get the base commit and use `SELECT distinct path 
FROM conflict WHERE merge_commit_id = x;` to get a list of the file paths. From there, 
investigate the code region in the base commit, left parent, and right parent to determine
if the conflicting region should be a conflict. Investigate the same region for all three
tools to determine the discrepancies. 

### Results

The zip file, database/refactoringAwareMerging_results.zip, contains the results from
 running the evaluation pipeline for RQ1. A description of how to use these results can be 
 found [here](https://github.com/ualberta-smr/RefactoringAwareMergingEvaluation/wiki/Datasets).
results/manual_sampling_results has the results for RQ2. 
