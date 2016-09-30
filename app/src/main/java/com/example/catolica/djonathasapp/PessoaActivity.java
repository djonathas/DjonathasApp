    package com.example.catolica.djonathasapp;

    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.greenrobot.eventbus.EventBus;
    import org.greenrobot.eventbus.Subscribe;
    import org.greenrobot.eventbus.ThreadMode;

    public class PessoaActivity extends AppCompatActivity {
        private EventBus eventBus = EventBus.getDefault();
        static final int REQUEST_IMAGE_CAPTURE = 1;
        private TextView txtNome;
        private TextView txtEmail;
        private TextView txtTelefone;
        private ImageView imgUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pessoa);

            final Context context = getApplicationContext();

            //obtendo os campos onde serão impressos as informações do formulario e a area da imagem
            txtNome = (TextView) findViewById(R.id.txtNomeValor);
            txtEmail = (TextView) findViewById(R.id.txtEmailValor);
            txtTelefone = (TextView) findViewById(R.id.txtTelefoneValor);
            imgUser = (ImageView) findViewById(R.id.imgUser);

            //evento ao clicar na area da imagem
            imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkCameraHardware(context)) {
                        dispatchTakePictureIntent();
                    } else {
                        Toast toast = Toast.makeText(context, getString(R.string.msgSemCamera), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            //setando evento ao clicar no TextView de email
            //a ação será abrir o cliente de e-mail do celular com os dados de destinatario,
            // assunto e corpo da mensagem preenchidos
            txtEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse(String.format("mailto:%s?subject=%s&body=%s",
                                    txtEmail.getText().toString(),
                                    getString(R.string.msgAssuntoEmail),
                                    getString(R.string.msgCorpoEmail))));
                    startActivity(intent);
                }
            });

            //setando evento ao clicar no TextView de telefone
            //a ação será abrir o discador do celular com o telefone ja inserido
            txtTelefone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = String.format("tel:%s", txtTelefone.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(number));
                    startActivity(intent);
                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
            eventBus.register(this);
        }

        @Override
        protected void onStop() {
            eventBus.unregister(this);
            super.onStop();
        }

        //evento invocado quando o método startActivityForResult é chamado
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            //condicional para verificar se a imagem foi capturada
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgUser.setImageBitmap(imageBitmap);
            }
        }

        //metodo reage ao eventBus enviado na tela anterior e recebe os dados enviados
        @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
        public void onPessoaEvent(final Pessoa pessoa) {
            //setando os dados da pessoa selecionada na tela
            txtNome.setText(pessoa.getNome());
            txtEmail.setText(pessoa.getEmail());
            txtTelefone.setText(String.valueOf(pessoa.getTelefone()));

            //obtendo o botão compartilhar
            Button btnCompartilhar = (Button) findViewById(R.id.btnCompartilhar);

            //setando o evento de clique do botão
            //abre o recurso de compartilhar texto do android, enviando o nome da pessoa como parametro
            btnCompartilhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, pessoa.getNome());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
        }

        // Verifica se o dispositivo tem camera
        private boolean checkCameraHardware(Context context) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }

        //abre a camera para tirar a foto
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //verifica se a foto foi tirada
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
