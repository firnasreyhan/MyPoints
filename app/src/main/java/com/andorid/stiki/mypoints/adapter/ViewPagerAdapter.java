package com.andorid.stiki.mypoints.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.andorid.stiki.mypoints.fragment.GradeFragment;
import com.andorid.stiki.mypoints.fragment.PointFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTab;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int numberOfTab) {
        super(fm);
        this.numberOfTab = numberOfTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GradeFragment gradeFragment = new GradeFragment();
                return gradeFragment;
            case 1:
                PointFragment pointFragment = new PointFragment();
                return pointFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTab;
    }
}
