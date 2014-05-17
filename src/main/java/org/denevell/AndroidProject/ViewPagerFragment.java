package org.denevell.AndroidProject;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class ViewPagerFragment extends Fragment {

    private static final String TAG = ViewPagerFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viewpager_fragment, container, false);
            
        ViewPager pager = (ViewPager) v.findViewById(R.id.viewpager_activity_viewpager);
        pager.setAdapter(new FragmentPageAdapter(getChildFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) v.findViewById(R.id.viewpager_activity_pageslidingtabstrip);
        tabs.setViewPager(pager);
        return v;
    }
    
    private class FragmentPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
        private ArrayList<String > mTabNames = new ArrayList<String>();
        
        public FragmentPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
            mFragments.add(new RedFragment());
            mTabNames.add(getString(R.string.tab_viewpager_one));
            mFragments.add(new GreenFragment());
            mTabNames.add(getString(R.string.tab_viewpager_two));
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
        	return mTabNames.get(position);
        }

        @Override
	    public Fragment getItem(int arg0) {
	        return mFragments.get(arg0);
    	}

	    @Override
    	public int getCount() {
    	    return mFragments.size();
    	}
    }
    
    public static class RedFragment extends Fragment {
        public RedFragment() {} 
        
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.red, container, false);
    		return v;
    	}
    }

    public static class GreenFragment extends Fragment {
        public GreenFragment() {} 

    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.green, container, false);
    		return v;
    	}
    }
}
