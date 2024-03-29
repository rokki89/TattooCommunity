package com.fedor.pavel.tattoocommunity.adapters;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.listeners.OnLoadNexDataPartListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadDataRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected ArrayList<T> items = new ArrayList<>();

    protected ArrayList<Integer> headers = new ArrayList<>();

    protected ArrayList<Integer> footers = new ArrayList<>();

    protected OnLoadNexDataPartListener listener;

    public static final String LOG_TAG = "LoadDataRVAdapter";

    protected boolean isLoadingData = false;

    protected int dataLimit = 5;

    protected int offset = 0;

    protected int totalDataNumber;

    protected int numOfPages = 1;

    private boolean isItGrid;

    private int visibleThreshold = 1;

    protected boolean canLoadMore = true;


    public void addAllItems(List<T> items) {


        int start = headers.size() + this.items.size();

        this.items.addAll(items);

        notifyItemRangeInserted(start, items.size());

    }

    public void clearItems() {

        int numOfItems = items.size();

        items.clear();

        notifyItemRangeRemoved(headers.size(), numOfItems);
    }

    public void removeItem(int position) {

        items.remove(position);

        notifyItemRemoved(position);

    }

    public void removeFooter(@LayoutRes int viewId) {

        int position = footers.indexOf(viewId);

        if (position > -1) {

            footers.remove(position);

            notifyItemRemoved(getItemCount() - 1);

        }

    }

    public void addFooter(@LayoutRes int viewId) {

        if (viewId == R.layout.item_rv_footer_progress_bar) {

            int position = footers.indexOf(R.layout.item_rv_footer_progress_bar);

            if (position < 0) {

                footers.add(viewId);

                notifyItemInserted(getItemCount() - 1);

            }

            return;

        }

        footers.add(viewId);

    }

    public T getItem(int position) {
        return items.get(position);

    }

    public void setIsLoadingData(boolean isLoading) {

        this.isLoadingData = isLoading;

    }

    public int getDataLimit() {

        return dataLimit;
    }

    public boolean isLoadingData() {

        return isLoadingData;
    }

    public void setTotalDataNumber(int totalDataNumber) {

        this.totalDataNumber = totalDataNumber;
    }

    @Override
    public int getItemViewType(int position) {

        if (!headers.isEmpty() && position < headers.size()) {

            return ViewTyp.VIEW_TYPE_HEADER;

        } else if (!footers.isEmpty() && position >= headers.size() + items.size()) {

            return ViewTyp.VIEW_TYPE_FOOTER;

        } else if (isItGrid) {

            return ViewTyp.VIEW_TYPE_GRID;

        } else {
            return ViewTyp.VIEW_TYPE_ITEM;
        }

    }

    @Override
    public int getItemCount() {

        return headers.size() + items.size() + footers.size();

    }

    public int getOnlyItemCount() {

        return items.size();

    }

    public void loadDataWithPagination(RecyclerView rvParent, OnLoadNexDataPartListener listener) {

        this.listener = listener;
        isItGrid = false;
        if (rvParent.getLayoutManager() instanceof GridLayoutManager) {

            isItGrid = true;

            ((GridLayoutManager) rvParent.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case ViewTyp.VIEW_TYPE_HEADER:
                        case ViewTyp.VIEW_TYPE_FOOTER:
                            return 3;
                        case ViewTyp.VIEW_TYPE_ITEM:
                            return 1;
                        case ViewTyp.VIEW_TYPE_GRID:
                            return 1;
                    }

                    return 0;
                }
            });
        }

        rvParent.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                Log.d(LOG_TAG,"dy = "+dy);

                if (dy > 0) {
                    pagination(recyclerView.getLayoutManager());
                }

                super.onScrolled(recyclerView, dx, dy);
            }

        });


    }

    private int getLastCompleteVisibleItem(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {

            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

        } else if (layoutManager instanceof GridLayoutManager) {

            return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();

        } else {

            throw new IllegalArgumentException("Wrong Layout manager! Mast be only grid or linear layout manager");

        }

    }

    private void pagination(RecyclerView.LayoutManager manager) {


        int itemsCount = getItemCount();

        int lastVisibleItem = getLastCompleteVisibleItem(manager);

        canLoadMore = false;


        if ((!isLoadingData)
                && (lastVisibleItem + visibleThreshold >= itemsCount)
                && (getOnlyItemCount() < totalDataNumber)
                ) {


            addFooter(R.layout.item_rv_footer_progress_bar);

            canLoadMore = true;

            setIsLoadingData(true);

            notifyItemChanged(lastVisibleItem);

            numOfPages++;

            offset = itemsCount;

            if (numOfPages == 5) {
                ImageLoader.getInstance().clearMemoryCache();
                numOfPages = 1;
            }

            listener.getNextPart();

        }

        if (!isLoadingData) {
            removeFooter(R.layout.item_rv_footer_progress_bar);
        }

    }

    public class ViewTyp {

        public static final int VIEW_TYPE_HEADER = 1;

        public static final int VIEW_TYPE_ITEM = 2;

        public static final int VIEW_TYPE_FOOTER = 3;

        public static final int VIEW_TYPE_GRID = 4;

    }

}
