#include "../Date.h"
#include "Drug.h"

#ifndef OB1_FDAAPPROVED_H
#define OB1_FDAAPPROVED_H


class FDAApproved : public Drug {
private:
    Date *dateApproved = new Date;
    string approvalId;
public:
    FDAApproved(int id, double price, const string &name, string approvalId) : Drug(id, price, name),
                                                                               approvalId(std::move(approvalId)) {}
    string toString() override;
};


#endif //OB1_FDANOTAPPROVED_H
