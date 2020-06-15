/**
 * Brandon Le
 * Harry Tran
 * HM3
 */

package com.example.myapplication;

//import android.support.v4.app.FragmentActivity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private EditText mEditText;
    private Button searchButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MarkerOptions> markedList = new ArrayList<MarkerOptions>();       // list of all confirmed searched addresses
    private LinearLayoutManager linearManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Address being entered by the user
        mEditText = findViewById(R.id.edit_text_search);

        // Recycler view
        mRecyclerView = findViewById(R.id.recycler_view);

        // Linear layout manager
        linearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearManager);

        // Search button
        searchButton = findViewById(R.id.search_button);

        // Adapter
        mAdapter = new MainAdapter(this, markedList, new MainAdapter.itemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MarkerOptions temp = markedList.get(position);

                // moves the map to the address
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp.getPosition(), 14));
            }
        });

        // set the adapter for the recycler view
        mRecyclerView.setAdapter(mAdapter);

        // Use to separate each address in recycler view
        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(),
                linearManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);

        mapFragment.getMapAsync(this);
    }

    // allows the user to either press the search button or press enter on the keyboard
    private void init()
    {
        // looks for enter key to be pressed to search for location of address entered
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    getLocationFromAddress();
                }
                return false;
            }
        });

        // searches for location of addresses entered when search button is pressed
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationFromAddress();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 15;

        LatLng home = new LatLng(33.688471, -117.964540);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));

        init();
    }

    // Gets the string of the address entered, converts to LatLng object, then moves the camera
    // to the location of the address
    private void getLocationFromAddress()
    {
        String searchAddress = mEditText.getText().toString();
        Geocoder coder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();

        try {
            addressList = coder.getFromLocationName(searchAddress, 1);
        }
        catch (IOException e) {
            Log.e(TAG, "getLocationFromAddress: IOException: " + e.getMessage());
        }

        // make sure that the address is in the list
        if (!addressList.isEmpty())
        {
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            MarkerOptions temp = new MarkerOptions().position(latLng).title(searchAddress);
            mMap.addMarker(temp);

            addToList(temp);
        }
        else {
            CharSequence invalidAddress = "Invalid Address";
            Toast toast = Toast.makeText(getApplicationContext(), invalidAddress, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // adds new address to list of saved addresses
    // new address is added to the top of the list
    public void addToList(MarkerOptions input) {
        markedList.add(0, input);
        mRecyclerView.getAdapter().notifyItemInserted(0);

        mRecyclerView.smoothScrollToPosition(0);
    }
}

