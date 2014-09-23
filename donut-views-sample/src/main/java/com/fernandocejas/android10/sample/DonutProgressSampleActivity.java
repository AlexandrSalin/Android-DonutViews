/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample;

import android.app.Activity;
import android.os.Bundle;
import com.fernandocejas.android10.donut.component.DonutProgressView;

public class DonutProgressSampleActivity extends Activity {

  private static final int MAX_PROGRESS_ANIMATION = 300;

  private DonutProgressView donutProgressViewOne;
  private DonutProgressView donutProgressViewTwo;

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
    this.donutProgressViewOne = (DonutProgressView)findViewById(R.id.v_donut_progress_one);
    this.donutProgressViewTwo = (DonutProgressView)findViewById(R.id.v_donut_progress_two);
  }


  /**
   * Start animating view
   */
  private void startViewAnimation() {
    final Runnable runnable = new Runnable() {
      public void run() {
        for (int progress = 0; progress < MAX_PROGRESS_ANIMATION; progress++) {
          //donutProgressViewOne.incrementProgress();
          //donutProgressViewTwo.incrementProgress();
          try {
            Thread.sleep(4);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        //donutProgressViewOne.setProgress(MAX_PROGRESS_ANIMATION);
        //donutProgressViewTwo.setProgress(MAX_PROGRESS_ANIMATION);
      }
    };
    Thread thread = new Thread(runnable);
    thread.start();
  }
}
