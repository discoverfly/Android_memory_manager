#include <cstdio>
#include <algorithm>
#include <cstring>
#include <iostream>
#include <vector>
using namespace std;

const int N = 10003;
int n,m;
vector<int> adj[N];
double ar[N][3];
double k[N],e[N];

void dfs(int cur,int p = -1 ){
	double s = adj[cur].size();
	ar[cur][0] = 100;
	ar[cur][1] = k[cur];
	if(p!= -1)
		ar[cur][2] = (100 - k[cur] - e[cur])/s;
	else ar[cur][2] = 0;
	double t = (100 - k[cur] - e[cur])/s;

	double det = 1.0;
	for (int i = 0; i < adj[cur].size();++i){
		int v = adj[cur][i];
		dfs(v,cur);
		ar[cur][0] += t*ar[v][0];
		ar[cur][1] += t*ar[v][1];
		det -= t*ar[cur][2];
	}

	for(int i = 0; i < 3; ++i)
		ar[cur][i]/=det;
}

int mian(){
	int cas;
	for (cin >> cas;cas; --cas ){
		cin >> n;
		int a,b;
		for (int i  = 1; i <= n; ++i)
			adj[i].clear();
		for (int i = 1; i < n;++i){
			scanf("%d%d",&a,&b);
			adj[a].push_back(b);
			adj[b].push_back(a);
		}
		for (int i = 1; i <= n; ++i){
			scanf("%lf%lf",&k[i],&e[i]);
		}
	    dfs(1);
	    for (int i = 1; i <= n;++i){
	    	for(int j = 0; j < 3; ++j)
	    		cout << ar[i][j] << " ";
	    	cout << endl;
	    }
	}
	return 0;
}

