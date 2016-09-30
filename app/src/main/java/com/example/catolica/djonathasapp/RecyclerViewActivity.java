package com.example.catolica.djonathasapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmResults;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PessoaAdapter pessoaAdapter;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        init();
    }

    //inicia os dados para exibição da recyclerview
    private void init() {
        //obtem o objeto do tipo RecyclerView presente no layout
        recyclerView = (RecyclerView) findViewById(R.id.pessoas_recyclerview);
        //seta se recyclerview terá tamanho fixo (não varia com o conteudo)
        recyclerView.setHasFixedSize(true);

        //instanciando o tipo de layout que será utilizado pela recyclerview
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new LinearLayoutManager(this);

        //setando o tipo de layout
        recyclerView.setLayoutManager(layoutManager);

        //colocando pra mostrar em forma de grid
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        recyclerView.setLayoutManager(gridLayoutManager);

        //instanciando um objeto pessoa apenas para utilizar alguns metodos
        Pessoa pessoaStatic = new Pessoa(getApplicationContext());
        //obtendo a lista completa de pessoas
        final RealmResults<Pessoa> pessoas = pessoaStatic.getAllPessoas();
        //instanciando um objeto adapter que realizará o loop de preenchimento dos cards
        pessoaAdapter = new PessoaAdapter(getApplicationContext(), pessoas, new PessoaAdapter.PessoaOnClickListener() {
            //evento que será disparado ao clicar em um card
            @Override
            public void onClickEvent(View view, int index) {
                //instanciando a activity da tela dos detalhes de pessoa
                Intent intent = new Intent(getApplicationContext(), PessoaActivity.class);
                //obtendo o objeto da pessoa selecionada
                Pessoa pessoa = pessoas.get(index);
                //passando os dados para a outra tela via eventBus
                eventBus.postSticky(pessoa);
                //iniciando a activity
                startActivity(intent);
            }
        });
        //setando o adapter na recyclerview
        recyclerView.setAdapter(pessoaAdapter);
    }
}
