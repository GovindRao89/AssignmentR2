package com.app.assignmentr2.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.assignmentr2.R;
import com.app.assignmentr2.model.Words;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Govind on 28-02-2016.
 */
public class WordRecyclerAdapter extends RecyclerView.Adapter<WordRecyclerAdapter.WordsViewHolder> {
    private List<Words> wordsList;
    private Context mContext;

    public WordRecyclerAdapter(Context context, List<Words> wordsList) {
        this.wordsList = wordsList;
        this.mContext = context;
    }

    @Override
    public WordsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        WordsViewHolder viewHolder = new WordsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WordsViewHolder wordsViewHolder, int i) {
        Words words = wordsList.get(i);
        String imageUrl;
        //Handle click event on both title and image click
        wordsViewHolder.textViewTitle.setOnClickListener(clickListener);
        wordsViewHolder.textViewMsg.setOnClickListener(clickListener);
        wordsViewHolder.imageView.setOnClickListener(clickListener);

        wordsViewHolder.textViewMsg.setTag(wordsViewHolder);
        wordsViewHolder.textViewTitle.setTag(wordsViewHolder);
        wordsViewHolder.imageView.setTag(wordsViewHolder);

        if (!TextUtils.isEmpty(words.getUri())) {
            //Download image using picasso library
            Picasso.with(mContext).load(words.getUri())
                    .error(R.mipmap.error)
                    .placeholder(R.mipmap.placeholder)
                    .into(wordsViewHolder.imageView);
        }

        //Setting text view title
        wordsViewHolder.textViewTitle.setText(words.getWord());
        wordsViewHolder.textViewMsg.setText(words.getMeaning());
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WordsViewHolder holder = (WordsViewHolder) view.getTag();
            int position = holder.getPosition();

            Words words = wordsList.get(position);
            Toast.makeText(mContext, words.getWord(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int getItemCount() {
        return (null != wordsList ? wordsList.size() : 0);
    }


    public class WordsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textViewMsg;
        protected TextView textViewTitle;

        public WordsViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textViewTitle = (TextView) view.findViewById(R.id.word_title);
            this.textViewMsg = (TextView) view.findViewById(R.id.word_message);
        }
    }
}
