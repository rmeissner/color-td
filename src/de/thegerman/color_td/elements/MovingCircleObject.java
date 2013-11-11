package de.thegerman.color_td.elements;

import java.util.List;

import de.thegerman.color_td.GameThread;
import de.thegerman.color_td.GameView;


public abstract class MovingCircleObject<T extends CircleObject<T>> extends CircleObject<T> {

	protected float xspeed;
	protected float yspeed;
	private float speedamp;

	public MovingCircleObject(float x, float y, float radius, int color, float dx, float dy, int speedamp) {
		super(x, y, radius, color);
		this.xspeed = dx;
		this.yspeed = dy;
		this.speedamp = speedamp;
	}
	
	public void move(long timespan){
		this.x += this.xspeed * timespan * (this.speedamp / 1000);
		this.y += this.yspeed * timespan * (this.speedamp / 1000);
	}

	public boolean update(long timespan, GameThread game, List<T> newMovingObjects) {
		this.move(timespan);
		if (this.getX() < 0 || this.getX() > game.getWidth()
				|| this.getY() < 0 || this.getY() > game.getHeight() - radius - GameView.CONTROL_HEIGHT)
			return true;
		return false;
	}
}
