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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import ca.synx.miway.interfaces.IStopsTask;
import ca.synx.miway.models.Stop;
import ca.synx.miway.util.DatabaseHandler;
import ca.synx.miway.util.StorageHandler;

public class MapActivity extends ActionBarActivity implements IStopsTask {

    private Context mContext;
    private DatabaseHandler mDatabaseHandler;
    private StorageHandler mStorageHandler;

    private GoogleMap mGoogleMap;
    private LocationListener mLocationListener;
    private List<Stop> mStops;

    private ProgressDialog mProgressDialog;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        mContext = this;
        mDatabaseHandler = new DatabaseHandler(this);
        mStorageHandler = new StorageHandler(mDatabaseHandler);

        // Display loading dialog.
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_stops));
        mProgressDialog.show();

        // Update the title of the activity.
        setTitle(String.format(
                getString(R.string.map)
        ));

        // Initiate map.
        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map))
                .getMap();

        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setMyLocationEnabled(true);

        // Get a fix on the location.
        getLocation();
    }

    protected void getLocation() {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), 13)
                );
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
                LocationManager.GPS_PROVIDER, 0, 0, mLocationListener
        );
    }

    protected void drawMarkers() {

        int progress = 0;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.processing_map_data));
        mProgressDialog.setProgress(progress);
        mProgressDialog.setMax(mStops.size());
        mProgressDialog.show();

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

        // Dismiss the progress dialog (if any)
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onStopsTaskComplete(List<Stop> stops) {
        mStops = stops;

        // Dismiss the progress dialog (if any)
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        drawMarkers();
    }
}