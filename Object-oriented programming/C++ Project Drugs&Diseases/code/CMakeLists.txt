cmake_minimum_required(VERSION 3.17)
project(ob1)

set(CMAKE_CXX_STANDARD 14)

add_executable(ob1 main.cpp drugs/Drug.cpp drugs/Drug.h cabinet.cpp cabinet.h Program.cpp Program.h Date.cpp Date.h drugs/FDAApproved.cpp drugs/FDAApproved.h drugs/AntiviralDrug.cpp drugs/AntiviralDrug.h drugs/Antibiotic.cpp drugs/Antibiotic.h drugs/FDANotApproved.cpp drugs/FDANotApproved.h drugs/Hallucinogen.cpp drugs/Hallucinogen.h drugs/unGroupedDrug.cpp drugs/unGroupedDrug.h drugs/Stimulant.cpp drugs/Stimulant.h Cabinet.cpp Cabinet.h drugs/Drug.cpp drugs/Drug.h)