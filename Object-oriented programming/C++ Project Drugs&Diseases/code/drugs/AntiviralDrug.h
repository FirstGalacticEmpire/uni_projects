#include "FDAApproved.h"

#ifndef OB1_ANTIVIRALDRUG_H
#define OB1_ANTIVIRALDRUG_H


class AntiviralDrug : public FDAApproved {
private:
    string virusType;
public:

    AntiviralDrug(int id, double price, const string &name, const string &approvalId, string virusType = "RNA")
            : FDAApproved(id, price, name, approvalId), virusType(move(virusType)) {}

    string toString() override;
};


#endif //OB1_ANTIVIRALDRUG_H
