package org.denevell.AndroidProject.nav;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FixedSavedState;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import org.denevell.AndroidProject.ChildFragmentsManager;
import org.denevell.AndroidProject.nav.NavigationDrawerCallbacks;
import org.denevell.AndroidProject.ViewPagerFragment;
import org.denevell.AndroidProject.PreferencesActivity;
import org.denevell.AndroidProject.MapFragment;
import org.denevell.AndroidProject.LicencesActivity;
import org.denevell.AndroidProject.NavItemOneFragment;
import org.denevell.AndroidProject.R;

/**
 * We inflate a view with a DrawerLayout, a drawer fragment and a content FrameLayout holder, and we
 * wait for a method call from the drawer fragment that tells us to open a fragment relating to an
 * id, which we do by replacing the content holder's area with the new fragment.
 * 
 * Before going to a new fragment, we save the state of the old fragment in a HashMap according to 
 * the name of the Fragment, and when new fragments are started we check that HashMap for saved state
 * for that Fragment. This HashMap is saved in the lifecycle methods.
 * 
 * When a back press is issued, we check with the current fragment as to whether it has a backstack > 0
 * itself, and pops that instead if so.
 * 
 * We also implement an interface method that child fragments use to ascertain if they should set
 * their options menu etc, which we say yes to if the fragment is a normal fragment and the drawer is closed,
 * or if the drawer is open and the fragment is the navigation drawer fragment. We only set our options 
 * menu if the drawer is closed, too.
 * 
 * The Fragments we show are normal ones, except they use the methods herein to set the title (since 
 * we'd want to control that if this were a tablet layout) and ask us if they should set their options menu.
 */
public class NavManagerFragmentActivity extends FragmentActivity
	implements NavigationDrawerCallbacks, ChildFragmentsManager {

	private DrawerLayout mDrawerLayout;
	private View mNavDrawerView;
	// So we have a reference to the previously switched to fragment to save its state, or saving current state on onSaveInstanceState
	private Fragment mCurrentFragment; 
	// Save fragment states for when we switch to another one
	private HashMap<String, SavedState> mSavedStates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nav_drawer_main_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawerView = findViewById(R.id.navigation_drawer);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	// Get the state of the currently active fragment and save it.
	SavedState savedState = getSupportFragmentManager().saveFragmentInstanceState(mCurrentFragment);
	mSavedStates.put(mCurrentFragment.getClass().getSimpleName(), savedState);
	// Now save all the saved fragment states
    	outState.putStringArray("savedStateStrings", mSavedStates.keySet().toArray(new String[0]));
    	outState.putParcelableArray("savedStateStates", mSavedStates.values().toArray(new Parcelable[0]));
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	// Restore all the fragment states back into the HashMap
    	Parcelable[] states = savedInstanceState.getParcelableArray("savedStateStates");
    	String[] strings = savedInstanceState.getStringArray("savedStateStrings");
    	for (int i = 0; i < strings.length; i++) {
    		mSavedStates.put(strings[i], (SavedState) states[i]);
	}
    }
    
    /**
     * If the current fragment has a backstack, pop that on backpress.
     */
    @Override
    public void onBackPressed() {
    	if(mCurrentFragment!=null && mCurrentFragment.getChildFragmentManager().getBackStackEntryCount()>0) {
    		mCurrentFragment.getChildFragmentManager().popBackStack();
    	} else {
	    	super.onBackPressed();
    	}
    }

    /**
     * Used to work out if child fragments should set their options menu / action bar stuff
     * @return
     */
     private boolean isDrawerOpen() {
        return mDrawerLayout != null && mNavDrawerView != null && mDrawerLayout.isDrawerOpen(mNavDrawerView);
     }

    /**
     * Set the options that are common among all the fragments.
     * If the drawer is open, don't bother setting it, since the drawer fragment should then do so.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.nav_main_activity_options, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Define what happens when one of the options common to all fragments is pressed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.action_activity) {
    		Toast.makeText(this, "Act action.", Toast.LENGTH_SHORT).show();
    		return true;
    	} else if(item.getItemId() == R.id.preferences_option) {
    		startActivity(new Intent(this, PreferencesActivity.class));
    	} else if(item.getItemId() == R.id.licences_option) {
    		startActivity(new Intent(this, LicencesActivity.class));
    	}
        return super.onOptionsItemSelected(item);
    }

    /**
     * Only allow the child fragments to set their options menu / action bar stuff 
     * if the drawer menu is closed, unless the child is the drawer fragment itself.
     */
    @Override
    public boolean shouldChildSetOptionsMenuAndActionBar(int fragmentType, String fragmentName) {
    	if(fragmentType==ChildFragmentsManager.NORMAL_FRAGMENT && !isDrawerOpen()) {
    		return true;
    	} else if(fragmentType==ChildFragmentsManager.NAV_MENU_FRAGMENT && isDrawerOpen()) {
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * Called by the drawer fragment if it thinks we haven't learnt it yet.
     */
    @Override
    public void onUserHasntLearntAboutDrawer() {
        if(mNavDrawerView!=null && mDrawerLayout != null) mDrawerLayout.openDrawer(mNavDrawerView);
    }
	
    /**
     * If we're a tablet design, this would likely not allow the fragment to set the title.
     */
    @Override
    public void setTitleFromChild(String title) {
        getActionBar().setTitle(title);
    }

    @Override
    public void onNavigationDrawerItemSelected(int resourceId) {
    	Fragment fragment = null;
	switch (resourceId) {
		case R.id.section_one_fragment:
    		fragment = new NavItemOneFragment();
    	   	setFragmentsSavedState(fragment);
		break;
		case R.id.section_two_fragment:
    	   	fragment = new ViewPagerFragment();
    	   	setFragmentsSavedState(fragment);
		break;
		case R.id.section_three_fragment:
    	   	fragment = new MapFragment();
    	   	setFragmentsSavedState(fragment);
		break;
		default:
    		fragment = new ViewPagerFragment.RedFragment();
			Log.e(getClass().getSimpleName(), "Couldn't match fragment id to fragment object.");
			break;
	}
	// Save the state of the old fragment from which we've just switched 
	if(mCurrentFragment!=null) {
		SavedState savedState = getSupportFragmentManager().saveFragmentInstanceState(mCurrentFragment);
		mSavedStates.put(mCurrentFragment.getClass().getSimpleName(), savedState);
	}
	getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                .commit();
	// Save a reference to the newly switched to fragment so we can save it's state on next switch
	mCurrentFragment = fragment;
	// Close the drawer if it's open so we can see the fragment
        if(mNavDrawerView!=null && mDrawerLayout != null) mDrawerLayout.closeDrawer(mNavDrawerView);
    }

    /**
     * Sets the fragments saved state by looking up its name in mSavedState.
     */
    private void setFragmentsSavedState(Fragment fragment) {
        SavedState savedState = mSavedStates.get(fragment.getClass().getSimpleName());
        if(savedState!=null) {
            FixedSavedState pp = new FixedSavedState(savedState);
            fragment.setInitialSavedState(pp);
        }
    }

}
