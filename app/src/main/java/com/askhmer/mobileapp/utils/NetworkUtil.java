package com.askhmer.mobileapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	
  //현재 사용중인 네트워크 타입 얻기
  public static int getNetworkType(Context context){
      ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

      if(networkInfo == null){
          return  -1;
      }else{
          return networkInfo.getType();
      }
  }

  //현재 네트워크 상태 값을 얻기
  public static NetworkInfo.State getNetworkState(Context context){
      ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

      if(networkInfo == null ){
          return NetworkInfo.State.UNKNOWN;
      }
      return connectivityManager.getActiveNetworkInfo().getState();
  }
  
  //인터넷 연결 체크
  public static boolean wifi3GCheck(Context context) {
      boolean check = false;

      ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

      if(mobile.isConnected() || wifi.isConnected()){
          check = true;
      }
      return check;
  }

}
