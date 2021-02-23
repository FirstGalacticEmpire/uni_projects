#include "unGroupedDrug.h"

string unGroupedDrug::toString() {
    return FDANotApproved::toString() + " ungrouped drug. It's ChemSpider is: " + to_string(ChemSpider) + ".";
}
