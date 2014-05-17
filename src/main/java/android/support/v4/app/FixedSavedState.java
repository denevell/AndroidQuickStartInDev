package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcel;

import org.denevell.AndroidProject.nav.NavManagerFragmentActivity;

/**
 * Fixed missing class loader problem
 */
public class FixedSavedState extends Fragment.SavedState {
    FixedSavedState(Bundle b) {
        super(b);
    }

    FixedSavedState(Parcel in, ClassLoader loader) {
        super(in, loader);
    }

    public FixedSavedState(Fragment.SavedState ss) {
        super(ss.mState);
        ss.mState.setClassLoader(NavManagerFragmentActivity.class.getClassLoader());
    }

}
