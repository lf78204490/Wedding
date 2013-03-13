package cn.op.wedding.ui;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.op.common.util.GMapV2Direction;
import cn.op.wedding.AppException;
import cn.op.wedding.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapFragment extends SupportMapFragment implements
		OnMarkerClickListener, OnInfoWindowClickListener {

	private LatLng mPosFija;
	private FragmentActivity activity;
	private GoogleMap mMap;
	private MapView mapView;
	private View viewOver;
	private LayoutInflater inflater;
	private Marker markerDest;
	protected Location mCurtLocation;
	private GMapV2Direction md;
	protected Location mLastlocation;

	public GoogleMapFragment() {
		super();
	}

	public static GoogleMapFragment newInstance(LatLng posicion) {
		GoogleMapFragment frag = new GoogleMapFragment();
		frag.mPosFija = posicion;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState); // 必须

		this.inflater = inflater;
		View view = inflater.inflate(R.layout.google_map, null);
		md = new GMapV2Direction();
		mapView = (MapView) view.findViewById(R.id.mapView);
		viewOver = view.findViewById(R.id.viewOver);
		View btnRoute = view.findViewById(R.id.btnRoute);

		// 过度背景，否则会出现透明
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				viewOver.setBackgroundColor(getResources().getColor(
						R.color.transparent));
			}
		};
		new Thread() {
			@Override
			public void run() {
				try {
					this.sleep(2000);
					h.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

		btnRoute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurtLocation != null) {
					drawDirection(new LatLng(mCurtLocation.getLatitude(),
							mCurtLocation.getLongitude()), mPosFija);
				}
			}
		});

		return view;
	}

	/**
	 * 绘制路线
	 * 
	 * @param from
	 * @param to
	 */
	protected void drawDirection(final LatLng fromPosition,
			final LatLng toPosition) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == -1) {
					((AppException) msg.obj).makeToast(activity);
				} else {
					ArrayList<LatLng> directionPoint = (ArrayList<LatLng>) msg.obj;

					PolylineOptions rectLine = new PolylineOptions().width(3)
							.color(Color.RED);
					for (int i = 0; i < directionPoint.size(); i++) {
						rectLine.add(directionPoint.get(i));
					}

					mMap.addPolyline(rectLine);
				}
			}
		};

		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					Document doc = md.getDocument(fromPosition, toPosition,
							GMapV2Direction.MODE_DRIVING);
					ArrayList<LatLng> directionPoint = md.getDirection(doc);

					msg.what = directionPoint.size();
					msg.obj = directionPoint;
				} catch (AppException e) {
					e.printStackTrace();
					msg = new Message();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			};
		}.start();

	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = mapView.getMap();
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);

				mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
					@Override
					public void onMyLocationChange(Location loca) {
						mCurtLocation = loca;
						Toast.makeText(activity,
								"loca.getLatitude()= " + loca.getLatitude(),
								Toast.LENGTH_LONG).show();
					}
				});

				setUpMap();
			}
		}
	}

	private void setUpMap() {
		if (mPosFija != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosFija, 15));
			markerDest = mMap.addMarker(new MarkerOptions()
					.position(mPosFija)
					.title("粤珍轩（新城广场）")
					.snippet("电话: 1,213,000")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.map_mark)));

			// Setting an info window adapter allows us to change the both the
			// contents and look of the
			// info window.
			mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

			// Set listeners for marker events. See the bottom of this class for
			// their behavior.
			mMap.setOnMarkerClickListener(this);
			mMap.setOnInfoWindowClickListener(this);
		}

		UiSettings uiSettings = mMap.getUiSettings();
		uiSettings.setAllGesturesEnabled(true);
		uiSettings.setZoomControlsEnabled(false);

	}

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		// These a both viewgroups containing an ImageView with id "badge" and
		// two TextViews with id
		// "title" and "snippet".
		private final View mWindow;
		private final View mContents;

		CustomInfoWindowAdapter() {
			mWindow = inflater.inflate(R.layout.custom_info_window, null);
			mContents = inflater.inflate(R.layout.custom_info_contents, null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			int badge;
			// Use the equals() method on a Marker to check for equals. Do not
			// use ==.
			if (marker.equals(markerDest)) {
				badge = R.drawable.ic_launcher;
			} else {
				// Passing 0 to setImageResource will clear the image view.
				badge = 0;
			}
			((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				// Spannable string allows us to edit the formatting of the
				// text.
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
						titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}

			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null && snippet.length() > 12) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0,
						3, 0);
				snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 3,
						snippet.length(), 0);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		// 必须
		mapView.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!this.isGoogleMapsInstalled()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage("您的手机缺少“Google play 服务”，需要先安装此服务。");
			builder.setCancelable(false);
			builder.setPositiveButton("去市场搜索", getGoogleMapsListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		mapView.onResume();

		setUpMapIfNeeded();
	}

	public boolean isGoogleMapsInstalled() {
		try {
			ApplicationInfo info = activity.getPackageManager()
					.getApplicationInfo("com.google.android.apps.maps", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public OnClickListener getGoogleMapsListener() {
		return new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Intent intent = new Intent(
				// Intent.ACTION_VIEW,
				// Uri.parse("market://details?id=com.google.android.maps"));
				Intent intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://search?q=pname:com.google.android.gms"));
				startActivity(intent);

				// Finish the activity so they can't circumvent the check
				activity.finish();
			}
		};
	}

	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			mMap.setMyLocationEnabled(false);
		} else {
			mMap.setMyLocationEnabled(true);
		}
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
}