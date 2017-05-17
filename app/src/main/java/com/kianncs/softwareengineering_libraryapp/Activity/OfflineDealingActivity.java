package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.kianncs.softwareengineering_libraryapp.Fragments.MyPostFragment;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.Fragments.SearchPostFragment;

public class  OfflineDealingActivity extends AppCompatActivity {

    private static final String TAG = "OfflineDealingActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_dealing);

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new SearchPostFragment(),
                    new MyPostFragment()
            };
            private final String[] mFragmentNames = new String[] {
                    "Search",
                    "My post"
            };
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            public int getCount() {
                return mFragments.length;
            }
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfflineDealingActivity.this, NewPostActivity.class));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        return true;
    }

    public String getUserID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
