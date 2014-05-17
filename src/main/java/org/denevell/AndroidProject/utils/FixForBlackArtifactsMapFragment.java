package org.denevell.AndroidProject.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.maps.SupportMapFragment;

public class FixForBlackArtifactsMapFragment extends SupportMapFragment {
	
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, 
                             ViewGroup container, 
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Fix for black background on devices < 4.1
        if (android.os.Build.VERSION.SDK_INT < 
            android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setMapTransparent((ViewGroup) view);
        }
        
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setBackgroundColor(
            getResources().getColor(android.R.color.transparent));
        ((ViewGroup) view).addView(frameLayout, 0,
            new ViewGroup.LayoutParams(
                LayoutParams.FILL_PARENT, 
                LayoutParams.FILL_PARENT
            )
        );
        
        return view;
    }

    private void setMapTransparent(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                setMapTransparent((ViewGroup) child);
            } else if (child instanceof SurfaceView) {
                child.setBackgroundColor(0x00000000);
            }
        }
    }

}
