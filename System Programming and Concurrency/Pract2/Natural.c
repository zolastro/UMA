#include <stdio.h>
#include <stdlib.h>
#include "Natural.h"
#include <math.h>

Natural recursiveAdd(Natural n1, Natural n2, int carry);
Natural recursiveSub(Natural n1, Natural n2, int carry);
void recursiveIsGreater(Natural n1, Natural n2, int* state);

Natural assign (unsigned long v){
  Natural n;
  if(v < 10){
    n = (Node *)malloc(sizeof(Node));
    n->value = v;
    n->next = NULL;
  }else{
    n = (Node *)malloc(sizeof(Node));
    n->value = v % 10;
    n->next = assign(v/10);
  }
  return n;
}

void show (Natural n){
  if(n == NULL){
    printf("-");
    return;
  }
  if(n->next == NULL){
    printf("%d",n->value);
  }else {
    show(n->next);
    printf("%d",n->value);
  }
}

void erase(Natural n){
  Natural prev = NULL;
  while(n != NULL){
    prev = n;
    n = n->next;
    free(prev);
  }
}

Natural add(Natural n1, Natural n2){
  return recursiveAdd(n1, n2, 0);
}

Natural sub(Natural n1, Natural n2){

  if(is_greater(n2, n1)){
    return assign(0);
  }
  Natural ans =  recursiveSub(n1, n2, 0);
  //To prevent something like 000000...0

}

Natural addZeroesAndDigits(unsigned long z, unsigned long result){
  // printf("Zeroes:%d, result: %d\n",z, result );
  Natural n = NULL;
  if(z > 0){
    n = (Node*) malloc(sizeof(Node));
    n->value = 0;
    n->next = addZeroesAndDigits(z-1, result);
  }else if(z == 0 && result > 0){
      n = (Node*) malloc(sizeof(Node));
      n->value = result%10;
      n->next = addZeroesAndDigits(0, result/10);
  }
  return n;

}

Natural mul(Natural n1, Natural nSecond){
  Natural n = (Node *)malloc(sizeof(Node));
  n->value = 0;
  n->next = NULL;
  int i = 0;
  while(n1 != NULL){
    int d1 = n1->value;
    int j = 0;
    Natural n2 = nSecond;
    while(n2 != NULL){
      int d2 =n2->value;
      //There is overflow here :(
      unsigned long result = (d1*d2);
      Natural nB = addZeroesAndDigits(i+j, result);
      n = add(n, nB);
      n2 = n2->next;
      j++;
    }
    n1 = n1->next;
    i++;
  }
  return n;
}


int is_greater(Natural n1, Natural n2){
  int state = -1;
  recursiveIsGreater(n1, n2, &state);
  if(state == -1){
    state = 0;
  }
  return state;
}


void recursiveIsGreater(Natural n1, Natural n2, int* state){
  if(n1 != NULL && n2 != NULL){
    // show(n1);
    // printf(" /// " );
    // show(n2);
    // printf("\n" );
    // printf("Go to next\n" );
    recursiveIsGreater(n1->next, n2->next, state);
    if(*state == -1){
      if(n1->value == n2 ->value){
        *state = -1;
      }else{
        // printf("%d // %d // %d\n", n1->value, n2->value, n1->value > n2->value);
        *state = n1->value > n2->value;
      }
    }
  }else if(n1 != NULL && n2 == NULL){
    // printf("Node: " );
    // show(n1);
    // printf("\n" );
    while(n1 != NULL && n1->value == 0){
      n1 = n1->next;
    }
    if(n1 != NULL){
      // There are not just a bunch of zeroes ahead...
      *state = 1;
    }
  }else if(n1 == NULL && n2 != NULL){
    // printf("Node: " );
    // show(n1);
    // printf("\n" );
    while(n2 != NULL && n2->value == 0){
      n2 = n2->next;
    }
    if(n2 != NULL){
      // There are not just a bunch of zeroes ahead...
      *state = 0;
    }
  }
}

Natural recursiveAdd(Natural n1, Natural n2, int carry){
  Natural n = NULL;
  int carry_aux;
  if(n1 != NULL && n2 != NULL){

    n = (Node*) malloc(sizeof(Node));
    n->value = (n1->value + n2->value + carry)%10;
    carry_aux = (n1->value + n2->value + carry)/10;
    n->next = recursiveAdd(n1->next, n2->next, carry_aux);
  }else if(n1 == NULL && n2 == NULL){
    if(carry_aux == 1){
        n = (Node*) malloc(sizeof(Node));
        n->value = 1;
        n->next = NULL;
    }
  }else if(n1 != NULL && n2 == NULL){
    n = (Node*) malloc(sizeof(Node));
    n->value = (n1->value + carry)%10;
    carry_aux = (n1->value + carry)/10;
    n->next = recursiveAdd(n1->next, n2, carry_aux);
  }else if(n1 == NULL && n2 != NULL){
      n = (Node*) malloc(sizeof(Node));
      n->value = (n2->value + carry)%10;
      carry_aux = (n2->value + carry)/10;
      n->next = recursiveAdd(n1, n2->next, carry_aux);
  }
  return n;
}

Natural factorial(Natural f){
  // printf("\n");
  // show(f);
  // printf("\n");
  if(is_greater(assign(1), f)){
    return assign(1);
  }else{
    // printf("Recursion: Current f: " );
    // show(f);
    Natural aux = assign(1);
    aux = sub(f, aux);
    // printf(", next f: ");
    // show(aux);
    // printf("\n" );
    return mul(f, factorial(sub(f, assign(1))));
  }
}


Natural recursiveSub(Natural n1, Natural n2, int carry){
  Natural n = NULL;
  int carry_aux;
  int val;
  if(n1 != NULL && n2 != NULL){
    n = (Node*) malloc(sizeof(Node));
    val = (n1->value - n2->value - carry);
    carry_aux = 0;
    if(val < 0){
      val += 10;
      carry_aux = 1;
    }
    n->value = val;
    n->next = recursiveSub(n1->next, n2->next, carry_aux);
  }else if(n1 != NULL && n2 == NULL){
    n = (Node*) malloc(sizeof(Node));
    val = (n1->value - carry)%10;
    carry_aux = 0;
    if(val < 0){
      val += 10;
      carry_aux = 1;
    };
    n->value = val;
    n->next = recursiveAdd(n1->next, n2, carry_aux);
  }else if(n1 == NULL && n2 != NULL){
      n = (Node*) malloc(sizeof(Node));
      val = (n2->value - carry)%10;
      carry_aux = 0;
      if(val < 0){
        val += 10;
        carry_aux = 1;
      };
      n->value = val;
      n->next = recursiveAdd(n1, n2->next, carry_aux);
  }
  return n;
}
