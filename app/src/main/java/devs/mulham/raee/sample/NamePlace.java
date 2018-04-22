package devs.mulham.raee.sample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.project.kmitl57.beelife.R;

import java.util.List;
import java.util.Locale;

import static devs.mulham.raee.sample.MainActivity4.isInMap;

public class NamePlace extends Dialog implements android.view.View.OnClickListener {

    public static String Name="";
    public EditText place_name;
    public Button place_ok;
    public static double latitude;
    public static double longitude;
    public Context mCx;

    public NamePlace(@NonNull Context context) {
        super(context);
        mCx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_place);
        place_name=(EditText)findViewById(R.id.placeName);
        Button clear = (Button)findViewById(R.id.clear_place_name);
        place_ok=(Button)findViewById(R.id.place_ok);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        place_name.setText(Name);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_name.setText("");
            }
        });
        place_name.setSelection(0);
        place_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearPlaceType nearPlaceType = new NearPlaceType(place_name.getText().toString(),
                        latitude,longitude);
                MainActivity4.location = new Place() {
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
                        return null;
                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public CharSequence getName() {
                        return place_name.getText().toString();
                    }

                    @Override
                    public LatLng getLatLng() {
                        return new LatLng(latitude,longitude);
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

                    @Override
                    public Place freeze() {
                        return null;
                    }

                    @Override
                    public boolean isDataValid() {
                        return false;
                    }
                };
                if(!isInMap(nearPlaceType)){
                    MainActivity4.mDbMapDatabase_clone.createMapDatabase(nearPlaceType);
                    MainActivity4.RefreshMapHistory();
                    Toast.makeText(mCx,"Create Map List",Toast.LENGTH_SHORT).show();
                    MainActivity4.cdd.locate.setText(place_name.getText().toString());
                }else {
                    MainActivity4.mDbMapDatabase_clone.UpdateList(nearPlaceType);
                    MainActivity4.RefreshMapHistory();
                    Toast.makeText(mCx,"Update Map List",Toast.LENGTH_SHORT).show();
                    MainActivity4.cdd.locate.setText(place_name.getText().toString());
                }
                cancel();
                ((Activity)mCx).finish();
            }
        });

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

    }
}
