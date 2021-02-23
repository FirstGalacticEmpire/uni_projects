#include "Cabinet.h"
#include "Program.h"

int main() {
    int numberToAdd;
    double pDisease, pRecovery;

    cout << "Enter number of random drugs to add to cabinet (int), then two floats pDisease and pRecovery:" << endl;
    while (true) {
        if ((!(cin >> numberToAdd >> pDisease >> pRecovery)) || (!(pDisease > 0.0 && pDisease <= 1.0)) ||
                                                                (!(pRecovery > 0.0 && pRecovery <= 1.0))) {
            cin.clear();
            cin.ignore(1024, '\n');
            cout << "Incorrect entry. Try again:" << endl;
        } else {
            break;
        }
    }
    cout << endl;

//    Cabinet<Drug> cabinet;
//    Program::addRandomDrugsToCabinet(cabinet, 105);
//    Program program(0.2, 0.3);
//    program.run_program(cabinet);

    Cabinet<Drug> cabinet;
    Program::addRandomDrugsToCabinet(cabinet, numberToAdd);
    Program program(pDisease, pRecovery);
    program.run_program(cabinet);
}
