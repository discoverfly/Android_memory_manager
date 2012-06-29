#include <cstdlib>
#include <cstdio>
#include <algorithm>
#include <iostream>
#include <cmath>
#include <cstring>
#include <string>
#include <vector>
#include <ctime>
#include <map>
#include <set>
#include <queue>
using namespace std;

#define rep(i,a,b) for(int i = a; i < b; ++i)
#define pb(x) push_back(x)
#define sz size()
typedef long long ll;
const int N = 100000 + 11;


struct T{
    ll a,b;
}ar[N];

bool cmp(const T& x, const T& y){
    return x.b < y.b;
}

int n;
int d[N];

void getD()
{
    // ar[i].a  >= x >= d[i];
    d[0] = max(1, ar[0].a - ar[0].b + 1);
    for (int i = 1; i < n; ++i){
        
    }
}


int main() {
    int cas;
    for (cin >> cas; cas; --cas){
        scanf("%d",&n);
        int ta,tb;
       
        for (int i = 0; i  < n; ++i){
            scanf("%d%d",&ta,&tb);
            ar[i].a = ta;
            ar[i].b = tb;
        }
        sort(ar,ar+n,cmp);
        getD();
    }
}