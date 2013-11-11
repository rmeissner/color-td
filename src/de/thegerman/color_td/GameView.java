package de.thegerman.color_td;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

	public static final int WIDTH = 1000;
	public static final int CONTROL_HEIGHT = 150;
	public static final long MAX_TIMESPAN = 100;
	public static final int COLOR_BASE_VALUE = 105;
	public static final int COLOR_MIN_VALUE = 55;
	public static final int COLOR_MAX_VALUE = 255;
	private GameThread thread;
	private Context context;

	public GameView(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		this.context = context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		this.thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.thread = new GameThread(this.context, holder);
		this.thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.thread = null; 
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.thread != null) {
			return this.thread.handleTouchEvent(event);
		} else {
			return false;
		}
	}

}
