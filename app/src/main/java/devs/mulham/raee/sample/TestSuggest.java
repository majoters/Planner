package devs.mulham.raee.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hotmildc.shareact.MainActivity;
import com.project.kmitl57.beelife.DataAnalysis;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

public class TestSuggest extends AppCompatActivity {

    public static boolean AddCheck=false;
    public static boolean suggest=false;
    public static List_Database list_database;
    public ArrayList<String> choice;
    public static Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_suggest);

        /*final ListView suggest =(ListView)findViewById(R.id.Suggest);
        ListView history =(ListView)findViewById(R.id.Recent);
        choice = new ArrayList<>();
        ArrayList<DataAnalysis> historical = new ArrayList<>();
        choice.clear();
        historical.clear();

        if(MainActivity4.mDbDataForAnalysis_Model.GetNumberDatabase()>5){
            choice=MainActivity4.ResultSuggest;
        }
        else{
            String string ="Examination";
            choice.add(string);
            string ="Study Specail";
            choice.add(string);
            string ="Special Day";
            choice.add(string);
            string = "Appointment";
            choice.add(string);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestSuggest.this, android.R.layout.simple_list_item_1, android.R.id.text1, choice);
        suggest.setAdapter(adapter);
        suggest.invalidate();

        for (int n=0,i=MainActivity4.mDbDataForAnalysis_Model.GetNumberDatabase();
             i>0;i--){
            historical.add(MainActivity4.mDbDataForAnalysis_Model.GetByID(i).getDescription());
            if(n>3)
                break;
            n++;
        }


        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TestSuggest.this, android.R.layout.simple_list_item_1, android.R.id.text1, historical);
        history.setAdapter(adapter2);
        history.invalidate();


        findViewById(R.id.Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCheck=true;
                startActivity(new Intent(TestSuggest.this,MainActivity4.class));
                MainActivity4.date=date;
            }
        });

        suggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list_database=new List_Database(0,0,GetChoice(i),"",0,0,MainActivity4.NO_SHARE_ACTIVITY);
                AddCheck=true;
                SetSuggest(true);
                startActivity(new Intent(TestSuggest.this,MainActivity4.class));
                MainActivity4.date=date;
            }
        });*/

    }

    private String GetChoice(int i){
        return choice.get(i);
    }

    private void SetSuggest(boolean i){
        suggest=i;
    }
}
