/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.fernandocejas.android10.donut.component.DonutProgressView;
import java.lang.ref.WeakReference;

public class DonutProgressSampleActivity extends Activity {

  private static final int MAX_PROGRESS_ANIMATION = 300;
  private static final int DELAY_ANIMATION = 300;

  private static final int MSG_START_ANIMATION_ONE = 1;
  private static final int MSG_START_ANIMATION_TWO = 2;

  private DonutProgressView donutProgressViewOne;
  private DonutProgressView donutProgressViewTwo;

  private boolean hasAnimationRun = false;

  private AnimationHandler animationHandler = new AnimationHandler(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donut_progress_sample);

    this.mapGUI();
  }

  @Override protected void onResume() {
    super.onResume();
    this.startViewAnimation();
  }

  private void mapGUI() {
    this.donutProgressViewOne = (DonutProgressView) findViewById(R.id.v_donut_progress_one);
    this.donutProgressViewTwo = (DonutProgressView) findViewById(R.id.v_donut_progress_two);
  }

  /**
   * Start animating views.
   */
  private void startViewAnimation() {
    if (!this.hasAnimationRun) {
      animationHandler.sendEmptyMessage(MSG_START_ANIMATION_ONE);
      animationHandler.sendEmptyMessageDelayed(MSG_START_ANIMATION_TWO, DELAY_ANIMATION);
      this.hasAnimationRun = true;
    } else if (donutProgressViewOne.getProgress() != MAX_PROGRESS_ANIMATION
        || donutProgressViewTwo.getProgress() != MAX_PROGRESS_ANIMATION) {
      donutProgressViewOne.setProgress(MAX_PROGRESS_ANIMATION);
      donutProgressViewTwo.setProgress(MAX_PROGRESS_ANIMATION);
    }
  }

  /**
   * Animate a rounded view increasing its progress gradually.
   *
   * @param donutProgressView the view to animate.
   */
  private void animateView(DonutProgressView donutProgressView) {
    donutProgressView.startViewAnimation(MAX_PROGRESS_ANIMATION);
  }

  /**
   * Handler for managing animation stuff.
   */
  private static class AnimationHandler extends Handler {
    final WeakReference<DonutProgressSampleActivity> donutProgressSampleActivityWeakReference;

    public AnimationHandler(DonutProgressSampleActivity controlSlideFragment) {
      this.donutProgressSampleActivityWeakReference =
          new WeakReference<DonutProgressSampleActivity>(controlSlideFragment);
    }

    @Override
    public void handleMessage(Message msg) {
      final DonutProgressSampleActivity donutProgressSampleActivity =
          this.donutProgressSampleActivityWeakReference
              .get();
      switch (msg.what) {
        case MSG_START_ANIMATION_ONE:
          if (donutProgressSampleActivity != null) {
            donutProgressSampleActivity.animateView(donutProgressSampleActivity.donutProgressViewOne);
          }
          break;
        case MSG_START_ANIMATION_TWO:
          if (donutProgressSampleActivity != null) {
            donutProgressSampleActivity.animateView(donutProgressSampleActivity.donutProgressViewTwo);
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }
}
