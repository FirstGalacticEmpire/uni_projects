from os import listdir
from os.path import isfile, join
from pathlib import Path
import cv2
import numpy as np
import operator
import matplotlib.pyplot as plt


def load_images():
    path = Path("./cathedral/")
    onlyfiles = ["./cathedral/" + str(f) for f in listdir(path) if isfile(join(path, f))]
    loaded_images = [cv2.imread(_) for _ in onlyfiles]
    return loaded_images


to_remove_keys = [[69, 69, 69]]


def pick_right_pixel1(pixels):
    # Twój pomysł Kacper
    pixels = np.array(pixels)
    # print(pixels)
    means = np.mean(pixels, axis=0)
    # print(means)
    difference = np.abs(pixels - means)
    # print(difference)
    sum_of_differences = np.sum(difference, axis=1)
    # print(sum_of_differences)
    # print(np.argmin(sum_of_differences))
    # print(pixels[3])
    return pixels[np.argmin(sum_of_differences)]


def pick_right_pixel(pixels, param1=0.15, param2=0.15):
    # Tu ta najwieksza czestotliwosc
    # pixels = [[135, 1, 151],
    #           [138, 1, 200],
    #           [100, 144, 154],
    #           [100, 144, 153],
    #           [141, 200, 155]]
    # pixels = [[0, 4, 5],
    #           [0, 5, 6],
    #           [0, 4, 5],
    #           [0, 1, 2],
    #           [8, 13, 14]]
    # # print(np.array(pixels))
    # param = 1.1
    # param2 = 0.9
    a_dict = {}
    a_dict[tuple(pixels[0])] = 1
    for pixel in pixels[1:]:
        should_continue = False
        for key, value in a_dict.items():
            truths = [r * (1 - param1) <= rp <= r * (1 + param2) for r, rp in zip(key, pixel)]
            if all(truths):
                a_dict[key] += 1
                # print("XD")
                should_continue = True
                break
        if should_continue:
            continue
        else:
            a_dict[tuple(pixel)] = 1
    # print(a_dict)
    # avoid = [[149, 26, 32],
    #          [215, 117, 121],
    #          [150, 10, 20],
    #          [206, 71, 60]]

    avoid = [[32, 26, 149],
             [121, 117, 215],
             [20, 10, 150],
             [60, 71, 206],
             [68, 36, 38],
             [52, 41, 40],
             [47, 37, 39],
             [88, 68, 65],
             [74, 42, 45],
             [47, 46, 63],
             [55, 55, 73]]

    to_remove_keys = []
    for to_avoid in avoid:
        for key, value in a_dict.items():

            difference_squared = np.sum((np.array(key) - np.array(to_avoid)) ** 2)
            # print(difference_squared, np.array(key) - np.array(to_avoid))
            if difference_squared < 1500:
                # print("taka sytuacja")
                to_remove_keys.append(key)
    # if len(to_remove_keys) != 0:
    #     print(to_remove_keys)
    for key in set(to_remove_keys):
        del a_dict[key]
    print(to_remove_keys)

    check_point = [True if value == 1 else False for value in a_dict.values()]
    if all(check_point):
        # print(a_dict)
        # print(list(np.mean(pixels, axis=0).astype(np.int32)))
        # print()
        # if len(a_dict.keys()) < 5:
        #     print(a_dict)
        #     print(np.array(pixels))
        #     print()

        return list(np.mean(pixels, axis=0).astype(np.int32))
    #     raise "Error"
    best_pixel = max(a_dict.items(), key=operator.itemgetter(1))[0]
    # print(best_pixel)

    return best_pixel


def iterate(images, param1, param2):
    print(images[0].shape)
    images = [image.reshape(-1, 3) for image in images]
    print(images[0].shape)
    final_image = []
    for dd in range(0, images[0].shape[0]):
        pixels = [list(images[x][dd]) for x in range(0, len(images))]
        best_pixel = pick_right_pixel(pixels, param1, param2)
        final_image.append(list(best_pixel))

    final_image = np.array(final_image)
    final_image = final_image.reshape((500, 752, 3))
    # print(final_image)
    plt.title(str(param1) + " " + str(param2))
    plt.imshow(final_image)
    plt.show()


if __name__ == '__main__':
    images = load_images()
    # print(images)
    aa = [0.05, 0.07, 0.10, 0.15, 0.20]
    aa2 = [0.05, 0.07, 0.10, 0.15, 0.20]
    # for a in aa:
    #     iterate(images, param=a)
    # for xd1 in aa:
    #     for xd2 in aa2:
    #         iterate(images, param1=xd1, param2=xd2)
    iterate(images, param1=0.07, param2=0.07)
