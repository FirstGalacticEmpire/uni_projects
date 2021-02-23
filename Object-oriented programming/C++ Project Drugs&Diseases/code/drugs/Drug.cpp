#include "Drug.h"

bool Drug::operator==(const Drug &drug) {
    if (this->id == drug.id && this->name == drug.name) {
        return true;
    } else {
        return false;
    }
}
