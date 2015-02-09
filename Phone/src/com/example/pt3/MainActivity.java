package com.example.pt3;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/* 수정된 Main Activity */
public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private int NUM_PAGES = 6;		// 최대 페이지의 수 
	
	
	ViewPager mViewPager;			// View pager를 지칭할 변수 
	
	Button page1Btn, page2Btn, page3Btn, page4Btn, page5Btn, page6Btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		// ViewPager를 검색하고 Adapter를 달아주고, 첫 페이지를 선정해준다.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
		mViewPager.setCurrentItem(0);

		page1Btn = (Button) findViewById(R.id.nickname);
		page1Btn.setOnClickListener(this);
		page2Btn = (Button) findViewById(R.id.testNoti);
		page2Btn.setOnClickListener(this);
		page3Btn = (Button) findViewById(R.id.privacy);
		page3Btn.setOnClickListener(this);
		page4Btn = (Button) findViewById(R.id.device_setting);
		page4Btn.setOnClickListener(this);
		page5Btn = (Button) findViewById(R.id.app_setting);
		page5Btn.setOnClickListener(this);
		page6Btn = (Button) findViewById(R.id.activate);
		page6Btn.setOnClickListener(this);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				page1Btn.setSelected(false);
				page2Btn.setSelected(false);
				page3Btn.setSelected(false);
				page4Btn.setSelected(false);
				page5Btn.setSelected(false);
				page6Btn.setSelected(false);

				switch(position){
					case 0:
						page1Btn.setSelected(true);
						break;
					case 1:
						page2Btn.setSelected(true);
						break;
					case 2:
						page3Btn.setSelected(true);
						break;
					case 3:
						page4Btn.setSelected(true);
						break;
					case 4:
						page5Btn.setSelected(true);
						break;
					case 5:
						page6Btn.setSelected(true);
						break;
					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		page1Btn.setSelected(true);
	}
	
	// FragmentPageAdater : Fragment로써 각각의 페이지를 어떻게 보여줄지 정의한다. 
	private class pagerAdapter extends FragmentPagerAdapter{

		public pagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		// 특정 위치에 있는 Fragment를 반환해준다.
		@Override
		public Fragment getItem(int position) {
			
			switch(position){
				case 0:
					return new NickNameActivity();
				case 1:
					return new NotificatonTestActivity();
				case 2:
					return new PrivacyActivity();
				case 3:
					return new BrowserActivity();
				case 4:
					return new AppListActivity();
				case 5:
					return new ActivateActivity();
			
				default:
					return null;
			}
		}
		
		// 생성 가능한 페이지 개수를 반환해준다.
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NUM_PAGES;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.nickname:
				mViewPager.setCurrentItem(0);
				break;
			case R.id.testNoti:
				mViewPager.setCurrentItem(1);
				break;
			case R.id.privacy:
				mViewPager.setCurrentItem(2);
				break;
			case R.id.device_setting:
				mViewPager.setCurrentItem(3);
				break;
			case R.id.app_setting:
				mViewPager.setCurrentItem(4);
				break;
			case R.id.activate:
				mViewPager.setCurrentItem(5);
				break;
		}
	}	
}
