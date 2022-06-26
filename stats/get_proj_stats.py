
import pandas as pd
import matplotlib.pyplot as plt
import scipy as sci
import numpy as np
import seaborn as sns
import matplotlib.ticker as ticker

REFMERGE = str(0.8)
GIT = str(0.5)
INTELLIMERGE = str(0.3)
DISTANCE = 0.3

def set_box_color(bp, color):
    plt.setp(bp['boxes'], color=color)
    plt.setp(bp['whiskers'], color=color)
    plt.setp(bp['caps'], color=color)
    plt.setp(bp['medians'], color="black")


#https://stackoverflow.com/questions/62630857/how-to-combine-two-boxplots-with-the-same-axes-into-one-boxplot-in-python
def plot_boxplot():
    data = pd.read_csv("DetailedProjStats.csv")
    dfl = pd.melt(data, id_vars='Project Name', value_vars=['Conflict Blocks','Involved Conflicts','Involved Refactorings'])

    sns.boxplot(x='Project Name', y='value', data=dfl, showfliers=False, hue='variable')

    projects = data.groupby(['Project Name'])

    labels = list(projects.groups.keys())

    #ax.set_ylabel("Number of Conflicting Files")

    plt.legend(loc="upper center", borderaxespad=0)

    plt.xticks(rotation=-30, rotation_mode="anchor")
    plt.rc('xtick', labelsize=8)

    #plt.setp(ax.xaxis.get_majorticklabels(), rotation=-30, ha="left", rotation_mode="anchor")

    plt.savefig('detailed_proj_stats.pdf')


plot_boxplot()