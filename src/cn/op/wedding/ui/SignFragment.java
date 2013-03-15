package cn.op.wedding.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.op.wedding.R;
import cn.op.wedding.domain.SignAccount;

/**
 * 签到
 * 
 * @author Frank
 * 
 */
public class SignFragment extends ListFragment {

	private LayoutInflater inflater;
	private View pb;
	private FragmentActivity activity;
	private SignAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.sign, null);
		pb = view.findViewById(R.id.pb);

		ListView listView = (ListView) view.findViewById(android.R.id.list);
//		listView.setOverscrollHeader(getResources().getDrawable(R.color.red));
//		listView.setOverscrollFooter(getResources().getDrawable(R.drawable.arrow_down));
		
		listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//		listView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();

		ArrayList<SignAccount> data = new ArrayList<SignAccount>();
		for (int i = 0; i < 25; i++) {
			data.add(new SignAccount("张三" + i, "（新郎亲属）", "祝你们白头到老" + i,
					"2013-05-01 08:13:14"));
		}
		adapter = new SignAdapter(data);
		setListAdapter(adapter);

		ListView listView = getListView();
//		listView.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				return ((MainActivity) activity).detector.onTouchEvent(event);
//			}
//		});
		
		

		
		
		// initDate();
	}

	private class SignAdapter extends BaseAdapter {
		private List<SignAccount> data;

		public SignAdapter(ArrayList<SignAccount> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public SignAccount getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class ViewHolder {

			public TextView tvName;
			public TextView tvRelation;
			public TextView tvMsg;
			public TextView tvDate;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.sign_lv_item, null);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.tvRelation = (TextView) convertView
						.findViewById(R.id.textView2);
				holder.tvMsg = (TextView) convertView
						.findViewById(R.id.textView3);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.textView4);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SignAccount q = getItem(position);

			holder.tvDate.setText(q.date);
			holder.tvMsg.setText(q.msg);
			holder.tvName.setText(q.name);
			holder.tvRelation.setText(q.relation);

			return convertView;
		}
	}

}
