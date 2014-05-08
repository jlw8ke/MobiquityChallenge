package com.jlwapps.mobiquitychallenge.app.NavigationDrawer;

/**
 * Created by jlw8k_000 on 5/7/2014.
 */
public class NavDrawerItem {
    private String title;
    private int icon;

    //region Constructors
    public NavDrawerItem(String title, int icon)
    {
        this.title = title;
        this.icon = icon;
    }
    //endregion

    //region Getters and Setters
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }
    //endregion

}
