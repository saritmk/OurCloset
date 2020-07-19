package com.example.wardrobe;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.wardrobe.model.entities.Garment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements GarmentsListFragment.Delegate {
    NavController navCtrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        navCtrl = Navigation.findNavController(this,R.id.home_nav_host);

        navCtrl = Navigation.findNavController(this,R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this,navCtrl);

        BottomNavigationView bottomNav = findViewById(R.id.home_bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navCtrl);

        if (auth.getCurrentUser() == null) {
            bottomNav.setVisibility(View.GONE);
            if (navCtrl.getCurrentDestination().getId() != R.id.loginFragment) {
                navCtrl.navigate(R.id.loginFragment);

            }
        } else {
            bottomNav.setVisibility(View.VISIBLE);
            if (navCtrl.getCurrentDestination().getId() != R.id.closetListFragment) {
               navCtrl.navigate(R.id.closetListFragment);
            }
        }

    }

    @Override
    public void onItemSelected(Garment garment) {
        NavController navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavGraphDirections.ActionGlobalGarmentDetailsFragment direction = GarmentsListFragmentDirections.actionGlobalGarmentDetailsFragment(garment);
        navCtrl.navigate(direction);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            navCtrl.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
