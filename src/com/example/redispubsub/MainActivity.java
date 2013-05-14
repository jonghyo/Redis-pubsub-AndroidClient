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
	    //publish���
		private PubFragment mPubFragment;
		//subscribe���
		private SubFragment mSubFragment;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			// ���C�A�E�g����TabHost���擾
			TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
			// �Z�b�g�A�b�v
			tabHost.setup();
			// ���C�A�E�g����ViewPager���擾
			ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
			// TabsAdaper�������
			TabsAdapter adapter = new TabsAdapter(this, tabHost, viewPager);
			// Publish���:PublishFragment������� 
			mPubFragment = (PubFragment) Fragment.instantiate(this,
					PubFragment.class.getName(), null);
			// Subscribe��ʁFSubscribeFragment�������
			mSubFragment = (SubFragment) Fragment.instantiate(this,
					SubFragment.class.getName(), null);
			// TabAdapter�ɒǉ�
			adapter.addTab("Publish", mPubFragment);
			adapter.addTab("Subscribe", mSubFragment);
		}

		private class TabsAdapter extends FragmentPagerAdapter implements
				TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
			private Context mContext;
			private TabHost mTabHost;
			private ViewPager mViewPager;
			ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

			// TabContentFactory�Ƃ����^�u�̃R���e���c�����N���X���K�v�ɂȂ邪����͉����\�����Ȃ��̂Ń_�~�[�̃R���e���c�����N���X���`����
			private class Dummy implements TabHost.TabContentFactory {
				private final Context mContext;

				public Dummy(Context context) {
					mContext = context;
				}

				@Override
				public View createTabContent(String tag) {
					// �T�C�Y0�@��View��Ԃ�
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
				// �^�u���I�΂�ĕω������Ƃ��̃��X�i�[���Z�b�g
				mTabHost.setOnTabChangedListener(this);
				// ViewPager�ɕ\������Adapter���Z�b�g
				mViewPager.setAdapter(this);
				// Viewpager�Ńy�[�W���ω��������Ƀ��X�i�[���Z�b�g
				mViewPager.setOnPageChangeListener(this);
			}

			public void addTab(String title, Fragment fragment) {
				// TabSpec�������
				TabSpec tabSpec = mTabHost.newTabSpec(title);
				//�\���^�C�g�����Z�b�g
				tabSpec.setIndicator(title);
				// �R���e���c���Z�b�g(�_�~�[)
				tabSpec.setContent(new Dummy(mContext));
				// �^�u��ǉ�
				mTabHost.addTab(tabSpec);
				// �t���O�����g�����X�g�ɒǉ�
				mFragments.add(fragment);
			}
			
			@Override
			public int getCount() {
				// Adapter�̃f�[�^�̐���Ԃ����\�b�h�ł��B
				// ����̓t���O�����g�̃��X�g�̐���Ԃ��܂��B
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int position) {
				// �w�肳�ꂽ�ʒu�i���Ԗڂ̃^�u���j�ɑ��݂���t���O�����g��Ԃ����\�b�h
				return mFragments.get(position);
			}

			@Override
			public void onTabChanged(String tabId) {
				// �^�u���I�΂�ĕω������ꍇ�͉��Ԗڂ̃^�u���擾����
				int position = mTabHost.getCurrentTab();
				// ViewPager�̌��݈ʒu���Z�b�g����
				mViewPager.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ����͉������Ȃ�
			}

			@Override
			public void onPageSelected(int position) {
				// ViewPager���X���C�v���ꂽ�Ƃ��ɌĂяo����܂��B
				// �^�u�̌��݈ʒu���Z�b�g����
				mTabHost.setCurrentTab(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// ����͉������Ȃ�
			}
		}
		
		
		
}