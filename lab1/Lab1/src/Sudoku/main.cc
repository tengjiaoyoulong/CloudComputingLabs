#include <assert.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/time.h>
#include <pthread.h>
#include "sudoku.h"
#include<stdlib.h>
 typedef struct {
   int first;
   int last;
   int** tqa;//问题和结果数组(test question and answer)
  }threadtask;

void* threadfun(void* args){
threadtask* task = (threadtask*) args;
   int first=task->first;
   int last=task->last;
   int** tqa=task->tqa;
   int i=0;

   for(i=first;i<last;i++){
solve_sudoku_dancing_links(tqa[i]);

}
pthread_exit(NULL);
return NULL;
}


int main(int argc, char* argv[])
{

char testfile[128]= "\0";
while(scanf("%s",testfile)){

  
  char puzzle[128];

FILE* f1 = fopen(testfile, "r");
int testnumber=0;
while (fgets(puzzle, sizeof puzzle, f1) != NULL) {
testnumber++;
}
fclose(f1);

int threadnumber=8;

    int i=0;
    int row = testnumber,col = N;
    //申请
    int **testqa = (int**)malloc(sizeof(int*) * row);  //sizeof(int*),不能少*，一个指针的内存大小，每个元素是一个指针。
    for (i = 0;i < row;i++)
    {
        testqa[i] = (int*)malloc(sizeof(int) * col);
    }




FILE* fp = fopen(testfile, "r");
i=0;
char tt[testnumber][128];//临时数组
while (fgets(tt[i], sizeof tt[i], fp) != NULL) {
i++;
}


//转换
for(int t1=0;t1<testnumber;t1++){
for (int t2 = 0; t2 < N; t2++) {
    testqa[t1][t2] = (int)(tt[t1][t2] - '0');
}}


int pertask= testnumber/threadnumber;
threadtask threadgroup[threadnumber];
pthread_t th[threadnumber]; 

if(pertask>=1){

   for(i=0;i<threadnumber;i++)
   {
     int first=(int)pertask*i;
     int last;
     if(i!=threadnumber-1)
       last=(int)pertask*(i+1);
     else
       last=testnumber;


     threadgroup[i].first=first;
     threadgroup[i].last=last;
     threadgroup[i].tqa=testqa;

     if(pthread_create(&th[i], NULL, threadfun, &threadgroup[i])!=0)
     {
       perror("pthread_create failed");
       exit(1);
     }

   }

   for(i=0;i<threadnumber;i++)
   pthread_join(th[i], NULL);



}
else{
 threadgroup[0].first=0;
     threadgroup[0].last=testnumber;
     threadgroup[0].tqa=testqa;
     if(pthread_create(&th[0], NULL, threadfun, &threadgroup[0])!=0)
     {
       perror("pthread_create failed");
       exit(1);
     }
pthread_join(th[0], NULL);
}




int j=0;
int k=0;
for(j=0;j<testnumber;j++){
for(k=0;k<N;k++){
printf("%d",testqa[j][k]);
}
printf("\n");
}


free(testqa);
fflush(stdout);
}
  return 0;
}

