/*
 * Copyright (c) 2014, SYNX (Gideon Bakx)
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ca.synx.miway.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ca.synx.miway.adapters.SpinnerItemAdapter;
import ca.synx.miway.interfaces.IRoutesTask;
import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Route;
import ca.synx.miway.models.Stop;
import ca.synx.miway.tasks.RouteStopsTask;
import ca.synx.miway.tasks.RoutesTask;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class MapActivity extends ActionBarActivity implements IRoutesTask, IStopsTask, ActionBar.OnNavigationListener {
    static final String sSTOP_DATA = "stopData";
    static final String sROUTE_DATA = "routeData";

    private static int mZoomLevel = 12;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private ProgressDialog mProgressDialog;
    private GoogleMap mGoogleMap;

    private LocationListener mLocationListener;
    private SpinnerItemAdapter<Route> mRouteAdapter;

    private List<Route> mRoutes;
    private Route mRoute;
    private List<Stop> mStops;
    private Stop mStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Check if the Google Play Service is available. This is required for Maps v2 to work.
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.sorry));
            alertDialogBuilder.setMessage(getString(R.string.map_not_available));
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        Intent intent = getIntent();
        mRoute = (Route) intent.getSerializableExtra(sROUTE_DATA);
        mStop = (Stop) intent.getSerializableExtra(sSTOP_DATA);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mContext = this;
        mDatabaseHandler = new DatabaseHandler(this);
        mStorageHandler = new StorageHandler(mDatabaseHandler);

        // Update the title of the activity.
        setTitle(String.format(
                getString(R.string.map)
        ));

        //
        // Initiate map.
        //

        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map))
                .getMap();

        if (mGoogleMap == null)
            return;

        mGoogleMap.setMyLocationEnabled(true);

        //
        // Location awareness.
        //

        // If a stop is not specified, get the user's location from the GPS.
        if (mStop == null)
            getLocation();

        //
        // Get route data.
        //

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);

        new RoutesTask(this, mStorageHandler).execute();
    }

    protected void getLocation() {

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                // Move camera to position mentioned by the GPS.
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), mZoomLevel)
                );

                // Stop listening for updates.
                locationManager.removeUpdates(this);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 60, 50, mLocationListener
        );
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        mProgressDialog.setMessage(getString(R.string.loading_stops));

        // Clear previous markers.
        mGoogleMap.clear();

        Route route = mRoutes.get(itemPosition);
        new RouteStopsTask(this, mStorageHandler).execute(route);
        return true;
    }

    protected void drawMarkers() {

        int progress = 0;

        mProgressDialog.setProgress(progress);
        mProgressDialog.setMax(mStops.size());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Draw all stops for selected route.
        for (Stop stop : mStops) {
            try {
                mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop))
                                .title(stop.getFull())
                                .snippet(stop.getStopId())
                                .position(new LatLng(stop.getStopLat(), stop.getStopLon()))
                );

                mProgressDialog.setProgress(progress++);

            } catch (Exception e) {
                Log.e("MapActivity:drawMarkers", "" + e.getMessage());
                e.printStackTrace();
            }
        }

        // Move the camera to the position of the stop (if set).
        if (mStop != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mStop.getStopLat(), mStop.getStopLon()), mZoomLevel));

            mStop = null;
        }

        // Dismiss the progress dialog (if any)
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    private int getRoutePosition() {
        if (mRoute != null && mRouteAdapter != null && mRouteAdapter.getCount() > 0) {
            for (int i = 0; i < mRouteAdapter.getCount(); i++) {

                Route route = mRouteAdapter.getItem(i);

                if (route.isEqual(mRoute))
                    return i;
            }
        }

        return -1;
    }

    @Override
    public void onRoutesTaskComplete(List<Route> routes) {
        mRoutes = routes;

        mRouteAdapter = new SpinnerItemAdapter<Route>(mContext, mRoutes, R.layout.spinner_item_single);
        getSupportActionBar().setListNavigationCallbacks(mRouteAdapter, this);

        // If mRoute is set, show this location by default.
        getSupportActionBar().setSelectedNavigationItem(getRoutePosition());
    }

    @Override
    public void onStopsTaskComplete(List<Stop> stops) {
        mStops = stops;

        drawMarkers();
    }
}