
package org.denevell.AndroidProject;

    /**
     * Implemented by fragments that are children of an FragmentActivity / parent Fragment
     *
     */
    public interface ChildFragmentsManager {
    public static int NORMAL_FRAGMENT = 0;
    public static int NAV_MENU_FRAGMENT = 1;
    /**
     * Used by the child fragment when it's about to show the options menu
     * @param fragmentType a static int on ChildFragmentsManager
     * @param fragmentName not currently used
     */
    boolean shouldChildSetOptionsMenuAndActionBar(int fragmentType, String fragmentName);
    /**
     * Used by the child instead of setTitle(), since the parent Fragment / FragmentActivity may want to control this itself.
     * @param title
     */
    void setTitleFromChild(String title);
}
