#include "FDANotApproved.h"
#ifndef OB1_UNGROUPEDDRUG_H
#define OB1_UNGROUPEDDRUG_H


class unGroupedDrug : public FDANotApproved {
private:
    int ChemSpider;
public:
    unGroupedDrug(int id, double price, const string &name, bool appendingApproval, bool isIllegal, int chemSpider)
            : FDANotApproved(id, price, name, appendingApproval, isIllegal), ChemSpider(chemSpider) {}

    string toString() override;
};



#endif //OB1_UNGROUPEDDRUG_H
