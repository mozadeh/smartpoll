package com.smartiky.smartpoll.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.smartiky.smartpoll.R;

public class ChoiceButton extends RadioButton {
	
	protected static final String TAG = "ChoiceButton";
	
	static final int animationPeriod = 1000;  // in milliseconds
	int mWidth, mHeight;
	int numResponses;
	int percentResponses;
	boolean showResult;
	Typeface tf,tf1;
	String color = "#ffffff";
	boolean animate = false;
	long animationStart;
	Paint bar, text, text1;
	DisplayMetrics metrics;
	boolean votedPoll=false;

	public ChoiceButton(Context context) {
		super(context);
		init();
	}

	public ChoiceButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ChoiceButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		metrics = getContext().getResources().getDisplayMetrics();
		
		setHeight((int) (38 * metrics.density));
		text = new Paint();
		text1 = new Paint();
		bar = new Paint();
		text.setAntiAlias(true);
		text.setTextSize(17 * metrics.density);
		text.setTypeface(tf);
		text1.setAntiAlias(true);
		text1.setTextSize(12 * metrics.density);
		text1.setTypeface(tf);

		graphicStateChanged();
		this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				graphicStateChanged();
			}
		});
	}

	public int getNumResponses() {
		return numResponses;
	}
	
	
	public void setNumResponses(int numResponses) {
		this.numResponses = numResponses;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPercentResponses() {
		return percentResponses;
	}

	public void setAnimation(boolean animate) {
		this.animate = animate;
	}

	public void setPercentResponses(int percentResponses) {
		this.percentResponses = percentResponses;
	}

	public boolean getShowResult() {
		return showResult;
	}

	public void setShowResult(boolean showResult) {
		this.showResult = showResult;
	}

	public void setTypeFace(Typeface typeFace) {
		tf = typeFace;
	}
	
	public void setTypeFaceNumVotes(Typeface typeFace) {
		tf1 = typeFace;
	}
	
	public void setVoted(boolean b){
		votedPoll=b;
		graphicStateChanged();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
	}


	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		graphicStateChanged();
	}

	
	protected void graphicStateChanged() {
		//check to see if put ihere
		text.setTypeface(tf);
		text1.setTypeface(tf1);
		// Select paints and set background image. This will cause the view to be invalidated.
		if (this.isChecked()) {
			bar.setColor(Color.parseColor("#ffffff"));
			text.setColor(Color.parseColor("#ffffff"));
			text1.setColor(Color.parseColor("#ffffff"));
			this.setBackgroundDrawable(this.getResources().getDrawable(
					R.drawable.gradient5));
			text.setShadowLayer(
					3 * metrics.density, 0, 0, 0xFF000000);

		} else {
			if (votedPoll) {
				text.setColor(Color.parseColor("#233051"));
				text1.setColor(Color.parseColor("#485892"));
				this.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.buttongradient));
				text.setShadowLayer(0, 0, 0, 0xFFFFFFFF);
			}
			else {
				//bar.setColor(Color.parseColor("#3e538b"));
				text.setColor(Color.parseColor("#ffffff"));
				text1.setColor(Color.parseColor("#ffffff"));
				this.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.buttongradientnotselected));
				text.setShadowLayer(
						4 * metrics.density, 0, 0, 0xFF000000);
			}
			
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int height = mHeight;
		int width = mWidth;

		text.setTextSize(16 * metrics.density);
		canvas.drawText(getText().toString(), 6f * metrics.density,
				23f * metrics.density, text);
		if (getShowResult()) {
			
			if (animate) {
				animationStart = System.currentTimeMillis();
				animate = false;
			}
			
			int x = (int) (System.currentTimeMillis() - animationStart);
			if (animationStart == 0 || x > animationPeriod)
				x = animationPeriod;
			if (x < 0)
				x = 0;
			

			String s = "("+getNumResponses()  + " votes) "
					+ (getPercentResponses() * x / animationPeriod) + "%";
			//text.setTextSize(12 * metrics.density);
			
			int left = (int) (2f * metrics.density);
			int length = width - (int) (86f * metrics.density);
			int right = (int) ((getPercentResponses()*(length)/100) );
			
			canvas.drawText(s, width - 80f * metrics.density, 36f * metrics.density,
					text1);
			
			//int left = (int) (width - (getPercentResponses()*(width - length)/100) );
			
			bar.setAlpha(255);
			bar.setShader(new LinearGradient(0, height, 0, height - 9 * metrics.density,Color.parseColor("#a2a2a2") ,Color.parseColor("#656564") , Shader.TileMode.MIRROR));
			
			//canvas.drawRect(length + 5 * metrics.density, height - 10 * metrics.density, width, height, bar);
			canvas.drawRoundRect(new RectF(left, height - 11 * metrics.density, left+length, height- 2 * metrics.density), 4 * metrics.density, 4 * metrics.density, bar);
			
			bar.setAlpha(x * 255 / animationPeriod);
			bar.setShader(new LinearGradient(0, height, 0, height - 9 * metrics.density, Color.parseColor("#a2a2a2"), Color.WHITE, Shader.TileMode.MIRROR));
			

			//canvas.drawRect(((left * x) / animationPeriod + (width * (animationPeriod - x) / animationPeriod))+ 7 * metrics.density,height - 10 * metrics.density, width, height , bar);
			canvas.drawRoundRect(new RectF(left, height - 11 * metrics.density, left + ( right * x) / animationPeriod, height - 2 * metrics.density), 4 * metrics.density, 4 * metrics.density, bar);
			

			if (x < animationPeriod) {
				invalidate();
			}
		}
	}

}
