package com.code_dream.almanach.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 22-Dec-16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> fragments = new ArrayList<>();
	ArrayList<String> tabTitles = new ArrayList<>();

	public void AddFragments(Fragment fragments)
	{
		this.fragments.add(fragments);
		this.tabTitles.add("");
	}

	public ViewPagerAdapter(FragmentManager fragmentManager)
	{
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return  tabTitles.get(position);
	}
}
