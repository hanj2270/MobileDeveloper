package com.example.nagato.mobiledeveloper.net;

import com.example.nagato.mobiledeveloper.data.Article;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nagato on 2016/9/18.
 */
public interface APIEndPoint {
    String BASE_URL = "http://gank.io/api/";
//  String BASE_URL="http://news-at.zhihu.com/";



    @GET("data/{column}/{count}/1")
    Call<data<List<Article>>>fetchLatestArticle(@Path("column")String column, @Path("count") int count);

    @GET("day/{date}")
    Call<data<Android>> dayAndroids(@Path("date") String date);

    @GET("day/{date}")
    Call<data<IOS>> dayIOSs(@Path("date") String date);

    @GET("day/{date}")
    Call<data<Webs>> dayWebs(@Path("date") String date);

    @GET("day/{date}")
    Call<data<Excited>> dayFuns(@Path("date") String date);

    @GET("day/{date}")
    Call<data<Apps>> dayApps(@Path("date") String date);

    @GET("day/{date}")
    Call<data<Others>> dayOthers(@Path("date") String date);

    class data<T>{
        public boolean error;
        public T results;
    }



        class Android{
            @SerializedName("Android")
            public List<Article> articles;
        }

        class IOS{
            @SerializedName("iOS")
            public List<Article> articles;
        }

        class Excited{
            @SerializedName("瞎推荐")
            public List<Article> articles;
        }

        class Others{
            @SerializedName("扩展资源")
            public List<Article> articles;
        }

        class Webs{
            @SerializedName("前端")
            public List<Article> articles;
        }

        class Apps{
            @SerializedName("App")
            public List<Article> articles;
        }

    @GET("api/4/news/latest")
    Call<testdata>apitest();
    class testdata{
        public String date;
        public List<App> stories;
    }

    @GET("day/2016/09/17")
    Call<data<Android>>fetchtest();
    class testResult{
        public boolean error;
        List<App> results;
    }
    class App{
        public String who;
    }


}
