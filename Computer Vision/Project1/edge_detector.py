from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2
import numpy as np
import operator
import matplotlib.pyplot as plt
import argparse
import os


def load_images():
    path = Path("./cathedral/")
    onlyfiles = ["./cathedral/" + str(f) for f in listdir(path) if isfile(join(path, f))]
    loaded_images = [cv2.imread(_) for _ in onlyfiles]
    return loaded_images


if __name__ == '__main__':
    images = load_images()
    img1 = images[0]
    plt.imshow(img1[:, :, ::-1])
    plt.show()

    fgModel = np.zeros((1, 65), dtype="float")
    bgModel = np.zeros((1, 65), dtype="float")



    # rect = (209,365, 265, 480)
    rect = (335,367,456,488)
    # img2 = img1[340:550, 367:480]
    # print(img2.shape)
    # plt.imshow(img2[:, :, ::-1])
    # plt.show()
    # rect = np.array(rect) - np.array((335,367,335,367))
    # print(rect)
    # rect = (15, 15, 115, 115)
    mask = np.zeros(img1.shape[:2], dtype="uint8")
    (mask, bgModel, fgModel) = cv2.grabCut(img1, mask, rect, bgModel,
                                           fgModel, 3, mode=cv2.GC_INIT_WITH_RECT)

    values = (
        ("Definite Background", cv2.GC_BGD),
        ("Probable Background", cv2.GC_PR_BGD),
        ("Definite Foreground", cv2.GC_FGD),
        ("Probable Foreground", cv2.GC_PR_FGD),
    )

    # for (name, value) in values:
    #     print("[INFO] showing mask for '{}'".format(name))
    #     valueMask = (mask == value).astype(np.uint8) * 255
    #     cv2.imshow(name, valueMask)
    #     cv2.waitKey(0)

    outputMask = np.where((mask == cv2.GC_BGD) | (mask == cv2.GC_PR_BGD),
                          0, 1)
    outputMask = (outputMask * 255).astype("uint8")
    output = cv2.bitwise_and(img1, img1, mask=outputMask)

    plt.imshow(output[:, :, ::-1])
    plt.show()
