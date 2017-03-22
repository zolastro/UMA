#ifndef NATURAL_H_
#define NATURAL_H_


struct _node;
typedef struct _node * Linked_List;
typedef struct _node{
 unsigned short value;
 Linked_List next;
} Node;

typedef Linked_List Natural;


Natural assign(unsigned long v);// Creates a Natural and initializes to the value of v
void erase(Natural n); // Deallocates the memory used by n
void show(Natural n); // Displays the actual value of the Natural
Natural add(Natural n1, Natural n2); // Returns the sum of n1 and n2
Natural sub(Natural n1, Natural n2); // Returns the sum of n1 and n2
Natural mul(Natural n1, Natural n2);
Natural factorial(Natural f);
#endif
