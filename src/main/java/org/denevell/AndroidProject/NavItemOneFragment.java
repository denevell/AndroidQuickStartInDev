package org.denevell.AndroidProject;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.denevell.AndroidProject.services.RecentPostsService;
import org.denevell.AndroidProject.services.XmlTestService;

public class NavItemOneFragment extends Fragment {

    private ChildFragmentsManager mChildFragmentsManager;

	public NavItemOneFragment() {}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.drawer_item_one_fragment, container, false);
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mChildFragmentsManager = (ChildFragmentsManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ChildFragmentsManager.");
        }
        Application.getEventBus().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Application.getEventBus().unregister(this);
    }

    @Override
    public void onResume() {
    	super.onResume();
        mChildFragmentsManager.setTitleFromChild(getString(R.string.drawer_item_one_title));
        displayPreferences();
        new RecentPostsService().fetch(0, 10);
        new XmlTestService().fetch();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
		if (mChildFragmentsManager.shouldChildSetOptionsMenuAndActionBar(ChildFragmentsManager.NORMAL_FRAGMENT, null)) {
    		inflater.inflate(R.menu.drawer_item_one_menu, menu);
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.drawer_item_one_menu_action) {
    		Toast.makeText(getActivity(), "Fragment one.", Toast.LENGTH_SHORT).show();
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

    private void displayPreferences() {
        String preferenceString = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(getString(R.string.settings_edittext_key), "EditTextPreference preference not set yet.");
        TextView preferencesTextView = (TextView) getView().findViewById(R.id.main_activity_preferences_string_textview);
        preferencesTextView.setText(preferenceString);
    }

    @Subscribe
    public void onRecentPosts(RecentPostsService.RecentPosts posts) {
       int i = 0;
    }

    @Subscribe
    public void onRecentPostsError(RecentPostsService.RecentPostsError error) {
        int i = 0;
    }

    @Subscribe
    public void onXmlTest(XmlTestService.Hi posts) {
        int i = 0;
    }

    @Subscribe
    public void onXmlTestError(XmlTestService.HiError error) {
        int i = 0;
    }

}
