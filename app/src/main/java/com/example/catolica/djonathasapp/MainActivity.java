package com.example.catolica.djonathasapp;

import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

        //obtendo valores dos inputs (editText)
        inpNome = (EditText) findViewById(R.id.inpNome);
        inpEmail = (EditText) findViewById(R.id.inpEmail);
        inpTelefone = (EditText) findViewById(R.id.inpTelefone);

        //inicializa os botões
        initConfig();

        //inicializa o sharedPreferences para guardar o contador de inicializações
        String PREF_NAME = "preferencia";
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        //verifica se existe a chave "cont" no sharedPreferences, se não houver retorna o valor default (0)
        Integer cont = sharedPreferences.getInt("cont", 0);

        //instanciando o editor do sharedPrecerences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //incrementa o valor do contador em +1
        editor.putInt("cont", cont + 1);
        //salvando as alterações
        editor.apply(); //pode usar .commit também

        Log.i("LOG", "Contador: " + cont.toString());
        //verifica se quantidade no cont é multiplo de 3 para exibir a mensagem, ou seja, a cada
        // 3 inicializações a mensagem será exibida
        if(cont % 3 == 0) {
            new AlertDialog.Builder(activity)
                    .setTitle("Bem-vindo")
                    .setMessage("Olá, não esqueça de compartilhar o aplicativo com seus amigos")
                    .show();
        }

        //iniciando o banco do Realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);
    }

    private void initConfig() {
        //obtendo os botões
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        Button btnApagar = (Button) findViewById(R.id.btnApagar);
        Button btnRecyclerView = (Button) findViewById(R.id.btnRecyclerView);

        //evento ao clicar no botão salvar
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Criando uma instancia da classe pessoa
                Pessoa pessoa = new Pessoa(getApplicationContext());

                //setando na instancia da classe os valores dos campos
                pessoa.setNome(inpNome.getText().toString());
                pessoa.setEmail(inpEmail.getText().toString());
                pessoa.setTelefone(Integer.parseInt(inpTelefone.getText().toString()));

                //salvando os dados da classe via realm
                pessoa.save();

                //imprimindo um alerta com a informação da quantidade de cadastros existentes
                new AlertDialog.Builder(activity)
                        .setTitle("Salvo com sucesso!")
                        .setMessage("Quant. de cadastros: " + String.valueOf(pessoa.getAllPessoas().size()))
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

        //chama a recyclerview
        btnRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                startActivity(intent);
            }
        });
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
