# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import numpy as np
from matplotlib import pyplot as plt
from matplotlib import style


def print_hi(name):

    style.use('ggplot')

    # Use a breakpoint in the code line below to debug your script.
    print("Hi")
    x, y = np.loadtxt('../bruteForce.txt', unpack=True, delimiter=',')
    plt.title('Brute Force')
    plt.ylabel('Intersections')
    plt.xlabel('Number of Circles')
    plt.plot(x, y)
    plt.show()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
