package org.denevell.AndroidProject;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class LicencesActivity extends FragmentActivity {

    private static final String TAG = LicencesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_licences);
            View tv = findViewById(R.id.licences_activity_google_play_licence_info_header_textview);
            tv.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
                	String licenceInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
                	AlertDialog.Builder licenceDialog = new AlertDialog.Builder(LicencesActivity.this);
			licenceDialog.setTitle(getString(R.string.google_play_licence_info_header));
			licenceDialog.setMessage(licenceInfo);
			licenceDialog.show();
		}
	    });
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
    }

}
