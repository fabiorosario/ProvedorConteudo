package fabiorosario.ime.pgsc.exercicioprovedorconteudo;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;

public class ContatosServico extends Service {

    private final IBinder mBinder = new ContatosServico.MyBinder();
    public class MyBinder extends Binder {
        ContatosServico getService(){
            return ContatosServico.this;
        }
    }
    public ContatosServico() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void inserirContato(Activity activity, String nome, String telefone){
        Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, nome);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, telefone);
        activity.startActivity(intent);
    }

    public Boolean buscarContato(String telefone){

        Uri endereco = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(telefone));

        Cursor cursor = getContentResolver().query(endereco,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst())
                return true;
        }
        return false;
    }
}