from pathlib import Path

import imutils as imutils
import matplotlib.pyplot as plt
from skimage.metrics import structural_similarity as compare_ssim
import argparse
# import imutils
from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2 as cv
import numpy as np

if __name__ == '__main__':
    img = cv.imread("dupa.png")
    # plt.imshow(image)
    # plt.show()

    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)  # make grayscale image
    # gray[gray != 0] = 0
    gray[gray == 0] = 255
    gray[gray != 255] = 0
    # gray[gray <= 254] =255

    plt.imshow(gray)
    plt.show()  # show our original image
    #
    corners = cv.goodFeaturesToTrack(gray, 2000, 0.01,
                                     5)  # find our corners, 2000 is the number of corners we can detect, 5 is the distance between corners

    xylist = []  # put all of our xy coords in here

    for corn in corners:  # extract our corners and put them in xylist
        x, y = corn[0]
        xylist.append((x, y))
        x = int(x)
        y = int(y)
        cv.rectangle(img, (x - 2, y - 2), (x + 2, y + 2), (100, 100, 0),
                     -1)  # now mark where our corners are on our original image

    res = [[] for i in range(int(len(xylist) / 4))]  # generate i nested lists for our rectangles

    for index, item in enumerate(xylist):  # format coordinates as you want them
        res[index % int(len(xylist) / 4)].append(item)

    print("\n" + "found ", int(len(xylist) / 4), "rectangles\n")  # how many rectangles did we have?
    print(res)

    plt.imshow(img)
    plt.show()  # show our original image
# show our new image with rectangle corners marked
