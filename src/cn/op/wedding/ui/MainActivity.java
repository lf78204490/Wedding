package cn.op.wedding.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import cn.op.common.util.FragmentUtil;
import cn.op.wedding.BaseActivity;
import cn.op.wedding.R;
import cn.op.wedding.UIHelper;
import cn.op.wedding.ui.PathMenuFragment.OnPathItemClickListener;

public class MainActivity extends BaseActivity implements
		OnPathItemClickListener {

	private FragmentUtil fragmentUtil;
	protected Fragment mapFragment;
	private PhotoFragment photoFragment;
	private IndexFragment indexFragment;
	private InvitFragment invitFragment;
	private SignFragment signFragment;
	private SupportFragment supportFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		FragmentManager fragManeger = getSupportFragmentManager();
		fragmentUtil = new FragmentUtil(getSupportFragmentManager());

		PathMenuFragment fragPathMenu = (PathMenuFragment) fragManeger
				.findFragmentById(R.id.frag_pathMenu);

		fragPathMenu.performPathItemClick(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPathItemClick(int index) {
		switch (index) {
		case 0:
			if (indexFragment == null) {
				indexFragment = new IndexFragment();
			}

			showFragment(indexFragment);

			break;
		case 1:

			if (invitFragment == null) {
				invitFragment = new InvitFragment();
			}

			showFragment(invitFragment);

			break;
		case 2:
			if (photoFragment == null) {
				photoFragment = new PhotoFragment();
			}
			showFragment(photoFragment);
			break;
		case 3:

			if (signFragment == null) {
				signFragment = new SignFragment();
			}
			showFragment(signFragment);

			break;
		case 4:
			if (supportFragment == null) {
				supportFragment = new SupportFragment();
			}
			showFragment(supportFragment);
			break;
		default:
			break;
		}
	}

	private void showFragment(Fragment fragment) {
		fragmentUtil.addFragmentsInActivity(R.id.layout_content, fragment);
	}

	@Override
	public void onBackPressed() {
		UIHelper.Exit(this);
	}
}