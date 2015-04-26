package com.example.oskar.vklocator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oskar.vklocator.test.PositionUpdater;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapPane extends Activity implements OnMapReadyCallback {

    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    public static GoogleMap mMap;
    PositionUpdater positionUpdater = new PositionUpdater(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (id){
            case R.id.action_icq:
                if(item.getTitle().equals("offline")) {
                    MakeOnline();
                    item.setTitle("online");
                    item.setIcon(R.drawable.online);
                }
                else
                {
                    MakeOffline();
                    item.setTitle("offline");
                    item.setIcon(R.drawable.offline);
                }

                break;


        }

        return super.onOptionsItemSelected(item);
    }


    // online/offline icq item
    public void MakeOnline() {

        positionUpdater.showMe();
    }

    public void MakeOffline() {

    }





    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        map.setMyLocationEnabled(true);

    }


    public void DeleteToken(View view) {
        MainActivity.account.clear();
        startActivity(new Intent(this, MainActivity.class));
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }
}

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


public static class MenuFragment extends Fragment {
    public static final String ARG_MENU_NUMBER = "menu_number";

    public MenuFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(int position) {
        Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        MapFragment mapFragment;
        View rootView;
        int i = getArguments().getInt(ARG_MENU_NUMBER);
        String menuTitle = getResources().getStringArray(R.array.menu_array)[i];

        switch (i)
        {
            case 0: //map
                rootView = inflater.inflate(R.layout.activity_map, container, false);

                break;

            case 1: //filter
                rootView = inflater.inflate(R.layout.fragment_filter, container, false);
                break;


            case 2: //settings
                rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                break;

            default:
                rootView = inflater.inflate(R.layout.activity_map, container, false);
        }


        getActivity().setTitle(menuTitle);
        return rootView;
    }
}
}
