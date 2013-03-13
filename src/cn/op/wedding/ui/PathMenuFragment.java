package cn.op.wedding.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.op.wedding.R;

import com.capricorn.ArcMenu;

public class PathMenuFragment extends Fragment {

	private static final int[] ITEM_DRAWABLES = { R.drawable.path_home,
			R.drawable.path_invit, R.drawable.path_photo, R.drawable.path_sign,
			R.drawable.path_support };

	private static final int[] ITEM_DRAWABLES_TRUE = {
			R.drawable.path_home_true, R.drawable.path_invit_true,
			R.drawable.path_photo_true, R.drawable.path_sign_true,
			R.drawable.path_support_true };

	ImageView items[];

	private FragmentActivity activity;

	protected int curtCheckedItem = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		activity = getActivity();

		View view = inflater.inflate(R.layout.path_menu, null);
		ArcMenu arcMenu = (ArcMenu) view.findViewById(R.id.arc_menu);

		final int itemCount = ITEM_DRAWABLES.length;
		items = new ImageView[itemCount];
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(activity);
			item.setImageResource(ITEM_DRAWABLES[i]);
			items[i] = item;

			final int position = i;

			// Add a menu item
			arcMenu.addItem(item, new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (curtCheckedItem != position) {
						performPathItemClick(position);
					}
				}
			});
		}

		// 默认选中的item
		items[curtCheckedItem]
				.setImageResource(ITEM_DRAWABLES_TRUE[curtCheckedItem]);
		arcMenu.setCurtClickItemIndex(curtCheckedItem);
		
		return view;
	}

	protected void performPathItemClick(int position) {
		toggleItemBg(position);
		curtCheckedItem = position;

		((PathMenuFragment.OnPathItemClickListener) activity)
				.onPathItemClick(position);
	}

	private void toggleItemBg(int position) {
		for (int j = 0; j < items.length; j++) {
			items[j].setImageResource(ITEM_DRAWABLES[j]);
		}
		items[position].setImageResource(ITEM_DRAWABLES_TRUE[position]);
	}

	public interface OnPathItemClickListener {
		/**
		 * @param v
		 * @param index
		 *            从左到右item的index
		 */
		void onPathItemClick(int index);
	}
}