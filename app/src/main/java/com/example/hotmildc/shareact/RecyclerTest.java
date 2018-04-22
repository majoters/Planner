package devs.mulham.raee.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

public class RecyclerTest extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<FriendListType> mDataSet;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_test);

        mDataSet = new ArrayList<>();
        /*for(int i = 1;i<=15;i++)
            mDataSet.add(String.valueOf(i));
        mDataSet.add("Orn BNK48");
        mDataSet.add("Pun BMK48");
        mDataSet.add("A");
        mDataSet.add("AB");
        mDataSet.add("AC");*/
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
