# Refactoring-aware Merging Evaluation

This repository contains the evaluation setup and results for our TSE submission titled "Refactoring-aware Operation-based Merging: An Empirical Evaluation", also available on [arXiv](http://arxiv.org/abs/2112.10370).

We perform a quantitative evaluation between operation-based refactoring-aware merging (implemented in [RefMerge](https://github.com/ualberta-smr/RefMerge)) and 
graph-based refactoring-aware merging (implemented in [IntelliMerge](https://github.com/Symbolk/IntelliMerge)). Afterwards, we dive deeper into the results by manually sampling 50 merge scenarios and 
investigate the conflicts that Git, RefMerge, and IntelliMerge report as well as what their causes are. 

## Note about RefMerge

We implemented operation-based rerfactoring-aware merging in [RefMerge](https://github.com/ualberta-smr/RefMerge). RefMerge works by undoing refactorings, merging, and then replaying the refactorings. When merging, RefMerge considers the interactions between each pair of refactorings and how these interactions can lead to a conflict or how they can result in a dependence relationship. The RefMerge repo provides detailed documentaiton on how it works. We provide the conflict and dependence detection logic for each pair in the [conflict detection wiki](https://github.com/ualberta-smr/RefMerge/wiki/Conflict-&-Dependence-Logic). 

## System requirements

* Linux
* git
* Java 11
* IntelliJ (Community Addition) Version 2020.1.2

## Setup Steps for Running The Evaluation

### 1. Clone and build RefactoringMiner 
Use `git clone https://github.com/tsantalis/RefactoringMiner.git` to clone RefactoringMiner. 
Then build RefactoringMiner with `./gradlew distzip`. It will be under build/distributions.

### 2. Add RefactoringMiner to your local maven repository
You will need to add RefactoringMiner to your local maven repository to
use it in the build.gradle. You can use `mvn install:install-file -Dfile=<path-to-file>`
to add it to your local maven repository. You can verify that it's been installed 
by checking the path `/home/username/.m2/repository/org/refactoringminer`.

### 3. Populate databases

You next need to create and populate the databases needed to run this evaluation. These databases contain the information of the 20 projects we evaluate on. A detailed description of these databases and how to use the provided DB can be found 
[here](https://github.com/ualberta-smr/RefactoringAwareMergingEvaluation/wiki/Datasets).

There are three databases, all provided in the `database/` folder:

1. `intelliMerge_data1`: This database contains the 10 projects and merge scenarios used in IntelliMerge's evaluation.
2. `original_analysis`: This is data collected by running the refactoring in merge scenarios pipeline developed by Mahmoudi et al., SANER '19. We use this data to select 10 additional projects for our evaluation. 
Use the refactoring analysis dump found [here](https://github.com/ualberta-smr/refactoring-analysis-results)
3. `database/refactoringAwareMerging_dataset`: This database ???

### 4. Configure IntelliMerge and RefMerge

This evaluation repository already comes with the versions of IntelliMerge and RefMerge we use in the paper:

- Our [fork of IntelliMerge](https://github.com/max-ellis/IntelliMerge/tree/evaluation), at commit [5966f75](https://github.com/max-ellis/IntelliMerge/commit/5966f75). Note that we had to fix some issues in IntelliMerge to be able to reproduce their results. We document these in in a [wiki page](https://github.com/max-ellis/IntelliMerge/wiki/Details) in our fork.
- [RefMerge](https://github.com/ualberta-smr/RefMerge), [release 1.0.0](https://github.com/ualberta-smr/RefMerge/releases/tag/1.0.0)

If you would like to use different versions than provided:

- For IntelliMerge, build the respective IntelliMerge version and include the resulting jar in the `lib` folder for this repository.
- For RefMerge: After you clone and checkout the desired version of RefMerge you want to use, copy the code in `ca.ualberta.cs.smr.refmerge` and replace the code in the `ca.ualberta.cs.smr.refmerge` package within this project (found [here](https://github.com/ualberta-smr/RefactoringAwareMergingEvaluation/tree/master/src/main/java/ca/ualberta/cs/smr/refmerge)) with it.

### 5. Edit IntelliJ Configurations

This evaluation runs within IntelliJ. Edit the configuration tasks in the IntelliJ IDE under `Run | Edit Configurations` (more information can be found [here](https://www.jetbrains.com/help/idea/run-debug-configuration.html#create-permanent)) to have `:runIde` and include set `-Pmode=` to `comparison`.


## Reproducing RQ1

The projects we evaluated on can be found in the file `experiment_data/refMerge_evaluation_projects`. A list of merge scenarios and their corresponding projects that we evaluated on are located in `experiment_data/refMerge_evaluation_commits`.

### Running RQ1 experiment

To reproduce RQ1:

1. `cd stats/`
2. Run `python project_sampler.py` to get the additional 10 projects used 
in the evaluation. 
3. Run `python refMerge_data_resolver.py` to get the commits with
refactoring-related conflicts.
4. Put the `refMerge_evaluation_commits` file generated by `refMerge_data_resolver` in the `src/main/resources` folder. 

While running the evaluation, you have to manually load each project but after loading the project, the rest of the evaluation is automated. For each project, you will need to do the following:

1. Add the corresponding evaluation project to the configuration in the IntelliJ IDE under `Run | Edit Configurations`. For example, `-PevaluationProject=error-prone` to evaluate on error-prone.

2. Clone the corresponding evaluation project (e.g., error-prone).

3. Open the evaluation project with the IntelliJ IDEA in a new window. 

4. Wait for IntelliJ to build the cloned project, then close it.

5. Run the evaluation for the project by clicking the `Run` button in the IntelliJ IDE.

6. Wait for the evaluation pipeline to finish processing that project.

The data from the evaluation pipeline will be stored in the database, `refMerge_evaluation`. The evaluation pipeline will create the database if it does not already exist. Finally, use the scripts in `stats/evaluation_data_plotter.py` to get tables and plots from the produced data.

### RQ1 results

The zip file, `database/refactoringAwareMerging_results.zip`, contains our results from running the evaluation pipeline for RQ1. A description of how to use these results can be found [here](https://github.com/ualberta-smr/RefactoringAwareMergingEvaluation/wiki/Datasets).


## Reproducing RQ2

A list of merge scenarios and their corresponding projects that we used in RQ2 is stored in `experiment_data/rq2_scenarios`.

### Running RQ2 experiment

- `cd stats` and run `python evaluation_data_resolver.py` to get the list of merge commit ids to sample. This will always result in the same merge scenarios when given the same data.

This experiment is a manual analysis and we manually re-run the merge for each merge scenario. These are the steps that we follow for each merge scenario:

1. Query the results in RQ1 using `SELECT * FROM merge_commit WHERE id = x` where x is the merge commit ID. Use this query to get the parent commits.

2. Use `git merge-base p1 p2` in the corresponding project to get the base commit where p1 is the left parent commit and p2 is the right parent commit.

3. Use `SELECT distinct path FROM conflict WHERE merge_commit_id = x;` to get a list of the conflicting file paths.

4. Compare each conflicting region with the corresponding region in the base, left, and right commits.

5. Record if the region should be a merge conflict or not based on the base, left, and right commits. 

6. Record if the conflict reported by the merge tool is a true positive or false positive based on the previous step. Record why you think so.

7. Investigate the same region in the other merge tools and record what you find. 

8. Record any additional notes about the discrepancies you find, such as the reasons for the discrepancies. 

### RQ2 results

The overall results for our manual analysis are stored in `results/manual_sampling_results.csv`. This includes the results for each merge scenario as well as reasons that we think each conflict falls into its corresponding category. The results used to produce the Git table in RQ2 are stored in `results/git_table.csv`. The results used for the RefMerge and IntelliMerge tables are respectively stored in `results/refMerge_table.csv` and `results/intelliMerge.csv`.

## IntelliMerge Replication

We use the IntelliMerge Replication to reproduce the IntelliMerge results as is with only the 10 projects in their paper.

### Using IntelliMerge

This project comes with the version of IntelliMerge that we used to run the IntelliMerge replication, 
found in our fork of IntelliMerge, https://github.com/max-ellis/IntelliMerge.git, at commit `5966f75`.

### Get IntelliMerge replication commits
Run `python intelliMerge_data_resolver` to get the IntelliMerge commits used in
the IntelliMerge replication. 

### Edit configuration
Edit the configuration tasks to have `:runIde -Pmode=comparison -PdataPath=path 
-PevaluationProject=project`, where path is the path to the cloned test projects
and project is the test project.

