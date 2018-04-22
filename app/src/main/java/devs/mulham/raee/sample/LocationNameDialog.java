package devs.mulham.raee.sample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.project.kmitl57.beelife.R;

import java.util.List;
import java.util.Locale;

import static devs.mulham.raee.sample.MainActivity4.isInMap;

public class LocationNameDialog extends Dialog {
    public static String Name="";
    Button ok;
    ImageButton clear;
    public static double latitude;
    public static double longitude;
    public EditText place_name;
    Context mCx;

    public LocationNameDialog(@NonNull final Context context) {
        super(context);
        mCx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_name_dialog);
        ok = (Button) findViewById(R.id.ok);
        place_name=(EditText)findViewById(R.id.editText);
        clear=(ImageButton)findViewById(R.id.button4);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        place_name.setText(Name);
        //----------------------------------------------------

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                        ((Activity)mCx).finish();
                        cancel();
                    }
                }, 300);

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_name.setText("");
            }
        });
    }
}
