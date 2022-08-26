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

    test_mask_image = cv2.imread("d001mask.png", cv2.IMREAD_COLOR)
    mask = np.all((test_mask_image == np.array([0, 255, 0])), axis=2).astype(np.uint8)
    print(mask)
    # mask = 1 - mask
    fgModel = np.zeros((1, 65), dtype="float")
    bgModel = np.zeros((1, 65), dtype="float")
    # apply GrabCut using the the bounding box segmentation method
    # mask = np.zeros(img1.shape[:2], dtype="uint8")
    # print(mask.shape)
    rect = (353, 364, 750, 496)
    (mask, bgModel, fgModel) = cv2.grabCut(img1, mask, None, bgModel,
                                           fgModel, 5, mode=cv2.GC_INIT_WITH_MASK)

    values = (
        ("Definite Background", cv2.GC_BGD),
        ("Probable Background", cv2.GC_PR_BGD),
        ("Definite Foreground", cv2.GC_FGD),
        ("Probable Foreground", cv2.GC_PR_FGD),
    )
    # loop over the possible GrabCut mask values
    for (name, value) in values:
        # construct a mask that for the current value
        print("[INFO] showing mask for '{}'".format(name))
        valueMask = (mask == value).astype(np.uint8) * 255
        # display the mask so we can visualize it
        cv2.imshow(name, valueMask)
        cv2.waitKey(0)

    outputMask = np.where((mask == cv2.GC_BGD) | (mask == cv2.GC_PR_BGD),
                          0, 1)
    outputMask = (outputMask * 255).astype("uint8")
    # apply a bitwise AND to the image using our mask generated by
    # GrabCut to generate our final output image
    output = cv2.bitwise_and(img1, img1, mask=outputMask)

    plt.imshow(output[:, :, ::-1])
    plt.show()