package pe.jrivera6.reconocimientofacialapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import pe.jrivera6.reconocimientofacialapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    private SharedPreferences sharedPreferences;
    private CardView cardCamera;
    private CardView cardGrafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username",null);
        Log.d(TAG, "onCreate: "+username);

        cardCamera = findViewById(R.id.card_camera);
        cardGrafico = findViewById(R.id.card_grafico);

        cardCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity();
            }
        });

        cardGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraficaActivity();
            }
        });

    }

    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toasty.info(this,"Presionar dos veces para salir",Toast.LENGTH_SHORT,true).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    public void cerrarSesion(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        boolean success = editor.putBoolean("islogged", false).commit();
        boolean success = editor.clear().commit();
        goToLoginActivity();
        finish();
    }


    private void goToLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cerrar_sesion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                cerrarSesion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void CameraActivity() {

        Intent i = new Intent(MainActivity.this, CamaraResultActivity.class);
        startActivity(i);
    }

    private void GraficaActivity(){
        Intent intent = new Intent(MainActivity.this,GraficaActivity.class);
        startActivity(intent);
    }

}
