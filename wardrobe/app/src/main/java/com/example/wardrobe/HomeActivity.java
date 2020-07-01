package com.example.wardrobe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.wardrobe.model.entities.Garment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements GarmentsListFragment.Delegate {
    NavController navCtrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navCtrl = Navigation.findNavController(this,R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this,navCtrl);

        BottomNavigationView bottomNav = findViewById(R.id.home_bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navCtrl);
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
