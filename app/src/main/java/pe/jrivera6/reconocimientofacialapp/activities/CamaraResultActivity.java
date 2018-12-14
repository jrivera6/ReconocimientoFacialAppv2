package pe.jrivera6.reconocimientofacialapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.helpers.ConectionApp;
import pe.jrivera6.reconocimientofacialapp.helpers.ImageHelper;
import pe.jrivera6.reconocimientofacialapp.models.ResponseMessage;
import pe.jrivera6.reconocimientofacialapp.repositories.FaceRepository;
import pe.jrivera6.reconocimientofacialapp.services.ApiService;
import pe.jrivera6.reconocimientofacialapp.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CamaraResultActivity extends AppCompatActivity {

    private static final String TAG = CamaraResultActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 200;
    private static final int PERMISSIONS_REQUEST = 300;


    ProgressDialog mProgressDialog;
    private ImageView imgVistaPrevia;
    private Button btnImagen, btnExaminar;


    private class TareaDetection extends AsyncTask<InputStream, String, Face[]> {
        private boolean funciono = true;

        @Override
        protected Face[] doInBackground(InputStream... params) {

            FaceServiceClient faceServiceClient = ConectionApp.getFaceServiceClient();

            try {
                publishProgress("Detectando...");

                // Start detection.
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        new FaceServiceClient.FaceAttributeType[]{
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender,
                                FaceServiceClient.FaceAttributeType.Smile,
                                FaceServiceClient.FaceAttributeType.Emotion
                        });
            } catch (Exception e) {
                funciono = false;
                Log.d("Fallo", "Fallo: ");
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0]);

        }

        @Override
        protected void onPostExecute(Face[] result) {
            if (funciono) {
//                addLog("Response: Success. Detected " + (result == null ? 0 : result.length)
//                    + " face(s) in " + mImageUri);
            }

            // Show the result on screen when detection is done.
            setImageAfterDetect(result, funciono);
        }
    }

    private Uri imagePath;
    private Bitmap bitmapImg;
    private Integer cantidad_rostros;
    private String nombre_captura;

    /// On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_result);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //
        imgVistaPrevia = findViewById(R.id.img_vistaprevia);
        btnImagen = findViewById(R.id.btn_imagen);
        btnExaminar = findViewById(R.id.btn_examinar);
        btnExaminar.setEnabled(false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Procesando");


        //Metodo para abrir la camara
        takeOrChossePicture();

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOrChossePicture();
            }
        });
        btnExaminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                examinar();
            }
        });
    }


    // Escoger o tomar foto
    private void takeOrChossePicture() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder opcionesAlert = new AlertDialog.Builder(CamaraResultActivity.this);
        opcionesAlert.setTitle("Elegir una opcion");
        opcionesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CamaraResultActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
                        return;
                    }

                    if (ContextCompat.checkSelfPermission(CamaraResultActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CamaraResultActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST);

                    } else {
                        openCameraActivity();
                    }


                } else if (opciones[i].equals("Cargar Imagen")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccionar Aplicación"), GALLERY_REQUEST);
                    Toast.makeText(CamaraResultActivity.this, "Cargar", Toast.LENGTH_SHORT).show();

                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        opcionesAlert.show();
    }


    //Verifica los permisos de camara
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Permiso de camara no aceptado", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            openCameraActivity();
                        }

                    }
                }
        }
    }

    //Despues de tomar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult() returned: " + data.getData());

        if (requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {

                try {


                    if (requestCode == CAMERA_REQUEST) {

                        Bundle extras = data.getExtras();

                        Bitmap bitmap = (Bitmap) extras.get("data");

                        File directory = new ContextWrapper(this).getDir("photos", Context.MODE_PRIVATE);   // Directorio interno "app_photos"
                        File photofile = new File(directory, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg");

                        Log.d("TAG", "Save photo file to " + photofile.getAbsolutePath());
                        FileOutputStream fos = new FileOutputStream(photofile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();




//                        imagePath = data.getData();
                        imagePath = Uri.fromFile(new File(photofile.getAbsolutePath()));

                        Log.d(TAG, "onActivityResult() returned: " + imagePath);
                        btnExaminar.setEnabled(true);
                        bitmapImg = ImageHelper.loadSizeLimitedBitmapFromUri(imagePath, getContentResolver());

                        if (bitmap != null) {
                            imgVistaPrevia.setImageBitmap(bitmap);
                        }

                    } else {
                        imagePath = data.getData();
                        btnExaminar.setEnabled(true);
                        bitmapImg = ImageHelper.loadSizeLimitedBitmapFromUri(imagePath, getContentResolver());
                        if (bitmapImg != null) {
                            imgVistaPrevia.setImageBitmap(bitmapImg);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

    }


    //Funcion para abrir camara
    public void openCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    //Funcion para examinar la imagen
    public void examinar() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        Log.d("Prueba", "examinar: " + inputStream);
        new TareaDetection().execute(inputStream);

    }

    //Funcion para enviar datos al servicio rest
    public void enviarCapturaServicio() {
        //Formatear fecha actual

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha_actual = new Date();
        String fecha_captura = dateFormat.format(fecha_actual);

        //Variables de la captura
        Long id_usuario = sharedPreferences.getLong("id", 0);
        Random r = new Random();
        int cont = r.nextInt(1000);
        nombre_captura = "captura" + cont + "-" + fecha_captura;


        //Llamada a la API
        ApiService apiService = ApiServiceGenerator.createService(ApiService.class);
        Call<ResponseMessage> call;
        call = apiService.createCapturas(fecha_captura, nombre_captura, cantidad_rostros, id_usuario);
        Log.d(TAG, "enviarDatosServicio: " + call);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseMessage responseMessage = response.body();
                        Toast.makeText(CamaraResultActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Throwable t) {

                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {



            }
        });


    }

    //Funcion despues de la deteccion
    private void setImageAfterDetect(Face[] result, boolean funciono) {

        mProgressDialog.dismiss();
        btnDisable();

        String resultadoDeteccion = "";
        if (funciono) {

            if (result.length<=0){
                Toasty.error(this,"No se encontraron rostros.\nIngrese otra imagen",Toast.LENGTH_LONG,true).show();
                return;
            }

            //Enmarca los rostros
            imgVistaPrevia.setImageBitmap(ImageHelper.drawFaceRectanglesOnBitmap(bitmapImg, result, true));

            cantidad_rostros = result.length;

            FaceRepository.build(result, bitmapImg);
            enviarCapturaServicio();
            Log.d(TAG, "NOMBRE DE LA CAPTURA: "+nombre_captura);

            //Cambiar de activity
            Intent i = new Intent(CamaraResultActivity.this, FaceResultActivity.class);
            i.putExtra("nombre_captura",nombre_captura);
            startActivity(i);

        }
    }

    private void btnDisable() {
        btnExaminar.setEnabled(false);

    }


}
