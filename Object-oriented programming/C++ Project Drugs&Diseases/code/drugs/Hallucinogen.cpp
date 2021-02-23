#include "Hallucinogen.h"

string Hallucinogen::toString() {
    return FDANotApproved::toString() + " Hallucinogen." + " Based on: " + baseOn + "." + " Plant based: " +
           BooleanToString(plantBased) + ".";
}
