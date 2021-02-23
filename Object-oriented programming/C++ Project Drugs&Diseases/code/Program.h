#include <iostream>
#include <random>
#include "Cabinet.h"
#include "drugs/Drug.h"
#include "drugs/Hallucinogen.h"
#include "drugs/unGroupedDrug.h"
#include "drugs/Stimulant.h"
#include "drugs/AntiviralDrug.h"
#include "drugs/Antibiotic.h"

#ifndef OB1_PROGRAM_H
#define OB1_PROGRAM_H
using namespace std;

class Program {
private:
    double pDisease;
    double pRecovery;
public:
    Program(double pDisease, double pRecovery);
    static void addRandomDrugsToCabinet(Cabinet<Drug> &cabinet, int numberToAdd);
    void run_program(Cabinet<Drug> &cabinet) const;
    void pandemic(Cabinet<Drug> &cabinet) const;
};

#endif //OB1_PROGRAM_H
