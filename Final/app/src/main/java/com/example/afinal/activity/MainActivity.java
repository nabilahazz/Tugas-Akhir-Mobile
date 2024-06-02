package com.example.afinal.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.afinal.fragment.HomeFragment;
import com.example.afinal.fragment.ProfileFragment;
import com.example.afinal.R;
import com.example.afinal.fragment.SearchFragment;
import com.example.afinal.fragment.WishlistFragment;

public class MainActivity extends AppCompatActivity {

    ImageView iv_home,iv_profile,iv_favorite,iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_home = findViewById(R.id.IV_Home);
        iv_search = findViewById(R.id.IV_Search);
        iv_profile = findViewById(R.id.IV_Profile);
        iv_favorite = findViewById(R.id.IV_Post);

        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());

        if (!(fragment instanceof HomeFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, homeFragment)
                    .commit();
        }
        iv_home.setOnClickListener(v -> {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .addToBackStack(null)
                    .commit();
        });
        iv_search.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });
        iv_profile.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        iv_favorite.setOnClickListener(v -> {
            WishlistFragment wishlistFragment = new WishlistFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, wishlistFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }
    public void updateFavoriteFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_container);
        if (currentFragment instanceof WishlistFragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }
}
