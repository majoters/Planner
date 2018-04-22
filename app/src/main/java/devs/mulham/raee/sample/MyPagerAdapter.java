package devs.mulham.raee.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.kmitl57.beelife.R;


public class MyPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public MyPagerAdapter(Context context, final FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(final int position) {
        if (position==0)
            return new FragmentFr();
        else if (position==1)
            return new FragmentRq();
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
                return mContext.getString(R.string.category_fr);
            case 1:
                return mContext.getString(R.string.category_rq);
            default:
                return null;
        }
    }
}
