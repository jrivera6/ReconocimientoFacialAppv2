package pe.jrivera6.reconocimientofacialapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.models.ResponseMessage;
import pe.jrivera6.reconocimientofacialapp.services.ApiService;
import pe.jrivera6.reconocimientofacialapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText txtNombres, txtApellidos, txtNombreUsuario, txtEmail, txtContraseña, txtConfirmContraseña;
    private RadioGroup radioGroup;
    private RadioButton radioButtonSexo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombres = findViewById(R.id.txt_firstname);
        txtApellidos = findViewById(R.id.txt_lastname);
        txtNombreUsuario = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);
        txtContraseña = findViewById(R.id.txt_password);
        txtConfirmContraseña = findViewById(R.id.txt_confirm_password);
        radioGroup = findViewById(R.id.rdb_sexo);
        Button btnRegistrar = findViewById(R.id.btn_registrar);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                radioButtonSexo = findViewById(radioButtonId);

                String nombres = txtNombres.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String nom_usuario = txtNombreUsuario.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtContraseña.getText().toString();
                String confirmPass = txtConfirmContraseña.getText().toString();

                if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || confirmPass.isEmpty() ) {
                    Toast.makeText(RegisterActivity.this, "Faltan llenar datos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!confirmPass.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiService service = ApiServiceGenerator.createService(ApiService.class);
                Call<ResponseMessage> call;

                call = service.createUser(nombres, apellidos, nom_usuario, password, email);

                call.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseMessage> call, @NonNull Response<ResponseMessage> response) {

                        try {
                            int statusCode = response.code();
                            Log.d(TAG, "HTTP status code: " + statusCode);

                            if (response.isSuccessful()) {

                                ResponseMessage responseMessage = response.body();
                                Log.d(TAG, "Response Message Positivo" + responseMessage);
                                Toast.makeText(RegisterActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.e(TAG, "onError: " + response.errorBody().string());
                                throw new Exception("Error en el servicio");
                            }
                        } catch (Throwable t) {
                            try {
                                Log.e(TAG, "onThrowable Mensaje Negativo: " + t.toString(), t);
                                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Throwable x) {
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                        Log.e(TAG, "onFailure: " + t.toString());
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();


                    }
                });

            }
        });

    }


}
