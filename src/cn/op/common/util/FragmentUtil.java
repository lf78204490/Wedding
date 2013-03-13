package cn.op.common.util;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.op.wedding.R;

public class FragmentUtil {
	private FragmentManager fm;
	private ArrayList<Fragment> framentList = new ArrayList<Fragment>();

	public FragmentUtil(FragmentManager fm) {
		this.fm = fm;
	}

	/**
	 * 添加要管理的fragment，如果已经添加过，则不再添加
	 * 
	 * @param id
	 * @param fragment
	 */
	public void addFragmentsInActivity(int id, Fragment fragment) {
		if (!fragment.isAdded()) {
			FragmentTransaction ft = fm.beginTransaction();
//			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
			ft.add(id, fragment);
			// fragmentTransaction.replace(id, fragment);
			ft.commit();
			fm.executePendingTransactions();

			framentList.add(fragment);
		} else {
			show(fragment);
		}
	}

	/**
	 * 显示fragment，在此之前确保调用了addFragmentsInActivity(int id, Fragment fragment)
	 * 
	 * @param fragment
	 */
	public void show(Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		// 动画
//		ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		for (int i = 0; i < framentList.size(); i++) {
			ft.hide(framentList.get(i));
		}
		ft.show(fragment);
		ft.commit();
	}

	public void hide(Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		// 动画
		ft.hide(fragment);
		ft.commit();
	}

}