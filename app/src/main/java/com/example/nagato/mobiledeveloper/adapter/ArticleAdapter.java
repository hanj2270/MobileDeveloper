package com.example.nagato.mobiledeveloper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nagato.mobiledeveloper.R;
import com.example.nagato.mobiledeveloper.data.Article;
import com.example.nagato.mobiledeveloper.utils.DateUtils;
import com.example.nagato.mobiledeveloper.utils.LogUtils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nagato on 2016/9/13.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.articleViewHolder> {

    protected final String mColumn;
    protected final Context mContext;
    protected final Realm mRealm;
    protected int currentArticleNumber;
    public RealmResults<Article> articleData;

    public ArticleAdapter(Realm mRealm, String mColumn, Context mContext) {
        this.mRealm = mRealm;
        this.mColumn = mColumn;
        this.mContext = mContext;
        articleData=Article.queryall(mRealm,mColumn);
        currentArticleNumber=articleData.size();
    }



    @Override
    public articleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new articleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.article_fragment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(articleViewHolder holder, int position) {
        final Article article= articleData.get(position);
        if(article.getAuthor()==null||article.getAuthor().equals("null")||article.getAuthor().equals("")){
            holder.author.setText("佚名");
        }
        holder.author.setText(article.getAuthor());
        holder.title.setText(article.getTitle());
        holder.date.setText(DateUtils.format(article.getPublishedAt()));
        LogUtils.d("position:"+position+";author:"+article.getAuthor());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return articleData.size();
    }
//////////////////////////////////////////////item的holder定义////////////////
    public class articleViewHolder extends RecyclerView.ViewHolder{
        LinearLayout articleCardLayout;
        TextView title, author, date;
        ImageButton likeBtn;
        public articleViewHolder(View itemView) {
            super(itemView);
            articleCardLayout= (LinearLayout) itemView.findViewById(R.id.article_item_content);
            title= (TextView) itemView.findViewById(R.id.article__title);
            likeBtn= (ImageButton) itemView.findViewById(R.id.like_btn);
            date= (TextView) itemView.findViewById(R.id.article_date);
            author= (TextView) itemView.findViewById(R.id.article_author);

        }
    }
    /////////////////////////根据数据更新UI//////////////////
    public void updateFetchedData(int number,boolean isPastData){
        if(isPastData){
            notifyItemRangeInserted(currentArticleNumber,number);
        }else{
            notifyItemRangeInserted(0,number);
        }
        currentArticleNumber+=number;
    }
}
