package homework.nick.searchandplay2.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import homework.nick.searchandplay2.R;
import homework.nick.searchandplay2.events.EventToActivity;
import homework.nick.searchandplay2.events.MEvent;
import homework.nick.searchandplay2.fragments.SearchFragment;
import homework.nick.searchandplay2.utils.PlayerCommands;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


// 3 файла по 30 слов, нужно посчитать кол-во слов java
//---------------------------------------------------------------
//------------------ACHTUNG!!!-----------------------------------
//------client-id 8a81c591a1701b27d7e76e7b4e780050 --------------
//------client-secret 9cce4c9960853e1613659cf4a1c375e2 ----------


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //--------------------------------------------------------
    //UI
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout toolbarContext;
    private FrameLayout fragmentContent;
    private LinearLayout bottomControlPanel;
    private ImageView forwadBottomButton;
    private ImageView perviousBottomButton;
    private ImageView startStopBottomButton;
    private BottomSheetBehavior bottomSheetBehavior;
    //--------------------------------------------------------
    //Fragments
    private SearchFragment searchFragment;
    //--------------------------------------------------------
    // player flags
    private boolean playing = false;
    private boolean extended = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);


        initFragments();
        fragmentContent = (FrameLayout) findViewById(R.id.fragment_container);
        changeFragment(searchFragment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //-----------------------------------------------------------------
        bottomControlPanel = (LinearLayout) findViewById(R.id.pannel);
        forwadBottomButton = (ImageView) bottomControlPanel.findViewById(R.id.bottom_panel_next_button);
        perviousBottomButton = (ImageView) bottomControlPanel.findViewById(R.id.bottom_panel_previous_button);
        startStopBottomButton = (ImageView) bottomControlPanel.findViewById(R.id.bottom_panel_pause_start_button);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomControlPanel);

        //-----------------------------------------------------------------
        toolbarContext = (LinearLayout) toolbar.findViewById(R.id.toolbar_context);
        toolbarContext.addView(getLayoutInflater().inflate(R.layout.toolbar_default_layout, toolbarContext, false));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_navigation_view);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_list_white_48dp);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        startStopBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    EventBus.getDefault().post(new MEvent(PlayerCommands.PAUSE_COMMAND));
                } else {
                    EventBus.getDefault().post(new MEvent(PlayerCommands.START_COMMAND));
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (extended) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    @Subscribe
    public void onEvent(EventToActivity eventToActivity) {
        Log.i("activity", "event is here");

        if (eventToActivity.getMessage().equals(PlayerCommands.PAUSE_COMMAND)) {
            Log.i("activity", "event is pause");
            playing = false;
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (eventToActivity.getMessage().equals(PlayerCommands.START_COMMAND)) {
            Log.i("activity", "event is start");
            playing = true;
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_search:
                toolbarContext.removeAllViews();
                toolbarContext.addView(getLayoutInflater().inflate(R.layout.toolbar_search_layout, toolbarContext, false));
                break;
        }


        return true;
    }

    private void initFragments() {
        searchFragment = new SearchFragment();
    }

    private void changeFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //-------
    }
}
