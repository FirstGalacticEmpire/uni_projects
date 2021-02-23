#include "Date.h"
using namespace std;

void Date::generateRandomDate(tm &aDate) {
    random_device random_device;
    mt19937 engine{random_device()};

    aDate.tm_hour = 0;
    aDate.tm_min = 0;
    aDate.tm_sec = 0;

    uniform_int_distribution<> year(2000, 2020);
    auto random_year = year(engine);
    aDate.tm_year = random_year;

    uniform_int_distribution<> month(1, 12);
    auto random_month = month(engine);
    aDate.tm_mon = random_month;

    uniform_int_distribution<> day(1, 30);
    auto random_day = day(engine);
    aDate.tm_mday = random_day;
}

string Date::dateTostring() const {
    string day =
            this->randomDate.tm_mday <= 9 ? "0" + to_string(this->randomDate.tm_mday) : to_string(
                    this->randomDate.tm_mday);

    string month = this->randomDate.tm_mon <= 9 ? "0" + to_string(this->randomDate.tm_mon) : to_string(
            this->randomDate.tm_mon);

    string year = to_string(this->randomDate.tm_year);

    return day+"."+month +"."+year;
}

Date::Date() {
    Date::generateRandomDate(this->randomDate);
}












