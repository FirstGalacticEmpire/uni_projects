#include "Antibiotic.h"

string Antibiotic::toString() {
    return FDAApproved::toString() + "Resistant: " + BooleanToString(antimicrobialResistance) + ". Gram Type: " +
           gram +".";
}

