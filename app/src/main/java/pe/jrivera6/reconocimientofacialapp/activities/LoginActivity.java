package pe.jrivera6.reconocimientofacialapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.blurry.Blurry;
import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.models.Usuario;
import pe.jrivera6.reconocimientofacialapp.services.ApiService;
import pe.jrivera6.reconocimientofacialapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private EditText txtEmail, txtPassword;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        linearLayout = findViewById(R.id.formulario);
        progressBar = findViewById(R.id.barra_circular);
        TextView txtRegistrar = findViewById(R.id.msg_register_click);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sharedUsername = sharedPreferences.getString("username",null);
        if (sharedUsername != null){
            txtEmail.setText(sharedUsername);
            txtPassword.requestFocus();
        }

        if(sharedPreferences.getBoolean("islogged",false)){
            goToMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                validarFormulario();

            }
        });

        txtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });



    }

    private void registrar(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void validarFormulario() {

        String usuario = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString();

        if (usuario.isEmpty() || password.isEmpty() ){
            Toasty.info(this,"Necesario llenar datos", Toast.LENGTH_SHORT).show();
            return;
        }else{
            login(usuario,password);
        }
    }
//        for(Usuario u: usuarios){
//
//            String uEmail = u.getEmail();
//            String uUsername = u.getUsername();
//            String uPassword = u.getPassword();
//        }




    @Override
    protected void onStart() {
        super.onStart();

        ImageView rootWall = findViewById(R.id.wallp_login);
        TextView msgRegister = findViewById(R.id.msg_register_click);
        rootWall.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(getApplicationContext())
                        .radius(5)
                        .sampling(2)
                        .async()
                        .capture(findViewById(R.id.wallp_login))
                        .into((ImageView) findViewById(R.id.wallp_login));
            }
        });

    }


    public void login(final String usuario, final String password){

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<List<Usuario>> call = service.getUsuarios();

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                try{

                    int statusCode = response.code();
                    Log.d(TAG, "onResponse: "+statusCode);
                    if(response.isSuccessful()){

                        List<Usuario> usuarios = response.body();
                        Log.d(TAG, "onResponse: "+usuarios);

                        Usuario usu = null;
                        boolean valid = false;

                        for (Usuario u:usuarios){

                            if(usuario.equals(u.getUsername()) && password.equals(u.getPassword())){

                                usu=u;
                                valid=true;
                            }

                        }

                        if(valid){
                            linearLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            boolean existe = sharedPreferences.getBoolean("isExist",false);
                            Log.d(TAG, "EXISTE: "+existe);
                            if (!existe){
                                Log.d(TAG, "CREANDO SHAREDPREFENCE");
                                Long id = usu.getId();
                                String nombres = usu.getNombres();
                                String apellidos = usu.getApellidos();
                                String email = usu.getEmail();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                boolean success = editor
                                        .putBoolean("islogged",true)
                                        .putBoolean("isExist",true)
                                        .putLong("id",id)
                                        .putString("nombres",nombres)
                                        .putString("apellidos",apellidos)
                                        .putString("username",usu.getPassword())
                                        .putString("email",email)
                                        .commit();
                                goToMainActivity();

                            }else{
                                Log.d(TAG, "LO PASA DE FRENTE: ");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                boolean success = editor
                                        .putBoolean("islogged",true)
                                        .commit();
                                goToMainActivity();
                            }

                        }else {
                            Toasty.error(LoginActivity.this,"Usuario o contraseña no coinciden",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Log.d(TAG, "onResponse: "+response.errorBody().toString());
                        throw new Exception("Error en el servicio");
                    }

                }catch (Throwable t){

                }


            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                Toasty.error(LoginActivity.this,"Sin conexión a internet",Toast.LENGTH_SHORT).show();

            }
        });




    }

}
