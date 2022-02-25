from pathlib import Path

import imutils as imutils
import matplotlib.pyplot as plt
from skimage.metrics import structural_similarity as compare_ssim
import argparse
# import imutils
from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2
import numpy as np
import time

def load_images():
    path = Path("./cathedral/")
    onlyfiles = ["./cathedral/" + str(f) for f in listdir(path) if isfile(join(path, f))]
    loaded_images = [cv2.imread(_) for _ in onlyfiles]
    return loaded_images


if __name__ == '__main__':
    images = load_images()
    images2 = load_images()

    result = []

    for imageA in images:
        for imageB in images2:
            if (imageA == imageB).all():
                print("asd")
                continue
        # imageA = cv2.imread("cathedral/d002.jpg")
        # imageB = cv2.imread("cathedral/d004.jpg")

            grayA = cv2.cvtColor(imageA, cv2.COLOR_BGR2GRAY)
            grayB = cv2.cvtColor(imageB, cv2.COLOR_BGR2GRAY)

            (score, diff) = compare_ssim(grayA, grayB, full=True)
            diff = (diff * 255).astype("uint8")
            # print("SSIM: {}".format(score))

            thresh = cv2.threshold(diff, 0, 255,
                                   cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
            cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
                                    cv2.CHAIN_APPROX_SIMPLE)
            cnts = imutils.grab_contours(cnts)

            copy_imageA = np.copy(imageA)
            copy_imageB = np.copy(imageB)
            for c in cnts:
                # compute the bounding box of the contour and then draw the
                # bounding box on both input images to represent where the two
                # images differ
                (x, y, w, h) = cv2.boundingRect(c)
                cv2.rectangle(copy_imageA, (x, y), (x + w, y + h), (0, 0, 0), -1)
                cv2.rectangle(copy_imageB, (x, y), (x + w, y + h), (0, 0, 0), -1)
            # show the output images
            result.append(copy_imageA)
            result.append(copy_imageB)
            # plt.imshow(copy_imageA)
            # plt.show()
            # time.sleep(0.5)
            # plt.imshow(copy_imageB)
            # plt.show()
            # time.sleep(0.6)
        # break




    final = np.full(images[0].shape, 0)
    for i in range(0, len(result)):
        output = result[i]
        cur_mask = np.stack((np.all(final == np.array([0, 0, 0]), axis=-1),) * 3, axis=-1)
        final += output * cur_mask
    #
    plt.imshow(final)
    plt.show()
    cv2.imwrite("dupa.png", final)
    # print(final.shape)


    # grayA = cv2.cvtColor(final, cv2.COLOR_BGR2GRAY)
    # grayB = cv2.cvtColor(imageB, cv2.COLOR_BGR2GRAY)
    #
    # (score, diff) = compare_ssim(grayA, grayB, full=True)
    # diff = (diff * 255).astype("uint8")
    # # print("SSIM: {}".format(score))
    #
    # thresh = cv2.threshold(diff, 0, 255,
    #                        cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
    # cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
    #                         cv2.CHAIN_APPROX_SIMPLE)
    # cnts = imutils.grab_contours(cnts)
    #
    # copy_imageA = np.copy(final)
    # copy_imageB = np.copy(imageB)
    # for c in cnts:
    #     # compute the bounding box of the contour and then draw the
    #     # bounding box on both input images to represent where the two
    #     # images differ
    #     (x, y, w, h) = cv2.boundingRect(c)
    #     cv2.rectangle(copy_imageA, (x, y), (x + w, y + h), (0, 0, 0), 2)
    #     cv2.rectangle(copy_imageB, (x, y), (x + w, y + h), (0, 0, 0), 2)
    # plt.imshow(copy_imageA)
    # plt.show()

