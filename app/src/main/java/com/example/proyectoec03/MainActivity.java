package com.example.proyectoec03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationHost, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CharactersFragment(), "CATALOGUE_FRAGMENT")
                    .commit();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment);
        if (addToBackStack == true) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_characters:
                navigateTo(new CharactersFragment(), true);
                return true;
            case R.id.nav_episodes:
                navigateTo(new EpisodesFragment(), true);
                return true;
            case R.id.nav_profile:
                navigateTo(new ProfileUserFragment(), true);
                return true;
            case R.id.nav_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                Toast.makeText(this, "Sesion Cerrada", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void hideShowFragment(Fragment oldFragment, Fragment newFragment, String tag) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (newFragment.isAdded()) {
            transaction.hide(oldFragment)
                    .show(newFragment);
        } else {
            transaction.hide(oldFragment)
                    .add(R.id.container, newFragment, tag);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}