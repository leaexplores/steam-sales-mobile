/*
Steam Sales Mobile - Android application to keep track of the steam sales.
        Copyright (C) 2013  Mathieu Rhéaume <mathieu@codingrhemes.com>

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.
*/

package com.codingrhemes.steamsalesmobile;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameSaleActivity extends FragmentActivity implements ActionBar.TabListener {

    private final static String URL_API = "http://steam-sales.codingrhemes.com/";
    private final static String API_DAILY_DEAL = "dailydeal";
    private final static String API_MOST_POPULAR = "mostpopular";
    private final static String API_SPECIAL = "special";

    // making that fragment a singleton to reload the pictures from an other class!!
    private static GamesFragment gamesFragment;
    private static GamesFragment mostPopularFragment;
    private static DealOfTheDayFragment dailyDealFragment;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamesaleactivity);


        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        if (savedInstanceState == null) {
        dailyDealFragment = new DealOfTheDayFragment();
        gamesFragment = new GamesFragment();
            // Setting up bundle to keep values
            Bundle mostPopGames = new Bundle();
            mostPopGames.putBoolean("isMostPopular", true);
            mostPopularFragment = new GamesFragment();
            // set them to the fragment
            mostPopularFragment.setArguments(mostPopGames);
        }
        else
        {
            dailyDealFragment = (DealOfTheDayFragment) getSupportFragmentManager().getFragment(savedInstanceState, getString(R.string.title_section1).toUpperCase(Locale.getDefault()));
            gamesFragment = (GamesFragment) getSupportFragmentManager().getFragment(savedInstanceState, getString(R.string.title_section2).toUpperCase(Locale.getDefault()));
            mostPopularFragment = (GamesFragment) getSupportFragmentManager().getFragment(savedInstanceState, getString(R.string.title_section3).toUpperCase(Locale.getDefault()));
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_sale_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_reload:
                switch (getActionBar().getSelectedNavigationIndex())
                {
                    // This is Daily Deal
                    case 0:
                        dailyDealFragment.reloadDataset();
                        break;
                    // This is Sales
                    case 1:
                        gamesFragment.reloadDataset();
                        break;
                    case 2: // Most popular
                        mostPopularFragment.reloadDataset();
                        break;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);


        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            switch (position) {
                case 0: // Deal of the day
                    return dailyDealFragment;
                case 1: // Sales
                    return gamesFragment;
                case 2: // Most popular
                    return mostPopularFragment;
                default:
                    return gamesFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static class DealOfTheDayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Game> {
        TextView pGameName;
        TextView pPriceTag;
        ImageView pImgGame;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_deal_of_the_day, container, false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
            pGameName = (TextView) view.findViewById(R.id.text_item_dealoftheday);
            pPriceTag = (TextView) view.findViewById(R.id.price_item_dealoftheday);
            pImgGame = (ImageView) view.findViewById(R.id.picture_item_dealoftheday);
            pGameName.setText("No Games loaded from Steam yet!");
            pPriceTag.setText("");


            return view;
        }

        public void reloadDataset() {
            pGameName.setText("No Games loaded from Steam yet!");
            pPriceTag.setText("");
            pImgGame.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            getLoaderManager().restartLoader(0, null, this);
        }

        @Override
        public Loader<Game> onCreateLoader(int i, Bundle bundle) {
            return new DataListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Game> gameLoader, Game game) {
            pGameName.setText(game.getName());
            pPriceTag.setText(game.getFinal_price());
            pImgGame.setImageBitmap(game.getHeader_bitmap());
        }

        @Override
        public void onLoaderReset(Loader<Game> gameLoader) {

        }


        public static class DataListLoader extends AsyncTaskLoader<Game> {

            Game mGame;

            public DataListLoader(Context context) {
                super(context);
            }

            @Override
            public Game loadInBackground() {
                Game pGame;

                // Loading the data from the web yo.
                String JSON_From_API = JSON.readJSONFeed(URL_API.concat(API_DAILY_DEAL));
                try {
                    JSONObject jsonObject = new JSONObject(JSON_From_API);
                    pGame = JSON.ParseDealOfTheDayJSONFromAPI(jsonObject);
                    pGame.setHeader_bitmap(HttpThumbnails.readPictureFromTheWeb(pGame.getHeader_image()));
                } catch (Exception e) {
                    Log.d("ReadSteamJSONFeed", "Couldn't load the daily deal!");

                    // TODO GERER CAS FAIL LOAD NETWORK
                    pGame = new Game();
                    pGame.setName("Failed to load deal of the day from steam!");


                }


                return pGame;
            }

            @Override
            public void deliverResult(Game vGame) {
                mGame = vGame;

                // If the Loader is currently started, we can immediately
// deliver its results.
                if (isStarted()) {
                    super.deliverResult(mGame);
                }
            }

            private void onReleaseResources(List<Game> oldGames) {
                // TODO Auto-generated method stub

            }

            /**
             * Handles a request to start the Loader.
             */
            @Override
            protected void onStartLoading() {
                if (mGame != null) {
                    // If we currently have a result available, deliver it
                    // immediately.
                    deliverResult(mGame);
                }


                if (takeContentChanged() || mGame == null) {
                    // If the data has changed since the last time it was loaded
                    // or is not currently available, start a load.
                    forceLoad();
                }
            }
        }

    }

    public static class GamesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Game>> {
        CustomArrayAdapter mAdapter;
        Boolean isMostPopular = false;

/// Tell if it's for the sales or most popular fragment
        public Boolean getIsMostPopular() {
            if (isMostPopular)
                return true;
            else
                return false;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments()!=null)
                this.isMostPopular = getArguments().getBoolean("isMostPopular", false);

            // Create an empty adapter we will use to display the loaded data.
            mAdapter = new CustomArrayAdapter(getActivity());
            setListAdapter(mAdapter);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No Games loaded from Steam yet!");

            // Start out with a progress indicator.
            setListShown(false);
        }

        public void reloadList() {
            mAdapter.notifyDataSetChanged();
        }

        public void reloadDataset() {
            // Start out with a progress indicator.
            setListShown(false);
            mAdapter.clear();
            getLoaderManager().restartLoader(0,null,this);
        }


        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Starting the webpage associated to the steam store...
            Log.i("DataListFragment", "Link clicked: " + mAdapter.getItem(position).getAppUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mAdapter.getItem(position).getAppUrl()));
            startActivity(intent);
        }

        @Override
        public Loader<List<Game>> onCreateLoader(int arg0, Bundle arg1) {
            if (getIsMostPopular())
                return new DataListLoader(getActivity(), true);
            else
                return new DataListLoader(getActivity(), false);
        }

        @Override
        public void onLoadFinished(Loader<List<Game>> arg0, List<Game> data) {
            mAdapter.setData(data);
            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        public static class DataListLoader extends AsyncTaskLoader<List<Game>> {

            List<Game> mGames;
            Boolean mIsMostPopular;
            public DataListLoader(Context context, Boolean isMostPopular) {
                super(context);
                mIsMostPopular = isMostPopular;
            }

                        @Override
                        public List<Game> loadInBackground() {
                        List<Game> lstGames;
                        String JSON_From_API;

                        // Loading the data from the web yo.
                        if (mIsMostPopular)
                            JSON_From_API = JSON.readJSONFeed(URL_API.concat(API_MOST_POPULAR));
                        else
                            JSON_From_API = JSON.readJSONFeed(URL_API.concat(API_SPECIAL));


                        try {
                            JSONObject jsonObject = new JSONObject(JSON_From_API);
                            if (mIsMostPopular)
                                lstGames = JSON.ParseJSONFromAPI(jsonObject, true);
                            else
                                lstGames = JSON.ParseJSONFromAPI(jsonObject, false);

                            for (int iCpt = 0; iCpt < lstGames.size(); iCpt++)
                            {
                                lstGames.get(iCpt).setHeader_bitmap(HttpThumbnails.readPictureFromTheWeb(lstGames.get(iCpt).getSmall_capsule_img()));
                    }

                } catch (Exception e) {
                    Log.d("ReadSteamJSONFeed", "Couldn't load games from most popular or sales!!");

                    // TODO GERER CAS FAIL LOAD NETWORK
                    lstGames = getFailLoadGames();
                }


                return lstGames;
            }

            private List<Game> getFailLoadGames() {
                List<Game> lstGames;
                lstGames = new ArrayList<Game>(1);
                Game a = new Game();
                a.setName("I was not able to load games from the steam api. :(");
                a.setFinal_price("0.00$");
                lstGames.add(a);
                return lstGames;
            }

            @Override
            public void deliverResult(List<Game> lstGames) {
                if (isReset()) {
                    // An async query came in while the loader is stopped.  We
                    // don't need the result.
                    if (lstGames != null) {
                        onReleaseResources(lstGames);
                        lstGames = getFailLoadGames();
                    }
                }
                List<Game> oldGames = lstGames;
                mGames = lstGames;

                if (isStarted()) {
                    // If the Loader is currently started, we can immediately
                    // deliver its results.
                    super.deliverResult(lstGames);
                }

                // At this point we can release the resources associated with
                // 'oldApps' if needed; now that the new result is delivered we
                // know that it is no longer in use.
                if (oldGames != null) {
                    onReleaseResources(oldGames);
                }
            }

            private void onReleaseResources(List<Game> oldGames) {
                // TODO Auto-generated method stub

            }

            /**
             * Handles a request to start the Loader.
             */
            @Override
            protected void onStartLoading() {
                if (mGames != null) {
                    // If we currently have a result available, deliver it
                    // immediately.
                    deliverResult(mGames);
                }


                if (takeContentChanged() || mGames == null) {
                    // If the data has changed since the last time it was loaded
                    // or is not currently available, start a load.
                    forceLoad();
                }
            }


        }

        @Override
        public void onLoaderReset(Loader<List<Game>> arg0) {
        }
}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, getString(R.string.title_section1).toUpperCase(Locale.getDefault()), dailyDealFragment);
        getSupportFragmentManager().putFragment(outState, getString(R.string.title_section2).toUpperCase(Locale.getDefault()), gamesFragment);
        getSupportFragmentManager().putFragment(outState, getString(R.string.title_section3).toUpperCase(Locale.getDefault()), mostPopularFragment);
    }
}
