# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import numpy as np
from matplotlib import pyplot as plt
from matplotlib import style


def plotter():

    style.use('ggplot')

    x_bf, y_bf = np.loadtxt('../bruteForce.txt', unpack=True, delimiter=',')
    x_nsl, y_nsl = np.loadtxt('../naiveSweepline.txt', unpack=True, delimiter=',')
    x_sl, y_sl = np.loadtxt('../sweepLine.txt', unpack=True, delimiter=',')
    fig = plt.figure()
    ax = fig.add_subplot(111)

    plt.title('Plots for three algorithms')
    plt.ylabel('Intersections')
    plt.xlabel('Number of Circles')
    # ax = plt.subplot(111)

    ax.plot(x_bf, y_bf, label="Brute Force", alpha=0.5, c="b", ls="solid")
    ax.plot(x_bf, y_nsl, label="Naive Sweepline", c="r", ls="dotted", linewidth=1.5)
    ax.plot(x_bf, y_sl, label="Sweepline", c="m")
    plt.legend()
    plt.show()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    plotter()
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
