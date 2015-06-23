package com.seyhanf.cobidemo;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by seyhanf on 23/06/15.
 */
public final class AnimationUtil {

    private static final float ALPHA_OPAQUE = 1.0f;
    private static final float ALPHA_TRANSPARENT = 0.0f;
    private static final float DEFAULT_ROTATION_ANGLE = -72.0f;
    private static final float TOTAL_ANGLE = 360.0f;
    private static final int ALPHA_ANIMATION_DURATION_SHORT = 500;
    private static final int ALPHA_ANIMATION_DURATION_LONG = 2000;
    private static final int ALPHA_ANIMATION_START_OFFSET_SHORT = 200;
    private static final int ALPHA_ANIMATION_START_OFFSET_LONG = 1000;

    public interface OnRotationCompletedListener {
        public void onRotationCompeted();
    }

    /**
     * private constructor.
     */
    private AnimationUtil() {
    }

    public static void animateRotatingViewAlpha(View view, float rotationAngle) {

        float initialRotation = view.getRotation();

        if (initialRotation % TOTAL_ANGLE == 0) {
            animateAlpha(view, false);
        } else if (Math.abs((initialRotation + rotationAngle) % TOTAL_ANGLE) == 0) {
            animateAlpha(view, true);
        } else {
            view.setAlpha(ALPHA_TRANSPARENT);
        }

    }

    /**
     * Animates alpha value of given view.
     *
     * @param view view to set alpha
     * @param show true to show the view, false to hide the view
     */
    public static void animateAlpha(final View view, final boolean show) {

        animateAlpha(view, show, ALPHA_ANIMATION_DURATION_SHORT, 0);

    }

    /**
     * Animates alpha value of given view.
     *
     * @param view     view to set alpha
     * @param show     true to show the view, false to hide the view
     * @param duration duration of animation
     * @param offset   offset of animation
     */
    public static void animateAlpha(
            final View view,
            final boolean show,
            final int duration,
            final int offset) {

        ValueAnimator animator = ValueAnimator.ofFloat(
                show ? ALPHA_TRANSPARENT : ALPHA_OPAQUE,
                show ? ALPHA_OPAQUE : ALPHA_TRANSPARENT);
        animator.setDuration(duration);
        animator.setStartDelay(offset);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.start();

    }

    public static void setPivotOfPentagon(View view) {
        view.setPivotX(getPentagonPivotX(view));
        view.setPivotY(getPentagonPivotY(view));
    }

    public static float getPentagonPivotX(final View pentagonView) {
        return pentagonView.getWidth()/2;
    }

    public static float getPentagonPivotY(final View pentagonView) {
        return ((float)pentagonView.getHeight())/1.82f;
    }

    public static void setPivotRelatively(View view, final View pentagonView) {

        float pivotX = getPentagonPivotX(pentagonView) + pentagonView.getX() - view.getX();
        float pivotY = getPentagonPivotY(pentagonView) + pentagonView.getY() - view.getY();
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

    }

    public static void setRotationIncrementally(View... views) {

        for (int i = 0; i < views.length; i++) {
            views[i].setRotation(DEFAULT_ROTATION_ANGLE * i);
            if (i != 0) {
                views[i].setAlpha(ALPHA_TRANSPARENT);
            }
        }

    }

    /**
     * Rotates view.
     *
     * @param views     views to rotate
     */
    public static void rotate(final OnRotationCompletedListener onRotationCompletedListener, final View... views) {

        rotate(DEFAULT_ROTATION_ANGLE, ALPHA_ANIMATION_DURATION_SHORT, 0, onRotationCompletedListener, views);

    }

    /**
     * Rotates view.
     *
     * @param views     views to rotate
     */
    public static void rotate(
            final float angle,
            final OnRotationCompletedListener onRotationCompletedListener,
            final View... views) {

        rotate(angle, ALPHA_ANIMATION_DURATION_SHORT, 0, onRotationCompletedListener, views);

    }

    /**
     * Rotates view.
     *
     * @param views     views to rotate
     * @param duration duration of animation
     * @param offset   offset of animation
     */
    public static void rotate(
            final float angle,
            final int duration,
            final int offset,
            final OnRotationCompletedListener onRotationCompletedListener,
            final View... views) {

        final float[] initialRotations = new float[views.length];

        for (int i = 0; i < views.length; i++) {

            initialRotations[i] = views[i].getRotation();
            if (i != 0) {
                animateRotatingViewAlpha(views[i], angle);
            }

        }

        ValueAnimator animator = ValueAnimator.ofFloat(0, angle);
        animator.setDuration(duration);
        animator.setStartDelay(offset);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float rotationAmount = (float) animation.getAnimatedValue();
                for (int i = 0; i < views.length; i++) {
                    views[i].setRotation(initialRotations[i] + rotationAmount % TOTAL_ANGLE);
                }

                if (rotationAmount == angle && onRotationCompletedListener != null) {
                    onRotationCompletedListener.onRotationCompeted();
                }

            }
        });
        animator.start();

    }

    /**
     * Adjust visibilities for reveal.
     *
     * @param target view.
     * @param behind view.
     * @param reveal reveal state.
     */
    private static void adjustVisibility(View target, View behind, boolean reveal) {
        if (reveal) {
            target.setVisibility(View.VISIBLE);
            behind.setVisibility(View.GONE);
        } else {
            target.setVisibility(View.GONE);
            behind.setVisibility(View.VISIBLE);
        }
    }

}
