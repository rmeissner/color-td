package de.thegerman.color_td.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.thegerman.color_td.GameThread;
import de.thegerman.color_td.elements.Enemy;
import de.thegerman.color_td.elements.Tower;
import de.thegerman.color_td.elements.Projectile;

public abstract class Level {
	
	protected abstract void spawnEnemies(long timespan, GameThread game);
	
	protected void updateEnemies(long timespan, GameThread game) {
		List<Enemy> enemies = game.getEnemies();

		List<Enemy> newMovingObjects = new ArrayList<Enemy>();
		
		for (Iterator<Enemy> iter = enemies.iterator(); iter.hasNext();) {
			Enemy enemy = iter.next();
			if (enemy.update(timespan, game, newMovingObjects)) {
				iter.remove();
			}
		}
		
		enemies.addAll(newMovingObjects);
	}
	
	protected void updateTowers(long timespan, GameThread game) {
		List<Tower> towers = game.getTowers();
		List<Projectile> projectiles = game.getProjectiles();
		
		List<Projectile> newMovingObjects = new ArrayList<Projectile>();
		
		for (Tower tower : towers) {
			tower.setInfluenceRadiusVisbility(false);
			tower.update(timespan, game, newMovingObjects);
		}

		for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
			Projectile projectile = iter.next();
			if (projectile.update(timespan, game, newMovingObjects)) {
				iter.remove();
			}
		}
		
		projectiles.addAll(newMovingObjects);
	}

	public void update(long timespan, GameThread game) {
		spawnEnemies(timespan, game);
		updateEnemies(timespan, game);
		updateTowers(timespan, game);	
		collisionDetection(game);
	}

	private void collisionDetection(GameThread game) {
		enemies: for (Iterator<Enemy> enemyIter = game.getEnemies().iterator(); enemyIter.hasNext();) {
			Enemy enemy = enemyIter.next();
			projectiles: for (Iterator<Projectile> projectileIter = game.getProjectiles().iterator(); projectileIter.hasNext();) {
				Projectile projectile = projectileIter.next();
				if (enemy.colidesWithCircle(projectile)) {
					projectileIter.remove();
					if (enemy.takeDamage(projectile)) {
						enemyIter.remove();
						continue enemies;
					}
					continue projectiles;
				}
			}
		}
	}

}
