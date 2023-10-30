package es.rafapuig.sensors.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView googleMapsView; // This is the widget with Google map
    GoogleMap googleMap; // This is an object that allows us to place pins on the googleMapsView
    // The system will give it to us via the callback "onMapReady"

    TextView tvNetworkCoords; // Text box for printing the coordinates of the network provider
    TextView tvGpsCoords; // Text box to print gps provider coordinates
    TextView tvInfo; //Text box for writing event information

    LocationManager locationManager; // Location system manager
    LocationListener listener; // Listener that receives gps readings

    String currentProvider = LocationManager.NETWORK_PROVIDER; // Location provider currently selected in the radiobuttons
    Location readMapCoords = null; // Last coordinates read to place the pin on google map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeUI(); // Assigning variables to interface components and registering listeners
        requestPermissions(); // In order to access the location system
        setInitialLocalization(); // Print and locate on the map the last known coordinates (before getting any readings!)

        // Google Maps initialization
        googleMapsView.onCreate(savedInstanceState); // We have to call the onCreate of the view with the map.
        googleMapsView.getMapAsync(this); // Get the GoogleMap object associated to the view (will call the "onMapReady" callback when ready)
    }

    private void initializeUI() {
        // Get references to interface components

        // TODO: get the references for googleMapsView, tvNetworkCoords, tvGpsCoords y tvInfo
        googleMapsView = ...
        tvNetworkCoords = ...
        tvGpsCoords = ...
        tvInfo = ...


        // Register listeners
        ((RadioGroup) findViewById(R.id.rg_selected_provider))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        // When changing providers in the radioButtons,
                        // turn off the listener, update the current provider and turn it back on.

                        // TODO: call to turnOffListener, update currentProvider and call to a turnOnListener


                        report("Changing to provider " + currentProvider + "...");
                    }
                });

        ((ToggleButton) findViewById(R.id.tb_switch_location))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        // When we change the state of the togglebutton to turn the gps readings on or off.

                        // TODO: if the button is pressed, call to initializeListener (that will give value to the variable "listener")
                        //  and call to turnOnListener
                        //  if not, call to turnOffListener and set the variable "listener" to null


                    }
                });

        report("Initialized");
    }


    private void initializeListener() {
        // We create the listener and tell it to print the new coordinates when the reading arrives
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //TODO: In which text box we print (network or gps). We have the variable "currentProvider"

                //TODO: Print these coordinates in this text box
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                report("Provider " + provider + " enabled");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                report("Provider " + provider + " disabled");
            }

        };
    }

    private void requestPermissions() {
        // Ask for ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION permissions if the app doesn't have them
        // (apart from putting it in the manifest, as of a certain version of Android
        // you must also ask for explicit permissions from the user).

        // TODO

    }

    private void setInitialLocalization() {
        // Get the location manager and get and print the last known coordinates (not obtained via the listener!)
        Location locRed = null;
        Location locGps = null;

        // TODO: Get the location manager "locationManager".

        try {
            // TODO: Get last known coordinates for the network provider
            locRed = ...
            // TODO: Get last known coordinates for the gps provider
            locGps = ...
        } catch (SecurityException e) {
            // When we get the last known coordinates, an error of permissions not being granted may occur!
            report("Error obtaining coordinates: " + e.getMessage());
        }

        // Print the coordinates and place them on the map
        printCoordinates(locRed, tvNetworkCoords); // TODO);
        printCoordinates(locGps, tvGpsCoords); // TODO);
    }

    private void printCoordinates(Location location, TextView which) {
        // Prints the supplied coordinates in the given TextView
        if (location != null) {
            report("New location!");

            // TODO: Print the coordinates in the TextView

            readMapCoords = location; // Define new coordinates to put on the map
            placeMarkerOnMap(); // and place a pin
        } else
            which.setText("Not available");
    }

    private void turnOnListener() {
        // It is called when we change the provider in the radioButtons, but also in the onResume event
        // We have to check that we have activated the location readings button (listener != null)
        if (listener != null) {
            report("Turning On the listener...");
            try {
                // Get readings every 100 milliseconds
                // as long as the minimum displacement distance is 1 metre, from the "currentProvider".

                // TODO: register the listener

            } catch (SecurityException e) {
                // When we try to register the listener, we may get an error that permissions have not been granted!
                report("Error turning on the listener: " + e.getMessage());
            }
        }
    }

    private void turnOffListener() {
        // It is called when we change the provider in the radioButtons, but also in the onPause event
        // We have to check that we have activated the location readings button (listener != null)
        if (listener != null) {
            report("Turning off the listener...");

            // TODO: unregister the listener
        }
    }

    private void report(String text) {
        // Añade la línea arriba de las demás
        tvInfo.setText(text + "\n" + tvInfo.getText());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleMapsView.onStart(); // call to onStart of the map
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleMapsView.onResume(); // call to onResume of the map
        turnOnListener();
    }

    @Override
    protected void onPause() {
        turnOffListener();
        googleMapsView.onPause(); // call to onPause of the map
        super.onPause();
    }

    @Override
    protected void onStop() { // call to onStop of the map
        googleMapsView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() { // call to onDestroy of the map
        googleMapsView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        googleMapsView.onSaveInstanceState(outState); // call to onSaveInstanceState of the map
    }

    @Override
    public void onLowMemory() {
        googleMapsView.onLowMemory(); // call to onLowMemory of the map
        super.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // This callback is called after googleMapsView.getMapAsyn(this) is called when the map object is ready.
        // It is inherited from the OnMapReadyCallback interface that this class implements.
        this.googleMap = googleMap; // Now we have the map object ready to place markers
        placeMarkerOnMap(); // Try to mark the location if we already have it.
    }

    private void placeMarkerOnMap() {
        // Highlights the location that we have saved in map coordinates (if already present) in the map view.
        // To do this, we need to have already obtained the map object (googleMap variable).
        if (readMapCoords != null && googleMap != null) { // Check that we have it all we need
            MarkerOptions mo = new MarkerOptions(); // We create the marker
            LatLng coord = new LatLng(readMapCoords.getLatitude(), readMapCoords.getLongitude()); // We construct coordinates
            // in the format that the marker (LatLng object) will ask us for
            mo.position(coord); // We create the marker for these coordinates
            googleMap.clear(); // We delete markers
            googleMap.addMarker(mo); // and place the new one in the map object. This will be reflected in the associated view (googleMapsView).
        }
    }
}

