package com.example.nagato.mobiledeveloper.widget.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.nagato.mobiledeveloper.adapter.ArticleAdapter;
import com.example.nagato.mobiledeveloper.utils.CommonUtils;
import com.example.nagato.mobiledeveloper.utils.LogUtils;

import io.realm.Realm;

/**
 * Created by nagato on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {
    protected Realm mrealm;
    protected LocalBroadcastManager mLocalBroadcastManager;
    protected  String mColumn;

    protected View mrootView;
    public RecyclerView mrecyclerView;
    protected RecyclerView.Adapter madapter;
    protected SwipeRefreshLayout mrefreshLayout;

    public int mLoadingState= IS_WAITING_DATA;
    public final static int IS_WAITING_DATA =0;
    public final static int IS_LOADING_DATA=1;
    public final static int NO_MORE_NEW_DATA=1;
    public final static int NO_MORE_PAST_DATA =2;
    public final static int NO_MORE_DATA =3;

/////创建时打开数据库////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mrealm=Realm.getDefaultInstance();
    }

////////////////////主布局和滑动控件加载相关方法////////////
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mrootView =getrootView();
        mrefreshLayout=getrefreshLayout();
        mrecyclerView=getrecyclerView();
        mrecyclerView.setLayoutManager(getLayoutmanager());
        madapter=getadapter();
        mrecyclerView.setAdapter(madapter);
        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                LogUtils.d("lastvisblepos="+lastVisiblePos+";max is"+madapter.getItemCount());
                if (mLoadingState == IS_WAITING_DATA && dy > 0&& lastVisiblePos >=madapter.getItemCount()-4 ) {
                    {
                        fetchPast();
                        LogUtils.d(mColumn+":scroll to fetchpast");
                    }
                }
            }
        });
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        return mrootView;
    }
    public RecyclerView.LayoutManager getLayoutmanager(){
        return new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);
    }

    public abstract View getrootView();
    public abstract SwipeRefreshLayout getrefreshLayout();
    public abstract RecyclerView getrecyclerView();
    public abstract ArticleAdapter getadapter();

    public abstract void fetchPast();
//////////////////////设置refresh动画效果和数据更新////////////////////////////////////
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mrefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNew();
                LogUtils.d("use refreshlistener to fetch new");
            }
        };
        mrefreshLayout.setOnRefreshListener(listener);
        if (savedInstanceState == null)
            listener.onRefresh();
    }
    public abstract void fetchNew();


    @Override
    public void onDestroy() {
        super.onDestroy();
        mrealm.removeAllChangeListeners();
        mrealm.close();
    }

    ////////////////////////////其他通用方法///////////////
    public void setRefreshLayout(final boolean state) {
        if (mrefreshLayout == null)
            return;
        LogUtils.d("refresh layout changed:"+state);
        CommonUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mrefreshLayout.setRefreshing(state);
            }
        });
    }

    public int getmLoadingState() {
        return mLoadingState;
    }

    public void updateData() {
        if (null == madapter)
            return;
        madapter.notifyDataSetChanged();
    }



}
