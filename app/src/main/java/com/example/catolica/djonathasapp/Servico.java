package com.example.catolica.djonathasapp;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catolica on 08/10/16.
 */
public class Servico extends Service {
    private static String TAG = "servico";
    public List<Trabalhador> trabalhadores = new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate:");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Trabalhador trabalhador = new Trabalhador(startId);
        trabalhador.start();
        trabalhadores.add(trabalhador);
        Log.i(TAG, "onStartCommand:");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy:");
        for(Trabalhador trabalhador : trabalhadores) {
            trabalhador.ativo = false;
        }
    }

    class Trabalhador extends Thread {
        public int quantidadeExecucao = 0;
        public int startID;
        public boolean ativo = true;

        public Trabalhador(int startID) {
            this.startID = startID;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void sendNotification(String title, String message) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(message);

            Intent resultIntent = new Intent(Servico.this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
        }

        private void vibrate(int during) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(during);
        }

        public void run() {
            while (ativo) {
                try {
                    Thread.sleep(5000);
//                    sendNotification("DjonathasAPP", "Teste de Notificação");
//                    vibrate(500);
                    Log.i(TAG, "Chamando a thread");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(startID);
        }
    }
}
