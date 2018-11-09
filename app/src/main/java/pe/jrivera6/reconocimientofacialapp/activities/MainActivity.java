package pe.jrivera6.reconocimientofacialapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import pe.jrivera6.reconocimientofacialapp.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardCamera = findViewById(R.id.card_camera);

        cardCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cambiarActivity();
            }
        });
    }


    private void cambiarActivity(){
        Intent intent = new Intent(MainActivity.this, CamaraResultActivity.class);
        startActivity(intent);

    }
}
