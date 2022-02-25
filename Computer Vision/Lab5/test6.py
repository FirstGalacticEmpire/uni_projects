import os
import random
from pathlib import Path
import numpy as np
import cv2
import matplotlib.pyplot as plt
import itertools as it


def select_images(n=20, cat_or_dog=None):
    path = Path("./PetImages/")
    labels = [folder.stem for folder in path.iterdir()]
    # Version 1: equal number of cats and dogs + labels.

    files = [list((path / label).iterdir()) for label in labels]
    selected_paths = random.sample(files[0], int(n / 2)) + random.sample(files[1], int(n / 2))
    print(len(files))
    if cat_or_dog is None:
        flat_files = list(it.chain.from_iterable(files))
        # print(flat_files)
        one_image = random.sample(flat_files, 1)
    elif cat_or_dog == 0:
        one_image = random.sample(files[0], 1)
    elif cat_or_dog == 1:
        one_image = random.sample(files[1], 1)
    else:
        one_image = [selected_paths[0]]
    # labels = ["Cat" for _ in range(int(n / 2))] + ["Dog" for _ in range(int(n / 2))]
    return selected_paths, one_image
    # return random.sample(files[0],1) + selected_paths

    # Version 2: pick n random,
    # files = list(chain.from_iterable([list((path / label).iterdir()) for label in labels]))
    # return random.sample(files, n)


def load_images(selected_paths):
    loaded_images = [cv2.imread(str(path)) for path in selected_paths]
    return loaded_images


def detect_key_points_and_descriptors(img, key_point_detector, descriptor):
    key_points = key_point_detector.detect(img, None)
    descriptors = descriptor.compute(img, key_points)
    return key_points, descriptors[1]
    # print(key_points)
    # print(descriptors)
    # img_clevr = cv2.drawKeypoints(img, key_points, None, flags=cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
    # plt.imshow(img_clevr)
    # plt.show()


def compare_images(img1, img2, kp1, kp2, desc1, desc2, matcher=cv2.BFMatcher(cv2.NORM_L2), num_matches=20):
    matches = matcher.match(desc1, desc2)
    matches = sorted(matches, key=lambda x: x.distance)

    # print([x.distance for x in matches])
    # print(matches)

    # drawn_matches = cv2.drawMatches(img1, kp1, img2, kp2, matches[:num_matches], None,
    #                                 flags=cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS)
    # drawn_matches = drawn_matches[:, :, ::-1]
    # # plt.imshow(drawn_matches)
    # # plt.show()

    return sum([x.distance for x in matches])


def find_n_closest(selected_img, images, n=5, key_point_method1=cv2.SIFT_create(),
                   key_point_method2=cv2.SIFT_create(), descriptor_method=cv2.SIFT_create(), matcher=cv2.BFMatcher(cv2.NORM_L2)):
    plt.imshow(selected_img[:, :, ::-1])
    plt.show()

    kp1, desc1 = detect_key_points_and_descriptors(selected_img, key_point_method1, descriptor_method)
    a_dict = {}
    for id_, img2 in enumerate(images):
        kp2, desc2 = detect_key_points_and_descriptors(img2, key_point_method2, descriptor_method)
        distance = compare_images(selected_img, img2, kp1, kp2, desc1, desc2, matcher=matcher)
        a_dict[id_] = distance

    a_dict = dict(sorted(a_dict.items(), key=lambda item: item[1]))
    a_dict = list(a_dict.keys())[:n]
    closest_images = [cv2.resize(images[img], (256, 256))[:, :, ::-1] for img in a_dict]
    closest_images = np.concatenate(closest_images, 1)

    plt.figure(figsize=(20, 25))
    plt.imshow(closest_images)
    plt.show()



if __name__ == '__main__':
    # random.seed(13)
    # None for random image
    # 0 For random cat
    # 1 For random dog
    # 2 For first image in selected dataset (for verification)
    selected_paths, query_image = select_images(10, 0)
    images = load_images(selected_paths)
    one_image = load_images(query_image)[0]
    find_n_closest(selected_img=one_image, images=images)

    # img1 = images_[1]
    # img2 = images_[2]
    # kp1, desc1 = detect_key_points_and_descriptors(img1, cv2.SIFT_create(), cv2.SIFT_create())
    # kp2, desc2 = detect_key_points_and_descriptors(img2, cv2.SIFT_create(), cv2.SIFT_create())
    # # print(desc1[1])
    # compare_images(img1, img2, kp1, kp2, desc1, desc2)

    # plt.imshow(images_[0])
    # plt.show()
#
