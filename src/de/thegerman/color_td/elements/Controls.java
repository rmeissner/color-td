package de.thegerman.color_td.elements;

import static de.thegerman.color_td.GameView.COLOR_MAX_VALUE;
import android.graphics.Canvas;
import android.graphics.Color;
import de.thegerman.color_td.GraphicalObject;

public class Controls extends GraphicalObject {

	private float width;
	private float height;
	private Tower tower = null;
	
	public Controls(float x, float y, float width, float height) {
		super(x, y);
		this.paint.setColor(Color.argb(150, 200, 200, 200));
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Canvas c) {
		paint.setColor(Color.argb(150, 200, 200, 200));
		c.drawRect(x, y, x+width, y+height, paint);
		if (tower != null) {
			float btnWidth = width / 3;
			if (tower.getRedLevel() < COLOR_MAX_VALUE) {
  			paint.setColor(Color.RED);
  			c.drawRect(x, y, x+btnWidth, y+height, paint);
			}
			if (tower.getGreenLevel() < COLOR_MAX_VALUE) {
  			paint.setColor(Color.GREEN);
  			c.drawRect(x+btnWidth, y, x+2*btnWidth, y+height, paint);
			}
			if (tower.getBlueLevel() < COLOR_MAX_VALUE) {
  			paint.setColor(Color.BLUE);
  			c.drawRect(x+2*btnWidth, y, x+width, y+height, paint);
			}
		}
	}
	
	public boolean containsPoint(float ox, float oy) {
		if (ox < x || ox > (x + width) || oy < y || oy > (oy + height)) return false;
		return true;
	}

	public void deactiveTowerMenu() {
		tower = null;
	}

	public void activeTowerMenu(Tower tower) {
		this.tower = tower;
	}

	public void performActionOnTower(float xpos, float ypos) {
		if (tower != null) {
			float btnWidth = width / 3;
			if (xpos >= x && xpos <= btnWidth) {
				tower.upradeRed();
			} else if (xpos > btnWidth && xpos <= 2*btnWidth) {
				tower.upradeGreen();
			} else if (xpos > 2*btnWidth && xpos <= width) {
				tower.upradeBlue();
			}
		}
	}

}
