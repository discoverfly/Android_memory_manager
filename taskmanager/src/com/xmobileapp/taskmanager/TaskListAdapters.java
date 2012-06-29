

package com.xmobileapp.taskmanager;

import java.util.ArrayList;
import java.util.List;

import com.xmobileapp.taskmanager.R;

import com.xmobileapp.taskmanager.ProcessInfo.PsRow;

import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapters {

    public final static class ProcessListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<DetailProcess> list;
        private TaskManager ctx;
        private PackageManager pm;

        public ProcessListAdapter(TaskManager context, ArrayList<DetailProcess> list) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            this.list = list;
            this.ctx = context;
            this.pm = ctx.getPackageManager();
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
            	//ͨ��LayoutInflater�������ListView���µ���list_main.xml�ж���Ĳ���
                convertView = mInflater.inflate(R.layout.list_main, null);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.list_icon);
                holder.text_name = (TextView) convertView.findViewById(R.id.list_name);
                holder.text_size = (TextView) convertView.findViewById(R.id.list_size);

                // ����TAG���Ա��д���и����ؼ�������
                convertView.setTag(holder);
            } else {
            	// ��ȡTAG
                holder = (ViewHolder) convertView.getTag();
            }

            // ��ȡ����б�
            final DetailProcess dp = list.get(position);
            convertView.setVisibility(View.VISIBLE);
            String cmd = dp.getRuninfo().processName;
            
            // ͨ��TAG��ֵ
            holder.icon.setImageDrawable(dp.getAppinfo().loadIcon(pm));
            holder.text_name.setText(dp.getTitle());
            PsRow row = dp.getPsrow();
            if (row == null) {
                holder.text_size.setText(R.string.memory_unknown);
            } else {
                holder.text_size.setText((int) Math.ceil(row.mem / 1024) + "K");
            }
            
           
            convertView.setOnClickListener(new OnClickListener() {

                public void onClick(View view) {
                    MiscUtil.getTaskMenuDialog(ctx, dp).show();
                }
                
            });
            
            return convertView;
        }

    }

    private static class ViewHolder {
        ImageView icon;
        TextView text_name;
        TextView text_size;
    }

}
