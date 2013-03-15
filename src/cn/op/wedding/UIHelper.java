package cn.op.wedding;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;

	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;

	public static final int REQUEST_CODE_PUB_REMARK = 1;

	/**
	 * 退出程序
	 * 
	 * @param cont
	 */
	public static void Exit(final Activity cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 调用系统安装了的应用分享
	 * 
	 * @param context
	 * @param title
	 *            文本
	 * @param url
	 *            图片url
	 * @param imgPath
	 *            图片本地路径
	 */
	public static void showShareMore(Activity context, final String title,
			final String url, String imgPath) {

		String content = title;
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);

		if (!(imgPath == null) && !"".equals(imgPath)) { // 有本地图片，怎分享图片
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(imgPath)));
		} else { // 无本地图片，则将图片url添到文本后面
			content = title + " " + url;
		}

		intent.putExtra(Intent.EXTRA_TEXT, content);

		context.startActivity(Intent.createChooser(intent, "选择分享"));
	}

	/**
	 * 打电话
	 * 
	 * @param tel
	 *            纯数字号码
	 */
	public static void call(Context context, String tel) {
		// 先打开拨号界面
		Uri uri = Uri.parse("tel:" + tel);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(it);

		// 直接拨打
		// Uri uri = Uri.parse("tel:"+ "4006311234");
		// intent = new Intent(Intent.ACTION_CALL,uri);
		// startActivity(intent);
	}
}