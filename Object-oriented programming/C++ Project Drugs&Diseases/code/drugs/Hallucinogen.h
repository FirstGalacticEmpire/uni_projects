#include "FDANotApproved.h"
#ifndef OB1_HALLUCINOGEN_H
#define OB1_HALLUCINOGEN_H


class Hallucinogen : public FDANotApproved {
private:
    string baseOn;
    bool plantBased;
public:
    Hallucinogen(int id, double price, const string &name, bool appendingApproval, bool isIllegal, string baseOn,
                 bool plantBased=true) : FDANotApproved(id, price, name, appendingApproval, isIllegal),
                                    baseOn(std::move(baseOn)),
                                    plantBased(plantBased) {}

    string toString() override;
};


#endif //OB1_HALLUCINOGEN_H
