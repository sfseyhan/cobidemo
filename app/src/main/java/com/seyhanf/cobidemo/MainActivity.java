package com.seyhanf.cobidemo;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by seyhanf on 23/06/15.
 */
public class MainActivity extends AppCompatActivity implements AnimationUtil.OnAnimationCompletedListener {

    @InjectView(R.id.activity_main_pentagon_right) FrameLayout layoutPentagonRight;
    @InjectView(R.id.activity_main_pentagon_left) FrameLayout layoutPentagonLeft;
    @InjectView(R.id.activity_main_rotating_pentagon) View viewPentagon;
    @InjectView(R.id.activity_main_rotating_content_first) LinearLayout layoutFirst;
    @InjectView(R.id.activity_main_rotating_content_second) LinearLayout layoutSecond;
    @InjectView(R.id.activity_main_rotating_content_third) LinearLayout layoutThird;
    @InjectView(R.id.activity_main_rotating_content_fourth) LinearLayout layoutFourth;
    @InjectView(R.id.activity_main_rotating_content_fifth) LinearLayout layoutFifth;

    private boolean isRotating;
    private boolean isTranslating;
    private boolean viewsExitedScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setPentagonSpecs(layoutPentagonRight, true);
        setPentagonSpecs(layoutPentagonLeft, false);

        layoutPentagonRight.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                layoutPentagonRight.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                prepareRotatingPentagon();
            }
        });
    }

    private void prepareRotatingPentagon() {

        AnimationUtil.setPivotOfPentagon(viewPentagon);

        for (int i = 0; i < layoutPentagonRight.getChildCount(); i++) {
            AnimationUtil.setPivotRelatively(layoutPentagonRight.getChildAt(i), viewPentagon);
        }

    }

    @OnClick(R.id.activity_main_pentagon_right)
    public void rotateRightPentagon() {

        if (isRotating) {
            return;
        }

        isRotating = true;

        AnimationUtil.rotate(
                this,
                getChildren(layoutPentagonRight));
    }

    @OnClick(R.id.activity_main_pentagon_left)
    public void movePentagon() {

        if (isTranslating) {
            return;
        }

        isTranslating = true;

        final Animation animRightPentagon = AnimationUtils.loadAnimation(
                this, viewsExitedScreen ? R.anim.translate_in_from_right : R.anim.translate_out_to_right);

        final Animation animLeftPentagon = AnimationUtils.loadAnimation(
                this, viewsExitedScreen ? R.anim.translate_in_from_left : R.anim.translate_out_to_left);

        animRightPentagon.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onTranslationCompeted();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutPentagonRight.startAnimation(animRightPentagon);
        layoutPentagonLeft.startAnimation(animLeftPentagon);

        viewsExitedScreen = !viewsExitedScreen;

    }


    @Override
    public void onRotationCompeted() {
        isRotating = false;
    }

    @Override
    public void onTranslationCompeted() {
        isTranslating = false;
    }

    private void setPentagonSpecs(FrameLayout frameLayout, boolean expandToRight) {

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        params.height = getDeviceHeight() + getOffScreenVertical() * 2;
        //TODO Assuming device height would be smaller than width since it's landscape. Might have to check if width is smaller.
        params.width = params.height;
        frameLayout.setLayoutParams(params);

        if (expandToRight) {
            params.rightMargin = -((params.width/2) + getOffScreenHorizontal());
        } else {
            params.leftMargin = -((params.width/2) + getOffScreenHorizontal());
        }

    }

    private int getOffScreenVertical() {
        return getResources().getDimensionPixelOffset(R.dimen.pentagon_off_screen_vertical);
    }

    private int getOffScreenHorizontal() {
        return getResources().getDimensionPixelOffset(R.dimen.pentagon_off_screen_horizontal);
    }

    /**
     * @return height of device
     */
    private int getDeviceHeight() {

        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size.y;

    }

    private View[] getChildren(ViewGroup viewGroup) {
        final View[] views = new View[viewGroup.getChildCount()];
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            views[i] = viewGroup.getChildAt(i);
        }
        return views;
    }

}
