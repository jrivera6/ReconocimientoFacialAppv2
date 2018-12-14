package pe.jrivera6.reconocimientofacialapp.activities;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Toast;

import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.adapters.RostroAdapter;
import pe.jrivera6.reconocimientofacialapp.models.Captura;
import pe.jrivera6.reconocimientofacialapp.models.ResponseMessage;
import pe.jrivera6.reconocimientofacialapp.models.Rostro;
import pe.jrivera6.reconocimientofacialapp.repositories.FaceRepository;
import pe.jrivera6.reconocimientofacialapp.services.ApiService;
import pe.jrivera6.reconocimientofacialapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceResultActivity extends AppCompatActivity {

    private static final String TAG = "FaceResultActivity";
    
    private RecyclerView recyclerView;
    private FloatingActionButton fbtnGuardar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_result);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.recycle_rostros);
        fbtnGuardar = findViewById(R.id.floatingActionButton);
        fbtnGuardar.setScaleX(0);
        fbtnGuardar.setScaleX(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            fbtnGuardar.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(1000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }

        rostrosDetectados();

        fbtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDatos();


            }
        });

    }


    public void rostrosDetectados(){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RostroAdapter rostroAdapter = new RostroAdapter();

        recyclerView.setAdapter(rostroAdapter);

    }



    public void obtenerDatos(){

        Bundle extras = getIntent().getExtras();
        final String nombre = extras.getString("nombre_captura");
        Log.d(TAG, "obtenerDatos: "+nombre);

        ApiService apiService = ApiServiceGenerator.createService(ApiService.class);
        Call<List<Captura>> call = apiService.getCapturas();
        call.enqueue(new Callback<List<Captura>>() {
                @Override
                public void onResponse(Call<List<Captura>> call, Response<List<Captura>> response) {

                    try {

                        if (response.isSuccessful()) {

                            List<Captura> capturas = response.body();
                            enviarRostrosServicio(capturas,nombre);


                        } else {
                            Log.e(TAG, "onResponse: "+ response.errorBody().string());
                            throw new Exception("Error en el servicio");
                        }

                    }catch (Throwable t){
                        try {
                            Log.e(TAG, "onThrowable Mensaje Negativo: " + t.toString(), t);
                        } catch (Throwable x) {
                        }
                    }

                }

            @Override
            public void onFailure(Call<List<Captura>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

    }

    public void enviarRostrosServicio(List<Captura> capturas, String nombre_captura){

        Log.d(TAG, "enviarRostrosServicio: LLEGO");
        Captura captura = null;
        List<Rostro> rostros = FaceRepository.rostroLista;
        SharedPreferences.Editor editor = sharedPreferences.edit();


        for(Captura c:capturas){
            if (c.getNombre_captura().equals(nombre_captura)){
                captura=c;
                Log.d(TAG, "FUNCIONO: ");
            }
        }

        boolean success = editor.putLong("captura_id",captura.getId()).commit();

        for (Rostro r:rostros){

            String estado_rostro = r.getEstado_rostro();
            String array[] = estado_rostro.split(":");
            String estado = array[0];
            Log.d(TAG, "enviarRostrosServicio: "+estado);

            ApiService apiService = ApiServiceGenerator.createService(ApiService.class);
            Call<ResponseMessage> call;
            call = apiService.createRostros(r.getGenero_rostro(), estado,captura.getId());
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if(response.isSuccessful()){
                        Toasty.success(FaceResultActivity.this,"Se guardaron los rostros", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });

        }

        Intent intent = new Intent(FaceResultActivity.this,GraficaActivity.class);
        intent.putExtra("usuario_id",captura.getId());
        Log.d(TAG, "enviarRostrosServicio: "+captura.getId());
        startActivity(intent);

    }

}
