package com.example.nagato.mobiledeveloper.widget.activities;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nagato.mobiledeveloper.R;
import com.example.nagato.mobiledeveloper.data.Constant;
import com.example.nagato.mobiledeveloper.global.MyApplacation;
import com.example.nagato.mobiledeveloper.widget.fragment.ArticleFragment;
import com.example.nagato.mobiledeveloper.utils.CommonUtils;
import com.example.nagato.mobiledeveloper.widget.fragment.BaseFragment;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment mCurrFragment;
    public FragmentManager fm=getSupportFragmentManager();
    public String mCurrFragmentColumn;
    public Toolbar mtoolbar;
    public CoordinatorLayout mCoordinatorLayout;
    private Realm mrealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        //floatingbutton设置
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coor_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.makeSnackBar(mCoordinatorLayout,"回到顶部", Snackbar.LENGTH_LONG);
            }
        });
        //抽屉相关设置
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //fragment容器设置
        android.support.v4.app.Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = ArticleFragment.newInstance(Constant.Column.ANDROID.getApiName());
            mtoolbar.setTitle(R.string.android);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, Constant.Column.ANDROID.getId())
                    .commit();
            mCurrFragment = fragment;
            mCurrFragmentColumn = Constant.Column.ANDROID.getId();
        }
        mrealm=Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mrealm.close();
        CommonUtils.clearAllCache(MyApplacation.getContext());
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
    /////////////////////////////////抽屉点击效果/////////////////////////////////////////////////////
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if(((BaseFragment)mCurrFragment).mLoadingState==BaseFragment.IS_LOADING_DATA){
            CommonUtils.makeSnackBar(mCoordinatorLayout,"wait loading data",Snackbar.LENGTH_LONG);
            return true;
        }else{
        int id = item.getItemId();
        switch (id){
            case R.id.nav_beauty:
                showRightFragment(Constant.Column.BEAUTY);
                break;
            case R.id.nav_favorite:
                showRightFragment(Constant.Column.FAVORITE);
                break;
            default:
                for(Constant.Column column: Constant.Column.values()){
                    if(id==column.getResId()){
                       showRightFragment(column);
                    }
                }
                break;
        }

        closeDrawer();
        return true;}
    }
/////////////////////////////////显示正确的fragment////////////////////////////////////////
    private void showRightFragment(Constant.Column column){
        Fragment fragment=fm.findFragmentByTag(column.getId());
        if(fragment==null){
            switch(column){
                case BEAUTY:
                    fragment= ArticleFragment.newInstance(column.getApiName());
                    break;
                case FAVORITE:
                    fragment= ArticleFragment.newInstance(column.getApiName());
                    break;
                default:
                    fragment=ArticleFragment.newInstance(column.getApiName());
                    break;
            }
            fm.beginTransaction().hide(mCurrFragment).add(R.id.fragment_container,fragment,column.getId()).commit();
            mCurrFragment=fragment;
            mCurrFragmentColumn=column.getId();
            mtoolbar.setTitle(column.getStrId());
        }
        else{
            fm.beginTransaction().hide(mCurrFragment).show(fragment).commit();
            mCurrFragment=fragment;
            mCurrFragmentColumn=column.getId();
            mtoolbar.setTitle(column.getStrId());
        }
    }


    private void hideAllExcept(String mCurrFragmentColumn) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        for (Constant.Column column : Constant.Column.values()) {
            android.support.v4.app.Fragment fragment = manager.findFragmentByTag(column.getId());
            if (fragment == null)
                continue;

            if (column.equals(mCurrFragmentColumn)) {
                manager.beginTransaction().show(fragment).commit();
                mCurrFragment = fragment;
            } else {
                manager.beginTransaction().hide(fragment).commit();
            }
        }
    }
///////////////////////通用方法////////////////////////////

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
