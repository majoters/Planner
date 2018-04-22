package devs.mulham.raee.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FriendListShare extends Dialog implements android.view.View.OnClickListener {
    ListView friend_list;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<FriendListType> SearchList;
    ArrayList<FriendListType> CheckList = new ArrayList<>();
    static boolean searchpress=false;
    public static boolean checkpress=false;
    LinearLayoutManager layoutManager;

    public FriendListShare(@NonNull Context context) {
        super(context);
    }

    public FriendListShare(Context context,ArrayList<FriendListType> friendListShares){
        super(context);
        CheckList=friendListShares;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_share);

        final ConstraintLayout nf = (ConstraintLayout)findViewById(R.id.nf);
        final Button cancel = (Button) findViewById(R.id.cancel);
        Button select = (Button) findViewById(R.id.select);
        final ImageView pic = (ImageView) findViewById(R.id.status);
        final TextView nosearching = (TextView) findViewById(R.id.nofri);
        final EditText textsearch =(EditText)findViewById(R.id.textSearch);

        SearchList = new ArrayList<>();
        /*for(int i = 1;i<=3;i++){
            CheckList.add(new FriendListType("Username",false));
        }*/
        FriendShareAdapter fsAdapter = new FriendShareAdapter(getApplicationContext(),SearchList);
        friend_list = (ListView) findViewById(R.id.friend_list);
        friend_list.setAdapter(fsAdapter);


        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.friend_dis);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainAdapter(CheckList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        /*if(SearchList.size()==0){
            mRecyclerView.setVisibility(View.INVISIBLE);
         }
         else{
            mRecyclerView.setVisibility(View.VISIBLE);
        }*/

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(!searchpress){
                    if(textsearch.getText().toString().length()==0){
                        nf.animate()
                                .alpha(1.0f)
                                .setDuration(1000)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        nf.setVisibility(View.VISIBLE);
                                        nosearching.setText("No Searching");
                                    }
                                });
                    }else{
                        nf.animate()
                                .alpha(1.0f)
                                .setDuration(1000)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        nf.setVisibility(View.INVISIBLE);
                                    }
                                });
                    }
                }

                for(int i=0;i<SearchList.size();i++){
                    if(SearchList.get(i).isCheck()==true){
                        if(!IsinChecklist(SearchList.get(i).getUsername())){
                            CheckList.add(SearchList.get(i));
                        }
                    }else {
                        if(IsinChecklist(SearchList.get(i).getUsername())){
                            CheckList.remove(SearchList.get(i));
                        }
                    }
                }


                FriendShareAdapter fsAdapter = new FriendShareAdapter(getApplicationContext(),SearchList);
                friend_list.setAdapter(fsAdapter);

                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView = (RecyclerView) findViewById(R.id.friend_dis);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new MainAdapter(CheckList);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(layoutManager);

                if(isShowing())
                    handler.postDelayed(this,1000);

            }
        };
        handler.post(runnable);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*nf.animate()
                        .alpha(0.0f)
                        .setDuration(1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                nf.setVisibility(View.GONE);
                            }
                        });*/
                cancel();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

            for(int i=0;i<CheckList.size();i++){
                if(CheckList.get(i).isCheck()){
                    MainActivity4.cdd.mDataSet.add(CheckList.get(i));
                }
            }
            cancel();

            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchpress=true;
                if(SearchList.size()<=0){
                    nf.animate()
                            .alpha(1.0f)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    nf.setVisibility(View.VISIBLE);
                                    pic.setImageResource(R.drawable.no_status_friend);
                                    nosearching.setText("No Friend");
                                }
                            });

                }
            }
        });

        textsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchpress=false;
                SearchList.clear();
                for(int i=0;i<SocialPlan.memberName.size();i++){
                    if(SocialPlan.memberName.get(i).indexOf(textsearch.getText().toString())!=-1){
                        if(!IsInSearchList(SocialPlan.memberName.get(i))){
                            if(IsinChecklist(SocialPlan.memberName.get(i))){
                                SearchList.add(new FriendListType(SocialPlan.memberName.get(i),true));
                            }else{
                                SearchList.add(new FriendListType(SocialPlan.memberName.get(i),false));
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(SearchList.get(position).isCheck()){
                    SearchList.get(position).setCheck(true);
                    CheckList.add(SearchList.get(position));
                }else {
                    SearchList.get(position).setCheck(false);
                }
                Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();

                FriendShareAdapter fsAdapter = new FriendShareAdapter(getApplicationContext(),SearchList);
                friend_list.setAdapter(fsAdapter);
            }
        });*/

    }



    private boolean IsInSearchList(String s) {
        for(int i=0;i<SearchList.size();i++){
            if(SearchList.get(i).getUsername().compareTo(s)==0){
                return true;
            }
        }
        return false;
    }

    public boolean IsinChecklist(String name){
        for(int i=0;i<CheckList.size();i++){
            if(name.compareTo(CheckList.get(i).getUsername())==0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
