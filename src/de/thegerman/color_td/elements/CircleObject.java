package de.thegerman.color_td.elements;

import java.util.List;

import android.graphics.Canvas;
import de.thegerman.color_td.GameThread;
import de.thegerman.color_td.GraphicalObject;

public abstract class CircleObject<T extends CircleObject<T>> extends GraphicalObject {

	protected float radius;
	protected int color;

	public CircleObject(float x, float y, float radius, int color) {
		super(x, y);
		this.radius = radius;
		this.color = color;
		this.paint.setColor(color);
	}
	
	public boolean containsPoint(float f, float g){
		float dx = f - this.x;
		float dy = g - this.y;
		if (Math.sqrt(dx*dx + dy*dy) <= this.radius)
			return true;
		else
			return false;
	}
	
	public boolean colidesWithCircle(CircleObject<? extends CircleObject<?>> other){
		float dx = other.x - this.x;
		float dy = other.y - this.y;
		if (Math.sqrt(dx*dx + dy*dy) <= (this.radius + other.radius))
			return true;
		else
			return false;
	}

	public float getRadius() {
		return radius;
	}
	
	@Override
	public void draw(Canvas c) {
		paint.setColor(color);	
		c.drawCircle(x, y, radius, paint);
	}

	abstract boolean update(long timespan, GameThread game, List<T> newMovingObjects);

}
