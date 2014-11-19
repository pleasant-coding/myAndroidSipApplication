package kBase;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;

public class BaseSip  {

	private static final String TAG = "BaseSip";
	private static BaseSip mBaseSip=null;
	public SipManager mSipManager=null;
	public SipProfile mSipProfile=null;
	public KProfile kProfile=null;
	/**
	 * Singleton class
	 */
	
	private BaseSip(){}
	
	public static BaseSip getInstance(){
		if(mBaseSip!=null)
			return mBaseSip;
		else
			return new BaseSip();
	}
	
	
	public SipManager getSipManager(Context mContext){
		if(mSipManager!=null)
			return mSipManager;
		else return mSipManager=SipManager.newInstance(mContext);
	}
	
	
	public SipProfile getSipProfile(Context mContext,String username,String password,String serverProxy){
		try{
		SipProfile.Builder builder=new SipProfile.Builder(username.trim(),password.trim());
	//	builder.setAuthUserName(username);
		builder.setDisplayName(username);
	//	builder.setOutboundProxy(serverProxy);
		builder.setPassword(password);
		builder.setProfileName(username);
		builder.setSendKeepAlive(true);
		
		
		mSipProfile=builder.build();
		}catch(Exception sipException) {
			sipException.printStackTrace();
		}
	return mSipProfile;
	}
	
	
	public KProfile getUserBaseProfile(){
		
		if(kProfile!=null)return kProfile;
		else return new KProfile();
	
	}
	
	/*
	 * Open profile for making audio and video call
	 */
	
	public SipManager openLocalProfile(Context mContext,SipProfile mSipProfile) throws SipException{
		
		Intent intent=new Intent();
		intent.setAction("kBase INCOMING_CALL");
		PendingIntent pi= PendingIntent.getBroadcast(mContext, 0, intent, Intent.FILL_IN_DATA);
		mSipManager.open(mSipProfile,pi,null);
		
		return mSipManager;
	}
	
	/**
	 * Register Registration Listener 
	 * @throws SipException 
	 */
	
	public void setRegistrationListener(SipManager mSipManager,SipProfile mSipProfile) throws SipException{
		mSipManager.setRegistrationListener(mSipProfile.getUriString(),new SipRegistrationListener(){

			@Override
			public void onRegistering(String localProfileUri) {
				// TODO Auto-generated method stub
				//fire event for Registering
				Log.d(TAG,"Registering ....");
			}

			@Override
			public void onRegistrationDone(String localProfileUri,
					long expiryTime) {
				// TODO Auto-generated method stub
				//fire event for registration success
				Log.d(TAG,"Registration Done");
			}

			@Override
			public void onRegistrationFailed(String localProfileUri,
					int errorCode, String errorMessage) {
				// TODO Auto-generated method stub
				Log.d(TAG,"Registration fails with SipErrorCode "+ errorCode +" , SipError Message "+errorMessage);
			}
			
		});
		
	}
	
	public SipAudioCall.Listener setAudioCallListener(){
		SipAudioCall.Listener audiocallListener=null;
		audiocallListener=new SipAudioCall.Listener(){
			@Override
			public void onCallEstablished(SipAudioCall call) {
			    //fire event for audiocall started  
				
				call.startAudio();
			      //call.setSpeakerMode(true);
			      //call.toggleMute();
			        
			   }
			@Override
			public void onCallEnded(SipAudioCall call) {
			      // Do something.
				// fire event for Call Ended 
			}


		};
	
		return audiocallListener;
	}

	
	
	
	public boolean closeRegisterdProfile(SipProfile mSipProfile,SipManager mSipManager) throws SipException{
		if(mSipManager==null)
			return false;
		else{
			if(mSipProfile!=null){
				mSipManager.close(mSipProfile.getUriString());
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	public SipAudioCall makeCall(SipManager mSipManager,SipProfile mSipProfile,String calleSipAddress,SipAudioCall.Listener listener ,int timeout) throws SipException{
	SipAudioCall mSipAudioCall=null;
		if(mSipManager!=null &&  mSipProfile!=null && listener != null && calleSipAddress!=null && !calleSipAddress.isEmpty()){
			mSipAudioCall=	mSipManager.makeAudioCall(mSipProfile.getUriString(), calleSipAddress, listener, timeout);
			
		}
	if(mSipAudioCall!=null){
		return mSipAudioCall;
	}else
		return null;
	}

}

