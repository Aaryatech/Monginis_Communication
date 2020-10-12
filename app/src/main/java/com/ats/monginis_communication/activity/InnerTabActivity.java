package com.ats.monginis_communication.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.fragment.ComplaintFragment;
import com.ats.monginis_communication.fragment.LaunchProductFragment;
import com.ats.monginis_communication.fragment.MessagesFragment;
import com.ats.monginis_communication.fragment.NoticesFragment;
import com.ats.monginis_communication.fragment.NotificationsFragment;
import com.ats.monginis_communication.fragment.SuggestionFragment;
import com.ats.monginis_communication.interfaces.ComplaintInterface;
import com.ats.monginis_communication.interfaces.LaunchProductInterface;
import com.ats.monginis_communication.interfaces.MessagesInterface;
import com.ats.monginis_communication.interfaces.NoticesInterface;
import com.ats.monginis_communication.interfaces.NotificationsInterface;
import com.ats.monginis_communication.interfaces.SuggestionInterface;

public class InnerTabActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    public ViewPager viewPager;

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_tab);

        tabLayout = findViewById(R.id.inner_tabLayout);
        viewPager = findViewById(R.id.inner_viewPager);

        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    SuggestionInterface fragmentSuggestion = (SuggestionInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentSuggestion != null) {
                        fragmentSuggestion.fragmentGetVisible();
                    }
                } else if (position == 1) {
                    ComplaintInterface fragmentComplaint = (ComplaintInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentComplaint != null) {
                        fragmentComplaint.fragmentGetVisible();
                    }
                } else if (position == 2) {
                    LaunchProductInterface fragmentLaunchProd = (LaunchProductInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentLaunchProd != null) {
                        fragmentLaunchProd.fragmentGetVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            int tabId = 0;
            Intent mIntent = getIntent();
            tabId = mIntent.getIntExtra("tabId", 0);

            Log.e("TAB ID : ", "--------------" + tabId);
            if (tabId == 0) {
                viewPager.setCurrentItem(0);
            } else if (tabId == 1) {
                viewPager.setCurrentItem(1);
            } else if (tabId == 2) {
                viewPager.setCurrentItem(2);
            }
        } catch (Exception e) {
            Log.e("Exception : Tab Id : ", "-----" + e.getMessage());
            e.printStackTrace();
        }

    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SuggestionFragment();
                case 1:
                    return new ComplaintFragment();
                case 2:
                    return new LaunchProductFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return ""+getResources().getString(R.string.suggestion);
                case 1:
                    return ""+getResources().getString(R.string.complaint);
                case 2:
                    return "Feedback";
                default:
                    return null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InnerTabActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ProgressDialog", 0);
        startActivity(intent);
        finish();
    }

}
