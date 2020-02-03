package android.app.mobilebattery.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PageScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public PageScrollListener(LinearLayoutManager layoutManager){

        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstItemVisiblePosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()){
            if ((visibleItemCount + firstItemVisiblePosition) >= totalItemCount
                    && firstItemVisiblePosition >=0
                    && totalItemCount >= getPageCount()){
                loadMoreItem();
            }
        }
    }

    protected abstract void loadMoreItem();

    public abstract int getPageCount();

    public abstract Boolean isLoading();

    public abstract Boolean isLastPage();
}
