#include <stdio.h>
#include <stdlib.h>
#include "Scheduler.h"

 int main(int argc, char const *argv[]) {
 	Scheduler sch;
  Create(&sch);
  Show(sch);
  Insert(&sch, "Low priority process", 'B');
  Insert(&sch, "High priority process", 'A');
  Show(sch);
  Destroy(&sch);
  Show(sch);
  ReadFromFile("dataFile", &sch);
  Show(sch);
  RemoveHighProc(&sch, "Printer daemon");
  Show(sch);
 	return 0;
 }
