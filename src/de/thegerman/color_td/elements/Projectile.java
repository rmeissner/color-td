package de.thegerman.color_td.elements;

import static de.thegerman.color_td.GameView.COLOR_MIN_VALUE;

import java.util.List;

import android.graphics.Color;
import de.thegerman.color_td.GameThread;

public class Projectile extends MovingCircleObject<Projectile> {
	
	public static final float DAMAGE_MULIPLIER = 0.1f;

	private Enemy target;

	public Projectile(float x, float y, int color, float dx, float dy, Enemy target) {
		super(x, y, 5, color, dx, dy, 150);
		this.target = target;
	}

	@Override
	public boolean update(long timespan, GameThread game, List<Projectile> newMovingObjects) {

		if(game.getEnemies().contains(target)){
			float xd = target.getX() - this.x;
			float yd = target.getY() - this.y;
			float c = (float) Math.sqrt(xd * xd + yd * yd);
			if(c == 0) c = 1;
			this.xspeed = xd / c;
			this.yspeed = yd / c;
		}
		
		return super.update(timespan, game, newMovingObjects);
	}

	public float getRedDamage() {
		return (Color.red(color) - COLOR_MIN_VALUE) * DAMAGE_MULIPLIER;
	}

	public float getGreenDamage() {
		return (Color.green(color) - COLOR_MIN_VALUE) * DAMAGE_MULIPLIER;
	}

	public float getBlueDamage() {
		return (Color.blue(color) - COLOR_MIN_VALUE) * DAMAGE_MULIPLIER;
	};
	
}
