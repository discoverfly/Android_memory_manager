eiver 
#include <unistd.h>
#include <termios.h>
#include <fcntl.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <stdio.h>
#include <string.h>
#include "config.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <sys/fcntl.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/time.h>

#include "tslib.h"
#include "fbutils.h"

#define DEV_GPS			"/dev/ttySAC2"

#define ORG_GPS			1
#define SEL_GPGGA		2
#define SEL_GPGLL		3
#define SEL_GPGSA		4
#define SEL_GPGSV		5
#define SEL_GPRMC		6
#define SEL_GPVTG		7
#define SEL_GPZDA		8
#define FUNC_QUIT		9

#define BUFLEN			2000
#define	true			1;
#define false			0;
#define NR_COLORS (sizeof (palette) / sizeof (palette [0]))

static int palette [] =
{
	0x000000, 0xffe080, 0xffffff, 0xe0c0a0, 0x304050, 0x80b8c0
};

int z=0;
double x[20];
double y[20];
int num=-1;

static void gps_serial_init(int fd)
{
	struct termios newtio;

	bzero(&newtio, sizeof(newtio));

        cfsetispeed(&newtio,B9600);            // setup baud rate
        cfsetospeed(&newtio,B9600);

	newtio.c_lflag &= ~(ECHO | ICANON);
	//newtio.c_cflag = B9600 | CS8 | CLOCAL | CREAD;
	newtio.c_cflag = CS8 | CLOCAL | CREAD;
	newtio.c_iflag = IGNPAR;
	newtio.c_oflag = 0;
	newtio.c_oflag &= ~(OPOST);

	newtio.c_cc[VTIME] = 5;   /* inter-character timer unused */
	newtio.c_cc[VMIN] = 0;   /* blocking read until 9 chars received */

	tcflush(fd, TCIFLUSH);
	tcsetattr(fd, TCSANOW, &newtio);
}

static void gps_print_prompt()
{
	printf ("\nSelect what you want to read:\n");
	printf ("1 : Original GPS datas\n");
	printf ("2 : GPGGA\n");
	printf ("3 : GPGLL\n");
	printf ("4 : GPGSA\n");
	printf ("5 : GPGSV\n");
	printf ("6 : GPRMC\n");
	printf ("7 : GPVTG\n");
	printf ("8 : GPZDA\n");
	printf ("9 : Quit\n");
	printf (">");
}

// read datas from GPS
static int gps_read_data(int fd, char *rcv_buf)
{
	int retval;
	fd_set rfds;
	struct timeval tv;
	int ret, pos=0;

	tv.tv_sec = 1;
	tv.tv_usec = 0;

	while(1) {
		FD_ZERO(&rfds);
		FD_SET(fd, &rfds);

		retval = select(fd+1 , &rfds, NULL, NULL, &tv);
		if (retval == -1) {
			perror("select()");
			break;
		}
		else if (retval) { //ÅÐ¶ÏÊÇ·ñÓÐÊýŸÝ
			ret = read(fd, rcv_buf+pos, 2048);
			pos += ret;
			if (rcv_buf[pos-2] == '\r' && rcv_buf[pos-1] == '\n') {
				FD_ZERO(&rfds);
				FD_SET(fd, &rfds);
				retval = select(fd+1 , &rfds, NULL, NULL, &tv);
				if(!retval)
					break;// if no datas, break
			}
		}
	/*	else {
			printf("No data\n");
			break;
		}*/
	}

	return true;
}

// FUNCTION 1 show all receive data
static void gps_original_data(int fd)
{
	char rcv_buf[2048];
	int i=5;
	while (i--) {
		bzero(rcv_buf, sizeof(rcv_buf));
		if (gps_read_data(fd, rcv_buf))
			printf("%s",rcv_buf);
	}
}

// FUNCTION 2 resolve GPS GPGGA information
static void gps_resolve_gpgga(int fd)
{
	int i,j,k,count;
	char buf[BUFLEN];
	char c;

	char findok;
	char lati[20];
	char longi[20];
	char satenum[20];
	char msl[20];
	

	bzero(buf,sizeof(buf));
	gps_read_data(fd,buf);
	printf("GPGGA:latitude.N;longitude,E:\n");
	for(i=0;i<BUFLEN;i++){ 
		if((buf[i]=='$')&&(buf[i+1]=='G')&&(buf[i+2]=='P')&&(buf[i+3]=='G')&&(buf[i+4]=='G')&&(buf[i+5]=='A'))
		{					
			findok=true;   		
			break;
		}

	}
	if((i+78<BUFLEN)&&(findok)) 
	{
		count=0;
		for(j=i+6;j<50+6+i;j++) 
		{
         	 if(buf[j]==',')
		 {
			count++;
		        if(count==6)
			   break;
		 }
		}
		if((count==6)&&(buf[j+1]=='1'))
		{
		    lati[0] = longi[0] = msl[0] = satenum[0] = '\0';  //clear string
		  k=0;
		  for(j=i+18;j<i+18+15;j++)  //skip $GPGGA,hhmmss.sss ,get latitude
		   {
			  if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   lati[k++]=c;
				   }
			   }
			   else
			   {
				   break;
			   }
		   }
		   c=buf[j+1];       //set N or S
		   lati[k++]=c;
		   c='\0';
		   lati[k++]=c;
		   k=0;
		   for(j=i+30;j<i+30+15;j++)  //skip lati:ddmm.mmmm,N,get longitude
		   {
			   if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   longi[k++]=c;
				   }
			   }
			   else
			   {
				   break;
			   }
		   }
		   c=buf[j+1];               //set E or W
		   longi[k++]=c;
		   c='\0';
		   longi[k++]=c;
		   //printf("longitude:%s\n",longi);
		   k=0;
		   for(j=i+45;j<i+45+5;j++)  //skip ddmm.mmmm,E,1 ,get satelite number
		   {
			   if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   satenum[k++]=c;
				   }
			   }
			   else
			   {
				   c='\0';
				   satenum[k++]=c;
				   break;
			   }
		   }
		  //printf("satenum=%s\n",satenum);
		   for(j=i+48;j<i+48+15;j++)  //skip HKOP,
		   {
			   if(buf[j]==',')
			   {
                  break;
			   }
		   }
		}
		k=0;
		for(j=j+1;;j++)           //get MSL Altidude
		{
			if(buf[j]!=',')
			{
				if(k<20){
				c=buf[j];
				msl[k++]=c;
				}
			}
			else
			{
				c='\0';
				msl[k++]=c;
				break;
			}
		}
		//printf("msl=%s\n",msl);
		findok=false;
	}
	
	printf("=================================================================\n");
	//print ID
	printf("Message ID:		%c%c%c%c%c%c\n",buf[i],buf[i+1],buf[i+2],buf[i+3],buf[i+4],buf[i+5]);
	
	//print latitude
	int m=0;
	printf("Latitude:		");
	for(m=0;m<9;m++)
		printf("%c",lati[m]);
	printf("	//ddmm.mmmm//");
	printf("\n");
	printf("N/S Indicator:		%c		//N=North or S=South//\n",lati[9]);
	
	//print longitude
	printf("Longitude		");
	int n=0;
	for(n=0;n<10;n++)
		printf("%c",longi[n]);
	printf("	//dddmm.mmmm//");
	printf("\n");
	printf("E/W Indicator:		%c		//E=East or W=West//\n",longi[10]);
	
	//print satellites used
	printf("Satellites Used:	%c%c		//Range 0 to 12//\n",satenum[i],satenum[i+1]);
	
	//print msl altitude
	printf("MSL Altitude:		%c%c%c%c		//Unit:meters//\n",msl[0],msl[1],msl[2],msl[3]);
	printf("=================================================================\n");

}

// FUNCTION 3 resolve GPS GPGLL information
void gps_resolve_gpgll(int fd)
{
	char rcv_buf[2048];
	char *p=NULL;
	int i=0;

//	printf(" latitude and longitude is..........\n");
	printf("GPGLL:latitude.N;longitude,E\n");
	printf("GPGLL:");

	bzero(rcv_buf,sizeof(rcv_buf));

	if (gps_read_data(fd, rcv_buf)) {
		p = rcv_buf;
		while(*p) {
			p++;
			if(*p=='L') {
				p++;
				p++;

				while(i<24) {
					i++;
					p++;
					printf("%c",*p);
				}
				printf("\n");
			}
		}
	}
}

// FUNCTION 4 resolve GPS GPGSA information
static void gps_resolve_gpgsa(int fd)
{
}

// FUNCTION 5 resolve GPS GPGSV information
static void gps_resolve_gpgsv(int fd)
{
}

// FUNCTION 6 resolve GPS GPRMC information
static void gps_resolve_gprmc(int fd)
{
	int i,j,k,count;
	char c;

	char findok;
	char lati[20];
	char longi[20];
	char satenum[20];
	char msl[20];
	char flag;
	char time[20];
	
	//bzero(buf,sizeof(buf));
	//gps_read_data(fd,buf);
	
	char buf[]="$GPGSV,3,3,11,20,14,273,,25,13,058,21,03,04,198,*44\n$GPRMC,095326.000,A,4544.5251,N,12637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,6544.5251,N,14637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,7544.5251,N,13637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,2544.5251,N,11637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,3544.5251,N,19637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,2544.5251,N,13637.3842,E,0.64,92.08,050412,,,A*58\n$GPRMC,095326.000,A,1544.5251,N,12637.3842,E,0.64,92.08,050412,,,A*58";
	
	printf("GPGGA:latitude.N;longitude,E:\n");

	for(;z<BUFLEN;z++){ 
		if((buf[z]=='$')&&(buf[z+1]=='G')&&(buf[z+2]=='P')&&(buf[z+3]=='R')&&(buf[z+4]=='M')&&(buf[z+5]=='C'))
		{					
			findok=true;
			num++;   		
			break;
		}

	}
	if((z+78<BUFLEN)&&(findok)) 
	{
		count=0;
		for(j=z+6;j<50+6+z;j++) 
		{
         	 if(buf[j]==',')
		 {
			count++;
		        if(count==7)
			   break;
		 }
		}
		if(count==7)
		{
		    time[0]=flag = lati[0] = longi[0] = msl[0] = satenum[0] = '\0';  //clear string
		  k=0;
		
		  for(j=z+7;j<z+7+17;j++){
		           if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   time[k++]=c;
				   }
			   }
			   else
			   {
				   break;
			   }
		  }
		   c='\0';
		   lati[k++]=c;
		   k=0;
		   flag=buf[i+18];
		  for(j=z+20;j<z+20+15;j++)  //skip $GPGGA,hhmmss.sss ,get latitude
		   {
			  if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   lati[k++]=c;
				   }
			   }
			   else
			   {
				   break;
			   }
		   }
		   c=buf[j+1];       //set N or S
		   lati[k++]=c;
		   c='\0';
		   lati[k++]=c;
		   k=0;
		   for(j=z+32;j<z+32+15;j++)  //skip lati:ddmm.mmmm,N,get longitude
		   {
			   if(buf[j]!=',')
			   {
				   if(k<20){
				   c=buf[j];
				   longi[k++]=c;
				   }
			   }
			   else
			   {
				   break;
			   }
		   }
		   
		   c=buf[j+1];               //set E or W
		   longi[k++]=c;
		   c='\0';
		   longi[k++]=c;
	           k=0;
		}
		k=0;
		findok=false;
	}
	
	printf("=================================================================\n");
	//print ID
	printf("Message ID:		%c%c%c%c%c%c\n",buf[z],buf[z+1],buf[z+2],buf[z+3],buf[z+4],buf[z+5]);
	z=j;
	//print latitude
	int m=0;
	printf("Latitude:		");
	for(m=0;m<9;m++){
		sscanf(lati,"%lf",&x[num]);
		printf("%c",lati[m]);
	}
	printf(" x: %lf",x[num]);	
	printf("	//ddmm.mmmm//");
	printf("\n");
	printf("N/S Indicator:		%c		//N=North or S=South//\n",lati[9]);
	
	//print longitude
	printf("Longitude		");
	int n=0;
	for(n=0;n<10;n++){
		sscanf(longi,"%lf",&y[num]);
		printf("%c",longi[n]);
	}	
	printf(" y: %lf",y[num]);
	printf("	//dddmm.mmmm//");
	printf("\n");
	printf("E/W Indicator:		%c		//E=East or W=West//\n",longi[10]);
	printf("Z: %d\n NUM: %d",z,num);
	printf("=================================================================\n");
	put_cross(800*x[num]/10000, 480*y[num]/20000, 2 | XORMODE);
	
}

// FUNCTION 7 resolve GPS GPVTG information
static void gps_resolve_gpvtg(int fd)
{
}

// FUNCTION 8 resolve GPS GPZDA information
static void gps_resolve_gpzda(int fd)
{
	char rcv_buf[2048];
	char *p = NULL;
	int i = 0;

	printf("UTC time is..........\n");
	printf("GPZDA:hour,min,sec;00;date,month,year\n");
	printf("GPZDA:");
	
	bzero(rcv_buf, sizeof(rcv_buf));
	if(gps_read_data(fd, rcv_buf)) {
		p = rcv_buf;
		while(*p) {
			p++;
			if(*p=='Z') {
				p++;
				p++;
				p++;
				while(i<20) {
					i++;
					p++;
					printf("%c",*p);
				}
				printf("\n");
			}
		}
	}
}

static void gps_test(int fd)
{
	int select_func;
	ssize_t ret;
	int run = true;

	while(run) {
		gps_print_prompt();                 // print select functions
		scanf("%d", &select_func);  // user input select
		getchar(); // get ENTER <LF>

		switch(select_func) {
		case ORG_GPS:
			gps_original_data(fd);
			break;
		case SEL_GPGGA:
			gps_resolve_gpgga(fd);
			break;
		case SEL_GPGLL:
			gps_resolve_gpgll(fd);
			break;
		case SEL_GPGSA:
			gps_resolve_gpgsa(fd);
			break;
		case SEL_GPGSV:
			gps_resolve_gpgsv(fd);
			break;
		case SEL_GPRMC:
			gps_resolve_gprmc(fd);
			break;
		case SEL_GPVTG:
			gps_resolve_gpvtg(fd);
			break;
		case SEL_GPZDA:
			gps_resolve_gpzda(fd);
			break;
		case FUNC_QUIT:
			run = false;
			printf("Quit GPS function.  byebye\n");
			break;
		default :
			printf("please input your select use 1 to 8\n");
			break;
		}
	}
}


static void sig(int sig)
{
	close_framebuffer();
	fflush(stderr);
	printf("signal %d caught\n", sig);
	fflush(stdout);
	exit(1);
}

int main(int argc, char *argv[])
{
	struct tsdev *ts;
	unsigned int h;
	unsigned int smode = 0;
	int i;

	char *tsdevice=NULL;

	
	int fd;


	printf("This is a test about GPS : receive GPS signal\n");

	fd = open(DEV_GPS, O_RDONLY);
	if(fd < 0)
	{
		printf("Open GPS Device error !");	
		//return -1;
	}
	
	signal(SIGSEGV, sig);
	signal(SIGINT, sig);
	signal(SIGTERM, sig);

	if ((tsdevice = getenv("TSLIB_TSDEVICE")) == NULL) {
#ifdef USE_INPUT_API
		tsdevice = strdup ("/dev/input/event0");
#else
		tsdevice = strdup ("/dev/touchscreen/ucb1x00");
#endif /* USE_INPUT_API */
        }

	ts = ts_open (tsdevice, 0);

	if (!ts) {
		perror (tsdevice);
		exit(1);
	}

	if (ts_config(ts)) {
		perror("ts_config");
		exit(1);
	}

	if (open_framebuffer()) {
		close_framebuffer();
		exit(1);
	}

	for (i = 0; i < NR_COLORS; i++)
		setcolor (i, palette [i]);

	gps_serial_init(fd);
	gps_test(fd);
	
	close(fd);
	close_framebuffer();
	return 0;
}
