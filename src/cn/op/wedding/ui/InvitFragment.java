package cn.op.wedding.ui;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import cn.op.common.util.FragmentUtil;
import cn.op.wedding.R;

import com.google.android.gms.maps.model.LatLng;

/**
 * 请柬
 * 
 * @author Frank
 * 
 */
public class InvitFragment extends Fragment {

	private ImageView btnMap;
	private SlidingDrawer sd;
	private ImageView leftArrow;
	private ImageView rightArrow;
	private RelativeLayout handle;
	private RelativeLayout bottom;
	private FragmentUtil fragmentUtil;
	protected GoogleMapFragment mapFragment;
	private FragmentActivity activity;
	private ImageView btnReturn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		View view = inflater.inflate(R.layout.invit, null);
		FragmentManager fragManager = getChildFragmentManager();

		btnMap = (ImageView) view.findViewById(R.id.btn_map);
		btnReturn = (ImageView) view.findViewById(R.id.btn_return);
		sd = (SlidingDrawer) view.findViewById(R.id.slidingDrawer1);
		leftArrow = (ImageView) view.findViewById(R.id.invit_iv_lup);
		rightArrow = (ImageView) view.findViewById(R.id.invit_iv_rup);
		handle = (RelativeLayout) view.findViewById(R.id.handle);
		bottom = (RelativeLayout) view.findViewById(R.id.bottombar);

		fragmentUtil = new FragmentUtil(fragManager);

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

		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				// RelativeLayout rel = MenuFragment.dispath();
				// rel.setVisibility(View.VISIBLE);
				AnimationSet a = (AnimationSet) AnimationUtils.loadAnimation(
						activity, R.anim.todown);
				bottom.startAnimation(a);
				bottom.setVisibility(View.GONE);
				btnMap.setVisibility(View.INVISIBLE);

				leftArrow.setBackgroundResource(R.drawable.arrow_up);
				rightArrow.setBackgroundResource(R.drawable.arrow_up);
			}
		});
		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				// RelativeLayout rel = dispath(f);
				// rel.setVisibility(View.INVISIBLE);

				bottom.setVisibility(View.VISIBLE);
				AnimationSet a = (AnimationSet) AnimationUtils.loadAnimation(
						activity, R.anim.toup);
				bottom.startAnimation(a);
				btnMap.setVisibility(View.VISIBLE);

				leftArrow.setBackgroundResource(R.drawable.arrow_down);
				rightArrow.setBackgroundResource(R.drawable.arrow_down);
			}
		});

		return view;
	}

}
