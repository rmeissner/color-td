package de.thegerman.color_td.elements;

import static de.thegerman.color_td.GameView.COLOR_BASE_VALUE;
import static de.thegerman.color_td.GameView.COLOR_MAX_VALUE;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import de.thegerman.color_td.GameThread;

public class Tower extends CircleObject<Projectile> {
	
	protected int buildModeRadiusColor = Color.argb(100, COLOR_BASE_VALUE, COLOR_BASE_VALUE, COLOR_BASE_VALUE); 
	protected int buildModeRadiusErrorColor = Color.argb(100, 255, COLOR_BASE_VALUE, COLOR_BASE_VALUE);
	private int influenceRadius;
	private boolean buildError;
	private boolean buildMode;
	private boolean showInflunceRadius; 
	private long lastShotDelay;
	private int redLevel = COLOR_BASE_VALUE;
	private int greenLevel = COLOR_BASE_VALUE;
	private int blueLevel = COLOR_BASE_VALUE;
	

	public Tower(float x, float y) {
		super(x, y, 40, Color.argb(255, COLOR_BASE_VALUE, COLOR_BASE_VALUE, COLOR_BASE_VALUE));
		this.influenceRadius = 150;
	}

	@Override
	public void draw(Canvas c) {
		if(inBuildMode()) {
			paint.setColor(canBeBuild()?buildModeRadiusColor:buildModeRadiusErrorColor);
			c.drawCircle(x, y, influenceRadius, paint);
		} else if (showInfluenceRadius()) {
			paint.setColor(Color.argb(100, COLOR_BASE_VALUE, COLOR_BASE_VALUE, 255));
			c.drawCircle(x, y, influenceRadius, paint);
		}
		paint.setColor(color);	
		c.drawCircle(x, y, radius, paint);
	}
	
	public boolean showInfluenceRadius() {
		return showInflunceRadius;
	}
	
	public void setInfluenceRadiusVisbility(boolean active){
		showInflunceRadius = active;
	}

	public void setBuildMode(boolean active){
		buildMode = active;
	}
	
	public void setBuildError(boolean active){
		buildError = active;
	}

	public boolean canBeBuild() {
		return !buildError;
	}

	public boolean inBuildMode() {
		return buildMode;
	}
	
	public boolean interfers(Tower other){
		float dx = other.x - this.x;
		float dy = other.y - this.y;
		if (Math.sqrt(dx*dx + dy*dy) <= (this.influenceRadius + other.influenceRadius))
			return true;
		else
			return false;
	}

	public int getInfluenceRadius() {
		return influenceRadius;
	}
	
	public long getShootingDelay() {
		return 1000;
	}
	
	@Override
	public boolean update(long timespan, GameThread game, List<Projectile> newMovingObjects) {
		this.lastShotDelay += timespan;
		if(this.lastShotDelay > getShootingDelay()) {
			Enemy target = null;
			double distance = Integer.MAX_VALUE;
			for(Enemy enemy : game.getEnemies()){
				double dist = Math.sqrt(Math.pow((this.x - enemy.getX()),2)+Math.pow((this.y - enemy.getY()),2));
				if(dist < distance){
					distance = dist;
					target = enemy;
					distance = dist;
				}
			}
			if (target != null) {
				newMovingObjects.add(new Projectile(x, y, color, 0, -1, target));
				this.lastShotDelay = 0;
			}
		}
		return false;
	}

	public void upradeRed() {
		redLevel += 50;
		if (redLevel > COLOR_MAX_VALUE) redLevel = COLOR_MAX_VALUE;
		refreshColor();
	}

	public void upradeGreen() {
		greenLevel += 50;
		if (greenLevel > COLOR_MAX_VALUE) greenLevel = COLOR_MAX_VALUE;
		refreshColor();
	}

	public void upradeBlue() {
		blueLevel += 50;
		if (blueLevel > COLOR_MAX_VALUE) blueLevel = COLOR_MAX_VALUE;
		refreshColor();
		
	}
	
	private void refreshColor() {
		color = Color.argb(255, redLevel, greenLevel, blueLevel);
	}

	public int getRedLevel() {
		return redLevel;
	}

	public int getGreenLevel() {
		return greenLevel;
	}

	public int getBlueLevel() {
		return blueLevel;
	}

	public boolean canBeUpgraded() {
		return redLevel < COLOR_MAX_VALUE || greenLevel < COLOR_MAX_VALUE || blueLevel < COLOR_MAX_VALUE;
	}
	
}
