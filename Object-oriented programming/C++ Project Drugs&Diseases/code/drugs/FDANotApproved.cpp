#include "FDANotApproved.h"

string FDANotApproved::toString() {
    string legal = this->appendingApproval ? " legal, " : " not legal, ";
    return "FDA not-approved drug,"  + legal + Drug::getName();
}

