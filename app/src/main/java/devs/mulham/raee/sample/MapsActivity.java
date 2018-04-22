package devs.mulham.raee.sample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supakorn.Nearby.DownloadUrl;
import com.example.supakorn.Nearby.GetNearbyPlacesData;
import com.example.supakorn.checking.GetUpdateCurrent;
import com.example.supakorn.notification_morning.NotificationHelper2;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.project.kmitl57.beelife.BottomSheetListView;
import com.project.kmitl57.beelife.ChatActivity;
import com.project.kmitl57.beelife.CustombottombarMenu;
import com.project.kmitl57.beelife.MainActivity;
import com.project.kmitl57.beelife.R;

import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    public static GoogleMap mMap;
    public static String Search;
    public List<Address> GetAddress;
    public static float zoom = 17f;
    public static ArrayList<String> allplace;
    Button nearplace;
    public int doubleclick=0;
    public Marker publicmarker;
    public static ArrayList<NearPlaceType> nearPlaceTypes;
    public BottomSheetDialog bottomSheetDialog;
    public String name_place="";
    int public_index;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nearplace=(Button)findViewById(R.id.nearplace);
        allplace=new ArrayList<>();
        //ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        //params.height = 1650;

        GetAddress=new ArrayList<>();
        nearPlaceTypes=new ArrayList<>();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent i = new Intent(MapsActivity.this,GetUpdateCurrent.class);
        this.startService(i);
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        initialMap();

    }

    private void initialMap() {

        //final EditText editText = (EditText)findViewById(R.id.editText3);
        final Geocoder geocoder = new Geocoder(this);

        imageButton=(ImageButton)findViewById(R.id.Backed);
        Intent i = new Intent(this,GetUpdateCurrent.class);
        this.startService(i);
        double CurrentLat;
        double CurrentLon;
        try {
            CurrentLat=GetUpdateCurrent.location.getLatitude();
            CurrentLon=GetUpdateCurrent.location.getLongitude();
        }catch (NullPointerException e){
            CurrentLat=0;
            CurrentLon=0;
        }

        LatLng current = new LatLng(CurrentLat,CurrentLon);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,zoom));
        mMap.addMarker(new MarkerOptions().position(current).title("You Here"));
        View bottomSheetView = getLayoutInflater().inflate(R.layout.maps_btm_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetListView list = (BottomSheetListView) bottomSheetView.findViewById(R.id.list);
        CustombottombarMenu test = new CustombottombarMenu(getApplicationContext(),allplace);
        list.setAdapter(test);

        //editText.setText(Search);
        //getPlaceDetail(current.latitude,current.longitude,"hospital");
        /*CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*Toast.makeText(MapsActivity.this,String.valueOf(latLng.latitude)+"\n"+
                        String.valueOf(latLng.longitude),Toast.LENGTH_SHORT);*/
                name_place="";
                allplace.clear();
                getPlace(latLng.latitude,latLng.longitude,"Hotel");
                Toast.makeText(MapsActivity.this,String.valueOf(latLng.latitude)+" "+String.valueOf(latLng.longitude),Toast.LENGTH_SHORT).show();
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                if(!MainActivity4.isInMap(new NearPlaceType("",latLng.latitude,latLng.longitude))){
                    mMap.addMarker(new MarkerOptions().position(latLng).title(getAddress(MapsActivity.this,
                            latLng.latitude,latLng.longitude)));
                }else{
                    mMap.addMarker(new MarkerOptions().position(latLng).title(MainActivity4.getNameByLatLng(
                            latLng.latitude,latLng.longitude)));
                }
                //TextView Tag = (TextView)findViewById(R.id.textView4);
                //Tag.setText(String.valueOf(latLng.latitude)+" "+String.valueOf(latLng.longitude));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                name_place="";
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                if(!MainActivity4.isInMap(new NearPlaceType("",latLng.latitude,latLng.longitude))){
                    mMap.addMarker(new MarkerOptions().position(latLng).title(getAddress(MapsActivity.this,
                            latLng.latitude,latLng.longitude)));
                }else{
                    mMap.addMarker(new MarkerOptions().position(latLng).title(MainActivity4.getNameByLatLng(
                            latLng.latitude,latLng.longitude)));
                }
                NamePlace nameplace=new NamePlace(MapsActivity.this);
                nameplace.latitude=latLng.latitude;
                nameplace.longitude=latLng.longitude;
                if(name_place.compareTo("")!=0){
                    nameplace.Name="";
                    nameplace.Name=name_place;
                }else if(!MainActivity4.isInMap(new NearPlaceType("",latLng.latitude,
                        latLng.longitude))){
                    nameplace.Name="";
                    nameplace.Name=getAddress(MapsActivity.this,latLng.latitude
                            ,latLng.longitude);
                }else{
                    nameplace.Name="";
                    nameplace.Name=MainActivity4.getNameByLatLng(latLng.latitude,
                            latLng.longitude);
                }
                nameplace.show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LocationNameDialog nameplace=new LocationNameDialog(MapsActivity.this);
                nameplace.latitude=marker.getPosition().latitude;
                nameplace.longitude=marker.getPosition().longitude;
                if(name_place.compareTo("")!=0){
                    nameplace.Name="";
                    nameplace.Name=name_place;
                }else if(!MainActivity4.isInMap(new NearPlaceType("",marker.getPosition().latitude,
                        marker.getPosition().longitude))){
                    nameplace.Name="";
                    nameplace.Name=getAddress(MapsActivity.this,marker.getPosition().latitude
                            ,marker.getPosition().longitude);
                }else{
                    nameplace.Name="";
                    nameplace.Name=MainActivity4.getNameByLatLng(marker.getPosition().latitude,
                            marker.getPosition().longitude);
                }
                nameplace.show();

                /*publicmarker=marker;
                doubleclick++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(doubleclick==1){
                            try {
                                GetAddress = geocoder.getFromLocation(publicmarker.getPosition().latitude,publicmarker.getPosition().longitude,100);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(GetAddress.size()>0){
                                MainActivity4.location = new Place() {
                                    @Override
                                    public Place freeze() {
                                        return null;
                                    }

                                    @Override
                                    public boolean isDataValid() {
                                        return false;
                                    }

                                    @Override
                                    public String getId() {
                                        return null;
                                    }

                                    @Override
                                    public List<Integer> getPlaceTypes() {
                                        return null;
                                    }

                                    @Override
                                    public CharSequence getAddress() {
                                        return GetAddress.toString();
                                    }

                                    @Override
                                    public Locale getLocale() {
                                        return null;
                                    }

                                    @Override
                                    public CharSequence getName() {
                                        return GetAddress.get(0).toString();
                                    }

                                    @Override
                                    public LatLng getLatLng() {
                                        LatLng latLng1 = new LatLng(GetAddress.get(0).getLatitude(),GetAddress.get(0).getLongitude());
                                        return latLng1;
                                    }

                                    @Override
                                    public LatLngBounds getViewport() {
                                        return null;
                                    }

                                    @Override
                                    public Uri getWebsiteUri() {
                                        return null;
                                    }

                                    @Override
                                    public CharSequence getPhoneNumber() {
                                        return null;
                                    }

                                    @Override
                                    public float getRating() {
                                        return 0;
                                    }

                                    @Override
                                    public int getPriceLevel() {
                                        return 0;
                                    }

                                    @Override
                                    public CharSequence getAttributions() {
                                        return null;
                                    }
                                };
                                //MainActivity4.cdd.locate.setText("Latitude : " + GetAddress.get(0).getLatitude()+","+
                                //        "Longitude : " + GetAddress.get(0).getLongitude()+"," + GetAddress.get(0).getAdminArea());
                            }
                            //MapsActivity.super.finish();
                        }else if(doubleclick==2){
                            Toast.makeText(MapsActivity.this,"Double Click",Toast.LENGTH_SHORT).show();
                        }
                        doubleclick=0;
                    }
                },500);

                */
                return false;
            }
        });


        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());
                getPlace(place.getLatLng().latitude,place.getLatLng().longitude,"Hotel");
                name_place="";
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),zoom));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                Toast.makeText(MapsActivity.this,place.getName(),Toast.LENGTH_SHORT).show();
                MainActivity4.location=place;
                MainActivity4.cdd.locate.setText(place.getName());
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .build();

                autocompleteFragment.setFilter(typeFilter);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        nearplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Handler handler = new Handler();
                public_index=position;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapsActivity.this, MapsActivity.nearPlaceTypes.get(public_index).getName(),Toast.LENGTH_SHORT).show();
                        mMap.clear();
                        LatLng latLng = new LatLng(nearPlaceTypes.get(public_index).getLatitude(),
                                nearPlaceTypes.get(public_index).getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(nearPlaceTypes.get(public_index).getName()));
                        name_place=nearPlaceTypes.get(public_index).getName();
                        bottomSheetDialog.cancel();
                    }
                };
                handler.postDelayed(runnable,300);

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this,MainActivity4.class));
                finish();
            }
        });

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 500);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public void getPlace(double latitude,double longitude,String place){
        mMap.clear();
        String url = getUrl(latitude, longitude, place);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        Toast.makeText(MapsActivity.this,"Nearby Hotel", Toast.LENGTH_LONG).show();
    }

    public void getPlaceDetail(double latitude,double longitude,String place){
        mMap.clear();
        String url = getUrl(latitude, longitude, place);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        /*GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);*/
        Toast.makeText(MapsActivity.this,url, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            return add;
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
