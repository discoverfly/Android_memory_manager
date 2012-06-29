package will.ok.MemoryManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	List<Programe> list = new ArrayList<Programe>();
	LayoutInflater la;
	Context context;

	public ListAdapter(List<Programe> list ,Context context){
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
			la = LayoutInflater.from(context);
			convertView=la.inflate(will.ok.MemoryManager.R.layout.list_item, null);

			holder = new ViewHolder();
			holder.imgage=(ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		 final Programe pr = (Programe)list.get(position);
		//设置图标
		holder.imgage.setImageDrawable(pr.getIcon());
		//设置程序名
		holder.text.setText(pr.getName());

		return convertView;
	}
}
class ViewHolder{
	TextView text;
	ImageView imgage;
}
