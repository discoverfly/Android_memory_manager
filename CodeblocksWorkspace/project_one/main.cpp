#include <iostream>
#include <algorithm>
#include <vector>
#include <cmath>
#include<cstdio>
//#include<fstream>

using namespace std;

const double DPINF = 1e100;
const double DM_PI=acos(-1.0);
const double Eps = 1e-12;

struct Point {
	double x, y;
	Point() {}
	Point(double x, double y) : x(x), y(y) {}
	Point(const Point &p) : x(p.x), y(p.y)    {}
	Point operator + (const Point &p)  const { return Point(x+p.x, y+p.y); }
	Point operator - (const Point &p)  const { return Point(x-p.x, y-p.y); }
	Point operator * (double c)     const { return Point(x*c,   y*c  ); }
	Point operator / (double c)     const { return Point(x/c,   y/c  ); }
};

double dot(Point p, Point q)     { return p.x*q.x+p.y*q.y; }
double dist2(Point p, Point q)   { return dot(p-q,p-q); }
double cross(Point p, Point q)   { return p.x*q.y-p.y*q.x; }



Point RotateCCW90(Point p)   { return Point(-p.y,p.x); }
Point RotateCW90(Point p)    { return Point(p.y,-p.x); }


Point PotSeg(Point a, Point b, Point c) {
	double r = dot(b-a,b-a);
	if (fabs(r) < Eps) return a;
	r = dot(c-a, b-a)/r;
	if (r < 0) return a;
	if (r > 1) return b;
	return a + (b-a)*r;
}


bool LinesParallel(Point a, Point b, Point c, Point d) {
	return fabs(cross(b-a, c-d)) < Eps;
}

bool LinesCollinear(Point a, Point b, Point c, Point d) {
	return LinesParallel(a, b, c, d) && fabs(cross(a-c, d-c)) < Eps;
}


bool SegmentsIntersect(Point a, Point b, Point c, Point d) {
	if (LinesCollinear(a, b, c, d)) {
		if (dist2(a, c) < Eps || dist2(a, d) < Eps ||
			dist2(b, c) < Eps || dist2(b, d) < Eps) return true;
		if (dot(c-a, c-b) > 0 && dot(d-a, d-b) > 0 && dot(c-b, d-b) > 0)
			return false;
		return true;
	}
	if (cross(d-a, b-a) * cross(c-a, b-a) > 0) return false;
	if (cross(a-c, d-c) * cross(b-c, d-c) > 0) return false;
	return true;
}

Point ComputeLineIntersection(Point a, Point b, Point c, Point d) {
	b=b-a; d=c-d; c=c-a;

	return a + b*cross(c, d)/cross(b, d);
}

Point ComputeCircleCenter(Point a, Point b, Point c) {
	b=(a+b)/2;
	c=(a+c)/2;
	return ComputeLineIntersection(b, b+RotateCW90(a-b), c, c+RotateCW90(a-c));
}


bool PointInPolygon(const vector<Point> p, Point q) {
	bool c = 0;
	for (int i = 0; i <(int)p.size(); i++){
		int j = (i+1)%(int)p.size();
		if ((p[i].y <= q.y && q.y < p[j].y ||
			p[j].y <= q.y && q.y < p[i].y) &&
			q.x < p[i].x + (p[j].x - p[i].x) * (q.y - p[i].y) / (p[j].y - p[i].y))
			c = !c;
	}
	return c;
}

bool PotOnPol(const vector<Point> p, Point q) {
	for (int i = 0; i <(int)p.size(); i++)
		if (dist2(PotSeg(p[i], p[(i+1)%p.size()], q), q) < Eps)
			return true;
	return false;
}


vector<Point> CirLinInt(Point a, Point b, Point c, double r) {
	vector<Point> PotAns;
	b = b-a;
	a = a-c;
	double A = dot(b, b);
	double B = dot(a, b);
	double C = dot(a, a) - r*r;
	double D = B*B - A*C;
	if (D < -Eps) return PotAns;
	PotAns.push_back(c+a+b*(-B+sqrt(D+Eps))/A);
	if (D > Eps)
		PotAns.push_back(c+a+b*(-B-sqrt(D))/A);
	return PotAns;
}


vector<Point> CirCirInt(Point a, Point b, double r, double R) {
	vector<Point> PotAns;
	double d = sqrt(dist2(a, b));
	if (d > r+R || d+min(r, R) < max(r, R)) return PotAns;
	double x = (d*d-R*R+r*r)/(2*d);
	double y = sqrt(r*r-x*x);
	Point v = (b-a)/d;
	PotAns.push_back(a+v*x + RotateCCW90(v)*y);
	if (y > 0)
		PotAns.push_back(a+v*x - RotateCCW90(v)*y);
	return PotAns;
}

/////////////////////////////////////////////////

bool operator<(const Point &p, const Point &q) { return make_pair(p.y,p.x) < make_pair(q.y,q.x); }
const double R = 1.0, SEPS = sqrt(Eps);

void Add(const vector<Point> &chosen, vector<Point> &new_events, const vector<Point> &tri, const vector<Point> &p)
{
	for (size_t i = 0; i < p.size(); i++)
	{
		double min_y = chosen.size() ? chosen.back().y : -DPINF;
		bool valid = p[i].y >= min_y - SEPS && (PointInPolygon(tri, p[i]) || PotOnPol(tri, p[i]));
		for (int j = int(chosen.size()) - 1; valid && j >= 0; j--)
		{
			if (chosen[j].y < p[i].y - 2*R - SEPS) break;
			if (dist2(p[i], chosen[j]) < 4*R*R - SEPS) valid = false;
		}
		if (valid) new_events.push_back(p[i]);
	}
}

int main() {
	//FILE *file=fopen("3.txt","w");
	double a, b, h=10000;
	int N;
	//cout << 2*sqrt(2.0) + 1.0 << endl;
	while (cin>>a>>b>>N)
	{
		if (a == 0) break;
		a *= DM_PI/180.0;
		b *= DM_PI/180.0;

		vector<Point> chosen;
		vector<Point> events;
		vector<Point> new_events;


		pair<Point,Point> left(Point(R*cos(a), R*sin(a)), Point(R*cos(a) - h*tan(a), R*sin(a) + h));
		pair<Point,Point> right(Point(-R*cos(b), R*sin(b)), Point(-R*cos(b) + h*tan(b), R*sin(b) + h));
		pair<Point,Point> top(Point(-h*tan(a), h-R), Point(h*tan(b), h-R));

		vector<Point> tri;
		tri.push_back(ComputeLineIntersection(left.first, left.second, right.first, right.second));
		tri.push_back(ComputeLineIntersection(right.first, right.second, top.first, top.second));
		tri.push_back(ComputeLineIntersection(top.first, top.second, left.first, left.second));
		if (tri[0].y < h-R + SEPS) Add(chosen, events, tri, vector<Point>(1, tri[0]));

		while ((int)chosen.size()<N)
		{
			Point p = events[0];
			//cerr << chosen.size() + 1 << " " << p << endl;
			chosen.push_back(p);
			new_events.clear();

			for (size_t i = 1; i < events.size(); i++)
				if (dist2(p, events[i]) > 4*R*R-Eps) new_events.push_back(events[i]);

			Add(chosen, new_events, tri, CirLinInt(left.first, left.second, p, 2*R));
			Add(chosen, new_events, tri, CirLinInt(right.first, right.second, p, 2*R));
			for (int i = int(chosen.size()) - 2; i >= 0; i--)
			{
				if (chosen[i].y < p.y - 2*R - SEPS)
					break;
				Add(chosen, new_events, tri, CirCirInt(p, chosen[i], 2*R, 2*R));
			}
			sort(new_events.begin(), new_events.end());
			new_events.swap(events);
		}
		printf("%.2f\n",1+chosen[chosen.size()-1].y);
		//fprintf(file,"%.2f\n",1+chosen[chosen.size()-1].y);
		//cout << chosen[chosen.size()-1].y << endl;
	}
	return 0;
}
