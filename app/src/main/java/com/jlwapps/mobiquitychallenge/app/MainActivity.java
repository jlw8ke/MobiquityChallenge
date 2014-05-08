package com.jlwapps.mobiquitychallenge.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerAdapter;
import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerClickInterface;
import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerItem;

import java.util.ArrayList;


public class MainActivity extends Activity implements NavDrawerClickInterface{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerAdapter mDrawerAdapter;

    private String mCurrentTitle;
    private String mDrawerTitle;

    private String[] navDrawerTitles;
    private TypedArray navDrawerIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentTitle = getTitle().toString();
        mDrawerTitle = mCurrentTitle;

        // Setting up the Navigation Drawer
        navDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //Setup ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(mCurrentTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Populate Navigation Drawer
        navDrawerItems = new ArrayList<NavDrawerItem>();
        for(int i = 0; i < navDrawerTitles.length; i++)
        {
            navDrawerItems.add(new NavDrawerItem(navDrawerTitles[i], navDrawerIcons.getResourceId(i, -1)));
        }
        navDrawerIcons.recycle();
        mDrawerAdapter = new NavDrawerAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayFragment(position);
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //Start with MyPicsFragment
        displayFragment(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mCurrentTitle = title.toString();
        getActionBar().setTitle(title);
    }

    //Handles action bar item clicks and home button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayFragment(int index) {
        Fragment theFragment;
        switch (index) {
            case 0:
                theFragment = new MyPicsFragment();
                break;
            case 1:
                theFragment = new FavoritesFragment();
                break;
            default:
                theFragment = null;
                break;
        }

        if(theFragment != null)
        {
            getFragmentManager().beginTransaction().replace(R.id.main_frame, theFragment).commit();
            setTitle(navDrawerTitles[index]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}
