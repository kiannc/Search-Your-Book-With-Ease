package com.kianncs.softwareengineering_libraryapp.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kianncs.softwareengineering_libraryapp.Entity.User;
import com.kianncs.softwareengineering_libraryapp.Fragments.AmazonFragment;
import com.kianncs.softwareengineering_libraryapp.Fragments.ChangePasswordFragments;
import com.kianncs.softwareengineering_libraryapp.Fragments.EbayFragment;
import com.kianncs.softwareengineering_libraryapp.Fragments.MyChatFragment;
import com.kianncs.softwareengineering_libraryapp.Fragments.NLBFragment;
import com.kianncs.softwareengineering_libraryapp.Fragments.UserProfileFragment;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.Fragments.MyPostFragment;
import com.kianncs.softwareengineering_libraryapp.Fragments.SearchPostFragment;
import com.kianncs.softwareengineering_libraryapp.UserAccountManager;
import com.kianncs.softwareengineering_libraryapp.UserListener;

import java.util.List;

public class UserAreaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

        Toolbar toolbar = null;
        TextView tvShowUserName;
        NavigationView navigationView = null;
        TextView textView = null;
        String title;
        NLBFragment nlbFragment;
        AmazonFragment amazonFragment;
        EbayFragment ebayFragment;
        UserProfileFragment userProfileFragment;
        ChangePasswordFragments changePasswordFragment;
        SearchPostFragment searchPostFragment;
        MyPostFragment myPostFragment;
        UserAccountManager userAccountManager;
        MyChatFragment myChatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //Set the fragment initally
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view  = navigationView.getHeaderView(0);
        tvShowUserName = (TextView) view.findViewById(R.id.tvShowUserNameNaviDrawer);
        nlbFragment = new NLBFragment();
        amazonFragment = new AmazonFragment();
        ebayFragment = new EbayFragment();
        userProfileFragment = new UserProfileFragment();
        changePasswordFragment = new ChangePasswordFragments();
        searchPostFragment = new SearchPostFragment();
        myPostFragment = new MyPostFragment();
        userAccountManager = new UserAccountManager();
        myChatFragment = new MyChatFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,userProfileFragment,"User Profile").addToBackStack("User Profile").commit();
        String userId = userAccountManager.getCurrentUserID();
        userAccountManager.getUser(userId, new UserListener() {
            @Override
            public void onUserDataChange(User user) {
                tvShowUserName.setText(user.getName());
            }
        });
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        String a="";
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
            FragmentManager fragmentManager = UserAreaActivity.this.getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if(fragments!=null) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible()) {
                        a = fragment.getTag();
                    }
                }
                if (a == "") {
                    getSupportActionBar().setTitle("User Profile");
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,userProfileFragment,"User Profile").addToBackStack("User Profile").commit();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserAreaActivity.this);
                    builder1.setMessage("Are you sure you want to Logout?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    userAccountManager.signOut();
                                    Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(UserAreaActivity.this, "Logout Sucessfully", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else { // set back to the previous title
                    getSupportActionBar().setTitle(a);
                }

            }else{
                Toast.makeText(this,"Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookSearch) {
            toolbar.setTitle("National Library Database");
            if(getSupportFragmentManager().findFragmentByTag("National Library Database")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,nlbFragment,"National Library Database").addToBackStack("National Library Database").commit();
            else{
                getSupportFragmentManager().popBackStack("National Library Database", 0);
            }
        } else if (id == R.id.nav_AmazonSearch) {
            toolbar.setTitle("Amazon");
            if(getSupportFragmentManager().findFragmentByTag("Amazon")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,amazonFragment,"Amazon").addToBackStack("Amazon").commit();
            else {
                getSupportFragmentManager().popBackStack("Amazon", 0);
            }
        } else if (id == R.id.nav_EbaySearch) {
            toolbar.setTitle("Ebay");
            if(getSupportFragmentManager().findFragmentByTag("Ebay")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,ebayFragment,"Ebay").addToBackStack("Ebay").commit();
            else {
                getSupportFragmentManager().popBackStack("Ebay", 0);
            }
        } else if (id == R.id.nav_transaction) {
            toolbar.setTitle("Search Post");
            if(getSupportFragmentManager().findFragmentByTag("Search Post")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchPostFragment,"Search Post").addToBackStack("Search Post").commit();
            else
                getSupportFragmentManager().popBackStack("Search Post",0);
        }
        else if (id == R.id.nav_myPost) {
            toolbar.setTitle("My Post");

            if(getSupportFragmentManager().findFragmentByTag("My Post")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, myPostFragment,"My Post").addToBackStack("My Post").commit();
            else
                getSupportFragmentManager().popBackStack("My Post",0);

        }else if (id == R.id.nav_manageAccount) {
            toolbar.setTitle("User Profile");
            if(getSupportFragmentManager().findFragmentByTag("User Profile")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,userProfileFragment,"User Profile").addToBackStack("User Profile").commit();
            else
                getSupportFragmentManager().popBackStack("User Profile",0);

        } else if (id == R.id.nav_changePassword) {
            toolbar.setTitle("Change Password");
            if(getSupportFragmentManager().findFragmentByTag("Change Password")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,changePasswordFragment,"Change Password").addToBackStack("Change Password").commit();
            else
                getSupportFragmentManager().popBackStack("Change Password",0);

        }else if (id == R.id.nav_logout) {
            userAccountManager.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(UserAreaActivity.this, "Logout Sucessfully", Toast.LENGTH_SHORT).show();
            finish();
        }else if(id == R.id.nav_myChat){
            toolbar.setTitle("My Chats");
            if(getSupportFragmentManager().findFragmentByTag("My Chats")==null)
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,myChatFragment,"My Chats").addToBackStack("My Chats").commit();
            else
                getSupportFragmentManager().popBackStack("My Chats",0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
