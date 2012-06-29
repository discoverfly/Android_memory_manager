

package com.xmobileapp.taskmanager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
//import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageStats;
import android.content.pm.PackageManager.NameNotFoundException;

public class DetailProcess implements Comparable {
    private ProcessInfo.PsRow psrow = null;
    private ApplicationInfo appinfo = null;
    private PackageInfo pkginfo = null;
    private ActivityManager.RunningAppProcessInfo runinfo = null;
    // private ActivityManager.RunningTaskInfo taskinfo = null;
    private String title = null;
    private PackageManager pm;
    private Intent intent = null;

    public DetailProcess(Context ctx, ActivityManager.RunningAppProcessInfo runinfo) {
        this.runinfo = runinfo;
        pm = ctx.getApplicationContext().getPackageManager();
    }

    public ProcessInfo.PsRow getPsrow() {
        return psrow;
    }

    public void setPsrow(ProcessInfo.PsRow psrow) {
        this.psrow = psrow;
    }

    public ApplicationInfo getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(ApplicationInfo appinfo) {
        this.appinfo = appinfo;
    }

    public PackageInfo getPkginfo() {
        return pkginfo;
    }

    public void setPkginfo(PackageInfo pkginfo) {
        this.pkginfo = pkginfo;
    }

    public ActivityManager.RunningAppProcessInfo getRuninfo() {
        return runinfo;
    }

    public void setRuninfo(ActivityManager.RunningAppProcessInfo runinfo) {
        this.runinfo = runinfo;
    }


    public void fetchApplicationInfo(PackagesInfo pkg) {
        if (appinfo == null) appinfo = pkg.getInfo(runinfo.processName);
    }

    public void fetchPackageInfo() {
        if (pkginfo == null && appinfo != null) pkginfo = MiscUtil.getPackageInfo(pm, appinfo.packageName);
    }

    public void fetchPsRow(ProcessInfo pi) {
        if (psrow == null) psrow = pi.getPsRow(runinfo.processName);
    }

    public boolean isGoodProcess() {
        return runinfo != null && appinfo != null && pkginfo != null && pkginfo.activities != null
                && (pkginfo.activities.length > 0);
    }

    public String getPackageName() {
        return appinfo.packageName;
    }

    public String getBaseActivity() {
        return pkginfo.activities[0].name;
    }

    public Intent getIntent() {
        if (intent != null) return intent;
        intent = null;
        intent = pm.getLaunchIntentForPackage(pkginfo.packageName);
		if (intent != null) {
		    intent = intent.cloneFilter();
		    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		    return intent;
		}
		if (pkginfo.activities.length == 1) {
		    intent = new Intent(Intent.ACTION_MAIN);
		    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		    intent.setClassName(pkginfo.packageName, pkginfo.activities[0].name);
		    return intent;
		}
		intent = IntentList.getIntent(pkginfo.packageName, pm);
		if (intent != null) {
		    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		    return intent;
		}
        return null;
    }

    public String getTitle() {
        if (title == null) title = appinfo.loadLabel(pm).toString();
        return title;
    }

//    static class PkgSizeObserver extends IPackageInstallObserver.Stub {
//        public int idx;
//
//        public void packageInstalled(String packageName, int returnCode){
//        	
//        }
//        
//        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
//            // pStats.cacheSize;
//        }
//    }

    public int compareTo(Object another) {
        if (another instanceof DetailProcess && another != null) {
            return this.getTitle().compareTo(((DetailProcess) another).getTitle());
        }
        return -1;
    }

}
