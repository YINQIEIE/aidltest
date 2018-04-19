package com.yq.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class IRemoteService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private IMyAidlInterface.Stub myBinder = new IMyAidlInterface.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int add(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }
    };

}
