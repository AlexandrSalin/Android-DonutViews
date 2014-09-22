package com.fernandocejas.android10.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends Activity {

  private final View.OnClickListener loadSampleDonutProgressOnClickListener =
      new View.OnClickListener() {
        @Override public void onClick(View v) {
        }
      };
  private Button btn_SampleDonutProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.mapGUI();
  }

  /**
   * Maps the graphical user interface controls.
   */
  private void mapGUI() {
    btn_SampleDonutProgress = (Button) findViewById(R.id.btn_SampleDonutProgress);
    btn_SampleDonutProgress.setOnClickListener(loadSampleDonutProgressOnClickListener);
  }
}
