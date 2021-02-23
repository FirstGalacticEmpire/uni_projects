import os
from math import log2
from matplotlib import pyplot as plt


def load_text_into_memory(file_name, file_folder):
    with open(f"{file_folder}/{file_name}", "r") as file:
        return file.read()


def letter_frequency_in_text(text_to_analyze):
    a_dict = dict()
    for letter in text_to_analyze:
        try:
            a_dict[letter] += 1
        except KeyError:
            a_dict[letter] = 1
    a_dict = dict(sorted(a_dict.items(), key=lambda kv: kv[0]))
    return a_dict


def letter_probabilities_in_text(text_to_analyze):
    probabilities = letter_frequency_in_text(text_to_analyze)
    sum_of_all_letters = sum(probabilities.values())
    for key, value in probabilities.items():
        probabilities[key] = value / sum_of_all_letters
    # for key in probabilities.keys():
    #     probabilities[key] = probabilities[key]/sum_of_all_letters
    return probabilities


def n_long_word_occurrence(text, n):
    a_dict = dict()
    for x in range(0, len(text) - (n - 1)):
        pair_of_letters = text[x:x + n]
        try:
            a_dict[pair_of_letters] += 1
        except KeyError:
            a_dict[pair_of_letters] = 1
    text_length = len(text)
    a_dict2 = {key: (value / (text_length - (n - 1))) for key, value in a_dict.items()}
    # denominator is skipped (it shortens in equation) as it doesn't influence the result, only makes calculation harder
    return a_dict, a_dict2


# calculates P("abc_")
def a_function(markov_order, text):
    a_dict = dict()
    for x in range(0, len(text) - (markov_order - 1)):
        pair_of_letters = text[x:x + markov_order]  # +1
        try:
            a_dict[pair_of_letters[:markov_order]] += 1
        except KeyError:
            a_dict[pair_of_letters[:markov_order]] = 1
    return a_dict


# calculates P(a|abc) = P("abca")/P("abc_")
def return_conditional_probabilities(markov_order, text_to_analyze):
    if markov_order == 0:
        lp = letter_probabilities_in_text(text_to_analyze)
        return lp, lp
    else:
        dict_1 = a_function(markov_order=markov_order, text=text_to_analyze)
        # dict_1, useless = n_long_word_occurrence(text_to_analyze, n=markov_order)
        dict_2, non_conditional_probabilities = n_long_word_occurrence(text=text_to_analyze, n=markov_order + 1)

        conditional_probabilities = {}

        for letters_, number_ in dict_2.items():
            value = dict_1[letters_[:markov_order]]
            conditional_probabilities[letters_] = number_ / value

        return conditional_probabilities, non_conditional_probabilities


def return_conditional_entropy(markov_order, text_to_analyze):
    conditional_entropy = []
    for x in range(0, markov_order + 1):
        conditional_probabilities, non_conditional_probabilities = return_conditional_probabilities(x,
                                                                                                    text_to_analyze)
        if x == 0:
            entropy = 0
            for key, probability in non_conditional_probabilities.items():
                entropy += probability * log2(1 / probability)
            conditional_entropy.append(entropy)
        else:
            entropy = 0
            for key, probability in non_conditional_probabilities.items():
                entropy += probability * log2(1 / conditional_probabilities[key])
            conditional_entropy.append(entropy)

    return conditional_entropy


def plot_result(conditional_entropy_of_letters, conditional_entropy_of_words):
    x = [x for x in range(0, len(conditional_entropy_of_letters))]
    plt.xlabel("Conditional Entropy Order")
    plt.ylabel("Conditional Entropy")
    plt.title("Conditional entropy of letters")
    plt.plot(x, conditional_entropy_of_letters, 'ro')
    plt.show()

    x = [x for x in range(0, len(conditional_entropy_of_words))]
    plt.xlabel("Conditional Entropy Order")
    plt.ylabel("Conditional Entropy")
    plt.title("Conditional entropy of words")
    plt.plot(x, conditional_entropy_of_words, 'ro')
    plt.show()


def plot_and_calculate(max_order, path_to_file, file_folder):
    text = load_text_into_memory(path_to_file, file_folder=file_folder)
    conditional_entropy_of_letters = return_conditional_entropy(max_order, text)
    print("conditional_entropy_of_letters:", conditional_entropy_of_letters)
    conditional_entropy_of_words = return_conditional_entropy(max_order, tuple(text.split()))
    print("conditional_entropy_of_words:", conditional_entropy_of_words)
    plot_result(conditional_entropy_of_letters, conditional_entropy_of_words)
    print()


if __name__ == '__main__':
    for path_to_file in os.listdir("wiki_data"):
        print("File name:", path_to_file)
        plot_and_calculate(max_order=6, path_to_file=path_to_file, file_folder="wiki_data")
    for path_to_file in os.listdir("sample_languages"):
        print("File name:", path_to_file)
        plot_and_calculate(max_order=6, path_to_file=path_to_file, file_folder="sample_languages")
