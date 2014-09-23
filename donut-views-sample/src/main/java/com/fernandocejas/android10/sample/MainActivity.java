package com.fernandocejas.android10.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

  private Button btn_SampleDonutProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.mapGUI();
  }

  private void mapGUI() {
    btn_SampleDonutProgress = (Button) findViewById(R.id.btn_SampleDonutProgress);
    btn_SampleDonutProgress.setOnClickListener(loadSampleDonutProgressOnClickListener);
  }

  private void startActivity(Class<? extends Activity> activity) {
    Intent intent = new Intent(MainActivity.this, activity);
    startActivity(intent);
  }

  private final View.OnClickListener loadSampleDonutProgressOnClickListener =
      new View.OnClickListener() {
        @Override public void onClick(View v) {
          MainActivity.this.startActivity(DonutProgressSampleActivity.class);
        }
      };
}
