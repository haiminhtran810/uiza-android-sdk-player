package testlibuiza.sample.v3.slide;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import testlibuiza.R;
import uizacoresdk.view.UZPlayerView;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;

public class VDHView extends LinearLayout {
    private final String TAG = getClass().getSimpleName();
    private View headerView;
    private View bodyView;
    private ViewDragHelper mViewDragHelper;
    private int mAutoBackViewX;
    private int mAutoBackViewY;
    //private float mInitialMotionX;
    //private float mInitialMotionY;
    private int mDragRange;
    private float mDragOffset;
    private boolean isEnableAlpha = true;
    private boolean isEnableRevertMaxSize = true;
    private boolean isMinimizedAtLeastOneTime;//header view is scaled at least 1
    private int sizeWHeaderViewOriginal;
    private int sizeHHeaderViewOriginal;
    private int sizeWHeaderViewMin;
    private int sizeHHeaderViewMin;
    private int newSizeWHeaderView;
    private int newSizeHHeaderView;
    private int mCenterY;
    private int mCenterX;
    private int screenW;
    private int screenH;

    public VDHView(@NonNull Context context) {
        this(context, null);
    }

    public VDHView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public interface Callback {
        public void onStateChange(State state);

        public void onPartChange(Part part);

        public void onViewPositionChanged(int left, int top, float dragOffset);

        public void onOverScroll(State state, Part part);
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mDetector = new GestureDetector(getContext(), new UizaGestureListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        screenW = LScreenUtil.getScreenWidth();
        screenH = LScreenUtil.getScreenHeight();
        headerView = findViewById(R.id.header_view);
        bodyView = findViewById(R.id.body_view);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                sizeWHeaderViewOriginal = headerView.getMeasuredWidth();
                sizeHHeaderViewOriginal = headerView.getMeasuredHeight();
                sizeWHeaderViewMin = sizeWHeaderViewOriginal / 2;
                sizeHHeaderViewMin = sizeHHeaderViewOriginal / 2;
                LLog.d(TAG, "fuck size original: " + sizeWHeaderViewOriginal + "x" + sizeHHeaderViewOriginal + " -> " + sizeWHeaderViewMin + "x" + sizeHHeaderViewMin);
            }
        });
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (mDragOffset == (float) top / mDragRange) {
                return;
            } else {
                mDragOffset = (float) top / mDragRange;
            }
            if (mDragOffset > 1) {
                mDragOffset = 1;
            }
            if (mDragOffset < 0) {
                mDragOffset = 0;
            }
            if (callback != null) {
                callback.onViewPositionChanged(left, top, mDragOffset);
            }

            int x = 0;
            int y = headerView.getHeight() + top;
            bodyView.layout(x, y, x + bodyView.getMeasuredWidth(), y + bodyView.getMeasuredHeight());
            if (isEnableAlpha) {
                bodyView.setAlpha(1 - mDragOffset / 2);
            } else {
                if (bodyView.getAlpha() != 1f) {
                    bodyView.setAlpha(1f);
                }
            }

            if (isMinimizedAtLeastOneTime) {
                if (isEnableRevertMaxSize) {
                    headerView.setPivotX(headerView.getWidth() / 2f);
                    headerView.setPivotY(headerView.getHeight());
                    headerView.setScaleX(1 - mDragOffset / 2);
                    headerView.setScaleY(1 - mDragOffset / 2);
                } else {

                }
            } else {
                headerView.setPivotX(headerView.getWidth() / 2f);
                headerView.setPivotY(headerView.getHeight());
                headerView.setScaleX(1 - mDragOffset / 2);
                headerView.setScaleY(1 - mDragOffset / 2);
            }

            newSizeWHeaderView = (int) (sizeWHeaderViewOriginal * headerView.getScaleX());
            newSizeHHeaderView = (int) (sizeHHeaderViewOriginal * headerView.getScaleY());

            mCenterX = left + sizeWHeaderViewOriginal / 2;
            mCenterY = top + newSizeHHeaderView / 2 + sizeHHeaderViewOriginal - newSizeHHeaderView;

            //int posX = centerX - newSizeWHeaderView / 2;
            //int posY = centerY - newSizeHHeaderView / 2;
            //LLog.d(TAG, "onViewPositionChanged left: " + left + ", top: " + top + ", mDragOffset: " + mDragOffset + " => newSizeW " + newSizeWHeaderView + "x" + newSizeHHeaderView + "=> centerX: " + centerX + ", centerY: " + centerY + ", posX: " + posX + ", posY: " + posY);
            LLog.d(TAG, "onViewPositionChanged left: " + left + ", top: " + top + ", mDragOffset: " + mDragOffset + " => newSizeW " + newSizeWHeaderView + "x" + newSizeHHeaderView + "=> mCenterX: " + mCenterX + ", mCenterY: " + mCenterY);

            if (mDragOffset == 0) {
                //top_left, top, top_right
                if (left <= -headerView.getWidth() / 2) {
                    changeState(State.TOP_LEFT);
                } else if (left >= headerView.getWidth() / 2) {
                    changeState(State.TOP_RIGHT);
                } else {
                    changeState(State.TOP);
                }
            } else if (mDragOffset == 1) {
                //bottom_left, bottom, bottom_right
                if (left <= -headerView.getWidth() / 2) {
                    changeState(State.BOTTOM_LEFT);
                } else if (left >= headerView.getWidth() / 2) {
                    changeState(State.BOTTOM_RIGHT);
                } else {
                    changeState(State.BOTTOM);
                }
                isMinimizedAtLeastOneTime = true;
            } else {
                //mid_left, mid, mid_right
                if (left <= -headerView.getWidth() / 2) {
                    changeState(State.MID_LEFT);
                } else if (left >= headerView.getWidth() / 2) {
                    changeState(State.MID_RIGHT);
                } else {
                    changeState(State.MID);
                }
            }
            if (mCenterY < screenH / 2) {
                if (mCenterX < screenW / 2) {
                    changePart(Part.TOP_LEFT);
                } else {
                    changePart(Part.TOP_RIGHT);
                }
            } else {
                if (mCenterX < screenW / 2) {
                    changePart(Part.BOTTOM_LEFT);
                } else {
                    changePart(Part.BOTTOM_RIGHT);
                }
            }
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return headerView == child;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int minY;
            if (isEnableRevertMaxSize) {
                minY = -child.getHeight() / 2;
            } else {
                minY = -sizeHHeaderViewMin * 3 / 2;
            }
            float scaledY = child.getScaleY();
            int sizeHScaled = (int) (scaledY * child.getHeight());
            int maxY = getHeight() - sizeHScaled * 3 / 2;
            //LLog.d(TAG, "clampViewPositionVertical getHeight:" + getHeight() + ", scaledY:" + scaledY + ", sizeHScaled: " + sizeHScaled + ", top:" + top + ", minY: " + minY + ", maxY: " + maxY);
            if (top <= minY) {
                return minY;
            } else if (top > maxY) {
                return maxY;
            } else {
                return top;
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int minX = -child.getWidth() / 2;
            int maxX = getWidth() - child.getWidth() / 2;
            if (left <= minX) {
                return minX;
            } else if (left > maxX) {
                return maxX;
            } else {
                return left;
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == headerView) {
                mViewDragHelper.settleCapturedViewAt(mAutoBackViewX, mAutoBackViewY);
            }
            invalidate();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
        }
    };

    private void changeState(State newState) {
        if (state != newState) {
            state = newState;
            LLog.d(TAG, "changeState: " + newState);
            if (callback != null) {
                callback.onStateChange(state);
            }
        }
    }

    private void changePart(Part newPart) {
        if (part != newPart) {
            part = newPart;
            LLog.d(TAG, "changePart: " + part);
            if (callback != null) {
                callback.onPartChange(part);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LLog.d(TAG, "fuck onTouchEvent isEnableSlide: " + isEnableSlide);
        if (isEnableSlide) {
            mViewDragHelper.processTouchEvent(event);
        } else {
            mViewDragHelper.cancel();
        }
        //mViewDragHelper.processTouchEvent(event);
        mDetector.onTouchEvent(event);
        final float x = event.getX();
        final float y = event.getY();
        boolean isViewUnder = mViewDragHelper.isViewUnder(headerView, (int) x, (int) y);
        //LLog.d(TAG, "onTouchEvent isViewUnder: " + isViewUnder);
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            /*case MotionEvent.ACTION_DOWN: {
                LLog.d(TAG, "onTouchEvent ACTION_DOWN touch:" + x + " x " + y);
                LLog.d(TAG, "onTouchEvent ACTION_DOWN isViewUnder:" + isViewUnder);
                break;
            }*/
            case MotionEvent.ACTION_UP: {
                if (state == null) {
                    break;
                }
                LLog.d(TAG, "onTouchEvent ACTION_UP state:" + state.name() + ", mCenterX: " + mCenterX);
                if (state == State.TOP_LEFT || state == State.TOP_RIGHT || state == State.BOTTOM_LEFT || state == State.BOTTOM_RIGHT) {
                    LLog.d(TAG, "destroy state: " + state.name());
                    if (callback != null) {
                        callback.onOverScroll(state, part);
                    }
                } else {
                    if (part == Part.BOTTOM_LEFT) {
                        minimizeBottomLeft();
                    } else if (part == Part.BOTTOM_RIGHT) {
                        minimizeBottomRight();
                    } else if (part == Part.TOP_LEFT) {
                        if (isEnableRevertMaxSize) {
                            maximize();
                        } else {
                            if (isMinimizedAtLeastOneTime) {
                                minimizeTopLeft();
                            }
                        }
                    } else if (part == Part.TOP_RIGHT) {
                        if (isEnableRevertMaxSize) {
                            maximize();
                        } else {
                            if (isMinimizedAtLeastOneTime) {
                                minimizeTopRight();
                            }
                        }
                    }
                }
                break;
            }
        }
        return isViewUnder;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mDragRange = getHeight() - headerView.getHeight();
        mAutoBackViewX = headerView.getLeft();
        mAutoBackViewY = headerView.getTop();
        //LLog.d(TAG, "onLayout l:" + l + ", t:" + t + ", r:" + r + ", b:" + b + ", mAutoBackViewX: " + mAutoBackViewX + ", mAutoBackViewY: " + mAutoBackViewY);
    }

    public enum State {TOP, TOP_LEFT, TOP_RIGHT, BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, MID, MID_LEFT, MID_RIGHT, NULL}

    public enum Part {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT}

    private State state = State.NULL;
    private Part part;

    public State getState() {
        return state;
    }

    public Part getPart() {
        return part;
    }

    public void maximize() {
        if (isEnableRevertMaxSize) {
            smoothSlideTo(0, 0);
        } else {
            Log.e(TAG, "Error: cannot maximize because isEnableRevertMaxSize is true");
        }
    }

    public void minimizeBottomLeft() {
        int posX = getWidth() - sizeWHeaderViewOriginal - sizeWHeaderViewMin / 2;
        int posY = getHeight() - sizeHHeaderViewOriginal;
        //LLog.d(TAG, "minimize " + posX + "x" + posY);
        smoothSlideTo(posX, posY);
    }

    public void minimizeBottomRight() {
        int posX = getWidth() - sizeWHeaderViewOriginal + sizeWHeaderViewMin / 2;
        int posY = getHeight() - sizeHHeaderViewOriginal;
        //LLog.d(TAG, "minimize " + posX + "x" + posY);
        smoothSlideTo(posX, posY);
    }

    public void minimizeTopRight() {
        if (isEnableRevertMaxSize) {
            Log.e(TAG, "Error: cannot minimizeTopRight because isEnableRevertMaxSize is true");
            return;
        }
        if (!isMinimizedAtLeastOneTime) {
            Log.e(TAG, "Error: cannot minimizeTopRight because isMinimizedAtLeastOneTime is false. This function only works if the header view is scrolled BOTTOM");
            return;
        }
        int posX = screenW - sizeWHeaderViewMin * 3 / 2;
        int posY = -sizeHHeaderViewMin;
        //LLog.d(TAG, "minimizeTopRight " + posX + "x" + posY);
        smoothSlideTo(posX, posY);
    }

    public void minimizeTopLeft() {
        if (isEnableRevertMaxSize) {
            Log.e(TAG, "Error: cannot minimizeTopRight because isEnableRevertMaxSize is true");
            return;
        }
        if (!isMinimizedAtLeastOneTime) {
            Log.e(TAG, "Error: cannot minimizeTopRight because isMinimizedAtLeastOneTime is false. This function only works if the header view is scrolled BOTTOM");
            return;
        }
        int posX = -sizeWHeaderViewMin / 2;
        int posY = -sizeHHeaderViewMin;
        //LLog.d(TAG, "minimizeTopRight " + posX + "x" + posY);
        smoothSlideTo(posX, posY);
    }

    public void smoothSlideTo(int positionX, int positionY) {
        if (mViewDragHelper.smoothSlideViewTo(headerView, positionX, positionY)) {
            ViewCompat.postInvalidateOnAnimation(this);
            postInvalidate();
        }
    }

    public boolean isMinimizedAtLeastOneTime() {
        return isMinimizedAtLeastOneTime;
    }

    public boolean isEnableAlpha() {
        return isEnableAlpha;
    }

    public void setEnableAlpha(boolean enableAlpha) {
        isEnableAlpha = enableAlpha;
    }

    public boolean isEnableRevertMaxSize() {
        return isEnableRevertMaxSize;
    }

    public void setEnableRevertMaxSize(boolean enableRevertMaxSize) {
        isEnableRevertMaxSize = enableRevertMaxSize;
    }

    public void toggleShowHideHeaderView() {
        if (headerView.getVisibility() == VISIBLE) {
            headerView.setVisibility(INVISIBLE);
        } else {
            headerView.setVisibility(VISIBLE);
        }
    }

    public void toggleShowHideBodyView() {
        if (bodyView.getVisibility() == VISIBLE) {
            bodyView.setVisibility(INVISIBLE);
        } else {
            bodyView.setVisibility(VISIBLE);
        }
    }

    public void onPause() {
        if (!isEnableRevertMaxSize) {
            minimizeBottomRight();
            headerView.setVisibility(INVISIBLE);
            bodyView.setVisibility(INVISIBLE);
        }
    }

    public void dissappear() {
        smoothSlideTo(screenW, screenH);
    }

    public void appear() {
        minimizeBottomRight();
    }

    private boolean isEnableSlide;

    public void setEnableSlide(boolean isEnableSlide) {
        this.isEnableSlide = isEnableSlide;
    }

    private GestureDetector mDetector;
    private UZPlayerView.OnTouchEvent onTouchEvent;

    public void setOnTouchEvent(UZPlayerView.OnTouchEvent onTouchEvent) {
        this.onTouchEvent = onTouchEvent;
    }

    private class UizaGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            //LLog.d(TAG, "onDown");
            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            LLog.d(TAG, "onSingleTapConfirmed " + e.getX() + " - " + e.getY());
            if (onTouchEvent != null) {
                onTouchEvent.onSingleTapConfirmed(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LLog.d(TAG, "onLongPress " + e.getX() + " - " + e.getY());
            if (onTouchEvent != null) {
                onTouchEvent.onLongPress(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LLog.d(TAG, "onDoubleTap " + e.getX() + " - " + e.getY());
            if (onTouchEvent != null) {
                onTouchEvent.onDoubleTap(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LLog.d(TAG, "onScroll");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LLog.d(TAG, "onFling");
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            //LLog.d(TAG, "onSwipeRight");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeRight();
                            }
                        } else {
                            //LLog.d(TAG, "onSwipeLeft");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeLeft();
                            }
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //LLog.d(TAG, "onSwipeBottom");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeBottom();
                            }
                        } else {
                            //LLog.d(TAG, "onSwipeTop");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeTop();
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return true;
        }
    }
}
