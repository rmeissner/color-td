package de.thegerman.color_td.elements;

import static de.thegerman.color_td.GameView.COLOR_MIN_VALUE;
import android.graphics.Color;


public class Enemy extends MovingCircleObject<Enemy> {
	
	private int rLive;
	private int gLive;
	private int bLive;

	public Enemy(float x, float y, int color, float dx, float dy) {
		super(x, y, 15, color, dx, dy, 100);
		this.rLive = Color.red(color);
		this.gLive = Color.green(color);
		this.bLive = Color.blue(color);
	}

	public boolean takeDamage(Projectile projectile) {
		rLive -= projectile.getRedDamage();
		gLive -= projectile.getGreenDamage();
		bLive -= projectile.getBlueDamage();
		if (rLive <= COLOR_MIN_VALUE && gLive <= COLOR_MIN_VALUE && bLive <= COLOR_MIN_VALUE) return true;
		return false;
	}
}
