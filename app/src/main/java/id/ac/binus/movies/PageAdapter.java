package id.ac.binus.movies;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    int mNoOfTabs;

    public PageAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
        this.mNoOfTabs = mNoOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                SearchTab searchTab = new SearchTab();
                return searchTab;
            case 1:
                SavedTab savedTab = new SavedTab();
                return savedTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Search Movies";
            case 1:
                return "Saved Movies";
            default:
                return null;
        }
    }
}
