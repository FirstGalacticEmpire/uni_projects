#include <iostream>
#include <utility>
#include <vector>
#include "drugs/Drug.h"

#ifndef OB1_CABINET_H
#define OB1_CABINET_H


template <class T>
class Cabinet {
private:
    vector<T *> listOfItems;
public:

    Cabinet &operator+=(T *item) {
        this->listOfItems.push_back(item);
        return *this;
    }

    Cabinet &operator-=(T *item) {
        typename vector<T *>::iterator iter;
        for (iter = this->listOfItems.begin(); iter != this->listOfItems.end(); iter++) {
            if ((*iter) == item) {
                iter = this->listOfItems.erase(iter); // would be need if we wanted to use iterator later in next iteration.
                return *this;
            }
        }
        return  *this;
    }

//    Cabinet &operator-=(int index) {
//        this->listOfItems.erase(this->listOfItems.begin() + index);
//        return *this;
//    }

    T* operator-=(int index)
    {
        T* drug = this->listOfItems[index];
        this->listOfItems.erase(this->listOfItems.begin()+index);
        return drug;
    }

    void listElementsInCabinet() {
        for (auto &iterator : this->listOfItems) {
            cout << iterator->toString() << endl;
        }
    }

    int numberOfItems() {
        return this->listOfItems.size();
    }

};


#endif //OB1_CABINET_H
