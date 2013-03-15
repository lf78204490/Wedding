package cn.op.common.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.op.wedding.R;

public class PullToRefreshLayout extends LinearLayout {

	// private static final String TAG = "PullToRefreshView";

	// refresh states

	private static final int PULL_TO_REFRESH = 2;

	private static final int RELEASE_TO_REFRESH = 3;

	private static final int REFRESHING = 4;

	// pull state

	private static final int PULL_UP_STATE = 0;

	private static final int PULL_DOWN_STATE = 1;

	/**
	 * 
	 * last y
	 */

	private int mLastMotionY;

	/**
	 * 
	 * lock
	 */

	private boolean mLock;

	/**
	 * 
	 * header view
	 */

	private View mHeaderView;

	/**
	 * 
	 * footer view
	 */

	private View mFooterView;

	/**
	 * 
	 * list or grid
	 */

	private AdapterView<?> mAdapterView;

	/**
	 * 
	 * scrollview
	 */

	private ScrollView mScrollView;

	/**
	 * 
	 * header view height
	 */

	private int mHeaderViewHeight;

	/**
	 * 
	 * footer view height
	 */

	private int mFooterViewHeight;

	/**
	 * 
	 * header view image
	 */

	private ImageView mHeaderImageView;

	/**
	 * 
	 * footer view image
	 */

	private ImageView mFooterImageView;

	/**
	 * 
	 * header tip text
	 */

	private TextView mHeaderTextView;

	/**
	 * 
	 * footer tip text
	 */

	private TextView mFooterTextView;

	/**
	 * 
	 * header refresh time
	 */

	private TextView mHeaderUpdateTextView;

	/**
	 * 
	 * footer refresh time
	 */

	// private TextView mFooterUpdateTextView;

	/**
	 * 
	 * header progress bar
	 */

	private ProgressBar mHeaderProgressBar;

	/**
	 * 
	 * footer progress bar
	 */

	private ProgressBar mFooterProgressBar;

	/**
	 * 
	 * layout inflater
	 */

	private LayoutInflater mInflater;

	/**
	 * 
	 * header view current state
	 */

	private int mHeaderState;

	/**
	 * 
	 * footer view current state
	 */

	private int mFooterState;

	/**
	 * 
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */

	private int mPullState;

	/**
	 * 
	 * ±äÎªÏòÏÂµÄ¼ýÍ·,¸Ä±ä¼ýÍ··½Ïò
	 */

	private RotateAnimation mFlipAnimation;

	/**
	 * 
	 * ±äÎªÄæÏòµÄ¼ýÍ·,Ðý×ª
	 */

	private RotateAnimation mReverseFlipAnimation;

	/**
	 * 
	 * footer refresh listener
	 */

	private OnFooterRefreshListener mOnFooterRefreshListener;

	/**
	 * 
	 * footer refresh listener
	 */

	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	/**
	 * 
	 * last update time
	 */

	private String mLastUpdateTime;

	public PullToRefreshLayout(Context context, AttributeSet attrs) {

		super(context, attrs);

		init();

	}

	public PullToRefreshLayout(Context context) {

		super(context);

		init();

	}

	/**
	 * 
	 * init
	 * 
	 * 
	 * 
	 * @description
	 * 
	 * @param context
	 * 
	 *            hylin 2012-7-26ÉÏÎç10:08:33
	 */

	private void init() {

		// Load all of the animations we need in code rather than through XML

		mFlipAnimation = new RotateAnimation(0, -180,

		RotateAnimation.RELATIVE_TO_SELF, 0.5f,

		RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		mFlipAnimation.setInterpolator(new LinearInterpolator());

		mFlipAnimation.setDuration(250);

		mFlipAnimation.setFillAfter(true);

		mReverseFlipAnimation = new RotateAnimation(-180, 0,

		RotateAnimation.RELATIVE_TO_SELF, 0.5f,

		RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());

		mReverseFlipAnimation.setDuration(250);

		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());

		// header view ÔÚ´ËÌí¼Ó,±£Ö¤ÊÇµÚÒ»¸öÌí¼Óµ½linearlayoutµÄ×îÉÏ¶Ë

		addHeaderView();

	}

	private void addHeaderView() {

		// header view

		mHeaderView = mInflater.inflate(
				R.layout.pull_to_refresh_header_vertical, this, false);

		mHeaderImageView = (ImageView) mHeaderView

		.findViewById(R.id.pull_to_refresh_image);

		mHeaderTextView = (TextView) mHeaderView

		.findViewById(R.id.pull_to_refresh_text);

		mHeaderUpdateTextView = (TextView) mHeaderView

		.findViewById(R.id.pull_to_refresh_text);

		mHeaderProgressBar = (ProgressBar) mHeaderView

		.findViewById(R.id.pull_to_refresh_progress);

		// header layout

		measureView(mHeaderView);

		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,

		mHeaderViewHeight);

		// ÉèÖÃtopMarginµÄÖµÎª¸ºµÄheader View¸ß¶È,¼´½«ÆäÒþ²ØÔÚ×îÉÏ·½

		params.topMargin = -(mHeaderViewHeight);

		// mHeaderView.setLayoutParams(params1);

		addView(mHeaderView, params);

	}

	private void addFooterView() {

		// footer view

		mFooterView = mInflater.inflate(
				R.layout.pull_to_refresh_header_vertical, this, false);

		mFooterImageView = (ImageView) mFooterView

		.findViewById(R.id.pull_to_refresh_image);

		mFooterTextView = (TextView) mFooterView

		.findViewById(R.id.pull_to_refresh_text);

		mFooterProgressBar = (ProgressBar) mFooterView

		.findViewById(R.id.pull_to_refresh_progress);

		// footer layout

		measureView(mFooterView);

		mFooterViewHeight = mFooterView.getMeasuredHeight();

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,

		mFooterViewHeight);

		// int top = getHeight();

		// params.topMargin

		// =getHeight();//ÔÚÕâÀïgetHeight()==0,µ«ÔÚonInterceptTouchEvent()·½·¨ÀïgetHeight()ÒÑ¾&shy;ÓÐÖµÁË,²»ÔÙÊÇ0;

		// getHeight()Ê²Ã´Ê±ºò»á¸³Öµ,ÉÔºòÔÙÑÐ¾¿Ò»ÏÂ

		// ÓÉÓÚÊÇÏßÐÔ²¼¾Ö¿ÉÒÔÖ±½ÓÌí¼Ó,Ö»ÒªAdapterViewµÄ¸ß¶ÈÊÇMATCH_PARENT,ÄÇÃ´footer
		// view¾Í»á±»Ìí¼Óµ½×îºó,²¢Òþ²Ø

		addView(mFooterView, params);

	}

	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();

		// footer view ÔÚ´ËÌí¼Ó±£Ö¤Ìí¼Óµ½linearlayoutÖÐµÄ×îºó

		addFooterView();

		initContentAdapterView();

	}

	/**
	 * 
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-30ÏÂÎç8:48:12
	 */

	private void initContentAdapterView() {

		int count = getChildCount();

		if (count < 3) {

			throw new IllegalArgumentException(

					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");

		}

		View view = null;

		for (int i = 0; i < count - 1; ++i) {

			view = getChildAt(i);

			if (view instanceof AdapterView<?>) {

				mAdapterView = (AdapterView<?>) view;

			}

			if (view instanceof ScrollView) {

				// finish later

				mScrollView = (ScrollView) view;

			}

		}

		if (mAdapterView == null && mScrollView == null) {

			throw new IllegalArgumentException(

			"must contain a AdapterView or ScrollView in this layout!");

		}

	}

	private void measureView(View child) {

		ViewGroup.LayoutParams p = child.getLayoutParams();

		if (p == null) {

			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,

			ViewGroup.LayoutParams.WRAP_CONTENT);

		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);

		int lpHeight = p.height;

		int childHeightSpec;

		if (lpHeight > 0) {

			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,

			MeasureSpec.EXACTLY);

		} else {

			childHeightSpec = MeasureSpec.makeMeasureSpec(0,

			MeasureSpec.UNSPECIFIED);

		}

		child.measure(childWidthSpec, childHeightSpec);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {

		int y = (int) e.getRawY();

		switch (e.getAction()) {

		case MotionEvent.ACTION_DOWN:

			// Ê×ÏÈÀ¹½ØdownÊÂ¼þ,¼ÇÂ¼y×ø±ê

			mLastMotionY = y;

			break;

		case MotionEvent.ACTION_MOVE:

			// deltaY > 0 ÊÇÏòÏÂÔË¶¯,< 0ÊÇÏòÉÏÔË¶¯

			int deltaY = y - mLastMotionY;

			if (isRefreshViewScroll(deltaY)) {

				return true;

			}

			break;

		case MotionEvent.ACTION_UP:

		case MotionEvent.ACTION_CANCEL:

			break;

		}

		return false;

	}

	/*
	 * 
	 * Èç¹ûÔÚonInterceptTouchEvent()·½·¨ÖÐÃ»ÓÐÀ¹½Ø(¼´onInterceptTouchEvent()·½·¨ÖÐ
	 * return
	 * 
	 * false)ÔòÓÉPullToRefreshView
	 * µÄ×ÓViewÀ´´¦Àí;·ñÔòÓÉÏÂÃæµÄ·½·¨À´´¦Àí(¼´ÓÉPullToRefreshView×Ô¼ºÀ´´¦Àí)
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mLock) {

			return true;

		}

		int y = (int) event.getRawY();

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			// onInterceptTouchEventÒÑ¾&shy;¼ÇÂ¼

			// mLastMotionY = y;

			break;

		case MotionEvent.ACTION_MOVE:

			int deltaY = y - mLastMotionY;

			if (mPullState == PULL_DOWN_STATE) {

				// PullToRefreshViewÖ´ÐÐÏÂÀ&shy;

				// Log.i(TAG, " pull down!parent view move!");

				// headerPrepareToRefresh(deltaY);

				// setHeaderPadding(-mHeaderViewHeight);

			} else if (mPullState == PULL_UP_STATE) {

				// PullToRefreshViewÖ´ÐÐÉÏÀ&shy;

				// Log.i(TAG, "pull up!parent view move!");

				footerPrepareToRefresh(deltaY);

			}

			mLastMotionY = y;

			break;

		case MotionEvent.ACTION_UP:

		case MotionEvent.ACTION_CANCEL:

			int topMargin = getHeaderTopMargin();

			if (mPullState == PULL_DOWN_STATE) {

				if (topMargin >= 0) {

					// ¿ªÊ¼Ë¢ÐÂ

					headerRefreshing();

				} else {

					// »¹Ã»ÓÐÖ´ÐÐË¢ÐÂ£¬ÖØÐÂÒþ²Ø

					setHeaderTopMargin(-mHeaderViewHeight);

				}

			} else if (mPullState == PULL_UP_STATE) {

				if (Math.abs(topMargin) >= mHeaderViewHeight

				+ mFooterViewHeight) {

					// ¿ªÊ¼Ö´ÐÐfooter Ë¢ÐÂ

					footerRefreshing();

				} else {

					// »¹Ã»ÓÐÖ´ÐÐË¢ÐÂ£¬ÖØÐÂÒþ²Ø

					setHeaderTopMargin(-mHeaderViewHeight);

				}

			}

			break;

		}

		return super.onTouchEvent(event);

	}

	/**
	 * 
	 * ÊÇ·ñÓ¦¸Ãµ½ÁË¸¸View,¼´PullToRefreshView»¬¶¯
	 * 
	 * 
	 * 
	 * @param deltaY
	 * 
	 *            , deltaY > 0 ÊÇÏòÏÂÔË¶¯,< 0ÊÇÏòÉÏÔË¶¯
	 * 
	 * @return
	 */

	private boolean isRefreshViewScroll(int deltaY) {

		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {

			return false;

		}

		// ¶ÔÓÚListViewºÍGridView

		if (mAdapterView != null) {

			// ×Óview(ListView or GridView)»¬¶¯µ½×î¶¥¶Ë

			if (deltaY > 0) {

				View child = mAdapterView.getChildAt(0);

				if (child == null) {

					// Èç¹ûmAdapterViewÖÐÃ»ÓÐÊý¾Ý,²»À¹½Ø

					return false;

				}

				if (mAdapterView.getFirstVisiblePosition() == 0

				&& child.getTop() == 0) {

					mPullState = PULL_DOWN_STATE;

					return true;

				}

				int top = child.getTop();

				int padding = mAdapterView.getPaddingTop();

				if (mAdapterView.getFirstVisiblePosition() == 0

				&& Math.abs(top - padding) <= 8) {// ÕâÀïÖ®Ç°ÓÃ3¿ÉÒÔÅÐ¶Ï,µ«ÏÖÔÚ²»ÐÐ,»¹Ã»ÕÒµ½Ô&shy;Òò

					mPullState = PULL_DOWN_STATE;

					return true;

				}

			} else if (deltaY < 0) {

				View lastChild = mAdapterView.getChildAt(mAdapterView

				.getChildCount() - 1);

				if (lastChild == null) {

					// Èç¹ûmAdapterViewÖÐÃ»ÓÐÊý¾Ý,²»À¹½Ø

					return false;

				}

				// ×îºóÒ»¸ö×ÓviewµÄBottomÐ¡ÓÚ¸¸ViewµÄ¸ß¶ÈËµÃ÷mAdapterViewµÄÊý¾ÝÃ»ÓÐÌîÂú¸¸view,

				// µÈÓÚ¸¸ViewµÄ¸ß¶ÈËµÃ÷mAdapterViewÒÑ¾&shy;»¬¶¯µ½×îºó

				if (lastChild.getBottom() <= getHeight()

				&& mAdapterView.getLastVisiblePosition() == mAdapterView

				.getCount() - 1) {

					mPullState = PULL_UP_STATE;

					return true;

				}

			}

		}

		// ¶ÔÓÚScrollView

		if (mScrollView != null) {

			// ×Óscroll view»¬¶¯µ½×î¶¥¶Ë

			View child = mScrollView.getChildAt(0);

			if (deltaY > 0 && mScrollView.getScrollY() == 0) {

				mPullState = PULL_DOWN_STATE;

				return true;

			} else if (deltaY < 0

			&& child.getMeasuredHeight() <= getHeight()

			+ mScrollView.getScrollY()) {

				mPullState = PULL_UP_STATE;

				return true;

			}

		}

		return false;

	}

	/**
	 * 
	 * header ×¼±¸Ë¢ÐÂ,ÊÖÖ¸ÒÆ¶¯¹ý³Ì,»¹Ã»ÓÐÊÍ·Å
	 * 
	 * 
	 * 
	 * @param deltaY
	 * 
	 *            ,ÊÖÖ¸»¬¶¯µÄ¾àÀë
	 */

	private void headerPrepareToRefresh(int deltaY) {

		int newTopMargin = changingHeaderViewTopMargin(deltaY);

		// µ±header viewµÄtopMargin>=0Ê±£¬ËµÃ÷ÒÑ¾&shy;ÍêÈ«ÏÔÊ¾³öÀ´ÁË,ÐÞ¸Äheader
		// view µÄÌáÊ¾×´Ì¬

		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {

			mHeaderTextView.setText(R.string.pull_to_refresh_release_label);

			mHeaderUpdateTextView.setVisibility(View.VISIBLE);

			mHeaderImageView.clearAnimation();

			mHeaderImageView.startAnimation(mFlipAnimation);

			mHeaderState = RELEASE_TO_REFRESH;

		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// ÍÏ¶¯Ê±Ã»ÓÐÊÍ·Å

			mHeaderImageView.clearAnimation();

			mHeaderImageView.startAnimation(mFlipAnimation);

			// mHeaderImageView.

			mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);

			mHeaderState = PULL_TO_REFRESH;

		}

	}

	/**
	 * 
	 * footer ×¼±¸Ë¢ÐÂ,ÊÖÖ¸ÒÆ¶¯¹ý³Ì,»¹Ã»ÓÐÊÍ·Å ÒÆ¶¯footer
	 * view¸ß¶ÈÍ¬ÑùºÍÒÆ¶¯header view
	 * 
	 * ¸ß¶ÈÊÇÒ»Ñù£¬¶¼ÊÇÍ¨¹ýÐÞ¸Äheader viewµÄtopmarginµÄÖµÀ´´ïµ½
	 * 
	 * 
	 * 
	 * @param deltaY
	 * 
	 *            ,ÊÖÖ¸»¬¶¯µÄ¾àÀë
	 */

	private void footerPrepareToRefresh(int deltaY) {

		int newTopMargin = changingHeaderViewTopMargin(deltaY);

		// Èç¹ûheader view topMargin µÄ¾ø¶ÔÖµ´óÓÚ»òµÈÓÚheader + footer µÄ¸ß¶È

		// ËµÃ÷footer view ÍêÈ«ÏÔÊ¾³öÀ´ÁË£¬ÐÞ¸Äfooter view µÄÌáÊ¾×´Ì¬

		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)

		&& mFooterState != RELEASE_TO_REFRESH) {

			mFooterTextView

			.setText(R.string.pull_to_refresh_from_bottom_release_label);

			mFooterImageView.clearAnimation();

			mFooterImageView.startAnimation(mFlipAnimation);

			mFooterState = RELEASE_TO_REFRESH;

		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {

			mFooterImageView.clearAnimation();

			mFooterImageView.startAnimation(mFlipAnimation);

			mFooterTextView
					.setText(R.string.pull_to_refresh_from_bottom_pull_label);

			mFooterState = PULL_TO_REFRESH;

		}

	}

	/**
	 * 
	 * ÐÞ¸ÄHeader view top marginµÄÖµ
	 * 
	 * 
	 * 
	 * @description
	 * 
	 * @param deltaY
	 * 
	 * @return hylin 2012-7-31ÏÂÎç1:14:31
	 */

	private int changingHeaderViewTopMargin(int deltaY) {

		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();

		float newTopMargin = params.topMargin + deltaY * 0.3f;

		// ÕâÀï¶ÔÉÏÀ&shy;×öÒ»ÏÂÏÞÖÆ,ÒòÎªµ±Ç°ÉÏÀ&shy;ºóÈ»ºó²»ÊÍ·ÅÊÖÖ¸Ö±½ÓÏÂÀ&shy;,»á°ÑÏÂÀ&shy;Ë¢ÐÂ¸ø´¥·¢ÁË,¸ÐÐ»ÍøÓÑyufengzungzheµÄÖ¸³ö

		// ±íÊ¾Èç¹ûÊÇÔÚÉÏÀ&shy;ºóÒ»¶Î¾àÀë,È»ºóÖ±½ÓÏÂÀ&shy;

		if (deltaY > 0 && mPullState == PULL_UP_STATE
				&& Math.abs(params.topMargin) <= mHeaderViewHeight) {

			return params.topMargin;

		}

		// Í¬ÑùµØ,¶ÔÏÂÀ&shy;×öÒ»ÏÂÏÞÖÆ,±ÜÃâ³öÏÖ¸úÉÏÀ&shy;²Ù×÷Ê±Ò»ÑùµÄbug

		if (deltaY < 0 && mPullState == PULL_DOWN_STATE
				&& Math.abs(params.topMargin) >= mHeaderViewHeight) {

			return params.topMargin;

		}

		params.topMargin = (int) newTopMargin;

		mHeaderView.setLayoutParams(params);

		invalidate();

		return params.topMargin;

	}

	/**
	 * 
	 * header refreshing
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-31ÉÏÎç9:10:12
	 */

	private void headerRefreshing() {

		mHeaderState = REFRESHING;

		setHeaderTopMargin(0);

		mHeaderImageView.setVisibility(View.GONE);

		mHeaderImageView.clearAnimation();

		mHeaderImageView.setImageDrawable(null);

		mHeaderProgressBar.setVisibility(View.VISIBLE);

		mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);

		if (mOnHeaderRefreshListener != null) {

			mOnHeaderRefreshListener.onHeaderRefresh(this);

		}

	}

	/**
	 * 
	 * footer refreshing
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-31ÉÏÎç9:09:59
	 */

	private void footerRefreshing() {

		mFooterState = REFRESHING;

		int top = mHeaderViewHeight + mFooterViewHeight;

		setHeaderTopMargin(-top);

		mFooterImageView.setVisibility(View.GONE);

		mFooterImageView.clearAnimation();

		mFooterImageView.setImageDrawable(null);

		mFooterProgressBar.setVisibility(View.VISIBLE);

		mFooterTextView

		.setText(R.string.pull_to_refresh_from_bottom_refreshing_label);

		if (mOnFooterRefreshListener != null) {

			mOnFooterRefreshListener.onFooterRefresh(this);

		}

	}

	/**
	 * 
	 * ÉèÖÃheader view µÄtopMarginµÄÖµ
	 * 
	 * 
	 * 
	 * @description
	 * 
	 * @param topMargin
	 * 
	 *            £¬Îª0Ê±£¬ËµÃ÷header view ¸ÕºÃÍêÈ«ÏÔÊ¾³öÀ´£»
	 *            Îª-mHeaderViewHeightÊ±£¬ËµÃ÷ÍêÈ«Òþ²ØÁË
	 * 
	 *            hylin 2012-7-31ÉÏÎç11:24:06
	 */

	private void setHeaderTopMargin(int topMargin) {

		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();

		params.topMargin = topMargin;

		mHeaderView.setLayoutParams(params);

		invalidate();

	}

	/**
	 * 
	 * header view Íê³É¸üÐÂºó»Ö¸´³õÊ¼×´Ì¬
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-31ÉÏÎç11:54:23
	 */

	public void onHeaderRefreshComplete() {

		setHeaderTopMargin(-mHeaderViewHeight);

		mHeaderImageView.setVisibility(View.VISIBLE);

		mHeaderImageView.setImageResource(R.drawable.indicator_arrow);

		mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);

		mHeaderProgressBar.setVisibility(View.GONE);

		// mHeaderUpdateTextView.setText("");

		mHeaderState = PULL_TO_REFRESH;

	}

	/**
	 * 
	 * Resets the list to a normal state after a refresh.
	 * 
	 * 
	 * 
	 * @param lastUpdated
	 * 
	 *            Last updated at.
	 */

	public void onHeaderRefreshComplete(CharSequence lastUpdated) {

		setLastUpdated(lastUpdated);

		onHeaderRefreshComplete();

	}

	/**
	 * 
	 * footer view Íê³É¸üÐÂºó»Ö¸´³õÊ¼×´Ì¬
	 */

	public void onFooterRefreshComplete() {

		setHeaderTopMargin(-mHeaderViewHeight);

		mFooterImageView.setVisibility(View.VISIBLE);

		mFooterImageView.setImageResource(R.drawable.indicator_arrow);

		mFooterTextView.setText(R.string.pull_to_refresh_pull_label);

		mFooterProgressBar.setVisibility(View.GONE);

		// mHeaderUpdateTextView.setText("");

		mFooterState = PULL_TO_REFRESH;

	}

	/**
	 * 
	 * Set a text to represent when the list was last updated.
	 * 
	 * 
	 * 
	 * @param lastUpdated
	 * 
	 *            Last updated at.
	 */

	public void setLastUpdated(CharSequence lastUpdated) {

		if (lastUpdated != null) {

			mHeaderUpdateTextView.setVisibility(View.VISIBLE);

			mHeaderUpdateTextView.setText(lastUpdated);

		} else {

			mHeaderUpdateTextView.setVisibility(View.GONE);

		}

	}

	/**
	 * 
	 * »ñÈ¡µ±Ç°header view µÄtopMargin
	 * 
	 * 
	 * 
	 * @description
	 * 
	 * @return hylin 2012-7-31ÉÏÎç11:22:50
	 */

	private int getHeaderTopMargin() {

		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();

		return params.topMargin;

	}

	/**
	 * 
	 * lock
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-27ÏÂÎç6:52:25
	 */

	private void lock() {

		mLock = true;

	}

	/**
	 * 
	 * unlock
	 * 
	 * 
	 * 
	 * @description hylin 2012-7-27ÏÂÎç6:53:18
	 */

	private void unlock() {

		mLock = false;

	}

	/**
	 * 
	 * set headerRefreshListener
	 * 
	 * 
	 * 
	 * @description
	 * 
	 * @param headerRefreshListener
	 * 
	 *            hylin 2012-7-31ÉÏÎç11:43:58
	 */

	public void setOnHeaderRefreshListener(

	OnHeaderRefreshListener headerRefreshListener) {

		mOnHeaderRefreshListener = headerRefreshListener;

	}

	public void setOnFooterRefreshListener(

	OnFooterRefreshListener footerRefreshListener) {

		mOnFooterRefreshListener = footerRefreshListener;

	}

	/**
	 * 
	 * Interface definition for a callback to be invoked when list/grid footer
	 * 
	 * view should be refreshed.
	 */

	public interface OnFooterRefreshListener {

		public void onFooterRefresh(final PullToRefreshLayout view);

	}

	/**
	 * 
	 * Interface definition for a callback to be invoked when list/grid header
	 * 
	 * view should be refreshed.
	 */

	public interface OnHeaderRefreshListener {

		public void onHeaderRefresh(final PullToRefreshLayout view);

	}

}
