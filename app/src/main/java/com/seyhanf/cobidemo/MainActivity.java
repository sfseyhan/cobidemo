package com.seyhanf.cobidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by seyhanf on 23/06/15.
 */
public class MainActivity extends AppCompatActivity implements AnimationUtil.OnRotationCompletedListener {

    @InjectView(R.id.activity_main_rotating_pentagon) View viewPentagon;
    @InjectView(R.id.activity_main_rotating_content_first) LinearLayout layoutFirst;
    @InjectView(R.id.activity_main_rotating_content_second) LinearLayout layoutSecond;
    @InjectView(R.id.activity_main_rotating_content_third) LinearLayout layoutThird;
    @InjectView(R.id.activity_main_rotating_content_fourth) LinearLayout layoutFourth;
    @InjectView(R.id.activity_main_rotating_content_fifth) LinearLayout layoutFifth;

    private boolean isRotating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        viewPentagon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                viewPentagon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                prepareLayout();
            }
        });
    }

    private void prepareLayout() {

        AnimationUtil.setPivotOfPentagon(viewPentagon);
        AnimationUtil.setPivotRelatively(layoutFirst, viewPentagon);
        AnimationUtil.setPivotRelatively(layoutSecond, viewPentagon);
        AnimationUtil.setPivotRelatively(layoutThird, viewPentagon);
        AnimationUtil.setPivotRelatively(layoutFourth, viewPentagon);
        AnimationUtil.setPivotRelatively(layoutFifth, viewPentagon);
        AnimationUtil.setRotationIncrementally(
                layoutFirst,
                layoutSecond,
                layoutThird,
                layoutFourth,
                layoutFifth);

    }

    @OnClick(R.id.activity_main_rotating_pentagon)
    public void rotatePentagon() {

        if (isRotating) {
            return;
        }

        isRotating = true;

        AnimationUtil.rotate(
                this,
                viewPentagon,
                layoutFirst,
                layoutSecond,
                layoutThird,
                layoutFourth,
                layoutFifth);
    }


    @Override
    public void onRotationCompeted() {
        isRotating = false;
    }
}
