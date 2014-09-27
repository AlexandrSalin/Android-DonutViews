/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.donut.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.fernandocejas.android10.donut.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Image view with a rounded animated border
 */
public class DonutProgressView extends ImageView {

  private static final int MAXIMUM_PROGRESS = 360;
  private static final int ANIMATION_DELAY = 3;

  private int layout_height = 0;
  private int layout_width = 0;

  private int paddingTop = 2;
  private int paddingBottom = 2;
  private int paddingLeft = 2;
  private int paddingRight = 2;

  private int fullRadius = 100;
  private int circleRadius = 80;
  private int barLength = 60;
  private int barWidth = 20;
  private int rimWidth = 20;
  private float contourSize = 0;

  private int barColor = 0xAA000000;
  private int contourColor = 0xAA000000;
  private int circleColor = 0x00000000;
  private int rimColor = 0xAADDDDDD;

  private Paint barPaint = new Paint();
  private Paint circlePaint = new Paint();
  private Paint rimPaint = new Paint();
  private Paint contourPaint = new Paint();

  private RectF circleBounds = new RectF();
  private RectF circleOuterContour = new RectF();
  private RectF circleInnerContour = new RectF();

  //The amount of pixels to move the bar by on each draw
  private int spinSpeed = 2;
  //The number of milliseconds to wait in between each draw
  private int delayMillis = 1000;

  private static SpinHandler spinHandler;

  private int progress = 0;
  private boolean isSpinning = false;

  /**
   * Constructor for the RoundedAnimatedView
   *
   * @param context
   * @param attrs
   */
  public DonutProgressView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.initHandler();
    parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.DonutProgressView));
  }

  /**
   * Initializes the handler to manage the progress of the animated view
   */
  private void initHandler() {
    if (spinHandler == null) {
      spinHandler = new SpinHandler(this);
    } else {
      spinHandler.addView(this);
    }
  }

  /**
   * Parse the attributes passed to the view from the XML
   *
   * @param attributes the attributes to parse
   */
  private void parseAttributes(TypedArray attributes) {
    barWidth = (int) attributes.getDimension(R.styleable.DonutProgressView_barWidth, barWidth);
    rimWidth = (int) attributes.getDimension(R.styleable.DonutProgressView_rimWidth, rimWidth);
    spinSpeed = (int) attributes.getDimension(R.styleable.DonutProgressView_spinSpeed, spinSpeed);
    delayMillis = attributes.getInteger(R.styleable.DonutProgressView_delayMillis, delayMillis);
    if (delayMillis < 0) {
      delayMillis = 0;
    }
    barColor = attributes.getColor(R.styleable.DonutProgressView_barColor, barColor);
    rimColor = attributes.getColor(R.styleable.DonutProgressView_rimColor, rimColor);
    circleColor = attributes.getColor(R.styleable.DonutProgressView_circleColor, circleColor);
    contourColor = attributes.getColor(R.styleable.DonutProgressView_contourColor, contourColor);
    contourSize = attributes.getDimension(R.styleable.DonutProgressView_contourSize, contourSize);
    barLength = (int) attributes.getDimension(R.styleable.DonutProgressView_barLength, barLength);
    attributes.recycle();
  }

  /**
   * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
   * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
   * Use this dimensions to setup the bounds and paints.
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    // Share the dimensions
    layout_width = w;
    layout_height = h;

    setupBounds();
    setupPaints();
    invalidate();
  }

  /**
   * Set the bounds of the component
   */
  private void setupBounds() {
    // Width should equal to Height, find the min value to setup the circle
    int minValue = Math.min(layout_width, layout_height);

    // Calc the Offset if needed
    int xOffset = layout_width - minValue;
    int yOffset = layout_height - minValue;

    // Add the offset
    paddingTop = this.getPaddingTop() + (yOffset / 2);
    paddingBottom = this.getPaddingBottom() + (yOffset / 2);
    paddingLeft = this.getPaddingLeft() + (xOffset / 2);
    paddingRight = this.getPaddingRight() + (xOffset / 2);

    circleBounds = new RectF(paddingLeft + barWidth, paddingTop + barWidth,
        this.getLayoutParams().width - paddingRight - barWidth,
        this.getLayoutParams().height - paddingBottom - barWidth);

    circleInnerContour = new RectF(circleBounds.left + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.top +
        (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.right - (rimWidth / 2.0f) -
        (contourSize / 2.0f), circleBounds.bottom - (rimWidth / 2.0f) - (contourSize / 2.0f));

    circleOuterContour = new RectF(circleBounds.left - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.top -
        (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.right + (rimWidth / 2.0f) +
        (contourSize / 2.0f), circleBounds.bottom + (rimWidth / 2.0f) + (contourSize / 2.0f));

    fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
    circleRadius = (fullRadius - barWidth) + 1;
  }

  /**
   * Set the properties of the paints we're using to draw the progress wheel
   */
  private void setupPaints() {
    barPaint.setColor(barColor);
    barPaint.setAntiAlias(true);
    barPaint.setStyle(Paint.Style.STROKE);
    barPaint.setStrokeWidth(barWidth);

    rimPaint.setColor(rimColor);
    rimPaint.setAntiAlias(true);
    rimPaint.setStyle(Paint.Style.STROKE);
    rimPaint.setStrokeWidth(rimWidth);

    circlePaint.setColor(circleColor);
    circlePaint.setAntiAlias(true);
    circlePaint.setStyle(Paint.Style.FILL);

    contourPaint.setColor(contourColor);
    contourPaint.setAntiAlias(true);
    contourPaint.setStyle(Paint.Style.STROKE);
    contourPaint.setStrokeWidth(contourSize);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    //Draw the rim
    canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
    canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
    canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);

    //Draw the bar
    if (isSpinning) {
      canvas.drawArc(circleBounds, progress - 90, barLength, false, barPaint);
    } else {
      canvas.drawArc(circleBounds, -90, progress, false, barPaint);
    }

    //Draw the inner circle
    canvas.drawCircle((circleBounds.width() / 2) + rimWidth + paddingLeft,
        (circleBounds.height() / 2) + rimWidth + paddingTop,
        circleRadius,
        circlePaint);
  }

  /**
   * Reset the count progress (in increment mode)
   */
  public void reset() {
    progress = 0;
    invalidate();
  }

  /**
   * Increment the progress by 1 (of 360)
   */
  private void incrementProgress() {
    isSpinning = false;
    progress++;
    if (progress > MAXIMUM_PROGRESS) {
      progress = 0;
    }
    spinHandler.sendEmptyMessage(0);
  }

  /**
   * Set the progress to a specific value
   */
  public void setProgress(int i) {
    isSpinning = false;
    progress = i;
    spinHandler.sendEmptyMessage(0);
  }

  public int getProgress() {
    return progress;
  }

  /**
   * Handles the empty messages sent by the handler for incrementing
   * progress of the current view
   */
  private void handleSpinHandlerMessage() {
    invalidate();
    if (isSpinning) {
      progress += spinSpeed;
      if (progress > MAXIMUM_PROGRESS) {
        progress = 0;
      }
      spinHandler.sendEmptyMessageDelayed(0, delayMillis);
    }
  }

  /**
   * Start animating view
   *
   * @param maxProgress the maximum progress that will be filled (value between 0 and 360).
   */
  public void startViewAnimation(final int maxProgress) {
    this.reset();
    if (maxProgress >= 0 && maxProgress <= MAXIMUM_PROGRESS) {
      final Runnable runnable = new Runnable() {
        public void run() {
          for (int progress = 0; progress < maxProgress; progress++) {
            DonutProgressView.this.incrementProgress();
            try {
              Thread.sleep(ANIMATION_DELAY);
            } catch (InterruptedException e) {
              //nothing to do here
            }
          }
          DonutProgressView.this.setProgress(maxProgress);
        }
      };
      Thread thread = new Thread(runnable);
      thread.start();
    }
  }

  private static class SpinHandler extends Handler {
    List<WeakReference<DonutProgressView>> viewList;

    public SpinHandler(DonutProgressView donutProgressView) {
      viewList = new ArrayList<WeakReference<DonutProgressView>>();
      viewList.add(new WeakReference<DonutProgressView>(donutProgressView));
    }

    @Override
    public void handleMessage(Message msg) {
      if (!viewList.isEmpty()) {
        for (WeakReference<DonutProgressView> view : viewList) {
          final DonutProgressView currentView = view.get();
          if (currentView != null) {
            currentView.handleSpinHandlerMessage();
          }
        }
      }
    }

    /**
     * Add a new instance of a view to be managed by the SpinHandler
     * @param donutProgressView the view to be added
     */
    public void addView(DonutProgressView donutProgressView) {
      viewList.add(new WeakReference<DonutProgressView>(donutProgressView));
    }
  }
}