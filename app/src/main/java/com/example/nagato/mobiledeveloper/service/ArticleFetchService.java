package com.example.nagato.mobiledeveloper.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nagato.mobiledeveloper.R;
import com.example.nagato.mobiledeveloper.data.Article;
import com.example.nagato.mobiledeveloper.data.Constant;
import com.example.nagato.mobiledeveloper.net.APIEndPoint;
import com.example.nagato.mobiledeveloper.net.APIService;
import com.example.nagato.mobiledeveloper.utils.DateUtils;
import com.example.nagato.mobiledeveloper.utils.LogUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import io.realm.internal.IOException;

/**
 * Created by nagato on 2016/9/18.
 */
public class ArticleFetchService extends IntentService {
    private String column;
    private String mException=null;
    private LocalBroadcastManager mbroadcastManager;
    @Override
    public void onCreate() {
        super.onCreate();
        mbroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    public ArticleFetchService() {
        super("ArticleFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        column=intent.getExtras().getString("column");
        LogUtils.d("articleService:onHandleIntent"+column+"trigger"+intent.getAction());
        Realm realm=Realm.getDefaultInstance();
        RealmResults<Article> currentData=Article.queryall(realm,column);
        int FETCH_COUNT=0;
        try {
            if(currentData.isEmpty()){
                LogUtils.d(column+":no data found,fetchAll");
                FETCH_COUNT=fetchAll(realm);
            } else if(intent.getAction().equals("fetchNew")){
                LogUtils.d(column+":data found,fetch new data start");
                List<String> dateAfter=DateUtils.getDateAfter(currentData.first().getPublishedAt());
                FETCH_COUNT=fetch(realm,dateAfter);
            }else if(intent.getAction().equals("fetchPast")){
                LogUtils.d(column+":data found,fetch Past data start");
                List<String> dateBefore=DateUtils.getDateBefore(currentData.last().getPublishedAt(),10);
                FETCH_COUNT=fetch(realm,dateBefore);
            }
        } catch (SocketTimeoutException e) {
            mException ="time out expection";
            e.printStackTrace();
        }
        catch (UnknownHostException e){
            mException=getString(R.string.NoHostException_string);
        }catch(IOException e){
            mException=getString(R.string.IOException_string);
        }catch(Exception e){
            e.printStackTrace();
        }
        realm.close();
        sendResult(intent,FETCH_COUNT);
    }
    public void sendResult(Intent intent,int fetchcount){
        LogUtils.d("fetch finish,fetched:"+fetchcount);
        Intent boardcast=new Intent("actionSendFetchResult");
        boardcast.putExtra("column",column);
        boardcast.putExtra("exception",mException);
        boardcast.putExtra("trigger",intent.getAction());
        boardcast.putExtra("fetchcount",fetchcount);
        mbroadcastManager.sendBroadcast(boardcast);
    }

    public int fetchAll(final Realm realm) throws java.io.IOException {

        APIEndPoint.data<List<Article>> data=APIService.getDefaultInstance().fetchLatestArticle(column,20).execute().body();
        LogUtils.d(data.error+":error data");
        if(data.error)
            { LogUtils.d("data error");
            return 0;}
        else{
            int n=data.results.size();
            for(int i=0;i<n;i++){
                if(!saveInDb(realm,data.results.get(i))){
                    LogUtils.d("can't save data:"+column+";data:"+i);
                    return i;}
            }
            return n;
        }
    }

    public int fetch(Realm realm,List<String> datelist)throws java.io.IOException{
        int fetchResult=0;
        if (column.equals(Constant.Column.ANDROID.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data<APIEndPoint.Android> stuffsResult = APIService.getDefaultInstance().fetchtest().execute().body();
// APIEndPoint.data<APIEndPoint.Android> stuffsResult = APIService.getDefaultInstance().dayAndroids(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results)
                    {LogUtils.d(column+":No Data fetched");
                        continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;
            }
            }
        } else if (column.equals(Constant.Column.IOS.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data<APIEndPoint.IOS> stuffsResult = APIService.getDefaultInstance().dayIOSs(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results.articles){
                    LogUtils.d(column+":No Data fetched");
                    continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;
                }
            }
        }else if(column.equals(Constant.Column.JS.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data<APIEndPoint.Webs> stuffsResult = APIService.getDefaultInstance().dayWebs(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results.articles)
                {LogUtils.d(column+":No Data fetched");
                    continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;
                }
            }
        } else if (column.equals(Constant.Column.APP.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data <APIEndPoint.Apps> stuffsResult = APIService.getDefaultInstance().dayApps(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results.articles)
                {LogUtils.d(column+":No Data fetched");
                    continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;

                }
            }
        } else if (column.equals(Constant.Column.EXCITED.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data<APIEndPoint.Excited> stuffsResult = APIService.getDefaultInstance().dayFuns(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results.articles)
                {LogUtils.d(column+":No Data fetched");
                    continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;
                }
            }
        } else if (column.equals(Constant.Column.OTHERS.getApiName())) {
            for (String date : datelist) {
                APIEndPoint.data<APIEndPoint.Others> stuffsResult = APIService.getDefaultInstance().dayOthers(date).execute().body();
                if (stuffsResult.error || null == stuffsResult.results || null == stuffsResult.results.articles)
                {LogUtils.d(column+":No Data fetched");
                    continue;}

                for (Article article : stuffsResult.results.articles) {
                    if (!saveInDb(realm, article))
                        return fetchResult;
                    LogUtils.d("already saved"+fetchResult);
                    fetchResult++;
                }
            }
        }
        return fetchResult;
    }







    //文件存入数据库，如果发现数据库内含有id相同数据，删除原有数据再次提交进行更新
    public boolean saveInDb(Realm realm,Article article){
        realm.beginTransaction();
        try {
            realm.copyToRealm(article);
        }catch (RealmPrimaryKeyConstraintException e){
            LogUtils.d("delete and update in db"+column);
            realm.where(Article.class)
                 .equalTo("id",article.getId())
                 .findFirst()
                 .deleteFromRealm();
            realm.commitTransaction();
            return true;
        }catch(Exception e){
            LogUtils.d("can't save data to realm");
            realm.cancelTransaction();
            return false;
        }
        realm.commitTransaction();
        return true;
    }
}
