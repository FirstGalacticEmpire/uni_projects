#include "AntiviralDrug.h"

string AntiviralDrug::toString() {
    return FDAApproved::toString() + "Antiviral drug. It's virus type is: " + virusType + ".";
}

