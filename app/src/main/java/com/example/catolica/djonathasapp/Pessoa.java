    package com.example.catolica.djonathasapp;

    import android.content.Context;

    import io.realm.Realm;
    import io.realm.RealmConfiguration;
    import io.realm.RealmObject;
    import io.realm.RealmResults;
    import io.realm.Sort;
    import io.realm.annotations.PrimaryKey;

    /**
     * Created by catolica on 24/09/16.
     */
    public class Pessoa extends RealmObject {
        @PrimaryKey
        private int id;
        private String nome, email;
        private int telefone;
        private static Context contextPessoa;
        private static RealmConfiguration realmConfigPessoa;

        //gera o id por autoincremento
        private int autoIncrementId() {
            //instanciando um objeto realm
            Realm realm = Realm.getInstance(realmConfigPessoa);
            //iniciando a chave inicial
            int key = 1;

            try {
                //obtem uma lista com todos os registros gravados em ordenados pelo ID em ordem
                //decrescente
                RealmResults<Pessoa> itens = realm.where(Pessoa.class).findAll().sort("id", Sort.DESCENDING);
                //verifica se a lista não esta vazia
                if (itens.size() > 0) {
                    //pega o id do primeiro elemento da lista (ultimo cadastrado) e incrementa +1
                    key = itens.get(0).getId() + 1;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //retorno com o proximo id via autoincremento
            return key;
        }

        public Pessoa() {}

        //construtor simples da classe
        public Pessoa(String nome, String email, int telefone) {
            this.nome = nome;
            this.email = email;
            this.telefone = telefone;
        }

        //construtor que recebe um context como parametro para ser utilizado no realm
        public Pessoa(Context context) {
            contextPessoa = context;
            realmConfigPessoa = new RealmConfiguration.Builder(contextPessoa).build();
        }

        /* --- GETs e SETs --- */

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getTelefone() {
            return telefone;
        }

        public void setTelefone(int telefone) {
            this.telefone = telefone;
        }

        public static Context getContextPessoa() {
            return contextPessoa;
        }

        public static void setContextPessoa(Context contextPessoa) {
            Pessoa.contextPessoa = contextPessoa;
        }

        public static RealmConfiguration getRealmConfigPessoa() {
            return realmConfigPessoa;
        }

        public static void setRealmConfigPessoa(RealmConfiguration realmConfigPessoa) {
            Pessoa.realmConfigPessoa = realmConfigPessoa;
        }

        /* --- GETs e SETs --- */

        //salva a instancia da classe usando o realm
        public void save() {
            Realm realm = Realm.getInstance(realmConfigPessoa);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Pessoa pessoa = realm.createObject(Pessoa.class);
                    pessoa.setId(autoIncrementId());
                    pessoa.setNome(getNome());
                    pessoa.setEmail(getEmail());
                    pessoa.setTelefone(getTelefone());
                }
            });
        }

        //obtem uma lista com todos os registros salvos
        public RealmResults<Pessoa> getAllPessoas() {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            return realm.where(Pessoa.class).findAll();
        }

        //obtem um registro a partir do id passado por parametro
        public Pessoa getPessoa(int id) {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            return realm.where(Pessoa.class).equalTo("id", id).findFirst();
        }

        //deleta o registro instanciado do realm
        public void delete() {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            final Pessoa pessoa = this;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    pessoa.deleteFromRealm();
                }
            });
        }

        //salva as alterações da instancia no realm
        public void update() {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(this);
            realm.commitTransaction();
        }
    }
