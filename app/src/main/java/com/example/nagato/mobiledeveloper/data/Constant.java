package com.example.nagato.mobiledeveloper.data;

import com.example.nagato.mobiledeveloper.R;

/**
 * Created by nagato on 2016/9/13.
 */
public interface Constant {

        enum Column {
            BEAUTY("BEAUTY", "福利", R.string.beauty, R.id.nav_beauty),
            ANDROID("ANDROID", "Android", R.string.android, R.id.nav_android),
            IOS("IOS", "iOS", R.string.ios, R.id.nav_ios),
            JS("JAVASCRIPT", "前端", R.string.js, R.id.nav_web),
            APP("APP", "App", R.string.app, R.id.nav_app),
            EXCITED("EXCITED", "瞎推荐", R.string.excited, R.id.nav_excited),
            OTHERS("OTHERS", "拓展资源", R.string.others, R.id.nav_others),
            FAVORITE("FAVORITE", "Collections", R.string.favorite, R.id.nav_favorite);
            //SEARCH_RESULTS("SEARCH_RESULTS", "search_results", R.string.nav_search, 0);

            private final String id;
            private final String apiName;
            private final int strId;
            private final int resId;

            Column(String id, String apiName, int strId, int resId) {
                this.id = id;
                this.apiName = apiName;
                this.strId = strId;
                this.resId = resId;
            }

            @Override
            public String toString() {
                return id;
            }

            public String getId() {
                return id;
            }

            public String getApiName() {
                return apiName;
            }

            public int getStrId() {
                return strId;
            }

            public int getResId() {
                return resId;
            }
            }

}
