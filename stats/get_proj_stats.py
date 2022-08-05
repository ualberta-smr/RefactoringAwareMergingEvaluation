
import pandas as pd
import matplotlib.pyplot as plt
import scipy as sci
import numpy as np
import seaborn as sns
import matplotlib.ticker as ticker

REFMERGE = "0.966"
GIT = "0.65"
INTELLIMERGE = "0.4"
#REFMERGE = str(0.8)
#GIT = str(0.5)
#INTELLIMERGE = str(0.3)
DISTANCE = 0.3

def set_box_color(bp, color, linestyle):
    plt.setp(bp['boxes'], color="black")
    plt.setp(bp['whiskers'], color="black")
    plt.setp(bp['caps'], color="black")
    plt.setp(bp['medians'], color="black")

    for patch in bp['boxes']:
        patch.set(facecolor=color)

#https://stackoverflow.com/questions/62630857/how-to-combine-two-boxplots-with-the-same-axes-into-one-boxplot-in-python
def plot_boxplot():
    data = pd.read_csv("DetailedProjStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['Conflict Blocks','Involved Conflict Blocks','Involved Refactorings'])
    fig, ax = plt.subplots(figsize=(15,8))

    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')

    projects = data.groupby(['Project Name'])

    labels = list(projects.groups.keys())

    #ax.set_ylabel("Number of Conflicting Files")

    plt.legend(loc="upper center", borderaxespad=0)

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=8)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_proj_stats.pdf')
    plt.clf()
    plt.cla()



def plot_boxplot_involved_refs_timeouts():
    data = pd.read_csv("DetailedTimedOutInvolvedRefsStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['RefMerge Timeout','IntelliMerge Timeout','No Timeout'])

    projects = data.groupby(['Project Name'])


    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')
    labels = list(projects.groups.keys())

    lgnd = plt.legend(loc="upper center", borderaxespad=0)
    lgnd.get_texts()[0].set_text('RefMerge')
    lgnd.get_texts()[1].set_text('IntelliMerge')
    lgnd.get_texts()[2].set_text('No Timeout')

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=8)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('involved_refs_timeouts.pdf')
    plt.clf()
    plt.cla()

    refMerge_data = data['RefMerge Timeout'].loc[data['RefMerge Timeout'] > 0]
    intelliMerge_data = data['IntelliMerge Timeout'].loc[data['IntelliMerge Timeout'] > 0]
    nto_data = data['No Timeout'].loc[data['No Timeout'] > 0]

    print('TIMEOUT INVOLVED REFS STATS')
    print('RefMerge Median: ', np.median(refMerge_data))
    print('IntelliMerge Median: ', np.median(intelliMerge_data))
    print('No Timeout Median: ', np.median(nto_data))


def plot_boxplot_conflicting_files_timeouts():
    data = pd.read_csv("DetailedTimedOutFileStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['RefMerge Timeout','IntelliMerge Timeout','No Timeout'])
    fig, ax = plt.subplots(figsize=(15,8))

    projects = data.groupby(['Project Name'])


    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')
    labels = list(projects.groups.keys())

    lgnd = plt.legend(loc="upper center", borderaxespad=0)
    lgnd.get_texts()[0].set_text('RefMerge Timeout Commits')
    lgnd.get_texts()[1].set_text('IntelliMerge Timeout Commits')
    lgnd.get_texts()[2].set_text('Commits with No Timeout')

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_file_timeout_boxplot.pdf')
    plt.clf()
    plt.cla()


    refMerge_data = data['RefMerge Timeout'].loc[data['RefMerge Timeout'] > 0]
    intelliMerge_data = data['IntelliMerge Timeout'].loc[data['IntelliMerge Timeout'] > 0]
    nto_data = data['No Timeout'].loc[data['No Timeout'] > 0]

    print('TIMEOUT FILE STATS')
    print('RefMerge Median: ', np.median(refMerge_data))
    print('IntelliMerge Median: ', np.median(intelliMerge_data))
    print('No Timeout Median: ', np.median(nto_data))


def plot_boxplot_commits_timeouts():
    data = pd.read_csv("DetailedTimedOutCommitStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['RefMerge Timeout','IntelliMerge Timeout','No Timeout'])
    fig, ax = plt.subplots(figsize=(15,8))

    projects = data.groupby(['Project Name'])


    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')
    labels = list(projects.groups.keys())

    lgnd = plt.legend(loc="upper center", borderaxespad=0)
    lgnd.get_texts()[0].set_text('RefMerge Timeout Commits')
    lgnd.get_texts()[1].set_text('IntelliMerge Timeout Commits')
    lgnd.get_texts()[2].set_text('Commits with No Timeout')

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.setp(ax.xaxis.get_majorticklabels(), rotation=-20, ha="left", rotation_mode="anchor")
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    axes = plt.gca()
    axes.xaxis.label.set_size(18)
    axes.yaxis.label.set_size(18)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_commits_timeout_boxplot.pdf')
    plt.clf()
    plt.cla()


def plot_boxplot_commits_timeouts_by_category():
    data = pd.read_csv("DetailedTimedOutCommitStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['RefMerge Timeout','IntelliMerge Timeout','No Timeout'])
    refMerge_data = data['RefMerge Timeout'].loc[data['RefMerge Timeout'] > 0]
    intelliMerge_data = data['IntelliMerge Timeout'].loc[data['IntelliMerge Timeout'] > 0]
    nto_data = data['No Timeout'].loc[data['No Timeout'] > 0]

    fig, ax = plt.subplots(figsize=(14,9))
    plots = [refMerge_data, nto_data, intelliMerge_data]
    labels = ['']
    bpr = plt.boxplot(refMerge_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4-0.7, sym='', widths=0.4)
    bpg = plt.boxplot(nto_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4, sym='', widths=0.4)
    bpi = plt.boxplot(intelliMerge_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4+0.7, sym='', widths=0.4)

    set_box_color(bpr, REFMERGE, "--")
    set_box_color(bpg, GIT, "-")
    set_box_color(bpi, INTELLIMERGE, ":")
    ax.set_xlabel('')




    plt.plot([], c=REFMERGE, label="RefMerge")
    plt.plot([], c=GIT, label='Git')
    plt.plot([], c=INTELLIMERGE, label='IntelliMerge')

    lgnd = ax.legend([bpr["boxes"][0], bpg["boxes"][0], bpi["boxes"][0]], ["RefMerge Timeouts", "No Timeouts", "IntelliMerge Timeouts"],
                    loc='lower center', bbox_to_anchor=(0.5, -0.35), frameon=False, fontsize=26, markerscale=100.0)

    plt.xticks(range(0, len(labels)), labels)
    plt.rc('xtick', labelsize=8)
    ax.set_ylabel('Commit History Length')
    ax.yaxis.label.set_size(32)
    ax.tick_params(axis='both', which='major', labelsize=32)
    plt.tick_params(axis = "x", which = "both", bottom = False)

    plt.tight_layout()
    plt.savefig('commits_by_timeout_category.pdf')

    print('TIMEOUT COMMITS STATS')
    print('RefMerge Median: ', np.median(refMerge_data))
    print('IntelliMerge Median: ', np.median(intelliMerge_data))
    print('No Timeout Median: ', np.median(nto_data))


def plot_boxplot_diff_files_timeouts_by_category():
    data = pd.read_csv("DetailedProjStats.csv")
    refMerge_data = data['Total Diff Files'].loc[data['RefMerge Timeout'] > 0]
    intelliMerge_data = data['Total Diff Files'].loc[data['IntelliMerge Timeout'] > 0]
    nto_data = data['Total Diff Files'].loc[data['No Timeout'] > 0]

    fig, ax = plt.subplots(figsize=(14,9))
    plots = [refMerge_data, nto_data, intelliMerge_data]
    labels = ['']
    bpr = plt.boxplot(refMerge_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4-0.7, sym='', widths=0.4)
    bpg = plt.boxplot(nto_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4, sym='', widths=0.4)
    bpi = plt.boxplot(intelliMerge_data, patch_artist=True, positions = np.array(range(len(labels)))*1.4+0.7, sym='', widths=0.4)

    set_box_color(bpr, REFMERGE, "--")
    set_box_color(bpg, GIT, "-")
    set_box_color(bpi, INTELLIMERGE, ":")
    ax.set_xlabel('')




    plt.plot([], c=REFMERGE, label="RefMerge")
    plt.plot([], c=GIT, label='Git')
    plt.plot([], c=INTELLIMERGE, label='IntelliMerge')

    lgnd = ax.legend([bpr["boxes"][0], bpg["boxes"][0], bpi["boxes"][0]], ["RefMerge Timeouts", "No Timeouts", "IntelliMerge Timeouts"],
                    loc='lower center', bbox_to_anchor=(0.5, -0.15), frameon=False, fontsize=24, markerscale=100.0, ncol=3)

    plt.xticks(range(0, len(labels)), labels)
    plt.rc('xtick', labelsize=8)

    ax.set_ylabel('Number of Changed Files')
    ax.yaxis.label.set_size(32)
    ax.tick_params(axis='both', which='major', labelsize=32)
    plt.tick_params(axis = "x", which = "both", bottom = False)

    plt.tight_layout()
    plt.savefig('diff_files_by_timeout_category.pdf')

    print('TIMEOUT DIFF FILE STATS')
    print('RefMerge Median: ', np.median(refMerge_data))
    print('IntelliMerge Median: ', np.median(intelliMerge_data))
    print('No Timeout Median: ', np.median(nto_data))


def plot_boxplot_intelliMerge_timeouts():
    df = pd.read_csv("DetailedTimedOutCommitStats.csv")
    data_grouped = df.groupby('Merge Tool')
    data = data_grouped.get_group('IntelliMerge')
    data.append(data_grouped.get_group('Both'))
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['Total Commits'])
    fig, ax = plt.subplots(figsize=(15,8))

    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')

    projects = data.groupby(['Project Name'])

    labels = list(projects.groups.keys())

    #ax.set_ylabel("Number of Conflicting Files")

    plt.legend(loc="upper center", borderaxespad=0)

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.setp(ax.xaxis.get_majorticklabels(), rotation=-20, ha="left", rotation_mode="anchor")
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    axes = plt.gca()
    axes.xaxis.label.set_size(18)
    axes.yaxis.label.set_size(18)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_intelliMerge_timeout_stats.pdf')
    plt.clf()
    plt.cla()

def plot_boxplot_refMerge_timeouts():
    df = pd.read_csv("DetailedTimedOutCommitStats.csv")
    data_grouped = df.groupby('Merge Tool')
    data = data_grouped.get_group('RefMerge')
    data.append(data_grouped.get_group('Both'))
    fig, ax = plt.subplots(figsize=(15,8))
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['Conflict Blocks','Involved Conflict Blocks','Involved Refactorings'])

    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')

    projects = data.groupby(['Project Name'])

    labels = list(projects.groups.keys())

    #ax.set_ylabel("Number of Conflicting Files")

    plt.legend(loc="upper center", borderaxespad=0)

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.setp(ax.xaxis.get_majorticklabels(), rotation=-20, ha="left", rotation_mode="anchor")
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    axes = plt.gca()
    axes.xaxis.label.set_size(18)
    axes.yaxis.label.set_size(18)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_refMerge_timeout_stats.pdf')
    plt.clf()
    plt.cla()


def plot_involved_ratio():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedProjStats.csv")
    fig, ax = plt.subplots(figsize=(15,8))
    sns.boxplot(x='Project Name', y='Involved Refactoring Ratio', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.setp(ax.xaxis.get_majorticklabels(), rotation=-20, ha="left", rotation_mode="anchor")
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    axes = plt.gca()
    axes.xaxis.label.set_size(18)
    axes.yaxis.label.set_size(18)
#    plt.rc('xtick', labelsize=10)
    fig.tight_layout()
    plt.savefig('involved_refactoring_ratio_block.pdf')
    plt.clf()

def plot_involved_refactorings():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedProjStats.csv")
    sns.boxplot(x='Project Name', y='Involved Refactorings', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.savefig('involved_refactoring.pdf')
    plt.clf()

def plot_number_of_commits():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedProjStats.csv")
    sns.boxplot(x='Project Name', y='Total Commits', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.savefig('commits_per_merge_scenario.pdf')
    plt.clf()


    grouped_data = data.groupby(data['Project Name'])

    print('TOTAL COMMITS STATS BY PROJECT')
    for group in grouped_data.groups:
        print(group, ': ', np.median(grouped_data.get_group(group)['Total Commits']))
        #print(group, ': ', np.median(group_data))


def plot_diff_files():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedProjStats.csv")

    grouped_data = data.groupby(by=["Project Name"])["Total Diff Files"].median().index
    grouped_data = data.groupby(["Project Name"])

    df = pd.DataFrame(columns=['Project Name','Median']) 
    for group in grouped_data.groups:
        refs = grouped_data.get_group(group)['Total Diff Files']
        df = df.append({'Project Name':group, 'Median':np.median(refs)}, ignore_index=True)

 
    ordered = df.sort_values(by="Median", ascending=True)['Project Name']




    sns.boxplot(x='Project Name', y='Total Diff Files', data=data, showfliers = False, order=ordered)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.savefig('diff_files_per_merge_scenario.pdf')
    plt.clf()


    grouped_data = data.groupby(data['Project Name'])

    print('TOTAL DIFF FILE STATS BY PROJECT')
    for group in grouped_data.groups:
        print(group, ': ', np.median(grouped_data.get_group(group)['Total Diff Files']))
        #print(group, ': ', np.median(group_data))




def plot_conflicting_files():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedProjStats.csv")
    fig, ax = plt.subplots(figsize=(15,8))
    sns.boxplot(x='Project Name', y='Conflicting Java Files', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.savefig('conflicting_files.pdf')
    plt.clf()

def plot_number_of_refactorings():
    #create boxplot for ratio alone
    data = pd.read_csv("TotalRefactoringsPerScenario.csv")
    data.loc[data['Project Name'] == 'platform_frameworks_support', 'Project Name'] = 'platform_fmwk_support'
    grouped_data = data.groupby(by=["Project Name"])["Total Refactorings"].median().index
    grouped_data = data.groupby(["Project Name"])

    df = pd.DataFrame(columns=['Project Name','Median']) 
    for group in grouped_data.groups:
        refs = grouped_data.get_group(group)['Total Refactorings']
        print(group + "   " + str(np.median(refs)))
        df = df.append({'Project Name':group, 'Median':np.median(refs)}, ignore_index=True)

 
    ordered = df.sort_values(by="Median", ascending=True)['Project Name']



    fig, ax = plt.subplots(figsize=(15,8))
    p = sns.boxplot(x='Project Name', y='Total Refactorings', data=data, showfliers = False, order=ordered, palette=sns.color_palette('gray'))
    plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    ax.set_ylabel('Refactorings per Scenario')

    plt.setp(ax.lines, color='k')
    ax.xaxis.label.set_size(18)
    ax.yaxis.label.set_size(18)
    plt.tight_layout()
    plt.savefig('refactorings_per_merge_scenario.pdf')
    plt.clf()

def plot_number_of_refactorings_for_timeout():
    #create boxplot for ratio alone
    data = pd.read_csv("TotalRefactoringsPerTimedOutScenario.csv")
    sns.boxplot(x='Project Name', y='Total Refactorings', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=7)
    plt.savefig('refactorings_per_timeout.pdf')
    plt.clf()

def plot_number_of_commits_for_timeout():
    #create boxplot for ratio alone
    data = pd.read_csv("DetailedTimedOutCommitStats.csv")
    sns.boxplot(x='Project Name', y='Total Commits', data=data, showfliers = False)
    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=8)
    plt.savefig('commits_per_timeout.pdf')

def get_potentially_resolvable():
    data = pd.read_csv("DetailedProjStats.csv")
    data = data.loc[data['is_potentially_fully_resolvable'] == True]
    grouped_data = data.groupby(data['Project Name'])

    print('TOTAL POTENTIALLY RESOLVABLE BY PROJECT')
    for group in grouped_data.groups:
        print(group, ': ', len(grouped_data.get_group(group)))


plot_boxplot_commits_timeouts()
plot_boxplot_commits_timeouts_by_category()
plot_boxplot()
plot_conflicting_files()
plot_involved_ratio()
plot_involved_refactorings()
plot_number_of_commits()
plot_number_of_refactorings()
plot_number_of_refactorings_for_timeout()
plot_boxplot_conflicting_files_timeouts()
plot_boxplot_involved_refs_timeouts()
plot_diff_files()
get_potentially_resolvable()
plot_boxplot_diff_files_timeouts_by_category()

