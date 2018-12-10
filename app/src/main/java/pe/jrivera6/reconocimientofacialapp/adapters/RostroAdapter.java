package pe.jrivera6.reconocimientofacialapp.adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.contract.Emotion;

import java.util.List;

import pe.jrivera6.reconocimientofacialapp.R;
import pe.jrivera6.reconocimientofacialapp.models.Rostro;
import pe.jrivera6.reconocimientofacialapp.repositories.FaceRepository;

public class RostroAdapter extends RecyclerView.Adapter<RostroAdapter.ViewHolder> {

    private static final String TAG = "RostroAdapter";

    private List<Rostro> rostroLista;
    private List<Bitmap> rostrosRecortados;



    public RostroAdapter(){
        rostroLista = FaceRepository.rostroLista;
        rostrosRecortados = FaceRepository.rostrosRecortado;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rostroRecortado;
        TextView rostroGenero;
        TextView rostroEstado;


        ViewHolder(View itemView) {
            super(itemView);

            rostroRecortado = itemView.findViewById(R.id.img_rostro_enfocado);
            rostroGenero = itemView.findViewById(R.id.msg_genero);
            rostroEstado = itemView.findViewById(R.id.msg_emocion);
            Log.d("AQUI", "EN EL VIEWHOLDER: LLEGO"+ rostroLista.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.face_items,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.d("ERROR", "onCreateViewHolder: LLEGO");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Rostro rostroModelo = rostroLista.get(i);
        viewHolder.rostroGenero.setText(rostroModelo.getGenero_rostro());
        viewHolder.rostroRecortado.setImageBitmap(rostrosRecortados.get(i));
        viewHolder.rostroEstado.setText("Prueba");
        Long estado = rostroModelo.getId();
        Log.d(TAG, "ADAPTER: "+estado);


    }

    @Override
    public int getItemCount() {

        int tamaño = rostroLista.size();
        Log.d("TAMAÑO", "getItemCount: "+tamaño);
        return tamaño;

    }


    private String getEmotion(Emotion emotion)
    {
        String emotionType = "";
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue)
        {
            emotionValue = emotion.anger;
            emotionType = "Molesto";
        }
        if (emotion.contempt > emotionValue)
        {
            emotionValue = emotion.contempt;
            emotionType = "Desprecio";
        }
        if (emotion.disgust > emotionValue)
        {
            emotionValue = emotion.disgust;
            emotionType = "Asco";
        }
        if (emotion.fear > emotionValue)
        {
            emotionValue = emotion.fear;
            emotionType = "Miedo";
        }
        if (emotion.happiness > emotionValue)
        {
            emotionValue = emotion.happiness;
            emotionType = "Felicidad";
        }
        if (emotion.neutral > emotionValue)
        {
            emotionValue = emotion.neutral;
            emotionType = "Neutral";
        }
        if (emotion.sadness > emotionValue)
        {
            emotionValue = emotion.sadness;
            emotionType = "Tristeza";
        }
        if (emotion.surprise > emotionValue)
        {
            emotionValue = emotion.surprise;
            emotionType = "Sorpresa";
        }
        return String.format(emotionType, emotionValue);
    }


}
