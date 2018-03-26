package com.jieyue.wechat.search.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.CommViewPagerAdapter;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.listener.CustomTabEntity;
import com.jieyue.wechat.search.listener.OnTabSelectListener;
import com.jieyue.wechat.search.listener.TabEntity;
import com.jieyue.wechat.search.service.MessageEvent;
import com.jieyue.wechat.search.ui.fragment.MsgListFragment;
import com.jieyue.wechat.search.ui.fragment.NoticeListFragment;
import com.jieyue.wechat.search.view.CommonTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author baipeng
 * @Title MsgNoticeActivity
 * @Date 2018/3/6 11:27
 * @Description MsgNoticeActivity.
 */
public class MsgNoticeActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    View llBack;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tl_4)
    CommonTabLayout mTabLayout_4;
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private MsgListFragment msgListFragment; //我的消息
    private NoticeListFragment noticeListFragment; //系统公告
    private int sub_tab = 0;
    private String[] mTitles = {"我的消息", "公告"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @OnClick({R.id.ll_back})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                EventBus.getDefault().post(new MessageEvent(Constants.GET_NEW_MSG));
                break;
            default:
                break;

        }
    }

    @Override
    public void OnTopLeftClick() {
        finish();
        EventBus.getDefault().post(new MessageEvent(Constants.GET_NEW_MSG));
    }

    @Override
    public void OnTopRightClick() {
    }

    @Override
    public void dealLogicAfterInitView() {
        llBack.setOnClickListener(this);
        mTabLayout_4.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_4.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_ask_price, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initPageAdapter();
    }

    private void initPageAdapter() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mTabLayout_4.setTabData(mTabEntities);
        msgListFragment = new MsgListFragment();
        noticeListFragment = new NoticeListFragment();
        fragmentList.add(msgListFragment);
        fragmentList.add(noticeListFragment);
        mViewPager.setOffscreenPageLimit(2);

        CommViewPagerAdapter adapter = new CommViewPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(sub_tab);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
