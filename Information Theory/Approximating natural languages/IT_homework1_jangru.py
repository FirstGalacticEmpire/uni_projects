# Code by Jan Gruszczyński 145464
import random


def load_data():
    with open('lab1/1.txt', 'r') as text_file:
        text = text_file.read()
    with open('lab1/2.txt', 'r') as text_file:
        text += text_file.read()
    # contains numbers
    with open('lab1/3.txt', 'r') as text_file:
        text += text_file.read()
    return text


def average_word_length(generated_text):
    b_list = generated_text.split(' ')
    b_list = [len(b_list[x]) for x in range(0, len(b_list)) if b_list[x] != '']
    average_word_len = sum(b_list) / len(b_list)
    print("Average length of word in generated text is equal to:", average_word_len)
    return average_word_len


def generate_text_with_given_weights(len_, alphabet_list, weights=None):
    string = ''
    for x in range(0, len_):
        if weights:
            choice = random.choices(alphabet_list, weights)[0]
        else:
            choice = random.choices(alphabet_list)[0]
        string += choice
    return string


def frequency_of_letters_in_text(alphabet, text):
    frequency = dict()
    for letter in alphabet:
        frequency[letter] = text.count(letter)
    return frequency


def first_order_weights(alphabet, text):
    probabilities = dict()
    text_length = len(text)
    for letter in alphabet:
        probabilities[letter] = text.count(letter) / text_length
    return probabilities


def n_long_word_occurrence(text, n):
    a_dict = dict()
    for x in range(0, len(text) - (n - 1)):
        pair_of_letters = text[x:x + n]
        try:
            a_dict[pair_of_letters] += 1
        except KeyError:
            a_dict[pair_of_letters] = 1
    # a_dict = {key: (value / (text_length - (n - 1))) for key, value in a_dict.items()}
    # denominator is skipped (it shortens in equation) as it doesn't influence the result, only makes calculation harder
    return a_dict


# calculates P(a|abc) = P("abca")/P("abc")
def return_conditional_probs2(markov_order, text):
    a_dict1 = n_long_word_occurrence(text=text, n=markov_order)
    a_dict2 = n_long_word_occurrence(text=text, n=markov_order + 1)

    condtional_probs = {letters: value / a_dict1[letters[-markov_order:]] for letters, value in a_dict2.items()}

    return condtional_probs


# calculates P("abc_")
def a_function(markov_order, text):
    a_dict = dict()
    for x in range(0, len(text) - (markov_order - 1)):
        pair_of_letters = text[x:x + markov_order + 1]
        try:
            a_dict[pair_of_letters[:markov_order]] += 1
        except KeyError:
            a_dict[pair_of_letters[:markov_order]] = 1
    return a_dict


# calculates P(a|abc) = P("abca")/P("abc_")
def return_conditional_probs3(markov_order, text):
    happy_dict = a_function(markov_order=markov_order, text=text)
    a_dict1 = n_long_word_occurrence(text=text, n=markov_order + 1)
    conditional_probs = {}

    for letters_, number_ in a_dict1.items():
        value = happy_dict[letters_[:markov_order]]
        conditional_probs[letters_] = number_ / value

    return conditional_probs


def markov_text_generation(first_order_probs, conditional_probs, n=1, length=300):
    if n == 1:
        first_letter = random.choices(alphabet_list, weights=list(first_order_probs.values()))[0]
        string = first_letter
        for x in range(0, length):
            if x == 0:
                conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                          key[0] == first_letter}
            else:

                conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                          key[0] == string[x - 1]}
            conditional_probs_temp = dict(sorted(conditional_probs_temp.items(), key=lambda kv: kv[0]))
            choice = random.choices(list(conditional_probs_temp.keys()), list(conditional_probs_temp.values()))[0][1]
            string += choice

        return string
    if n == 3:
        first_three_letters = 'the'
        # print(first_three_letters)
        string = first_three_letters
        for x in range(3, length, 1):
            if x == 3:
                conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                          key[:len(key) - 1] == first_three_letters}

            else:
                conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                          key[:len(key) - 1] == string[x - 3:x]}

            conditional_probs_temp = dict(sorted(conditional_probs_temp.items(), key=lambda kv: kv[0][-1]))
            choice = random.choices(list(conditional_probs_temp.keys()), list(conditional_probs_temp.values()))[0][-1]
            string += choice
        return string

    if n == 5:
        first_word = 'probability'
        string = first_word
        conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                  key[:len(key) - 1] == 'ility'}
        choice = random.choices(list(conditional_probs_temp.keys()), list(conditional_probs_temp.values()))[0][-1]

        string += choice
        for x in range(12, length, 1):
            conditional_probs_temp = {key: value for key, value in conditional_probs.items() if
                                      key[:len(key) - 1] == string[x - 5:x]}
            conditional_probs_temp = dict(sorted(conditional_probs_temp.items(), key=lambda kv: kv[0][-1]))
            choice = random.choices(list(conditional_probs_temp.keys()), list(conditional_probs_temp.values()))[0][-1]
            string += choice
        return string


def print_markov(markov_order, first_order_probs, text):
    print("Printing", markov_order, "Markov-order.")
    conditional_probs = return_conditional_probs3(markov_order=markov_order, text=text)
    generated_text = markov_text_generation(first_order_probs, conditional_probs, n=markov_order,
                                            length=len_of_generated_text)
    print(generated_text)
    average_word_length(generated_text)
    print()


alphabet = 'abcdefghijklmnopqrstuvwxyz '
alphabet_list = list(alphabet)

if __name__ == '__main__':
    len_of_generated_text = 300
    text = load_data()

    # Zeroth-order approximation
    print("Zeroth-order approximation:")
    generated_text = generate_text_with_given_weights(len_of_generated_text, alphabet_list, weights=None)
    print(generated_text)
    average_word_length(generated_text)
    print()

    # Frequency of letters in the text
    # The more letter occurs, the code assigned to this letter in Morse code is shorter.
    print("Frequency of letters in the text:")
    print(frequency_of_letters_in_text(alphabet, text))
    print()

    # First-order approximation
    print("First-order approximation:")
    first_order_probs = first_order_weights(alphabet, text)
    weights = list(first_order_probs.values())
    generated_text = generate_text_with_given_weights(len_of_generated_text, alphabet_list, weights=weights)
    print(generated_text)
    average_word_length(generated_text)
    print()

    # First-order Markov sources / conditional probabilities
    print_markov(markov_order=1, text=text, first_order_probs=first_order_probs)

    # Third-order Markov sources
    print_markov(markov_order=3, text=text, first_order_probs=first_order_probs)

    # Fifth-order Markov sources
    print_markov(markov_order=5, text=text, first_order_probs=first_order_probs)
