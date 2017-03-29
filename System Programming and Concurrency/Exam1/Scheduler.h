#ifndef SCHEDULER_H_
#define SCHEDULER_H_

struct _node;
typedef struct _node* TList;
typedef struct _sch {
  TList high;
  TList low;
} Scheduler;
typedef struct _node{
  char* name;
  char type;
  TList next;
} TProc;


void Create(Scheduler *plf);

void ReadFromFile(char *namef, Scheduler *plf);

void RemoveHighProc(Scheduler *plf, char *name);

void Show(Scheduler plf);
#endif
