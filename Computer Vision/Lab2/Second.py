import cv2
import matplotlib.pyplot as plt
import numpy as np
import PIL
import math
from pprint import pprint as pp


def plot_simple(ax):
    ax.set_title('Basic transformations')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, identity(i), label='identity')
    ax.plot(i, invert(i), label='inversion')
    ax.grid()
    ax.legend()


def plot_gamma(ax):
    ax.set_title('Gamma correction for diffrent gamma values')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, gamma(i, 0.1), label='0.1')
    ax.plot(i, gamma(i, 0.2), label='0.2')
    ax.plot(i, gamma(i, 0.5), label='0.5')
    ax.plot(i, gamma(i, 1.0), label='1.0')
    ax.plot(i, gamma(i, 1.8), label='1.8')
    ax.plot(i, gamma(i, 3.0), label='4.0')
    ax.plot(i, gamma(i, 4.5), label='4.5')
    ax.grid()
    ax.legend()


def plot_l_threshold(ax):
    ax.set_title('Low-pass filtering')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, l_threshold(i, 0.1), label='0.1')
    ax.plot(i, l_threshold(i, 0.5), label='0.5')
    ax.plot(i, l_threshold(i, 0.9), label='0.9')
    ax.grid()
    ax.legend()


def plot_h_threshold(ax):
    ax.set_title('High-pass filtering')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, h_threshold(i, 0.1), label='0.1')
    ax.plot(i, h_threshold(i, 0.5), label='0.5')
    ax.plot(i, h_threshold(i, 0.9), label='0.9')
    ax.grid()
    ax.legend()


def plot_quad(ax):
    ax.set_title('Quadratic key_point_detector')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, quad(i, 4.0), label='4.0')
    ax.plot(i, quad(i, 2.0), label='2.0')
    ax.plot(i, quad(i, 1.0), label='1.0')
    ax.grid()
    ax.legend()


def plot_stacked(ax):
    ax.set_title('Transformation stacked')
    ax.set_xlabel('Input intensity')
    ax.set_ylabel('Output intensity')
    ax.plot(i, h_threshold(gamma(invert(i), 0.3), 0.7), label='threshold(gamma(invert))')
    ax.plot(i, quad(l_threshold(i, 0.4), 3.0), label='quad(threshold)')
    ax.grid()
    ax.legend()


def identity(i):
    return i


def invert(i):
    return 1.0 - i


def gamma(i, g):
    return i ** g


def l_threshold(i, threshold):
    a = i
    print(a)
    print(threshold)
    print(i)
    a[a < threshold] = threshold
    return a



def h_threshold(i, threshold):
    a = i
    a[a > threshold] = threshold
    return a


# # todo:
def quad(i, a):
    copy = i
    copy = copy * (copy - 1)
    copy = copy * (-1) * a
    return copy


img_grayscale = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
buckets = 32 * (np.array(range(256)) // 32)
buckets = cv2.LUT(img_grayscale, buckets)
buckets = buckets.astype(np.uint8)
img_hot = cv2.applyColorMap(buckets, cv2.COLORMAP_HOT)
imshow(np.concatenate([img_grayscale, buckets],1))
imshow(img_hot)


if __name__ == '__main__':
    # i = np.arange(0.0, 1.0, 0.01)  # image domain

    # fig, ax = plt.subplots(1, 2, figsize=(15, 5), sharex='all', sharey='all')
    # axes = plt.gca()
    # axes.set_xlim([0.0, 1.0])
    # axes.set_ylim([0.0, 1.0])
    # plot_simple(ax[0])
    # plot_gamma(ax[1])
    # plt.show()
    i = np.arange(0.0, 1.0, 0.01)  # image domain

    fig, ax = plt.subplots(2, 2, figsize=(15, 15), sharex='all', sharey='all')
    axes = plt.gca()
    axes.set_xlim([0.0, 1.0])
    axes.set_ylim([0.0, 1.0])
    plot_l_threshold(ax[0, 0])
    plot_h_threshold(ax[0, 1])
    plot_quad(ax[1, 0])
    plot_stacked(ax[1, 1])
    plt.show()
