#include "Drug.h"
#ifndef OB1_FDANOTAPPROVED_H
#define OB1_FDANOTAPPROVED_H

class FDANotApproved : public Drug {
private:
    bool appendingApproval;
    bool isIllegal;
public:
    FDANotApproved(int id, double price, const string &name, bool appendingApproval=true, bool isIllegal=false) : Drug(id, price,
                                                                                                                  name),
                                                                                                             appendingApproval(
                                                                                                                     appendingApproval),
                                                                                                             isIllegal(
                                                                                                                     isIllegal) {}

public:

    string toString() override;
};


#endif //OB1_FDANOTAPPROVED_H
