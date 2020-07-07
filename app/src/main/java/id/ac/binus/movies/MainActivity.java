package id.ac.binus.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity{

    public static final String API_KEY = "7881ea7";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
        pageAdapter = new PageAdapter(getSupportFragmentManager());


//        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pageAdapter);
    }

}