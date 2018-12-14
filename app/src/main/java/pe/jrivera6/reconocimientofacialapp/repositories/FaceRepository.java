package pe.jrivera6.reconocimientofacialapp.repositories;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.jrivera6.reconocimientofacialapp.helpers.ImageHelper;
import pe.jrivera6.reconocimientofacialapp.models.Rostro;

import static android.support.constraint.Constraints.TAG;

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
                    rostroModelo.setEstado_rostro(getEmotion(face.faceAttributes.emotion));
                    Log.d(TAG, "build: "+rostroModelo.getEstado_rostro());
                    rostroLista.add(rostroModelo);
                }catch (IOException e){
                    Log.e("Prueba de error", "RostroAdapter: ", e);
                }
            }
    }

    private static String getEmotion(Emotion emotion)
    {
        String emotionType = "";
        double emotionValue = 0.0;
        float porcentaje = 0;
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
        DecimalFormat df = new DecimalFormat("#.00");
        
        porcentaje = (float) (emotionValue *100);

        return String.format("%s: %s",emotionType, df.format(porcentaje));
    }



}
