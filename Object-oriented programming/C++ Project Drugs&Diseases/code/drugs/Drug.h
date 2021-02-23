#include <iostream>
#include <utility>
#include <string>
#include "../Date.h"

#ifndef OB1_DRUG_H
#define OB1_DRUG_H


using namespace std;

inline string BooleanToString(bool b) {
    return (b ? "true" : "false");
}

class Drug {
private:
    int id;
    double price;
    string name;
public:

    Drug(int id, double price, string name) : id(id), price(price), name(std::move(name)) {}

    virtual bool operator==(const Drug &drug);

    virtual string toString() { // inline function shouldn't be move to cpp file i think
        return string();
    }

    const string &getName() const { // inline function shouldn't be move to cpp file i think
        return name;
    }

};

#endif //OB1_DRUG_H
