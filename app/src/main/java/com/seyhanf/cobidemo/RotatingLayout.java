package com.seyhanf.cobidemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by seyhanf on 23/06/15.
 */
public class RotatingLayout extends FrameLayout {

    private static final float ALPHA_TRANSPARENT = 0.0f;
    private static final float TOTAL_ANGLE = 360.0f;

    private View rotatingBackground;
    private int angleBetweenChildren;
    private boolean isRotating;

    private AnimationUtil.OnAnimationCompletedListener onAnimationCompletedListener
            = new AnimationUtil.OnAnimationCompletedListener() {

        @Override
        public void onRotationCompeted() {
            isRotating = false;
        }

    };

    public RotatingLayout(Context context) {
        this(context, null);
    }

    public RotatingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //for studio tools
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {

            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RotatingLayout);
            angleBetweenChildren = a.getInt(
                    R.styleable.RotatingLayout_angleBetweenChildren,
                    getResources().getInteger(R.integer.rotating_layout_angle_between_children));
            a.recycle();
        } else {
            angleBetweenChildren = getResources().getInteger(R.integer.rotating_layout_angle_between_children);
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                init();
            }
        });


    }

    private void init() {

        addRotatingBackground();
        setPivotOfPentagon();
        rotateChildrenToInitialPosition();

    }

    private void addRotatingBackground() {

        rotatingBackground = new View(getContext());
        rotatingBackground.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        rotatingBackground.setBackground(getResources().getDrawable(R.drawable.ic_pentagon));
        addView(rotatingBackground, 0);

    }

    private void setPivotOfPentagon() {
        setPivotX(calculatePentagonPivotX());
        setPivotY(calculatePentagonPivotY());
    }

    private float calculatePentagonPivotX() {
        return getWidth()/2;
    }

    private float calculatePentagonPivotY() {
        return getHeight()/1.81f;
    }

    private void rotateChildrenToInitialPosition() {

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            setChildPivot(childView);
            childView.setRotation(angleBetweenChildren * i);
        }
    }

    private void setChildPivot(View view) {

        float pivotX = calculatePentagonPivotX() - view.getX();
        float pivotY = calculatePentagonPivotY() - view.getY();
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

    }

    public boolean showNextChild(boolean rotateClockwise) {

        if (isRotating) {
            return false;
        }

        isRotating = true;

        float rotationAngle = rotateClockwise ? angleBetweenChildren : -angleBetweenChildren;

        View[] childViews = new View[getChildCount()];

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childViews[i] = childView;
            animateRotatingViewAlpha(childView, rotationAngle);
        }

        AnimationUtil.rotate(
                rotationAngle,
                onAnimationCompletedListener,
                childViews);
        return true;
    }

    private void animateRotatingViewAlpha(View view, float rotationAngle) {

        if (view == rotatingBackground) {
            return;
        }

        float initialRotation = view.getRotation();

        if (initialRotation % TOTAL_ANGLE == 0) {
            AnimationUtil.animateAlpha(view, false);
        } else if (Math.abs((initialRotation + rotationAngle) % TOTAL_ANGLE) == 0) {
            AnimationUtil.animateAlpha(view, true);
        } else {
            view.setAlpha(ALPHA_TRANSPARENT);
        }

    }

}
