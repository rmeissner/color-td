package de.thegerman.color_td;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.thegerman.color_td.elements.Controls;
import de.thegerman.color_td.elements.Enemy;
import de.thegerman.color_td.elements.PlayfieldDivider;
import de.thegerman.color_td.elements.Projectile;
import de.thegerman.color_td.elements.Tower;
import de.thegerman.color_td.levels.Level;
import de.thegerman.color_td.levels.Level01;

public class GameThread extends Thread {
	private static final int MAXFPS = 60;
	private long lastRunTime;
	private boolean alive = true;
	private int height;
	private int width;
	private SurfaceHolder mSurfaceHolder;
	private Context context;
	private float ratio;
	private GraphicalObject divider;
	private Controls controls;
	private int newTowerPointerId = -1;
	private Tower newTower;
	private List<Tower> towers = Collections.synchronizedList(new ArrayList<Tower>());
	private List<Enemy> enemies = Collections.synchronizedList(new ArrayList<Enemy>());
	private List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<Projectile>());
	private Level level;
	private boolean menuActive;

	public GameThread(Context context, SurfaceHolder holder) {
		this.mSurfaceHolder = holder;
		this.context = context;
		divider = new PlayfieldDivider(0, 0, GameView.WIDTH, 5);
		controls = new Controls(0, 0, GameView.WIDTH, GameView.CONTROL_HEIGHT);
		level = new Level01();
	}

	@Override
	public void run() {
		lastRunTime = System.currentTimeMillis();
		while (this.alive) {
			if (this.height > 0 && this.width > 0) {
				Canvas c = null;
				try {
					doPhysics();
					synchronized (this.mSurfaceHolder) {
						c = this.mSurfaceHolder.lockCanvas();
						if(c == null)continue; 
						c.scale(this.ratio, this.ratio);
						doDraw(c);
					}
				} finally {
					if (c != null) {
						this.mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
			try {
				GameThread.sleep((long) (1000 / MAXFPS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void doDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		divider.draw(canvas);
		controls.draw(canvas);
		
		for (Tower tower : towers) {
			tower.draw(canvas);
		}
		
		for (Projectile projectile : projectiles) {
			projectile.draw(canvas);
		}
		
		for (Enemy enemy : enemies) {
			enemy.draw(canvas);
		}
		
		if (newTower != null) {
			newTower.draw(canvas);
		}
	}

	private void doPhysics() {
		long timespan = Math.min(System.currentTimeMillis() - lastRunTime, GameView.MAX_TIMESPAN);

		level.update(timespan, this);
		
		if (newTower != null) {
			checkNewTowerState();
		}
		
		lastRunTime = System.currentTimeMillis();
	}

	private void checkNewTowerState() {
		boolean error = false;
		if (newTower.getX() < newTower.getInfluenceRadius() || newTower.getX() + newTower.getInfluenceRadius() > GameView.WIDTH ||
				newTower.getY() < newTower.getInfluenceRadius() + height/3 || newTower.getY() + newTower.getInfluenceRadius() > height - GameView.CONTROL_HEIGHT) {
			error = true;
		} else {
			for (Tower tower : towers) {
				if (newTower.interfers(tower)) {
					error = true;
					tower.setInfluenceRadiusVisbility(true);
				}
			}
		}
		newTower.setBuildError(error);
	}

	public boolean handleTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int sc = action & MotionEvent.ACTION_MASK;
		switch (sc) {
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_UP: {
			int pc = event.getPointerCount();
			for (int i = 0; i < pc; i++) {
				if (event.getPointerId(i) == newTowerPointerId && newTower != null) {
					if (newTower.canBeBuild()) {
						towers.add(newTower);
						newTower.setBuildMode(false);
					}
					newTower = null;
					newTowerPointerId = -1;
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN: {
			int pc = event.getPointerCount();
			for (int i = 0; i < pc; i++) {
				if (controls.containsPoint(event.getX(i) / this.ratio, event.getY(i) / this.ratio)) {
					if (menuActive) {
						controls.performActionOnTower(event.getX(i) / this.ratio, event.getY(i) / this.ratio);
						deactivateMenuForTowers();
					} else {
  					newTowerPointerId = event.getPointerId(i);
  					newTower = new Tower(event.getX(i) / this.ratio, event.getY(i) / this.ratio);
  					newTower.setBuildMode(true);
					}
				} else if (menuActive){
					deactivateMenuForTowers();
				} else {
					for (Tower tower : towers) {
						if (tower.containsPoint(event.getX(i) / this.ratio, event.getY(i) / this.ratio) && tower.canBeUpgraded()) {
							activateMenuForTower(tower);
						}
					}
				}
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int pc = event.getPointerCount();
			for (int i = 0; i < pc; i++) {
				if (event.getPointerId(i) == newTowerPointerId && newTower != null) {
					newTower.setPosition(event.getX(i) / this.ratio, event.getY(i) / this.ratio);
				}
			}
			break;
		}
		}
		return true;
	}

	private void deactivateMenuForTowers() {
		menuActive = false;
		controls.deactiveTowerMenu();
	}

	private void activateMenuForTower(Tower tower) {
		menuActive = true;
		controls.activeTowerMenu(tower);
	}

	public void setSurfaceSize(int width, int height) {
		synchronized (mSurfaceHolder) {
			if (this.width != width || this.height != height) {
				this.ratio = ((float) width) / GameView.WIDTH;
				this.width = (int) (width / this.ratio);
				this.height = (int) (height / this.ratio);
				divider.setY(this.height / 3);
				controls.setY(this.height - GameView.CONTROL_HEIGHT);
				// this.gameHeight = height/this.ratio;
				// this.pauseScreen.setHeight(this.gameHeight);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public List<Tower> getTowers() {
		return towers;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}
}
