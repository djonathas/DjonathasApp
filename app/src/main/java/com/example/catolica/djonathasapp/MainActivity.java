package com.example.catolica.djonathasapp;

import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
//    private EventBus eventBus = EventBus.getDefault();
    private Realm realm;

    private EditText inpNome;
    private EditText inpEmail;
    private EditText inpTelefone;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        inpNome = (EditText) findViewById(R.id.inpNome);
        inpEmail = (EditText) findViewById(R.id.inpEmail);
        inpTelefone = (EditText) findViewById(R.id.inpTelefone);

        initConfig();

        String PREF_NAME = "preferencia";
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Integer cont = sharedPreferences.getInt("cont", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cont", cont + 1);
        editor.apply(); //pode usar .commit também

        Log.i("LOG", "Contador: " + cont.toString());
        if(cont % 3 == 0) {
            new AlertDialog.Builder(activity)
                    .setTitle("Bem-vindo")
                    .setMessage("Olá, não esqueça de compartilhar o aplicativo com seus amigos")
                    .show();
        }

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);
    }

    private void initConfig() {
        //obtendo os botões e campos de texto
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        Button btnApagar = (Button) findViewById(R.id.btnApagar);

//        btnSalvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tela1 = new Intent(getApplicationContext(), Tela1Activity.class);
//
//                eventBus.postSticky(new Pessoa(inpNome.getText().toString(), inpEmail.getText().toString(), Integer.parseInt(inpTelefone.getText().toString())));
//
//                startActivity(tela1);
//            }
//        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pessoa pessoa = new Pessoa(getApplicationContext());
                pessoa.setNome(inpNome.getText().toString());
                pessoa.setEmail(inpEmail.getText().toString());
                pessoa.setTelefone(Integer.parseInt(inpTelefone.getText().toString()));
                pessoa.save();

                new AlertDialog.Builder(activity)
                        .setTitle("Salvo com sucesso!")
                        .setMessage("Quant. de cadastros: " + String.valueOf(Pessoa.getAllPessoas().size()))
                        .show();
            }
        });

        //apaga os dados dos 3 campos
        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inpNome.setText("");
                inpEmail.setText("");
                inpTelefone.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        eventBus.register(this);
    }

    @Override
    protected void onStop() {
//        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Pessoa event) {
        Log.i("LOG", "onMessageEvent: " + event.getNome());
    }
}
