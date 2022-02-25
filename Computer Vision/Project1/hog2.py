from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2
import numpy as np
import operator
import matplotlib.pyplot as plt
import matplotlib.pyplot as plt
import numpy as np
from skimage.feature import hog
from skimage import data, exposure
from pprint import pprint
import sys
import cv2


def load_images():
    path = Path("./cathedral/")
    onlyfiles = ["./cathedral/" + str(f) for f in listdir(path) if isfile(join(path, f))]
    loaded_images = [cv2.imread(_) for _ in onlyfiles]
    return loaded_images


def iterate(images, hogs, param1, param2):
    images = [image.reshape(-1, 3) for image in images]
    hogs = [hog.reshape(-1) for hog in hogs]
    # print(hogs[0].shape)
    final_image = []
    for dd in range(0, images[0].shape[0]):
        pixels = [list(images[x][dd]) for x in range(0, len(images))]
        hogs5 = [hogs[x][dd] for x in range(0, len(images))]
        best_pixel = pick_right_pixel(pixels, hogs5, param1, param2)

        final_image.append(list(best_pixel))
        # break

    final_image = np.array(final_image)
    final_image = final_image.reshape((500, 752, 3))
    # print(final_image)
    plt.title(str(param1) + " " + str(param2))
    plt.imshow(final_image)
    plt.show()


def pick_right_pixel(pixels, hogs, param1=0.15, param2=0.15):
    # hogs = [1, 5, 5, 5, 5]
    print(pixels)
    print(hogs)

    a_dict = {}
    a_dict[(tuple(pixels[0]), hogs[0])] = 1
    my_hog = hogs[0]

    for id_, hog_ in enumerate(hogs[1:]):
        # counter = 0
        should_continue = False
        for key, value in a_dict.items():
            pixel, hog__ = key
            # print(hog__)
            if hog__ * (1 - param1) <= hog_ <= hog__ * (1 + param2):
                a_dict[key] += 1
                should_continue = True
                break
        if should_continue:
            continue
        a_dict[(tuple(pixels[id_ + 1]), hogs[id_ + 1])] = 1

    best_pixel = max(a_dict.items(), key=operator.itemgetter(1))[0]
    print(a_dict)
    print(best_pixel)
    print()
    return best_pixel[0]


if __name__ == '__main__':
    images = load_images()
    hogs = []
    for image in images:
        fd, hog_image = hog(image, orientations=8, pixels_per_cell=(36,36),
                            cells_per_block=(1, 1), visualize=True)
        # print(hog_image.shape)
        # print(fd.shape)
        # break
        hogs.append(hog_image)
    iterate(images, hogs, 0.15, 0.15)
    # # image = images[0]
    # print(500 * 752)
    # # print(images[0].shape)
    # hog_image = hogs[3]
    # hog_image2 = hogs[4]
    #
    # table1 = hog_image2 * 0.85 < hog_image
    # table2 = hog_image < hog_image2 * 1.15
    # # print(np.sum((hogs[1]==hogs[0]).astype(np.int8)))
    # # print(image[0][hogs[0] == hogs[1]].shape)
    # # print(np.sum((table1*table2).astype(np.int8)))
    # img1 = images[0]
    # hog_image1 = hogs[0]
    # param = 0.25
    # plt.imshow(img1)
    # plt.show()
    #
    # for id_, image in enumerate(images[1:]):
    #     hog_image2 = hogs[id_ + 1]
    #     table1 = hog_image2 * (1 - param) <= hog_image
    #     table2 = hog_image <= hog_image2 * (1 + param)
    #     img1[table1 * table2] = images[id_ + 1][table1 * table2]
    #
    # plt.imshow(img1)
    # plt.show()
    #
    # #
    # # img1
