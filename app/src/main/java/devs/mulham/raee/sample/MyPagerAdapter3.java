package devs.mulham.raee.sample;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.kmitl57.beelife.FragmentPF;
import com.project.kmitl57.beelife.FragmentPFlist;
import com.project.kmitl57.beelife.R;

public class MyPagerAdapter3 extends FragmentPagerAdapter {

    private Context mContext;
    public MyPagerAdapter3(Context context, final FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(final int position) {
        if (position==0)
            return new FragmentPF();
        else if (position==1)
            return new FragmentPFlist();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "List friend";
            default:
                return null;
        }
    }
}