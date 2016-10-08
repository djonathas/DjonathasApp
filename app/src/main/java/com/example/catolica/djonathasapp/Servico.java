package com.example.catolica.djonathasapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
        Log.e(TAG, "onCreate:");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Trabalhador trabalhador = new Trabalhador(startId);
        trabalhador.start();
        trabalhadores.add(trabalhador);
        Log.e(TAG, "onStartCommand:");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:");
        for(Trabalhador trabalhador : trabalhadores) {
            trabalhador.ativo = false;
        }
    }

    class Trabalhador extends Thread {
        private static final String TAG = "trabalhador";
        public int quantidadeExecucao = 0;
        public int startID;
        public boolean ativo;

        public Trabalhador(int startID) {
            this.startID = startID;
        }

        public void start() {
        }

        public void run() {
            while (ativo) {
                try {
                    Thread.sleep(600000);
                    Log.e(TAG, "Chamando a thread");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(startID);
        }
    }
}
