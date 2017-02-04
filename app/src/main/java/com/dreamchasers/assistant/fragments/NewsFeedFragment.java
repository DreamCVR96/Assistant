package com.dreamchasers.assistant.fragments;



import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.adapters.AlbumAdapter;
import com.dreamchasers.assistant.adapters.ComplexRecyclerViewAdapter;
import com.dreamchasers.assistant.models.Album;
import com.dreamchasers.assistant.models.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macpro on 11/28/16.
 */

public class NewsFeedFragment extends Fragment {


    private int argsType;
    private String mVoiceInput = "";
    private AlbumAdapter adapter;
    private List<Album> albumList;
    @BindView(R.id.recycler_view_main_feed) RecyclerView recyclerViewMain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsfeed, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
     /*   albumList = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), albumList); */

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewMain.setLayoutManager(mLayoutManager);

       // recyclerViewMain.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        //recyclerViewMain.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewMain.setAdapter(adapter);
        bindDataToAdapter();
        initCollapsingToolbar();
     //   prepareAlbums();

     /*   try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        } */

        argsType = this.getArguments().getInt("TYPE");
        if (argsType == MainView.HOME) {

        //    imageView.setImageResource(R.drawable.ic_notifications_black_empty);
            Log.v("FRAGMENT GET ITEM", "MAIN/O");

           recyclerViewMain.setVisibility(View.VISIBLE);
          //  linearLayout.setVisibility(View.GONE);
           // relativeLayout.setVisibility(View.VISIBLE);

        }


    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Album a = new Album("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Album("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Album("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Album("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Album("Hello", 11, covers[8]);
        albumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                }
                else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private ArrayList<Object> getSampleArrayList() {
        ArrayList<Object> items = new ArrayList<>();
        items.add("image");
        items.add(new Album("I Need a Doctor", 1, R.drawable.album1));
        items.add(new Album("I Need a Doctor", 2, R.drawable.album5));
        items.add(new Album("I Need a Doctor", 3, R.drawable.album4));
        items.add(new Album("I Need a Doctor", 4, R.drawable.album3));


        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Album a = new Album("True Romance", 13, covers[0]);
        items.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        items.add(a);

        a = new Album("Maroon 5", 11, covers[2]);
        items.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        items.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        items.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        items.add(a);

        a = new Album("Loud", 11, covers[6]);
        items.add(a);

        a = new Album("Legend", 14, covers[7]);
        items.add(a);

        a = new Album("Hello", 11, covers[8]);
        items.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        items.add(a);


        return items;
    }

    private void bindDataToAdapter() {
        // Bind adapter to recycler view object
        recyclerViewMain.setAdapter(new ComplexRecyclerViewAdapter(getSampleArrayList()));

    }
}
