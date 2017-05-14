package com.backbase.weather_challenge;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backbase.weather_challenge.util.Bookmark;
import com.backbase.weather_challenge.util.BookmarkController;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookmarkListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Context myContext;
    private BookmarkController bookmarkStoreController;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookmarkListFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookmarkListFragment newInstance(int columnCount) {
        BookmarkListFragment fragment = new BookmarkListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
        myContext = this.getContext();
        bookmarkStoreController = new BookmarkController(myContext);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            loadBookmarkListTask();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadBookmarkListTask() {
        new AsyncTask<RecyclerView, Void, BookmarkRecyclerViewAdapter>() {

            RecyclerView view;

            @Override
            protected BookmarkRecyclerViewAdapter doInBackground(RecyclerView... params) {
                view = params[0];
                // ConnectionHelper connectionHelper = new ConnectionHelper();
                // String serverResponse = connectionHelper.getWeatherServiceInfo(0, 0, "");
                ArrayList<Bookmark> bookmarkList = bookmarkStoreController.getBookmarks();
                BookmarkRecyclerViewAdapter bookmarkAdapter = new BookmarkRecyclerViewAdapter(bookmarkList, mListener);
                return bookmarkAdapter;
            }

            @Override
            protected void onPostExecute(BookmarkRecyclerViewAdapter adapter) {
                super.onPostExecute(adapter);
                view.setAdapter(adapter);
            }

        }.execute(recyclerView);
    }

    public void removeBookmarkFromList(int index) {
        bookmarkStoreController.removeBookmark(index);
        loadBookmarkListTask();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSelectBookmarkFromList(Bookmark bookmark);

        void onRemoveBookmarkFromList(Bookmark bookmark, int index);
    }
}
