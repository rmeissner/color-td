package de.thegerman.color_td.levels;

import static de.thegerman.color_td.GameView.COLOR_BASE_VALUE;
import android.graphics.Color;
import de.thegerman.color_td.GameThread;
import de.thegerman.color_td.elements.Enemy;

public class Level01 extends Level {

	private long lastEnemySpawn;
	private long spawnDelay = 5000;
	private int currentColorIndex = 0;

	@Override
	protected void spawnEnemies(long timespan, GameThread game) {
		lastEnemySpawn += timespan;
		if (lastEnemySpawn > spawnDelay) {
			int radius = 20;
			float spawnX = (float) ((Math.random() * (game.getWidth() - radius)) + radius);
			int color;
			switch (currentColorIndex) {
			case 0:
				color = Color.argb(255, COLOR_BASE_VALUE, 0, 0);
				break;
			case 1:
				color = Color.argb(255, 0, 0, COLOR_BASE_VALUE);
				break;
			case 2:
			default:
				color = Color.argb(255, 0, COLOR_BASE_VALUE, 0);
				break;
			}
			game.getEnemies().add(new Enemy(spawnX, radius, color, 0, 1));
			currentColorIndex = (currentColorIndex + 1) % 3;
			lastEnemySpawn = 0;
		}
	}

}
