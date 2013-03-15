package cn.op.common.util;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
		addFragmentsInActivity(id, fragment, null, null);
	}

	/**
	 * 添加要管理的fragment，如果已经添加过，则不再添加
	 * 
	 * @param id
	 * @param fragment
	 * @param animIn
	 * @param animOut
	 */
	public void addFragmentsInActivity(int id, Fragment fragment,
			Integer animIn, Integer animOut) {
		if (!fragment.isAdded()) {
			FragmentTransaction ft = fm.beginTransaction();
			if (animIn != null && animOut != null) {
				ft.setCustomAnimations(animIn, animOut);
			}
			ft.add(id, fragment);
			hideOther(fragment, ft);

			ft.commit();
			fm.executePendingTransactions();

			framentList.add(fragment);
		} else {
			show(fragment, animIn, animOut);
		}
	}

	/**
	 * 显示fragment，在此之前确保调用了addFragmentsInActivity(int id, Fragment fragment)
	 * 
	 * @param fragment
	 */
	private void show(Fragment fragment) {
		show(fragment, null, null);
	}

	/**
	 * 显示fragment，在此之前确保调用了addFragmentsInActivity()
	 * 
	 * @param fragment
	 * @param animIn
	 * @param animOut
	 */
	private void show(Fragment fragment, Integer animIn, Integer animOut) {
		FragmentTransaction ft = fm.beginTransaction();
		// 动画
		if (animIn != null && animOut != null) {
			ft.setCustomAnimations(animIn, animOut);
		}
		ft.show(fragment);
		hideOther(fragment, ft);

		ft.commit();
	}

	private void hideOther(Fragment fragment, FragmentTransaction ft) {
		for (int i = 0; i < framentList.size(); i++) {
			if (framentList.get(i) != fragment) {
				ft.hide(framentList.get(i));
			}
		}
	}

	public void hide(Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		// 动画
		ft.hide(fragment);
		ft.commit();
	}
}