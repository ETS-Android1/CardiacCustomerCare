package com.kfs.cardiaccustomercare.Fragements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kfs.cardiaccustomercare.Home;

public class SimplePageAdapter extends FragmentPagerAdapter {

    private String [] tabsTitles={"Home","Profile","About"};
    public static final int COUNT=3;
    public SimplePageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return Record.newInstance();
            case 1:return Profile.newInstance();
            default:return About.newInstance();
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles[position];
    }
}
