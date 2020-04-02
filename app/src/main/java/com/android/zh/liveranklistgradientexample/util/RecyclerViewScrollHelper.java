package com.android.zh.liveranklistgradientexample.util;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : lizhi - hezihao
 * e-mail : hezihao@lizhi.fm
 * time   : 2020/04/02
 * desc   :滚动帮助类
 */
public class RecyclerViewScrollHelper {
    /**
     * 第一次进入界面时也会回调滚动，所以当手动滚动再监听
     */
    private boolean isNotFirst = false;
    /**
     * 列表控件
     */
    private RecyclerView scrollingView;
    /**
     * 回调
     */
    private Callback callback;

    public void attachRecyclerView(RecyclerView scrollingView, Callback callback) {
        this.scrollingView = scrollingView;
        this.callback = callback;
        setup();
    }

    private void setup() {
        scrollingView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isNotFirst = true;
                if (callback != null) {
                    //如果滚动到最后一行，RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                    if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                            !recyclerView.canScrollVertically(1)) {
                        callback.onScrolledToBottom();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (callback != null && isNotFirst) {
                    //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                    if (!recyclerView.canScrollVertically(-1)) {
                        callback.onScrolledToTop();
                    }
                    //下滑
                    if (dy < 0) {
                        callback.onScrolledToDown();
                    }
                    //上滑
                    if (dy > 0) {
                        callback.onScrolledToUp();
                    }
                }
            }
        });
    }

    public interface Callback {
        /**
         * 向下滚动
         */
        void onScrolledToDown();

        /**
         * 向上滚动
         */
        void onScrolledToUp();

        /**
         * 滚动到了顶部
         */
        void onScrolledToTop();

        /**
         * 滚动到了底部
         */
        void onScrolledToBottom();
    }

    public static class CallbackAdapter implements Callback {
        @Override
        public void onScrolledToDown() {
        }

        @Override
        public void onScrolledToUp() {
        }

        @Override
        public void onScrolledToTop() {
        }

        @Override
        public void onScrolledToBottom() {
        }
    }

    /**
     * 马上滚动到顶部
     */
    public void moveToTop() {
        if (scrollingView != null) {
            scrollingView.scrollToPosition(0);
        }
    }

    /**
     * 缓慢滚动到顶部
     */
    public void smoothMoveToTop() {
        if (scrollingView != null) {
            scrollingView.smoothScrollToPosition(0);
        }
    }
}