#include "FDANotApproved.h"
#ifndef OB1_STIMULANT_H
#define OB1_STIMULANT_H


class Stimulant : public FDANotApproved {
private:
    string stimulantType;
public:

    Stimulant(int id, double price, const string &name, bool appendingApproval, bool isIllegal,
              string stimulantType) : FDANotApproved(id, price, name, appendingApproval, isIllegal),
                                      stimulantType(std::move(stimulantType)) {}

    string toString() override;
};



#endif //OB1_STIMULANT_H
