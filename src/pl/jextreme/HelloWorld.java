package pl.jextreme;

import java.util.List;

import pl.jextreme.util.BeanDump;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class HelloWorld extends MapActivity {
	
	private long start = SystemClock.elapsedRealtime();
	private long stop = 0;
	private long totalPauseTime = 0;
	private static final String TAG = "OrangeClockwork";
	List<Overlay> mapOverlays;
	Drawable pointer;
	MyOverlay itemizedOverlay;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//getLastLocation();
		
		MapView mv = (MapView)findViewById(R.id.mapview);
		mv.setBuiltInZoomControls(true);
		mapOverlays = mv.getOverlays();
		pointer = getResources().getDrawable(R.drawable.map);
		itemizedOverlay = new MyOverlay(pointer);
		mapOverlays.add(itemizedOverlay);
		
		final ToggleButton toggle = (ToggleButton) findViewById(R.id.ToggleButton01);
		final Button locListenerBut = (Button)findViewById(R.id.Button02);
		final Chronometer chrono = (Chronometer) findViewById(R.id.Chronometer01);
		Button locationButon = (Button) findViewById(R.id.Button01);
		
		locationButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLastLocation();
			}
		});
		
		locListenerBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addLocationListener();
			}
		});
		
		
		
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.i("[chrono]", String.format("start:%d stop:%d delta: %d",start, stop, start - stop));
				Log.i("[chrono]", "base:" +chrono.getBase());
				Log.i("[chrono]", "elapsed time:" +SystemClock.elapsedRealtime());
				
				if (isChecked) {
					chrono.start();
					start = SystemClock.elapsedRealtime();
					chrono.setBase(start - totalPauseTime);
				} else {
					totalPauseTime += SystemClock.elapsedRealtime() - start;
					chrono.stop();
				}

			}
		});
		
	}
	private void getLastLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		TextView debug = (TextView)findViewById(R.id.TextView01);
		Log.i(TAG, locationManager.getAllProviders().toString());
		debug.setText(BeanDump.dumpFields(lastKnownLocation));
		
	}
	private void makeUseOfNewLocation(Location location) {
		TextView debug = (TextView)findViewById(R.id.TextView01);
		debug.setText(BeanDump.dumpFields(location));
		MapView mv = (MapView)findViewById(R.id.mapview);
		GeoPoint geoPoint = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
		
		mv.getController().animateTo(geoPoint);
		mv.getController().setZoom(18);
		itemizedOverlay.addItem(new OverlayItem(geoPoint, "", ""));
	}
	
	private void addLocationListener() {
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		      makeUseOfNewLocation(location);
		    }

			public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}