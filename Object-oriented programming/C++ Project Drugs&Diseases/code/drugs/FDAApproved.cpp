#include "FDAApproved.h"

string FDAApproved::toString() {
    return "FDA approved drug: " + Drug::getName() + "." + " Approved: " +
           dateApproved->dateTostring() + ". ApprovalId: " + approvalId + ". ";
}
