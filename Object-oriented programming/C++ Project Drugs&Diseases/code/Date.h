#include <iostream>
#include <utility>
#include <string>
#include <cstdio>
#include <ctime>
#include <cstdlib>
#include <iostream>
#include <random>

#ifndef OB1_DATE_H
#define OB1_DATE_H

using namespace std;
class Date {
private:
    struct tm randomDate{};
    static void generateRandomDate(tm & aDate);
public:
    Date();
    string dateTostring() const;
};
#endif //OB1_DATE_H
