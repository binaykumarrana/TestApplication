package demo.taskapplication.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import demo.taskapplication.R;

/**
 * Created by Binay on 26/04/17.
 */
public class WordCountAdapter extends RecyclerView.Adapter<WordCountAdapter.CustomViewHolder> {

    private final Context mContext;
    private List<Item> mItemList;

    /**
     * The type Custom view holder.
     */
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;

        private CustomViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.wordTextView);
        }
    }

    /**
     * Instantiates a new Word count adapter.
     *
     * @param context   the context
     * @param mItemList the m item list
     */
    public WordCountAdapter(Context context, List<Item> mItemList) {
        mContext = context;
        this.mItemList = mItemList;
    }

    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_word_count_adapter_item, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.mTitle.setText(mItemList.get(position).key + " : " + mItemList.get(position).value);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
