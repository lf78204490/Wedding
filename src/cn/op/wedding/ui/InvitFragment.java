package cn.op.wedding.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import cn.op.common.util.FragmentUtil;
import cn.op.wedding.R;

import com.capricorn.DisplayUtil;
import com.google.android.gms.maps.model.LatLng;

/**
 * 请柬
 * 
 * @author Frank
 * 
 */
public class InvitFragment extends Fragment {

	private ImageView btnMap;
	private ImageView leftArrow;
	private ImageView rightArrow;
	private RelativeLayout handle;
	private FragmentUtil fragmentUtil;
	protected GoogleMapFragment mapFragment;
	private FragmentActivity activity;
	private ImageView btnReturn;
	private View tvContent;
	private ScrollView sv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		View view = inflater.inflate(R.layout.invit, null);

		// TODO 如果使用childFragmentManager，会出现no activity 异常
		// FragmentManager fragManager = getChildFragmentManager();
		// 但是使用Activity的Fragmentmanager的话，就会将子fragmeng添加到Activity的布局中，而不是期望的添加到当前fragment的布局中
		FragmentManager fragManager = getActivity().getSupportFragmentManager();
		fragmentUtil = new FragmentUtil(fragManager);

		btnMap = (ImageView) view.findViewById(R.id.btn_map);
		btnReturn = (ImageView) view.findViewById(R.id.btn_return);
		leftArrow = (ImageView) view.findViewById(R.id.invit_iv_lup);
		rightArrow = (ImageView) view.findViewById(R.id.invit_iv_rup);
		handle = (RelativeLayout) view.findViewById(R.id.handle);
		tvContent = view.findViewById(R.id.content);
		sv = (ScrollView) view.findViewById(R.id.scrollview);


		handle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tvContent.getVisibility() == View.GONE) {
					tvContent.setVisibility(View.VISIBLE);
					// int height = tvContent.getHeight();
					// int measuredHeight = tvContent.getMeasuredHeight();
					// // sv.scrollBy(0, height);
					// sv.scrollTo(0, height);

					sv.post(new Runnable() {
						@Override
						public void run() {
							// 平滑滚动
							sv.smoothScrollBy(0, tvContent.getHeight());

							// 平滑滚动，可定义时间 ，Api>=11
							// final ObjectAnimator animScrollToTop =
							// ObjectAnimator
							// .ofInt(sv, "scrollY", tvContent.getHeight());
							// animScrollToTop.setDuration(2000);
							// animScrollToTop.start();
						}
					});

				} else {
					tvContent.setVisibility(View.GONE);

				}
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// sd.close();
				if (mapFragment == null) {
					mapFragment = GoogleMapFragment.newInstance(new LatLng(
							34.180, 108.940));
				}

				fragmentUtil.addFragmentsInActivity(R.id.layout_content,
						mapFragment);

				btnReturn.setVisibility(View.VISIBLE);
				btnMap.setVisibility(View.GONE);
			}
		});

		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentUtil.hide(mapFragment);
				btnMap.setVisibility(View.VISIBLE);
				btnReturn.setVisibility(View.GONE);
			}
		});

		// sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
		// @Override
		// public void onDrawerClosed() {
		// // RelativeLayout rel = MenuFragment.dispath();
		// // rel.setVisibility(View.VISIBLE);
		// AnimationSet a = (AnimationSet) AnimationUtils.loadAnimation(
		// activity, R.anim.todown);
		// bottom.startAnimation(a);
		// bottom.setVisibility(View.GONE);
		// btnMap.setVisibility(View.INVISIBLE);
		//
		// leftArrow.setBackgroundResource(R.drawable.arrow_up);
		// rightArrow.setBackgroundResource(R.drawable.arrow_up);
		// }
		// });
		// sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
		// @Override
		// public void onDrawerOpened() {
		// // RelativeLayout rel = dispath(f);
		// // rel.setVisibility(View.INVISIBLE);
		//
		// bottom.setVisibility(View.VISIBLE);
		// AnimationSet a = (AnimationSet) AnimationUtils.loadAnimation(
		// activity, R.anim.toup);
		// bottom.startAnimation(a);
		// btnMap.setVisibility(View.VISIBLE);
		//
		// leftArrow.setBackgroundResource(R.drawable.arrow_down);
		// rightArrow.setBackgroundResource(R.drawable.arrow_down);
		// }
		// });

		return view;
	}

}
