package com.example.catolica.djonathasapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmResults;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PessoaAdapter pessoaAdapter;

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

        Pessoa pessoa = new Pessoa(getApplicationContext());
        final RealmResults<Pessoa> pessoas = pessoa.getAllPessoas();
        pessoaAdapter = new PessoaAdapter(getApplicationContext(), pessoas, new PessoaAdapter.PessoaOnClickListener() {
            @Override
            public void onClickEvent(View view, int index) {
                Intent intent = new Intent(getApplicationContext(), PessoaActivity.class);
                intent.putExtra("id", pessoas.get(index).getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(pessoaAdapter);
    }
}
