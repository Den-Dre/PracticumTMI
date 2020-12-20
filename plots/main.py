# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import numpy as np
from matplotlib import pyplot as plt
from matplotlib import style


def plotter():
    plt.rcParams.update({'font.size': 22})
    style.use('ggplot')

# Compares
    x_bf, y_bf = np.loadtxt('./compares/bruteForceCompares.txt',       unpack=True, delimiter=',')
    x_nsl, y_nsl = np.loadtxt('./compares/naiveSweeplineCompares.txt', unpack=True, delimiter=',')
    x_sl, y_sl = np.loadtxt('./compares/sweepLineCompares.txt',        unpack=True, delimiter=',')
    fig = plt.figure()
    ax = fig.add_subplot(111)

    plt.title('Intersect-oproepen i.f.v. inputgrootte N')
    plt.ylabel('Intersections')
    plt.xlabel('Aantal cirkels N')
    # ax = plt.subplot(111)

    ax.plot(x_bf, y_bf,  label="Brute Force",     alpha=0.5, c="b", ls="solid")
    ax.plot(x_bf, y_nsl, label="Naive Sweepline", alpha=0.5, c="r", ls="dotted", linewidth=2.5)
    ax.plot(x_bf, y_sl , label="Sweepline",       alpha=0.5, c="m")
    plt.legend()
    # plt.show()

# Times
    x_bf2, y_bf2 = np.loadtxt('./times/bruteForceTime.txt',       unpack=True, delimiter=',')
    x_nsl2, y_nsl2 = np.loadtxt('./times/naiveSweeplineTime.txt', unpack=True, delimiter=',')
    x_sl2, y_sl2 = np.loadtxt('./times/sweepLineTime.txt',        unpack=True, delimiter=',')
    fig2 = plt.figure()
    ax2 = fig2.add_subplot(111)

    plt.title('Plots based on execution times')
    plt.ylabel('Execution time (milliseconds)')
    plt.xlabel('Number of Circles')
    # ax = plt.subplot(111)

    ax2.plot(x_bf2, y_bf2,  label="Brute Force",     alpha=0.5, c="b", ls="solid")
    ax2.plot(x_bf2, y_nsl2, label="Naive Sweepline",            c="r", ls="dotted", linewidth=1.5)
    ax2.plot(x_bf2, y_sl2,  label="Sweepline",                  c="m")
    plt.legend()

# Ratio
#     fig = plt.figure()
#     ax3 = fig.add_subplot(111)
#
#     plt.title('Vergelijking: uitvoeringstijd & aantal vergelijkingen')
#     plt.ylabel('Verhouding: uitvoeringstijd / aantal vergelijkingen')
#     plt.xlabel('Aantal cirkels')
#
#     ax3.plot(x_bf2[5:], y_bf2[5:]/y_bf[5:], label="Brute Force", alpha=0.5, c="b", ls="solid")
#     ax3.plot(x_bf2[5:], y_nsl2[5:]/y_nsl[5:], label="Naive Sweepline", c="r", ls="dotted", linewidth=5.5)
#     ax3.plot(x_bf2[5:], y_sl2[5:]/y_sl[5:], label="Sweepline", c="m", linewidth=0.5)
#     plt.legend()
    plt.show()


if __name__ == '__main__':
    plotter()
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
