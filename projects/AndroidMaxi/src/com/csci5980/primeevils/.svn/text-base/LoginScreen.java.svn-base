package com.csci5980.primeevils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.csci5980.primeevils.SessionEvents.AuthListener;
import com.facebook.android.Facebook;

public class LoginScreen extends Activity{
	
	public static final String APP_ID = "118362694909509";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LoginButton mLoginButton;
	    Facebook mFacebook;
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        mFacebook = new Facebook(APP_ID);
       	SessionStore.restore(mFacebook, this);
       	if(mFacebook.isSessionValid()){
       		showMainscreen();
       	}else{
       		SessionEvents.addAuthListener(new FacebookAuthListener());
       	}
   		mLoginButton.init(this, mFacebook);
       		
	}
	
	public void showMainscreen(){
		Intent mainIntent = new Intent(LoginScreen.this, SwissRoll.class);
        startActivityForResult(mainIntent, 0);
	}
	
	public class FacebookAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	showMainscreen();
        }

        public void onAuthFail(String error) {
            
        }
    }
}
