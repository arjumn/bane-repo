package com.threeprimeevils.andriodmini;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;

public class AndroidMini extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupGesture();
		MapView mapView = (MapView)findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true); 
	}
    
    private void setupGesture() {
	    GestureLibrary mlib = GestureLibraries.fromRawResource(this.getApplicationContext(), R.raw.gestures);
        if(!mlib.load())
            return;
        GestureOverlayView gestureView = (GestureOverlayView)findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(new MyGesturePerformedListener(mlib, this));
    }
    
    private class MyGesturePerformedListener implements GestureOverlayView.OnGesturePerformedListener
    {
        private GestureLibrary mlib;
        private Activity me;
        public MyGesturePerformedListener (GestureLibrary lib, Activity he)
        {
            mlib = lib;
            me = he;
        }

        public void onGesturePerformed(GestureOverlayView view, Gesture gesture)
        {
            List<Prediction> predictions = mlib.recognize(gesture);
            for(Prediction predict : predictions)
            {
                if(predict.score > 1.0)
                {
                    me.finish();
                }
            }
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}