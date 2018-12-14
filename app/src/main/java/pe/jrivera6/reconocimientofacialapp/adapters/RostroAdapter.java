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
        String estado = String.format("%s",rostroModelo.getEstado_rostro());
        String genero="";
        if(rostroModelo.getGenero_rostro().equals("male")){
            genero="Hombre";
        }else{
            genero="Mujer";
        }

        viewHolder.rostroGenero.setText(genero);
        viewHolder.rostroRecortado.setImageBitmap(rostrosRecortados.get(i));
        viewHolder.rostroEstado.setText(estado);

    }

    @Override
    public int getItemCount() {

        int tamaño = rostroLista.size();
        Log.d("TAMAÑO", "getItemCount: "+tamaño);
        return tamaño;

    }


}
