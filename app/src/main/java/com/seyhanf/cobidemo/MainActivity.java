package com.seyhanf.cobidemo;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by seyhanf on 23/06/15.
 */
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.activity_main_pentagon_right) RotatingLayout layoutPentagonRight;
    @InjectView(R.id.activity_main_pentagon_left) FrameLayout layoutPentagonLeft;

    private boolean isTranslating;
    private boolean viewsExitedScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setPentagonSpecs(layoutPentagonRight, true);
        setPentagonSpecs(layoutPentagonLeft, false);
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
                isTranslating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutPentagonRight.startAnimation(animRightPentagon);
        layoutPentagonLeft.startAnimation(animLeftPentagon);

        viewsExitedScreen = !viewsExitedScreen;

    }

    private void setPentagonSpecs(FrameLayout frameLayout, boolean expandToRight) {

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        params.height = getDeviceHeight() + getOffScreenVertical() * 2;
        if (!expandToRight) {
            params.height = (int)(params.height * 1.5f);
        }
        //TODO Assuming device height would be smaller than width since it's landscape. Might have to check if width is smaller.
        params.width = params.height;
        frameLayout.setLayoutParams(params);

        if (expandToRight) {
            params.rightMargin = -((params.width/2) + getOffScreenHorizontal());
        } else {
            params.leftMargin = -((params.width/2));
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

}
