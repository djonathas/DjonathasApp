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

        private int autoIncrementId() {
            Realm realm = Realm.getInstance(realmConfigPessoa);
            int key = 1;

            try {
                RealmResults<Pessoa> itens = realm.where(Pessoa.class).findAll().sort("id", Sort.DESCENDING);
                if (itens.size() > 0)
                    key = itens.get(0).getId() + 1;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return key;
        }

        public Pessoa(String nome, String email, int telefone) {
            this.nome = nome;
            this.email = email;
            this.telefone = telefone;
        }

        public Pessoa(Context context) {
            contextPessoa = context;
            realmConfigPessoa = new RealmConfiguration.Builder(contextPessoa).build();
        }

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

        public RealmResults<Pessoa> getAllPessoas() {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            return realm.where(Pessoa.class).findAll();
        }

        public Pessoa getPessoa(int id) {
            Realm realm = Realm.getInstance(realmConfigPessoa);

            return realm.where(Pessoa.class).equalTo("id", id).findFirst();
        }
    }
