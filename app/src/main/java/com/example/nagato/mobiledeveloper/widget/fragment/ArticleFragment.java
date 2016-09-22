package com.example.nagato.mobiledeveloper.widget.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nagato.mobiledeveloper.R;
import com.example.nagato.mobiledeveloper.adapter.ArticleAdapter;
import com.example.nagato.mobiledeveloper.service.ArticleFetchService;
import com.example.nagato.mobiledeveloper.utils.CommonUtils;
import com.example.nagato.mobiledeveloper.utils.LogUtils;

/**
 * Created by nagato on 2016/9/13.
 */
public class ArticleFragment extends BaseFragment {
    private fetchResultReceiver fetchResultReceiver;
    public static ArticleFragment newInstance(String column) {
        
        Bundle args = new Bundle();
        args.putString("column",column);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColumn=getArguments().getString("column");
        fetchResultReceiver=new fetchResultReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("Fragment onResume"+mColumn);
        mLocalBroadcastManager.registerReceiver(fetchResultReceiver, new IntentFilter("actionSendFetchResult"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("Fragment onPause"+mColumn);
        mLocalBroadcastManager.unregisterReceiver(fetchResultReceiver);
        mLoadingState=IS_WAITING_DATA;
        setRefreshLayout(false);
    }


    @Override
    public View getrootView() {return CommonUtils.getview(R.layout.article_fragment);}
    @Override
    public SwipeRefreshLayout getrefreshLayout() {return (SwipeRefreshLayout) mrootView.findViewById(R.id.article_refresh_layout);}
    @Override
    public RecyclerView getrecyclerView() {return (RecyclerView) mrootView.findViewById(R.id.article_recyclerview);}

    @Override
    public ArticleAdapter getadapter() {
        final ArticleAdapter adapter=new ArticleAdapter(mrealm,mColumn,getActivity());
        return adapter;
    }
//////////////////////////////////////////////数据加载方法//////////////////////////////
    @Override
    public void fetchPast() {
       if(mLoadingState==IS_LOADING_DATA)
           return;
        mLoadingState=IS_LOADING_DATA;
        setRefreshLayout(true);
        Intent intent=new Intent(getActivity(), ArticleFetchService.class);
        intent.setAction("fetchPast")
                .putExtra("column",mColumn);
        getActivity().startService(intent);

    }

    @Override
    public void fetchNew() {
        if(mLoadingState==IS_LOADING_DATA)
            return;
        mLoadingState=IS_LOADING_DATA;
        setRefreshLayout(true);
        Intent intent=new Intent(getActivity(), ArticleFetchService.class);
        intent.setAction("fetchNew")
                .putExtra("column",mColumn);
        getActivity().startService(intent);
    }



    public class fetchResultReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            final int fetchedcount = intent.getIntExtra("fetchcount", 0);
            final String column = intent.getStringExtra("column");
            final String exception = intent.getStringExtra("exception");
            final String trigger = intent.getStringExtra("trigger");
            LogUtils.d("column:"+column+";fetched count:" + fetchedcount + ";trigger" + trigger);
            if (column.equals(mColumn)) {
                if (fetchedcount == 0) {
                    if (exception != null) {
                        CommonUtils.makeSnackBar(mrefreshLayout, exception, Snackbar.LENGTH_SHORT);
                        mLoadingState = IS_WAITING_DATA;
                        setRefreshLayout(false);
                        return;
                    } else if (trigger.equals("fetchPast")) {
                        CommonUtils.makeSnackBar(mrefreshLayout, getString(R.string.No_more_fetch_data), Snackbar.LENGTH_SHORT);
                        mLoadingState = NO_MORE_PAST_DATA;
                        setRefreshLayout(false);
                        return;
                    }
                } else {
                    ((ArticleAdapter)madapter).updateFetchedData(fetchedcount,trigger.equals("fetchPast"));
                }
            }
            if(mLoadingState==IS_LOADING_DATA)
            mLoadingState= IS_WAITING_DATA;
            setRefreshLayout(false);
        }
    }
}
