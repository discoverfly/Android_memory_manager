#include <iostream>
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <vector>
#include <string>
using namespace std;

const int N = 1000;
struct T{
    double  x,y,z;
}ar[N];

int n;

bool cmp(const T& a,const T& b){
    return a.x*a.x + a.y*a.y < b.x*b.x + b.y*b.y;
}
int main(){
    scanf("%d",&n);
    for(int i=0;i<n;++i){
        scanf("%lf%lf%lf",&ar[i].x,&ar[i].y,&ar[i].z);
    }
    sort(ar,ar+n,cmp);
    for(int i=0;i<n;++i) printf("%.6lf\n",ar[i].z);
}
