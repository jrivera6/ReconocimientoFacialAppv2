package pe.jrivera6.reconocimientofacialapp.repositories;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.microsoft.projectoxford.face.contract.Face;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.jrivera6.reconocimientofacialapp.helpers.ImageHelper;
import pe.jrivera6.reconocimientofacialapp.models.Rostro;

public class FaceRepository {

    public static List<Rostro> rostroLista = new ArrayList<>();
    public static List<Bitmap> rostrosRecortado  = new ArrayList<>();

    public static void build(@NonNull Face[] resultDeteccion, Bitmap bitmapRecortar){

//        if (resultDeteccion != null){

            rostroLista.clear();
            rostrosRecortado.clear();

            List<Face> facesLista = Arrays.asList(resultDeteccion);
            for (Face face: facesLista){
                try{
                    rostrosRecortado.add(ImageHelper.generateFaceThumbnail(bitmapRecortar,face.faceRectangle));
                    Rostro rostroModelo = new Rostro();
                    rostroModelo.setGenero_rostro(face.faceAttributes.gender);
                    rostroModelo.setId_captura((long) 1);

                    //Aqui tengo la duda de los estados
                    rostroModelo.setId_estado((long) 1);
                    rostroLista.add(rostroModelo);
                }catch (IOException e){
                    Log.e("PRueba de error", "RostroAdapter: ", e);
                }
            }
//        }


    }



}
