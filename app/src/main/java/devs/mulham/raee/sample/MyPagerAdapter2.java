package devs.mulham.raee.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.kmitl57.beelife.R;


public class MyPagerAdapter2 extends FragmentPagerAdapter {
    private Context mContext;
    public MyPagerAdapter2(Context context, final FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(final int position) {
        if (position==0)
            return new PFcalendar();
        else if (position==1)
            return new PFoverview();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_calendar);
            case 1:
                return mContext.getString(R.string.category_overview);
            default:
                return null;
        }
    }
}
