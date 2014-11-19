	package callreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.util.Log;

public class SipInComingReceiver extends BroadcastReceiver {
	SipAudioCall sipAudioCall=null;
	private final String TAG="SipInComingReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SipAudioCall.Listener listener=new SipAudioCall.Listener(){
			@Override
			public void onRinging(SipAudioCall call,SipProfile caller){
				Log.d(TAG,"Ringing from remote side");
				// fire ringing event to notify user for incoming call
			}
		};
		
	}

}
