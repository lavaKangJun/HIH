package com.example.youmeelee.handinhand;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.youmeelee.handinhand.LoginActivity.ID_EMAIL;
import static com.example.youmeelee.handinhand.LoginActivity.ID_NAME;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    ListView lvDrawerList;
    ArrayAdapter<String> adtDrawerList;
    String[] menuItem = new String[]{"FirstFragment", "SecondFragment"};

    FirstFragment fragFirst;
    SecondFragment fragSecond;

    DrawerLayout drawerLayout_Main;
    NavigationView navigationView;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = ID_EMAIL;

        //툴바 셋팅
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);

        fragFirst = new FirstFragment();
        fragSecond = new SecondFragment();

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);

        TextView text = (TextView)header.findViewById(R.id.drawer_email);
        TextView text2 = (TextView)header.findViewById(R.id.drawer_name);

        text.setText(ID_EMAIL);
        text2.setText(ID_NAME);

        //뷰페이저 셋팅
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //탭 레이아웃
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initViewAndListener();
    }

    private void initViewAndListener(){
        //드로어
        drawerLayout_Main = (DrawerLayout)findViewById(R.id.drawerLayout_Main);

        //토글버튼(좌측상단 버튼)을 객체로 만들고 드로어레이아웃에 리스너로 설정합니다.
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout_Main,toolbar, R.string.app_name, R.string.app_name);
        drawerLayout_Main.setDrawerListener(actionBarDrawerToggle);

        //네비게이션 뷰를 레이아웃에서 가져오고
        //아이템을 클릭했을 때 수행 할 이벤트 핸들러를 만듭니다.
        //해당 선택한 메뉴가 인자로 넘어오며 그 인자를 selectFragment로 보냅니다.
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.navigation_item_1) {
                    menuItem.setChecked(true);
                    mViewPager.setCurrentItem(0);
                    drawerLayout_Main.closeDrawer(navigationView);
                } else if (id == R.id.navigation_item_2) {
                    menuItem.setChecked(true);
                    mViewPager.setCurrentItem(1);
                    drawerLayout_Main.closeDrawer(navigationView);
                }else if (id == R.id.navigation_item_5) {
                    menuItem.setChecked(true);
                    logout();
                    drawerLayout_Main.closeDrawer(navigationView);
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    Dialog logoutDialog;

    //로그아웃
    public void logout() {
        new AlertDialog.Builder(MainActivity.this).setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다!", Toast.LENGTH_LONG).show();
                        logoutDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
                        logoutDialog.dismiss();
                        Intent i = new Intent(MainActivity.this, tempTutorialActivity.class);
                        startActivity(i);
                        Process.killProcess(Process.myPid());
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                new AlertDialog.Builder(MainActivity.this).setTitle("프로그램 종료")
                        .setMessage("프로그램을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {            //메인에서 탭 선택할 때

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    FirstFragment firstFragment = new FirstFragment();
                    return firstFragment;
                case 1:
                    SecondFragment secondFragment = new SecondFragment();
                    return secondFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "타임라인";
                case 1:
                    return "핸디픽쳐스";
            }
            return null;
        }

    }
}