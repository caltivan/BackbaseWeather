package com.backbase.weather_challenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backbase.weather_challenge.BookmarkListFragment.OnListFragmentInteractionListener;
import com.backbase.weather_challenge.util.Bookmark;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Bookmark} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    private final List<Bookmark> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BookmarkRecyclerViewAdapter(List<Bookmark> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCityTextView.setText(mValues.get(position).city);
        holder.mCountryTextView.setText(mValues.get(position).country);
        holderViewGestureConfiguration(holder);
    }

    private void holderViewGestureConfiguration(final ViewHolder holder) {
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSelectBookmarkFromList(holder.mItem);
                }
            }
        });
        holder.mRemoveButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRemoveBookmarkFromList(holder.mItem,holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCityTextView;
        public final TextView mCountryTextView;
        public final ImageView mRemoveButtonView;

        public Bookmark mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCityTextView = (TextView) view.findViewById(R.id.bookmarkCity);
            mCountryTextView = (TextView) view.findViewById(R.id.bookmarkCountry);
            mRemoveButtonView = (ImageView) view.findViewById(R.id.removeBookmarkButton);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCountryTextView.getText() + "'";
        }
    }
}
