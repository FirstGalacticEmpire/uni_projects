from pathlib import Path

import imutils as imutils
import matplotlib.pyplot as plt
from skimage.metrics import structural_similarity as compare_ssim
import argparse
# import imutils
from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2 as cv2
import numpy as np



corners  =[(718.0, 489.0), (614.0, 479.0), (515.0, 386.0), (725.0, 460.0)]
if __name__ == '__main__':
    img = cv2.imread("dupa.png")
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # make grayscale image
    # gray[gray != 0] = 0
    gray[gray == 0] = 255
    gray[gray != 255] = 0
    # cv2.imshow("sad", gray)
    # cv2.waitKey()

    # imgContours = img.copy()  # Copy my Image for Contours
    # imgCanny = cv2.Canny(imgContours, 10, 50)  # Image to Edges
    #
    # contours, hierarchy = cv2.findContours(imgCanny, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    contours, hierarchy = cv2.findContours(gray, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    xd = [cv2.boundingRect(contour) for contour in contours]
    print(xd)
    for rect in xd[18:19]:
        print(rect)
        x, y, w, h = rect
        print((x, y), (x+w,y+h))
        cv2.rectangle(img, (x, y), (x+w,y+h), (0, 0, 250), 5)


    padding =10
    print(img[368+padding:479+padding, 559+padding:569+padding])
    cv2.imshow("sad", img[368-padding:479+padding, 559-padding:569+padding])
    cv2.waitKey()
    # print(img[559:569, 368:479])