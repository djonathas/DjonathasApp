package com.example.catolica.djonathasapp;

import android.Manifest;
import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "main";
    private Realm realm;

    private EditText inpNome;
    private EditText inpEmail;
    private EditText inpTelefone;

    private TextView txtLatitude;
    private TextView txtLongitude;

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

        txtLatitude = (TextView)findViewById(R.id.txtLatitudeValue);
        txtLongitude = (TextView)findViewById(R.id.txtLongitudeValue);

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
        if (cont % 3 == 0) {
            new AlertDialog.Builder(activity)
                    .setTitle("Bem-vindo")
                    .setMessage("Olá, não esqueça de compartilhar o aplicativo com seus amigos")
                    .show();
        }

        //iniciando o banco do Realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        minhaPosicao();
    }

    private void callDialog(String message, final String[] permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões necessárias");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, 0);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "A aplicação não funcionará corretamente sem as permissões solicitadas.", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private void minhaPosicao() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("Você precisa dar permissão de acesso a sua localização", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setLocation(location);
//            locationManager.removeUpdates(this);
            Log.i(TAG, "GPS");
        } else if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            setLocation(location);
//            locationManager.removeUpdates(this);
            Log.i(TAG, "Network");
        } else {
            Toast.makeText(MainActivity.this, "Seu GPS e sua rede estão desativos, favor ativá-los.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLocation(Location location) {
        txtLatitude.setText(String.valueOf(location.getLatitude()));
        txtLongitude.setText(String.valueOf(location.getLongitude()));
    }

    private void initConfig() {
        //obtendo os botões
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        Button btnApagar = (Button) findViewById(R.id.btnApagar);
        Button btnRecyclerView = (Button) findViewById(R.id.btnRecyclerView);
        Button btnMinhaLocalizacao = (Button) findViewById(R.id.btnMinhaLocalizao);

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

        btnMinhaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        try {
//            Intent intentService = new Intent(getApplicationContext(), Servico.class);
//            startService(intentService);
        } catch (Exception e) {
            Log.e(TAG, "onStart: " + e.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
