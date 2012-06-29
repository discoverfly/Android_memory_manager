

#include <cstdio>
#include <algorithm>
#include <cstring>
#include <cmath>
#include <iostream>
using namespace std;
const int N = 11, MaxN = 10001, S = 100000, M = 1000000;
int n, m;
char mat[N][N];

struct Hash {
    int g[MaxN], v[M], sum[M], next[M], tail, key;

    void init() {
        memset(g, 0, sizeof (g));
        tail = 2;
    }

    bool find(int t) {
        key = t % MaxN;
        for (int id = g[key]; id; id = next[id])
            if (v[id] == t) {
                ++sum[id];
                return true;
            }

        v[tail] = t;
        sum[tail] = 1;
        next[tail] = g[key];
        g[key] = tail++;
        return false;
    }

    int getSum(int t) {
        key = t % MaxN;
        for (int id = g[key]; id; id = next[id])
            if (v[id] == t) return sum[id];
        return 0;
    }

} hash;

void read() {
    for (int i = n - 1; i >= 0; --i)
        scanf("%s", mat[i]);
    n++;
    for (int i = 0; i < m; ++i) mat[n - 1][i] = '#';
    mat[n - 1][m] = '\0';
}

struct T {
    int i, j, s;

    void print() {
        printf("i:%d j:%d s:%10d\n", i, j, s);
    }
} q[S];

int hV(const T &x) {
    return x.i * 100000000 + x.j * 10000000 + x.s;
}

void bfs() {
    hash.init();
    int f = 0, r = 0;
    T t, te;
    t.i = 0;
    t.j = m;
    t.s = 1 + (2 << (2 * m - 2));
    hash.find(hV(t));
    q[r++] = t;
    int i, x, y, k, flag, temp;
    while (f < r) {
        t = q[f++];
        t.print();
        printf("%d\n", hash.getSum(hV(t)));
        if (f >= S) f -= S;

        if (t.i < n && t.j == m) {
            te.i = t.i + 1;
            te.j = 0;
            te.s = t.s << 2;
            if (hash.find(hV(te)) == false) {
                q[r++] = te;
                if (r >= S) r -= S;
            }
            continue;
        }
        if (t.i <= n && t.j < m) {
            if (mat[t.i - 1][t.j] == '.') {
                te.i = t.i;
                te.j = t.j + 1;
                i = t.j;
                x = (t.s >> (i * 2))&3;
                y = (t.s >> (i * 2 + 2))&3;
                if (!x && !y) {
                    te.s = t.s^(1 << (i * 2))^(2 << (i * 2 + 2));
                    if (hash.find(hV(te)) == false) {
                        q[r++] = te;
                        if (r >= S) r -= S;
                    }
                } else if (x && y) {
                    t.s = t.s ^(x << (i * 2)) ^(y << (i * 2 + 2));
                    if (x == 1 && y == 1) {
                        k = 2 * i + 4;
                        flag = 0;
                        while (true) {
                            temp = (t.s >> k)&3;
                            if (temp == 2) {
                                if (flag == 0) break;
                                --flag;
                            } else if (temp == 1) ++flag;
                            k += 2;
                        }
                        te.s = t.s^(3 << k);
                        if (hash.find(hV(te)) == false) {
                            q[r++] = te;
                            if (r >= S) r -= S;
                        }
                    } else if (x == 2 && y == 2) {
                        k = 2 * i - 2;
                        flag = 0;
                        while (true) {
                            temp = (t.s >> k)&3;
                            if (temp == 1) {
                                if (flag == 0) break;
                                --flag;
                            } else if (temp == 2) ++flag;
                            k -= 2;
                        }
                        te.s = t.s^(3 << k);
                        if (hash.find(hV(te)) == false) {
                            q[r++] = te;
                            if (r >= S) r -= S;
                        }
                    } else if (x == 1 && y == 2) {
                        te.s = t.s;
                        if (hash.find(hV(te)) == false) {
                            q[r++] = te;
                            if (r >= S) r -= S;
                        }

                    } else if (x == 2 && y == 1) {
                        te.s = t.s;
                        if (hash.find(hV(te)) == false) {
                            q[r++] = te;
                            if (r >= S) r -= S;
                        }
                    }

                } else {
                    t.s = t.s ^(x << (i * 2)) ^(y << (i * 2 + 2));
                    te.s = t.s^((x + y) << (i * 2));
                    if (hash.find(hV(te)) == false) {
                        q[r++] = te;
                        if (r >= S) r -= S;
                    }
                    if (te.j != m) {
                        te.s = t.s^((x + y) << (2 * i + 2));
                        if (hash.find(hV(te)) == false) {
                            q[r++] = te;
                            if (r >= S) r -= S;
                        }
                    }
                }
            } else {
                i = t.j;
                x = (t.s >> (i * 2))&3;
                y = (t.s >> (i * 2 + 2))&3;
                if (!x && !y) {
                    te.i = t.i;
                    te.j = t.j + 1;
                    te.s = t.s;
                    if (hash.find(hV(te)) == false) {
                        q[r++] = te;
                        if (r >= S) r -= S;
                    }
                }
            }
        }
    }
}

void solve() {
    if (n == 1) {
        printf("1\n");
        return;
    }
    bfs();
    T te;
    te.i = n;
    te.j = m;
    te.s = 0;
    printf("%d\n", hash.getSum(hV(te)));
}

int main() {
    while (cin >> n >> m && (n || m)) {
        read();
        for (int i = 0; i < n; ++i)
            printf("%s\n", mat[i]);
        solve();
    }
}
