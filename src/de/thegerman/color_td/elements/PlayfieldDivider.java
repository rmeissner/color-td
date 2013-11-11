package de.thegerman.color_td.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import de.thegerman.color_td.GraphicalObject;

public class PlayfieldDivider extends GraphicalObject {

	private float width;
	private float height;

	public PlayfieldDivider(float x, float y, float width, float height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Canvas c) {
		paint.setColor(Color.argb(150, 255, 255, 255));
		float space = height * 6;
		float count = (width - height) / space;
		for (int i = 0; i < count; i++) {
			c.drawCircle(x + (i * space) + height, y, height, paint);
		}
	}

}
