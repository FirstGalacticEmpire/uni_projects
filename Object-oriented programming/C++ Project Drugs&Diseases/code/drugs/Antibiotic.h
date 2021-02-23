#include "FDAApproved.h"

#ifndef OB1_ANTIBIOTIC_H
#define OB1_ANTIBIOTIC_H

class Antibiotic : public FDAApproved {
private:
    string gram;
    bool antimicrobialResistance;
public:

    Antibiotic(int id, double price, const string &name, const string &approvalId, string gram ="Negative",
               bool antimicrobialResistance=false) : FDAApproved(id, price, name, approvalId), gram(std::move(gram)),
                                               antimicrobialResistance(antimicrobialResistance) {}

    string toString() override;
};


#endif //OB1_ANTIBIOTIC_H
