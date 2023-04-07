package fabiorosario.ime.pgsc.exercicioprovedorconteudo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ContatosServico contatosServico;
    Intent intent;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ContatosServico.MyBinder b = (ContatosServico.MyBinder)service;
            contatosServico = b.getService();
            Toast.makeText(MainActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            contatosServico = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonCadastrarContato = findViewById(R.id.buttonCadastrarContato);
        buttonCadastrarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefone = ((EditText)findViewById(R.id.idTelefone)).getText().toString();
                if (contatosServico.buscarContato(telefone))
                    Toast.makeText(MainActivity.this, "Contato já cadastrado", Toast.LENGTH_SHORT).show();
                else{
                    String nome = ((EditText)findViewById(R.id.idNome)).getText().toString();
                    contatosServico.inserirContato(MainActivity.this, nome, telefone);
                }
            }
        });

        ActivityCompat.requestPermissions(this,
               new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onResume(){
        super.onResume();
        intent = new Intent(this, ContatosServico.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unbindService(serviceConnection);
    }
}