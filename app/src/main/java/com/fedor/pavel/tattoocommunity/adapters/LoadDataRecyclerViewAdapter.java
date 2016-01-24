package com.fedor.pavel.tattoocommunity.adapters;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
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

    protected int dataLimit = 10;

    protected int offset = 0;

    protected int totalDataNumber;

    protected int numOfPages = 1;


    public void addItem(T item) {

        items.add(item);


    }

    public void addAllItems(List<T> items) {

        int start = this.items.size();

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

    public void addFooter(@LayoutRes int viewId) {

        footers.add(viewId);

    }

    public void addHeader(@LayoutRes int viewId) {

        headers.add(viewId);

    }

    public void clearHeaders() {

        headers.clear();


    }

    public void clearFooters() {

        footers.clear();

    }

    public T getItem(int position){

       return items.get(position);

    }

    public void setIsLoadingData(boolean isLoading) {

        this.isLoadingData = isLoading;

    }

    public void setDataLimit(int dataLimit) {

        this.dataLimit = dataLimit;

    }

    public int getDataLimit() {
        return dataLimit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isLoadingData() {
        return isLoadingData;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public int getTotalDataNumber() {
        return totalDataNumber;
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

        if (rvParent.getLayoutManager() instanceof GridLayoutManager) {

            ((GridLayoutManager) rvParent.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case ViewTyp.VIEW_TYPE_HEADER:
                        case ViewTyp.VIEW_TYPE_FOOTER:
                            return 2;
                        case ViewTyp.VIEW_TYPE_ITEM:
                            return 1;

                    }

                    return 0;
                }
            });
        }

        rvParent.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                pagination(recyclerView.getLayoutManager());

            }
        });


    }

    private int getLastCompleteVisibleItem(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {

            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

        } else if (layoutManager instanceof GridLayoutManager) {

            return ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

        } else {

            throw new IllegalArgumentException("Wrong Layout manager! Mast be only grid or linear layout manager");

        }

    }

    private void pagination(RecyclerView.LayoutManager manager) {

        int itemsCount = getItemCount();

        int lastVisibleItem = getLastCompleteVisibleItem(manager);

        Log.d(LOG_TAG, "lastVisibleItem = " + lastVisibleItem);

        if ((!isLoadingData)
                && (lastVisibleItem == itemsCount - 1)
                && (getOnlyItemCount() < totalDataNumber)
                ) {

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

    }

    public class ViewTyp {

        public static final int VIEW_TYPE_HEADER = 1;

        public static final int VIEW_TYPE_ITEM = 2;

        public static final int VIEW_TYPE_FOOTER = 3;


    }

}
