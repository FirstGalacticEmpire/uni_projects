#include "Program.h"
#include <random>
#include <string>

string random_string(int length) {
    string str("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

    random_device rd;
    mt19937 generator(rd());

    shuffle(str.begin(), str.end(), generator);
    return str.substr(0, length);
}

void Program::addRandomDrugsToCabinet(Cabinet<Drug> &cabinet, int numberToAdd) {
    random_device random_device; // create object for seeding
    mt19937 engine{random_device()}; // create engine and seed it

    for (int i = 0; i < numberToAdd; i++) {
        uniform_int_distribution<> dist(1, 5); // create distribution for integers with [x;y] range
        auto random_number = dist(engine);
        switch (random_number) {
            case 1: {
                cabinet += new Hallucinogen(i, 2, random_string(5 + i), (random_number % 2 != 0),
                                            (random_number % 2 != 0),
                                            random_string(5), (random_number % 2 != 0));
                break;
            }

            case 2: {
                uniform_int_distribution<> spider(1, 8999999); // create distribution for integers with [x;y] range
                auto chemSpider = spider(engine);
                cabinet += new unGroupedDrug(i, 2, random_string(5), (random_number % 2 != 0),
                                             (random_number % 2 != 0),
                                             chemSpider);
                break;
            }
            case 3: {
                cabinet += new Stimulant(i, 2, random_string(5), (random_number % 2 != 0),
                                         (random_number % 2 != 0), random_string(5 + i));
                break;
            }
            case 4: {
                cabinet += new AntiviralDrug(i, 2, random_string(5), random_string(10), random_string(3));
                break;
            }
            case 5: {
                cabinet += new Antibiotic(i, 2, random_string(5), random_string(10),
                                          random_number % 2 != 0 ? "negative" : "positive",
                                          (random_number % 2 != 0));
                break;
            }
            default: {
                break;
            }
        }

    }
}

void Program::run_program(Cabinet<Drug> &cabinet) const {
    while (cabinet.numberOfItems() > 0) {
        cabinet.listElementsInCabinet();
        cout << "Number of drugs in cabinet: " << cabinet.numberOfItems() << endl;
        cout << endl;
        this->pandemic(cabinet);
    }
}

void Program::pandemic(Cabinet<Drug> &cabinet) const {
    for (int i = cabinet.numberOfItems() - 1; i >= 0; i--) {

        random_device rd;
        mt19937 mt(rd());
        uniform_real_distribution<double> dist(0.0, 1.0);

        if (dist(mt) <= this->pDisease) {

            Drug *drug = cabinet -= i; //looks terribly but works, can be very misleading.
            //drugs can be removed from the cabinet also in the following way:
            //cabinet -= new Drug(1, 2, "example name");
            // thanks to overloading of operator == in class drug. (Choosing drug with the same name and id)

            if (dist(mt) <= (1.0 - this->pRecovery)) {
                cabinet += drug;
            } else {
                delete drug;
            }
        }
    }
}

Program::Program(double pDisease, double pRecovery) : pDisease(pDisease), pRecovery(pRecovery) {}



