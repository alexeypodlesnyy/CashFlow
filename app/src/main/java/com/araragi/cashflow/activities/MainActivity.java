package com.araragi.cashflow.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.entity.CashTransaction;
import com.araragi.cashflow.fragments.NewCashTransactionFragment;
import com.araragi.cashflow.fragments.RecyclerViewCashFragment;
import com.araragi.cashflow.fragments.StatisticsFragment;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Box<CashTransaction> cashBox;

    private Query<CashTransaction> cashMoneyQuery;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public NewCashTransactionFragment newCashTransactionFragment;
    public RecyclerViewCashFragment listFragment;
    public StatisticsFragment statFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        BoxStore boxStore = ((CashFlowApp) getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransaction.class);


        newCashTransactionFragment = new NewCashTransactionFragment();
        listFragment = new RecyclerViewCashFragment();
        statFragment = new StatisticsFragment();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragments_container, newCashTransactionFragment, newCashTransactionFragment.TAG);
        fragmentTransaction.addToBackStack(newCashTransactionFragment.TAG);
        fragmentTransaction.commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment fragment = fragmentManager.findFragmentById(R.id.fragments_container);
                if(fragment instanceof NewCashTransactionFragment){
                        navigationView.setCheckedItem(R.id.nav_add_new);
                        toolbar.setTitle("Add");
                    }
                if(fragment instanceof RecyclerViewCashFragment){
                        navigationView.setCheckedItem(R.id.nav_list);
                        toolbar.setTitle("List");
                    }
                if(fragment instanceof StatisticsFragment){
                    navigationView.setCheckedItem(R.id.nav_statistics);
                    toolbar.setTitle("Statistics");
                    }
//              if(fragment instanceof DatabaseFragment({
//                    navigationView.setCheckedItem(R.id.nav_database);
//                }


            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_add_new:
                onAddNewClicked();
                break;
            case R.id.nav_list:
                onListClicked();
                break;
            case R.id.nav_statistics:
                onStatisticsClicked();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onListClicked() {


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments_container, listFragment, listFragment.TAG);
        fragmentTransaction.addToBackStack(listFragment.TAG);
        fragmentTransaction.commit();

    }
    public void onAddNewClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments_container, newCashTransactionFragment);
        fragmentTransaction.addToBackStack(NewCashTransactionFragment.TAG);
        fragmentTransaction.commit();

    }

    public void onDatabaseClicked() {

    }

    public void onStatisticsClicked() {


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments_container, statFragment);
        fragmentTransaction.addToBackStack(StatisticsFragment.TAG);
        fragmentTransaction.commit();

    }


}
