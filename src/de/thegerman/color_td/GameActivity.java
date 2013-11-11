package de.thegerman.color_td;


import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class GameActivity extends Activity {
	@Override
  public void onCreate(Bundle savedInstanceState) {
  	super.onCreate(savedInstanceState);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      setContentView(new GameView(this)); 
	}
}
