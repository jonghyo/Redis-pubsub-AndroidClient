package com.example.redispubsub;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity {
	    //publish画面
		private PubFragment mPubFragment;
		//subscribe画面
		private SubFragment mSubFragment;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			// レイアウトからTabHostを取得
			TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
			// セットアップ
			tabHost.setup();
			// レイアウトからViewPagerを取得
			ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
			// TabsAdaperを作って
			TabsAdapter adapter = new TabsAdapter(this, tabHost, viewPager);
			// Publish画面:PublishFragmentを作って 
			mPubFragment = (PubFragment) Fragment.instantiate(this,
					PubFragment.class.getName(), null);
			// Subscribe画面：SubscribeFragmentを作って
			mSubFragment = (SubFragment) Fragment.instantiate(this,
					SubFragment.class.getName(), null);
			// TabAdapterに追加
			adapter.addTab("Publish", mPubFragment);
			adapter.addTab("Subscribe", mSubFragment);
		}

		private class TabsAdapter extends FragmentPagerAdapter implements
				TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
			private Context mContext;
			private TabHost mTabHost;
			private ViewPager mViewPager;
			ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

			// TabContentFactoryというタブのコンテンツを作るクラスが必要になるが今回は何も表示しないのでダミーのコンテンツを作るクラスを定義する
			private class Dummy implements TabHost.TabContentFactory {
				private final Context mContext;

				public Dummy(Context context) {
					mContext = context;
				}

				@Override
				public View createTabContent(String tag) {
					// サイズ0　のViewを返す
					View v = new View(mContext);
					v.setMinimumWidth(0);
					v.setMinimumHeight(0);
					return v;
				}
			}

			public TabsAdapter(FragmentActivity activity, TabHost tabHost,
					ViewPager pager) {
				super(activity.getSupportFragmentManager());
				mContext = activity;
				mTabHost = tabHost;
				mViewPager = pager;
				// タブが選ばれて変化したときのリスナーをセット
				mTabHost.setOnTabChangedListener(this);
				// ViewPagerに表示するAdapterをセット
				mViewPager.setAdapter(this);
				// Viewpagerでページが変化した時にリスナーをセット
				mViewPager.setOnPageChangeListener(this);
			}

			public void addTab(String title, Fragment fragment) {
				// TabSpecを作って
				TabSpec tabSpec = mTabHost.newTabSpec(title);
				//表示タイトルをセット
				tabSpec.setIndicator(title);
				// コンテンツをセット(ダミー)
				tabSpec.setContent(new Dummy(mContext));
				// タブを追加
				mTabHost.addTab(tabSpec);
				// フラグメントをリストに追加
				mFragments.add(fragment);
			}
			
			@Override
			public int getCount() {
				// Adapterのデータの数を返すメソッドです。
				// 今回はフラグメントのリストの数を返します。
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int position) {
				// 指定された位置（何番目のタブか）に存在するフラグメントを返すメソッド
				return mFragments.get(position);
			}

			@Override
			public void onTabChanged(String tabId) {
				// タブが選ばれて変化した場合は何番目のタブか取得して
				int position = mTabHost.getCurrentTab();
				// ViewPagerの現在位置をセットする
				mViewPager.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 今回は何もしない
			}

			@Override
			public void onPageSelected(int position) {
				// ViewPagerがスワイプされたときに呼び出されます。
				// タブの現在位置をセットする
				mTabHost.setCurrentTab(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 今回は何もしない
			}
		}
		
		
		
}