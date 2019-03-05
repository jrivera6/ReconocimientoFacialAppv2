package pe.jrivera6.reconocimientofacialapp.services;

import java.util.List;

import pe.jrivera6.reconocimientofacialapp.models.Captura;
import pe.jrivera6.reconocimientofacialapp.models.Datos;
import pe.jrivera6.reconocimientofacialapp.models.ResponseMessage;
import pe.jrivera6.reconocimientofacialapp.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

//    String API_BASE_URL = "http://10.200.169.34:8888";
//    String API_BASE_URL = "http://ec2-54-242-190-59.compute-1.amazonaws.com:8080";
    String API_BASE_URL = "http://70.37.52.210:8080";



    //GET
    @GET("/api/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("/api/capturas")
    Call<List<Captura>> getCapturas();

    @GET("/api/datos/{id_captura}")
    Call<List<Datos>> getDatos(@Path("id_captura") Long id_captura);

    //POST
    @FormUrlEncoded
    @POST("/api/usuarios")
    Call<ResponseMessage> createUser(
            @Field("nombres")String nombre,
            @Field("apellidos")String apellidos,
            @Field("username")String nom_usuario,
            @Field("password")String password,
            @Field("email")String email
    );

    @FormUrlEncoded
    @POST("/api/capturas")
    Call<ResponseMessage> createCapturas(
            @Field("fecha_captura")String fecha_captura,
            @Field("nombre_captura")String nombre_captura,
            @Field("cantidad_rostros")Integer cantidad_rostros,
            @Field("id_usuario")Long id_usuario
    );

    @FormUrlEncoded
    @POST("/api/rostros")
    Call<ResponseMessage> createRostros(
            @Field("genero_rostro")String genero_rostro,
            @Field("estado_rostro")String estado_rostro,
            @Field("id_captura")Long id_captura

    );


}
