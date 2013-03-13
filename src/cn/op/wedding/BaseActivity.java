package cn.op.wedding;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.op.common.util.Log;

/**
 * 配合BaseActivityGroup使用，作为由BaseActivityGroup管理的的Activity可以继承此类，重写了onKeyDown()方法
 * ，提供了对返回按键的处理，
 * 使子Activity可以返回到上一子Activity；提供了打开子Activity的startSubActivity(Intent intent,
 * String flag)方法
 * 
 * @author lufei
 * 
 */
public class BaseActivity extends FragmentActivity {
	protected String TAG = Log.makeLogTag(getClass());
	public Activity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		AppManager.getAppManager().addActivity(this);
	}

}