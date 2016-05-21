package ro.netex.upack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public MenuActivity navigationMenu;
    public Menu menu;
    int currentItemId;
    UPackApi api;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.context = this;
        getSuppliers();
        addNavigationToolbar();

    }

    public void addNavigationToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        currentItemId = R.id.supplier;
        navigationMenu = new MenuActivity();
        navigationMenu.setContext(this);
        navigationMenu.setNavigationToolbar(navigationView, menu, toolbar, drawer, currentItemId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getSuppliers();
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                @Override
                public boolean onMyLocationButtonClick()
                {
                    //TODO: Any custom actions
                    return false;
                }
            });
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            zoomMapToCurrentLocation();
            getCurrentUserLocation();
        }

    }

    public void populateMapWithSuppliers(JSONArray suppliers) {

        // parse suppliers list
        for (int i = 0; i < suppliers.length(); i++) {
            JSONObject jsonobject = null;
            JSONObject address = null;
            try {
                jsonobject = suppliers.getJSONObject(i);
                String name = jsonobject.getString("name");

                // get supplier's lat and lng
                address = jsonobject.getJSONObject("address");
                Double lat = address.getDouble("lat");
                Double lng = address.getDouble("lng");
                // Add a marker supplier coordinates on map
                LatLng supplierLat = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(supplierLat).title(name));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(supplierLat));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // get all available suppliers on Map
    public void getSuppliers() {
        this.api = new UPackApi("hacktm.netex.ro", this);
        this.api.call("get_suppliers");
    }

    // set zoom to current user location
    private void zoomMapToCurrentLocation() {

        if (checkGPSStatus()) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                }
            });
        } else {
            buildAlertMessageNoGps();
        }
    }

    // check if GPS is enable
    public boolean checkGPSStatus() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;


    }

    // show dialog where GPS is not enabled
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // get user coordinates
    public JSONArray getCurrentUserLocation() {

        JSONArray coordinates = new JSONArray();

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            JSONObject itemA = new JSONObject();
            try {
                itemA.put("lat", lat);
                itemA.put("lng", lng);
                coordinates.put(itemA);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coordinates;
    }

}
