package com.geekyouup.android.thecleaner;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Cleaner extends Activity {
    private RelativeLayout mView;
	private WallpaperManager mWPM;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mView = (RelativeLayout) findViewById(R.id.mainview);
        mView.setFocusableInTouchMode(false);

        //hide system bar for honeycomb+ devices
    	if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        mWPM = WallpaperManager.getInstance(this);
        
        Button upgradeButton = (Button) findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.geekyouup.android.thecleanerpro"));
		        startActivity(marketIntent);
			}
		});
        
        Animation am = AnimationUtils.loadAnimation(this, R.anim.launchanim);
        am.setFillEnabled(true);
        am.setFillAfter(true);
        //launchImage.startAnimation(am);
        upgradeButton.startAnimation(am);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
        
    	AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setDuration(1000);
        mView.startAnimation(aa);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
   
    float xPos = 0;
    float xWP = 0.5f;
    float yPos = 0;
    float yWP = 0.5f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		xPos = event.getX();
    		yPos = event.getY();
    		//kill app if touched in bottom right corner
    		if(xPos > mView.getWidth()-50 && event.getY()>mView.getHeight()-50) finish();
    		else
    		{
    			mWPM.sendWallpaperCommand(mView.getWindowToken(), WallpaperManager.COMMAND_TAP, (int) xPos, (int) event.getY(), 0, null);
    		}
    	}else if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		xWP = xWP + (xPos-event.getX())/500;
    		xPos = event.getX();
    		if(xWP < 0) xWP = 0;
    		if(xWP > 1) xWP = 1;
    		
    		yWP = yWP + (yPos-event.getY())/500;
    		yPos = event.getY();
    		if(yWP < 0) yWP = 0;
    		if(yWP > 1) yWP = 1;
    		mWPM.setWallpaperOffsets(mView.getWindowToken(), xWP, yWP);
    	}
    	return super.onTouchEvent(event);
    }
    
}