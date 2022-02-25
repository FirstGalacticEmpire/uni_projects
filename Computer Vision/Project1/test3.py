import numpy as np
import operator
import matplotlib.pyplot as plt
import argparse
import os
from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2


def load_images():
    path = Path("./cathedral/")
    onlyfiles = ["./cathedral/" + str(f) for f in listdir(path) if isfile(join(path, f))]
    loaded_images = [cv2.imread(_) for _ in onlyfiles]
    return loaded_images


if __name__ == '__main__':
    images = load_images()

    test = cv2.imread("d001mask.png", cv2.IMREAD_COLOR)
    res = np.all((test == np.array([0, 255, 0])), axis=2).astype(np.uint8)
    print(res)
    img1 = images[1]

    # gray = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    # test = cv2.imread("d001mask.png", cv2.IMREAD_COLOR)
    # res = np.all((test == np.array([0, 255, 0])), axis=2).astype(np.int32)
    # print(res)
    # gray[gray > 250] = 0
    # print(gray)
    # print(res.shape)

    roughOutput = cv2.bitwise_and(images[0], images[0], mask=res)
    plt.imshow(roughOutput)
    plt.show()
    plt.imshow(images[0])
    plt.show()