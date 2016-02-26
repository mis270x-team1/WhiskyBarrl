package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainTabbedActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private TabLayout mainTabBar;
    private ViewPager mainViewPager;
    private FragmentManager fragmentManager;
    private String currentUsername;

    private Fragment accountFragment;
    private Fragment mainWhiskeyListFragment;
    private Fragment userFavoritesFragment;
    private Fragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        currentUsername = getIntent().getStringExtra("username");

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mainTabBar = (TabLayout) findViewById(R.id.mainTabBar);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        fragmentManager = getSupportFragmentManager();

        mainToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mainToolbar);

        initTabBar();
        initViewPager();

        mainTabBar.setTabGravity(TabLayout.GRAVITY_FILL);
        mainTabBar.setupWithViewPager(mainViewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_new_whiskey:
                // Start a new activity to add a whiskey
                Intent i = new Intent(this, NewWhiskeyActivity.class);
                startActivity(i);
                break;
            case R.id.menu_item_search_whiskeys:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initTabBar() {
        Bundle arguments = new Bundle();
        arguments.putString("username", currentUsername);

        accountFragment = new AccountActivity();
        accountFragment.setArguments(arguments);
        mainWhiskeyListFragment = new MainWhiskeyListActivity();
        mainWhiskeyListFragment.setArguments(arguments);
        userFavoritesFragment = new UserFavoritesActivity();
        userFavoritesFragment.setArguments(arguments);
        searchFragment = new SearchActivity();
        searchFragment.setArguments(arguments);

        TabLayout.Tab accountTab = mainTabBar.newTab().setIcon(R.drawable.user_account);
        TabLayout.Tab mainWhiskeyListTab = mainTabBar.newTab().setIcon(R.drawable.whiskey_list);
        TabLayout.Tab favoritesTab = mainTabBar.newTab().setIcon(R.drawable.favorites);
        //TabLayout.Tab userWhiskeysTab = mainTabBar.newTab().setIcon(R.drawable.user_whiskey);

        mainTabBar.addTab(accountTab);
        mainTabBar.addTab(mainWhiskeyListTab);
        mainTabBar.addTab(favoritesTab);
        //mainTabBar.addTab(userWhiskeysTab);
    }

    // Most of the below code adapted from the following URL:
    // http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPagerAdapter.addFragment(accountFragment, "Account");
        viewPagerAdapter.addFragment(mainWhiskeyListFragment, "Whiskeys");
        viewPagerAdapter.addFragment(userFavoritesFragment, "Favorites");

        mainViewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
