
package org.denevell.AndroidProject.nav;

    /**
     * Implemented by the Activity / Fragment that contains a navigation menu
     */
    public interface NavigationDrawerCallbacks {
    /**
     * Used by the navigation drawer fragment to tell the main Fragment / FragmentActivity what screen to show
     */
    void onNavigationDrawerItemSelected(int resourceId);
    /**
     * Used by the navigation drawer fragment to tell the main Fragment / FragmentActivity that the user
     * has not yet learnt of the drawer, the main Fragment, depending on implementation, then opens it to show the user.
     */
    void onUserHasntLearntAboutDrawer();
}

