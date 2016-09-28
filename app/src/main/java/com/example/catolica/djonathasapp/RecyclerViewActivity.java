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

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.pessoas_recyclerview);
        recyclerView.setHasFixedSize(true);

//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //colocando pra mostrar em forma de grid
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        recyclerView.setLayoutManager(gridLayoutManager);

        Pessoa pessoaStatic = new Pessoa(getApplicationContext());
        final RealmResults<Pessoa> pessoas = pessoaStatic.getAllPessoas();
        pessoaAdapter = new PessoaAdapter(getApplicationContext(), pessoas, new PessoaAdapter.PessoaOnClickListener() {
            @Override
            public void onClickEvent(View view, int index) {
                Intent intent = new Intent(getApplicationContext(), PessoaActivity.class);
                Pessoa pessoa = pessoas.get(index);

                eventBus.postSticky(pessoa);

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(pessoaAdapter);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        eventBus.register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        eventBus.unregister(this);
//        super.onStop();
//    }
}
