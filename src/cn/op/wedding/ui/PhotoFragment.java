package cn.op.wedding.ui;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.op.common.util.Log;
import cn.op.wedding.R;

public class PhotoFragment extends Fragment {

	protected static final String TAG = Log.makeLogTag(PhotoFragment.class);
	private LayoutInflater inflater;
	private FragmentActivity activity;
	private SamplePagerAdapter adapter;
	protected boolean isPageEnd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;

		activity = getActivity();
		View view = inflater.inflate(R.layout.photo, null);

		ViewPager vpPhoto = (ViewPager) view.findViewById(R.id.vpPhoto);
		adapter = new SamplePagerAdapter();
		vpPhoto.setAdapter(adapter);

		vpPhoto.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (positionOffsetPixels == 0) {
					isPageEnd = true;
				} else {
					isPageEnd = false;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

//		vpPhoto.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (isPageEnd) {
//					return ((MainActivity) activity).detector.onTouchEvent(event);
//				} else {
//					return false;
//				}
//			}
//		});

		return view;
	}

	class SamplePagerAdapter extends PagerAdapter {
		private int[] sDrawables = { R.drawable.photo1, R.drawable.photo2,
				R.drawable.photo3, R.drawable.photo4 };

		@Override
		public int getCount() {
			return sDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = inflater.inflate(R.layout.photo_vp_item, null);

			PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_photo);
			final TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);

			photoView.setImageResource(sDrawables[position]);
			tvInfo.setVisibility(View.GONE);

			photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
					if (tvInfo.getVisibility() == View.GONE) {
						tvInfo.setText("我是一艘漂泊的小船，当我遇见你，让我停留在你爱的港湾，我愿献出我所有的收获，只要你愿你，我不再远航。只因遇见你");
						Animation animation = AnimationUtils.loadAnimation(
								activity, R.anim.translucent_zoom_in);

						tvInfo.startAnimation(animation);
						tvInfo.setVisibility(View.VISIBLE);
					} else {
						Animation animation = AnimationUtils.loadAnimation(
								activity, R.anim.translucent_zoom_out);

						tvInfo.startAnimation(animation);
						tvInfo.setVisibility(View.GONE);
					}
				}
			});

			// Now just add PhotoView to ViewPager and return it
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
