package devs.mulham.raee.sample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.project.kmitl57.beelife.DataAnalysis;
import com.project.kmitl57.beelife.KmeanType;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

public class ShowDatabase extends AppCompatActivity {
    public ArrayList<String> arrayList;
    Date d = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_database);
        final ListView listView =(ListView)findViewById(R.id.ShowDatabase);
        arrayList = new ArrayList<>();

        findViewById(R.id.Database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                ArrayList<List_Database> list_databaseArrayList = new ArrayList<>();
                list_databaseArrayList=MainActivity4.mDbAdabter_Model.fecthAllList();
                try {
                    for(int i=0;i<list_databaseArrayList.size();i++){
                        arrayList.add("ID : "+MainActivity4.mDbAdabter_Model.ListToID(list_databaseArrayList.get(i))+"\n"+
                                "Date : "+MainActivity.ConvertDateToString(list_databaseArrayList.get(i).getDate())+"\n"+
                                "Time : "+MainActivity.ConvertTimeToString(list_databaseArrayList.get(i).getTime())+"\n"+
                                "Description : "+list_databaseArrayList.get(i).getDescription()+"\n"+
                                "LocationName : "+list_databaseArrayList.get(i).getLocationName()+"\n"+
                                "Latitude : "+String.valueOf(list_databaseArrayList.get(i).getLatitude())+"\n"+
                                "Longitude : "+String.valueOf(list_databaseArrayList.get(i).getLongitude())+"\n"+
                                "Status : "+String.valueOf(list_databaseArrayList.get(i).getStatus()));
                    }
                }catch (NullPointerException e){

                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);
                listView.invalidate();
            }
        });

        findViewById(R.id.Analysis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                ArrayList<DataAnalysis> dataAnalysisArrayList = new ArrayList<>();
                dataAnalysisArrayList=MainActivity4.mDbDataForAnalysis_Model.GetAll();
                for(int i=0;i<dataAnalysisArrayList.size();i++){
                    arrayList.add("Time : "+MainActivity.ConvertTimeToString(dataAnalysisArrayList.get(i).getTime())+"\n"+
                    "TimeAct : "+MainActivity.ConvertTimeToString(dataAnalysisArrayList.get(i).getTimeAct())+"\n"+
                    "Description : "+dataAnalysisArrayList.get(i).getDescription()+"\n"+
                    "LocationName : "+dataAnalysisArrayList.get(i).getLocationName()+"\n"+
                    "Latitude : "+String.valueOf(dataAnalysisArrayList.get(i).getLatitude())+"\n"+
                    "Longitude : "+String.valueOf(dataAnalysisArrayList.get(i).getLongitude())+"\n"+
                    "Frequency : "+String.valueOf(dataAnalysisArrayList.get(i).getFrequency())+"\n"+
                    "Group : "+String.valueOf(dataAnalysisArrayList.get(i).getGroup()));
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);
                listView.invalidate();
            }
        });

        findViewById(R.id.Kmean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                ArrayList<KmeanType> DataKmean = new ArrayList<>();
                DataKmean=MainActivity4.mDbKmean_Model.GetAll();
                for(int i=0;i<DataKmean.size();i++){
                    arrayList.add("ID : "+String.valueOf(i+1)+"\n"+
                    "TimeAgent : "+MainActivity.ConvertTimeToDouble(DataKmean.get(i).getTimeAgent())+"\n"+
                            "Latitude : "+String.valueOf(DataKmean.get(i).getLatitude())+"\n"+
                            "Longitude : "+String.valueOf(DataKmean.get(i).getLongitude())+"\n"+
                    "Frequency : "+String.valueOf(DataKmean.get(i).getFrequency()));
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);
                listView.invalidate();
            }

        });

        findViewById(R.id.Share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                ArrayList<ShareType> ShareDatabase = new ArrayList<>();
                ShareDatabase=MainActivity4.SHR_Model.getAllData();
                for(int i=0;i<ShareDatabase.size();i++){
                    arrayList.add("ID : "+String.valueOf(i+1)+"\n"+
                            "Foriegn Key : "+String.valueOf(ShareDatabase.get(i).getForiegnKey())+"\n"+
                            "Ref : "+ShareDatabase.get(i).getRef()+"\n"+
                            "Firebase Key : "+ShareDatabase.get(i).getFirebaseKey()+"\n"+
                            "Status : "+ShareDatabase.get(i).getStatus());
                }
                Toast.makeText(ShowDatabase.this,String.valueOf(ShareDatabase.size()),Toast.LENGTH_SHORT).show();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);
                listView.invalidate();
            }
        });

        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                ArrayList<NearPlaceType> nearPlaceTypes = new ArrayList<>();
                nearPlaceTypes=MainActivity4.all_history_map;
                for(int i=0;i<nearPlaceTypes.size();i++){
                    arrayList.add("ID : "+String.valueOf(nearPlaceTypes.get(i).getId_data())+"\n"+
                            "Name : "+String.valueOf(nearPlaceTypes.get(i).getName())+"\n"+
                            "Lat : "+nearPlaceTypes.get(i).getLatitude()+"\n"+
                            "Long : "+nearPlaceTypes.get(i).getLongitude());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);
                listView.invalidate();
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ShowDatabase.this,
                        d.toString(),Toast.LENGTH_SHORT).show();

                d=new Date(d.getTime()+(1000*60*60*24));
                handler.postDelayed(this,3000);
            }
        };

        handler.post(runnable);

    }


}
