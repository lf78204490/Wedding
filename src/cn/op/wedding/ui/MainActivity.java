package cn.op.wedding.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;
import cn.op.common.util.DepthPageTransformer;
import cn.op.common.util.FragmentUtil;
import cn.op.wedding.BaseActivity;
import cn.op.wedding.R;
import cn.op.wedding.UIHelper;
import cn.op.wedding.ui.PathMenuFragment.OnPathItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends BaseActivity implements
		OnPathItemClickListener {

	private FragmentUtil fragmentUtil;
	protected Fragment mapFragment;
	private PhotoFragment photoFragment;
	private IndexFragment indexFragment;
	private InvitFragment invitFragment;
	private SignFragment signFragment;
	private SupportFragment supportFragment;
	private PathMenuFragment fragPathMenu;
	private ArrayList<Fragment> framentList;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		framentList = new ArrayList<Fragment>();
		framentList.add(new IndexFragment());
//		framentList.add(new SupportFragment());
		framentList.add(new InvitFragment());
		framentList.add(new PhotoFragment());
		framentList.add(new SignFragment());
		framentList.add(new SupportFragment());

		FragmentManager fragManeger = getSupportFragmentManager();
		fragmentUtil = new FragmentUtil(getSupportFragmentManager());

		fragPathMenu = (PathMenuFragment) fragManeger
				.findFragmentById(R.id.frag_pathMenu);

//		fragPathMenu.performPathItemClick(0);

		// mPullRefreshScrollView = (PullToRefreshScrollView)
		// findViewById(R.id.pull_refresh_scrollview);
		// mPullRefreshScrollView
		// .setOnRefreshListener(new OnRefreshListener<ScrollView>() {
		// @Override
		// public void onRefresh(
		// PullToRefreshBase<ScrollView> refreshView) {
		// showFragment(fragPathMenu.getCurtCheckedItem() + 1);
		// mPullRefreshScrollView.onRefreshComplete();
		// }
		// });
		
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(fragManeger);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.
//				invalidateOptionsMenu();
				fragPathMenu.togglePathItemBg(position);
			}
		});
		
		mPager.setPageTransformer(true, new DepthPageTransformer());

	}
	
	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return framentList.get(position);
//			return ScreenSlidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return framentList.size();
		}
	}

	@Override
	public void onPathItemClick(int index) {
		mPager.setCurrentItem(index);
//		showFragment(index);
	}

	private void showFragment(int index) {
		showFragment(index, null, null);
	}

	private void showFragment(int index, Integer animIn, Integer animOut) {
		if (index >= 0 && index <= framentList.size() - 1) {
			showFragment(framentList.get(index), animIn, animOut);
			fragPathMenu.togglePathItemBg(index);
		}
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return detector.onTouchEvent(event);
	// }

	public GestureDetector detector = new GestureDetector(mContext,
			new OnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {

					return false;
				}

				@Override
				public void onLongPress(MotionEvent e) {
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					int checkedItem = fragPathMenu.getCurtCheckedItem();

					if (e1.getX() - e2.getX() > 120) {// 向左滑动
						showFragment(++checkedItem, R.anim.slide_in_right,
								R.anim.slide_out_left);

					} else if (e2.getX() - e1.getX() > 120) {// 向右滑动
						showFragment(--checkedItem, R.anim.slide_in_left,
								R.anim.slide_out_right);
					}
					return true;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					return false;
				}
			});

	private void showFragment(Fragment fragment) {
		showFragment(fragment, null, null);
	}

	private void showFragment(Fragment fragment, Integer animIn, Integer animOut) {
		fragmentUtil.addFragmentsInActivity(R.id.layout_content, fragment,
				animIn, animOut);
	}

	@Override
	public void onBackPressed() {
		UIHelper.Exit(this);
	}
}