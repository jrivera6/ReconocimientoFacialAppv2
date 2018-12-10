package pe.jrivera6.reconocimientofacialapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

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
    
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_result);
        recyclerView = findViewById(R.id.recycle_rostros);

        rostrosDetectados();
        obtenerDatos();


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

        for(Captura c:capturas){
            if (c.getNombre_captura().equals(nombre_captura)){
                captura=c;
                Log.d(TAG, "FUNCIONO: ");
            }
        }

        for (Rostro r:rostros){

            ApiService apiService = ApiServiceGenerator.createService(ApiService.class);
            Call<ResponseMessage> call;
            call = apiService.createRostros(r.getGenero_rostro(), (long) 1,captura.getId());
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(FaceResultActivity.this, "Enviado Correctamente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });

        }



    }

}
