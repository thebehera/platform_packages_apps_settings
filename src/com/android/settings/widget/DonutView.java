/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.android.settings.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.android.settings.R;
import com.android.settings.Utils;

/**
 * DonutView represents a donut graph. It visualizes a certain percentage of fullness with a
 * corresponding label with the fullness on the inside (i.e. "50%" inside of the donut).
 */
public class DonutView extends View {
    private static final int TOP = -90;
    private float mStrokeWidth;
    private float mDeviceDensity;
    private int mPercent;
    private Paint mBackgroundCircle;
    private Paint mFilledArc;
    private TextPaint mTextPaint;
    private TextPaint mBigNumberPaint;
    private String mPercentString;
    private String mFullString;

    public DonutView(Context context) {
        super(context);
    }

    public DonutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDeviceDensity = getResources().getDisplayMetrics().density;
        mStrokeWidth = 6f * mDeviceDensity;
        final ColorFilter mAccentColorFilter =
                new PorterDuffColorFilter(
                        Utils.getColorAttr(context, android.R.attr.colorAccent),
                        PorterDuff.Mode.SRC_IN);

        mBackgroundCircle = new Paint();
        mBackgroundCircle.setAntiAlias(true);
        mBackgroundCircle.setStrokeCap(Paint.Cap.BUTT);
        mBackgroundCircle.setStyle(Paint.Style.STROKE);
        mBackgroundCircle.setStrokeWidth(mStrokeWidth);
        mBackgroundCircle.setColorFilter(mAccentColorFilter);
        mBackgroundCircle.setColor(context.getColor(R.color.meter_background_color));

        mFilledArc = new Paint();
        mFilledArc.setAntiAlias(true);
        mFilledArc.setStrokeCap(Paint.Cap.BUTT);
        mFilledArc.setStyle(Paint.Style.STROKE);
        mFilledArc.setStrokeWidth(mStrokeWidth);
        mFilledArc.setColor(Utils.getDefaultColor(mContext, R.color.meter_consumed_color));
        mFilledArc.setColorFilter(mAccentColorFilter);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Utils.getColorAccent(getContext()));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(14f * mDeviceDensity);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBigNumberPaint = new TextPaint();
        mBigNumberPaint.setColor(Utils.getColorAccent(getContext()));
        mBigNumberPaint.setAntiAlias(true);
        mBigNumberPaint.setTextSize(30f * mDeviceDensity);
        mBigNumberPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDonut(canvas);
        drawInnerText(canvas);
    }

    private void drawDonut(Canvas canvas) {
        canvas.drawArc(
                0 + mStrokeWidth,
                0 + mStrokeWidth,
                getWidth() - mStrokeWidth,
                getHeight() - mStrokeWidth,
                TOP,
                360,
                false,
                mBackgroundCircle);

        canvas.drawArc(
                0 + mStrokeWidth,
                0 + mStrokeWidth,
                getWidth() - mStrokeWidth,
                getHeight() - mStrokeWidth,
                TOP,
                (360 * mPercent / 100),
                false,
                mFilledArc);
    }

    private void drawInnerText(Canvas canvas) {
        final float centerX = getWidth() / 2;
        final float centerY = getHeight() / 2;
        final float totalHeight = getTextHeight(mTextPaint) + getTextHeight(mBigNumberPaint);
        final float startY = centerY + totalHeight / 2;

        // The first line is the height of the bottom text + its descender above the bottom line.
        canvas.drawText(mPercentString, centerX,
                startY - getTextHeight(mTextPaint) - mBigNumberPaint.descent(),
                mBigNumberPaint);
        // The second line starts at the bottom + room for the descender.
        canvas.drawText(mFullString, centerX, startY - mTextPaint.descent(), mTextPaint);
    }

    /**
     * Set a percentage full to have the donut graph.
     */
    public void setPercentage(int percent) {
        mPercent = percent;
        mPercentString = Utils.formatPercentage(mPercent);
        mFullString = getContext().getString(R.string.storage_percent_full);
        invalidate();
    }

    private float getTextHeight(TextPaint paint) {
        // Technically, this should be the cap height, but I can live with the descent - ascent.
        return paint.descent() - paint.ascent();
    }
}
