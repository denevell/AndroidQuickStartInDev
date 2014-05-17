package org.denevell.AndroidProject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import org.denevell.AndroidProject.utils.FixForBlackArtifactsMapFragment;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_map, container, false);
    	String mapTag = "mapTag";
    	FragmentManager supportFragManager = getChildFragmentManager();
    	SupportMapFragment possiblyExtantMap = (SupportMapFragment) supportFragManager.findFragmentByTag(mapTag);

    	if(possiblyExtantMap==null) {
    		possiblyExtantMap = new FixForBlackArtifactsMapFragment();
	    	FragmentTransaction fragmentTransaction = supportFragManager.beginTransaction();
        	fragmentTransaction.replace(R.id.maps_map_fragment_holder, possiblyExtantMap, mapTag);
        	fragmentTransaction.commit();
        	supportFragManager.executePendingTransactions();
    	}
    	return v;
    }
}
