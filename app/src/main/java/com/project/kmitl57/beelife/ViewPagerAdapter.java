package com.project.kmitl57.beelife;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by hotmildc on 20/11/2560.
 */

public class ViewPagerAdapter extends PagerAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};
    String Email;
    FirebaseDatabase firebaseDatabase;

    public ViewPagerAdapter(Context context,String Email) {
        this.context = context;
        this.Email=Email;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.new_custom_layout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        final int i=position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==1) {
                    int space = Email.toString().indexOf(".");
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/" +
                            Email.toString().substring(0, space) + "/");
                    databaseReference.child("Type").setValue(i);
                    Intent intent = new Intent(v.getContext(), MainActivity4.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().getApplicationContext().startActivity(intent);
                }else{
                    Toast.makeText(context,"It's not ready for use",Toast.LENGTH_SHORT).show();
                }

            }
        });

        imageView.setImageResource(images[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return  view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
