#include <stdio.h>
#include <stdlib.h>
#include "Natural.h"

int main(int argc, char const *argv[]) {
  Natural n;
  n = assign(10);
  show(n);
  erase(n);
  n = NULL;
  Natural n1 = assign (213);
  Natural n2 = assign (32);

  printf("\n");
  printf("%d\n",is_greater(n2, n1));
  Natural n3 = add(n1, n2);
  show(n3);
  erase(n);
  printf("\n");
  printf("...Substraction...\n" );
  show(n1);
  printf(" - " );
  show(n2);
  n3 = sub(n1, n2);
  printf(" = " );
  show(n3);

  printf("\n");



  printf("...Multiplication...\n" );
  erase(n1);
  erase(n2);
  n1 = assign (62454);
  n2 = assign (7114545);
  show(n1);
  printf(" * " );
  show(n2);
  printf(" = " );
  n3 = mul(n1, n2);
  show(n3);

  printf("\n ...Comparison....\n" );
  erase(n1);
  erase(n2);
  n1 = assign (10);
  n2 = assign (33);
  show(n1);
  printf(" > " );
  show(n2);
  printf(" ? = " );
  int v = is_greater(n1, n2);
  printf("%d\n", v);
  printf("\n ...Factorial....\n" );
  unsigned long num = 1000;
  printf("%lu! = ", num);
  n3 = factorial(assign(num));
  show(n3);
  return 0;
}
