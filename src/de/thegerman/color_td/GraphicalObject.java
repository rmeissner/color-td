package de.thegerman.color_td;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GraphicalObject {
protected float x = 0, y = 0;
protected Paint paint = new Paint();
	
	public GraphicalObject(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	abstract public void draw(Canvas c);
	
	public void setPosition(float f, float g) {
		this.x = f;
		this.y = g;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
