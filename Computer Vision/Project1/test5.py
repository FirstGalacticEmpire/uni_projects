import cv2
import numpy as np
from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2
import numpy as np
import operator
import matplotlib.pyplot as plt
import argparse
import os

# Read images
if __name__ == '__main__':
    dst = cv2.imread("cathedral/d001.jpg")
    src = cv2.imread("cathedral/d002.jpg")

    # plt.imshow(dst[:, :, ::-1])
    # plt.show()


    # Create a rough mask around the airplane.
    src_mask = np.zeros(src.shape, src.dtype)
    # poly = np.array([[4, 80], [30, 54], [151, 63], [254, 37], [298, 90], [272, 134], [43, 122]], np.int32)
    poly = np.array([[209,371], [280, 383], [200, 479] ,[285, 473]])
    cv2.fillPoly(src_mask, [poly], (255, 255, 255))

    # This is where the CENTER of the airplane will be placed
    center = (232, 411)

    # Clone seamlessly.
    output = cv2.seamlessClone(src, dst, src_mask, center, cv2.NORMAL_CLONE)

    # Save result
    plt.imshow(output[:, :, ::-1])
    plt.show()

    # output = cv2.seamlessClone(src, output, src_mask, center, cv2.NORMAL_CLONE)
    #
    # # # Save result
    # plt.imshow(output[:, :, ::-1])
    # plt.show()