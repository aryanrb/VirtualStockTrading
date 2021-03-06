package com.aryan.virtualtrading.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.aryan.virtualtrading.GetUserCallback;
import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.UserRequest;
import com.aryan.virtualtrading.api.BalanceAPI;
import com.aryan.virtualtrading.api.UserAPI;
import com.aryan.virtualtrading.fragments.HomeFragment;
import com.aryan.virtualtrading.fragments.LeaderboardFragment;
import com.aryan.virtualtrading.fragments.PortfolioFragment;
import com.aryan.virtualtrading.models.BalanceModel;
import com.aryan.virtualtrading.models.UserModel;
import com.facebook.AccessToken;
import com.facebook.login.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{

    //Creating user object for easy access from all fragments
    public static UserModel userProfile;
    public static BalanceModel userBalance;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    TextView navUserProfile, navUsername, navUserBalance;
    SharedPreferences sharedPreferences;

//    public static UserModel currentUser;
//    //New changes
//    private static final int RESULT_PROFILE_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Did you just clicked here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //nav header views
        View headerView = navigationView.getHeaderView(0);
        navUserProfile = (TextView) headerView.findViewById(R.id.tv_profile);
        navUsername = (TextView) headerView.findViewById(R.id.profileName);
        navUserBalance = (TextView) headerView.findViewById(R.id.balance);

        if(RetrofitUrl.token.equals("Bearer ")){
            Intent backtoLogin = new Intent(this, LoginActivity.class);
            startActivity(backtoLogin);
            deleteSavedUser();
            finish();
        }
        else {
            getUserProfile();
        }

        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
//        if (AccessToken.getCurrentAccessToken() == null) {
//            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(loginIntent);
//        }
//
//        // Make a button which leads to profile information of the user
//                if (AccessToken.getCurrentAccessToken() == null) {
//                    Intent profileIntent = new Intent(MainActivity.this, LoginActivity
//                            .class);
//                    startActivityForResult(profileIntent, RESULT_PROFILE_ACTIVITY);
//                } else {
////                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
////                    startActivity(profileIntent);
//                }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_market, R.id.nav_portfolio,
                R.id.nav_leaderboard, R.id.nav_history, R.id.nav_share, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(this);

    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case RESULT_PROFILE_ACTIVITY:
//                if (resultCode == RESULT_OK) {
////                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
////                    startActivity(profileIntent);
//                }
//                break;
//            default:
//                super.onActivityResult(requestCode,resultCode, data);
//        }
//    }

//    //Old code
//    @Override
//    protected void onResume() {
//        super.onResume();
//        UserRequest.makeUserRequest(new GetUserCallback(MainActivity.this).getCallback());
//    }

//    @Override
//    public void onCompleted(UserModel user) {
//        currentUser = user;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Getting logged in user profile
    public UserModel getUserProfile(){

        UserAPI userAPI = RetrofitUrl.getInstance().create(UserAPI.class);
        Call<UserModel> usersCall = userAPI.getUserProfile(RetrofitUrl.token);

        usersCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error loading profile!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                userProfile = response.body();
                showdetail(userProfile.getFullName());
                getBalanceDetail(userProfile);
                refresh(5000);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading profile...", Toast.LENGTH_SHORT).show();

            }
        });
        return userProfile;
    }

    public void getBalanceDetail(final UserModel user){
        BalanceAPI balanceAPI = RetrofitUrl.getInstance().create(BalanceAPI.class);
        Call<BalanceModel> balanceCall = balanceAPI.getBalanceDetail(RetrofitUrl.token, user.get_id());

        balanceCall.enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Code:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                userBalance = response.body();
                if(userBalance == null){
                    createBalance(user);
                }
                showAmt(userBalance.getvCoinBalance());

            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createBalance(UserModel userModel){
        BalanceModel model = new BalanceModel(100000f, 100000f, 100000f, 0f, userModel);
        userBalance = model;
        BalanceAPI balanceAPI = RetrofitUrl.getInstance().create(BalanceAPI.class);
        Call<Void> balanceCall = balanceAPI.createBalance(RetrofitUrl.token, model);

        balanceCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showdetail(String name){
        //Changing the content of nav header
        navUserProfile.setText(name.charAt(0)+ "");
        navUsername.setText(name+"");
    }

    public void showAmt(float amt){
        navUserBalance.setText("NRs. "+amt);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.nav_logout){
            deleteSavedUser();
            RetrofitUrl.token = "Bearer ";
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteSavedUser(){
        sharedPreferences = getSharedPreferences("User", 0);
        sharedPreferences.edit().clear().commit();
    }

    //for refreshing the activity every 5 second
    public void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getUserProfile();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

}
