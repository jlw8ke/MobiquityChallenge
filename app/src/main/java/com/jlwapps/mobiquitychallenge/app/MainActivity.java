package com.jlwapps.mobiquitychallenge.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.preference.PreferenceManager;


import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerAdapter;
import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerClickInterface;
import com.jlwapps.mobiquitychallenge.app.NavigationDrawer.NavDrawerItem;
import com.jlwapps.mobiquitychallenge.app.fragments.BottomActionBarFragment;
import com.jlwapps.mobiquitychallenge.app.fragments.FavoritesFragment;
import com.jlwapps.mobiquitychallenge.app.fragments.MyPicsFragment;
import com.jlwapps.mobiquitychallenge.app.fragments.MyPicsLoginFragment;


import java.util.ArrayList;


public class MainActivity extends Activity implements NavDrawerClickInterface,
        DropBoxInterface {

    // region Dropbox Specific Attributes
    private static final String APP_KEY = "9xwd05d2k716h5b";
    private static final String APP_SECRET = "mo296utiboc7mrk";
    private static final AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

    private DropboxAPI<AndroidAuthSession> mDBApi;
    //endregion

    // region SharedPreferences Keys
    private static final String PREF_SECRET_KEY = "SECRET_KEY";
    private static final String NAV_DRAWER_POSITION = "NAV_DRAWER_POSITION";
    private static SharedPreferences mSharedPreferences;

    // endregion

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerAdapter mDrawerAdapter;
    private int mNavDrawerPosition;

    private String mCurrentTitle;
    private String mDrawerTitle;

    private String[] navDrawerTitles;
    private TypedArray navDrawerIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(savedInstanceState == null)
        {
            //Lower ActionBar
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            BottomActionBarFragment lowerActionBar = new BottomActionBarFragment();
            ft.add(R.id.lower_action_bar_frame, lowerActionBar);
            ft.commit();
        }

        //Creating AuthSession for Dropbox API
        AndroidAuthSession session = buildDropBoxSession();
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

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

        if(savedInstanceState == null) {
            mNavDrawerPosition = 0;
        }
        else {
            mNavDrawerPosition = savedInstanceState.getInt(NAV_DRAWER_POSITION);
        }
        mDrawerList.performItemClick(mDrawerAdapter.getView(mNavDrawerPosition, null, null),
                mNavDrawerPosition,
                mDrawerList.getItemIdAtPosition(mNavDrawerPosition));
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
        displayFragment(mDrawerList.getSelectedItemPosition());
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
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mDBApi.getSession();

        if(session.authenticationSuccessful())
        {
            try {
                session.finishAuthentication();
                displayFragment(mNavDrawerPosition);
            } catch (IllegalStateException e)
            {
                Log.i("DBAuthLog", "Error authenticating");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeDropBoxSession(mDBApi.getSession());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(NAV_DRAWER_POSITION, mNavDrawerPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void displayFragment(int index) {
        Fragment theFragment;
        switch (index) {
            case 0:
                if(!mDBApi.getSession().isLinked())
                    theFragment = new MyPicsLoginFragment();
                else
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
            mNavDrawerPosition = index;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // region Dropbox Methods
    private AndroidAuthSession buildDropBoxSession()
    {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadDropBoxSession(session);
        return session;
    }

    private void loadDropBoxSession(AndroidAuthSession session)
    {
        String secret = mSharedPreferences.getString(PREF_SECRET_KEY, null);
        if(secret != null)
            session.setOAuth2AccessToken(secret);
    }

    private void storeDropBoxSession(AndroidAuthSession session)
    {
        String oauth_token = session.getOAuth2AccessToken();
        if(oauth_token != null)
        {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(PREF_SECRET_KEY, oauth_token);
            editor.commit();
        }
    }

    @Override
    public DropboxAPI<AndroidAuthSession> getDropboxAPI() {
        return mDBApi;
    }

    // endregion
}
