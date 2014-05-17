package org.denevell.AndroidProject.nav;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.denevell.AndroidProject.ChildFragmentsManager;
import org.denevell.AndroidProject.nav.NavigationDrawerCallbacks;
import org.denevell.AndroidProject.R;

/**
 * We inflate a ListView for the navigation drawer, set its adapter, and on list item click
 * we call a method which translates that position into a fragment id which we send to the 
 * host fragment or activity. It's current selected position is saved via the lifecycle methods
 * in order to restore it on rotation / configuration change.
 * 
 * Once the activity is attached, we get the overall DrawerLayout, set its shadow, calls a method on
 * the host if the user hasn't learnt about the drawer (which would normally open the drawer to show the
 * user), and sets a listener on the DrawerLayout which is a DrawerToggle, which sets an icon, invalidates the
 * host's option menus on open / close and ascertains if the user has learnt the drawer (i.e. opened it). 
 * 
 * We save the open / closed drawer state in the lifecycle methods, so if they say the drawer was already
 * opened then we open it in the onResume method call.
 * 
 * The option menu create method asks the host if it should set the options / title / actionbar, and the hosts 
 * says yes if the drawer is open. If the host says yes it also saves the previously title on the host, and when
 * the host says "no, don't set the options menu etc" it restores that title (and nulls the title so it
 * can't set it again).
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private static final String STATE_PREVIOUS_TITLE = "previous_title";
	private static final String STATE_DRAWER_OPEN = "drawer_open";
    private NavigationDrawerCallbacks mDrawerHolderCallbacks;
    private ChildFragmentsManager mChildFragmentManagerCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private int mCurrentSelectedPosition = 0;
    /**
     * If it is from saved instance, then we've just rotated or similar, so in this case
     * don't bother with doing something if the user hasn't learnt the navigation drawer, 
     * since we only do that on app start, else we may be opening the navigation drawer
     * to show the user it exists on each rotation -- which would be annoying to the user.
     */
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    /**
     * Used to reset the previous fragment title when the nav drawer closes
     */
	private CharSequence mPreviousFragmentTitleToRestore;
	protected boolean mIsDrawerOpen;

    public NavigationDrawerFragment() {}

    /**
     * When we start, we restore 
     * - look if the user has learnt the drawer interaction, to do something if they have not.
     * - the last selected nav item position, to reselect it.
     * - the previous activity / fragment title, to reset it when the navigation drawer closes.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mIsDrawerOpen = savedInstanceState.getBoolean(STATE_DRAWER_OPEN);
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mPreviousFragmentTitleToRestore = savedInstanceState.getCharSequence(STATE_PREVIOUS_TITLE);
            mFromSavedInstanceState = true;
        }

    }
    
    @Override
    public void onResume() {
    	super.onResume();
        openNavivgationItem(mCurrentSelectedPosition);
        if(mIsDrawerOpen) {
        	View drawerView = getActivity().findViewById(R.id.navigation_drawer);
        	View drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        	if(drawerView!=null && drawerLayout!=null && drawerLayout instanceof DrawerLayout) {
        		((DrawerLayout)drawerLayout).openDrawer(drawerView);
        	}
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_DRAWER_OPEN, mIsDrawerOpen);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putCharSequence(STATE_PREVIOUS_TITLE, mPreviousFragmentTitleToRestore);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig); // As specified by the DrawerToggle
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDrawerHolderCallbacks = (NavigationDrawerCallbacks) activity;
            mChildFragmentManagerCallbacks = (ChildFragmentsManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks and ChildFragmentManager.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDrawerHolderCallbacks = null;
        mChildFragmentManagerCallbacks = null;
    }    

    /**
     * We inflate the layout for the drawer, a list view, set its adapter, 
     * when an item is pressed call selectItem(), and set its checked item if we've saved it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            		// Uncomment to prevent navigation drawer selecting the same fragment twice
            		//if(position == mCurrentSelectedPosition) {
            		//	return;
            		//}
            		mCurrentSelectedPosition = position;
            		if (mDrawerListView != null) {
            			mDrawerListView.setItemChecked(position, true);
            		}
            		openNavivgationItem(position);
            	}
        });
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActivity().getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.nav_item1),
                        getString(R.string.nav_item2),
                        getString(R.string.nav_item3),
                }));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }
    
    /**
     * When the parent activity is created, get grab the overall DrawerLayout, 
     * - set its shadow
     * - set up the DrawerToggle on it
     * - set the DrawerLayout's listener to the DrawerToggle
     * - and sync the DrawerToggle, and open it if the saved state tells us to.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        setupActionBarDrawerToggle(drawerLayout);

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
        	mDrawerHolderCallbacks.onUserHasntLearntAboutDrawer();
        }

        drawerLayout.setDrawerListener(mDrawerToggle);

        // Defer code dependent on restoration of previous instance state.
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
    
    /**
     * - Setup the ActionBar's home as up
     * - Setup the ActionBar's home button as enabled
     * Then create the DrawerToggle, by 
     * - Setting its icon
     * - Telling it to invalidate the activity's options menu when the nav closes / opens, so to restore those 
     * And once we know the drawer is openeed, set the 'is learnt' preference to true.
     */
    private void setupActionBarDrawerToggle(DrawerLayout drawerLayout) {
		ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    
                drawerLayout,                    
                R.drawable.ic_drawer,             
                R.string.navigation_drawer_open,  
                R.string.navigation_drawer_close  
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                mIsDrawerOpen = false;
                getActivity().invalidateOptionsMenu(); // Restore context menus for fragments  + activity
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                mIsDrawerOpen = true;
                getActivity().invalidateOptionsMenu(); // Refresh, i.e. disable, context menus for fragments  + activity 
            }
        };
	}

    /**
     * When the drawer is open, set the title from the string resources, and save the old 
     * title in order to restore it later.
     * Else, if we have a previous activity title to restore, i.e. we've close the nav drawer,
     * and want to restore the previously visible fragment's title, restore it.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ActionBar actionBar = getActivity().getActionBar();
        // The if statement is used since this method will be also called when the fragment
        // lives and but the drawer is closed.
        if (mChildFragmentManagerCallbacks.shouldChildSetOptionsMenuAndActionBar(ChildFragmentsManager.NAV_MENU_FRAGMENT, null)) {
            inflater.inflate(R.menu.nav_drawer_fragment_options, menu);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            mPreviousFragmentTitleToRestore = getActivity().getActionBar().getTitle(); 
            actionBar.setTitle(R.string.app_name);
        } else {
            if(mPreviousFragmentTitleToRestore!=null && !mPreviousFragmentTitleToRestore.equals(getString(R.string.app_name))) {
            	actionBar.setTitle(mPreviousFragmentTitleToRestore);
            	mPreviousFragmentTitleToRestore = null;
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) { // As specified by the DrawerToggle's implementation
            return true;
        }
    	if (item.getItemId() == R.id.action_global) {
    		Toast.makeText(getActivity(), "Global action.", Toast.LENGTH_SHORT).show();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
    }


    /**
     * From the position as passed from the ListView, select a fragment name, as represented
     * by a id resource, to then pass to the parent fragment / activity in order to open 
     * a navigation item.
     */
    private void openNavivgationItem(int position) {
        if (mDrawerHolderCallbacks != null) {
        	int fragmentResourceId = -1;
        	switch (position) {
			case 0:
				fragmentResourceId = R.id.section_one_fragment;
				break;
			case 1:
				fragmentResourceId = R.id.section_two_fragment;
				break;
			case 2:
				fragmentResourceId = R.id.section_three_fragment;
				break;
			default:
				fragmentResourceId = R.id.section_one_fragment;
				Log.e(getClass().getSimpleName(), "Couldn't match listview to fragment id.");
				break;
			}
            mDrawerHolderCallbacks.onNavigationDrawerItemSelected(fragmentResourceId);
        }
    }

}
