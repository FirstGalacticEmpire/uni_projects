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
    dst = cv2.imread("frankenface/sources/andy.png")
    src = cv2.imread("frankenface/sources/kajiya.png")

    # Create a rough mask around the airplane.
    src_mask = np.zeros(src.shape, src.dtype)
    # poly = np.array([[4, 80], [30, 54], [151, 63], [254, 37], [298, 90], [272, 134], [43, 122]], np.int32)
    poly = np.array([[169, 289], [260, 307], [286, 414], [251, 504], [180, 529], [80, 454], [71, 345]])
    cv2.fillPoly(src_mask, [poly], (255, 255, 255))

    # This is where the CENTER of the airplane will be placed
    center = (292, 436)

    # Clone seamlessly.
    output = cv2.seamlessClone(src, dst, src_mask, center, cv2.NORMAL_CLONE)

    # Save result
    plt.imshow(output[:, :, ::-1])
    plt.show()
