package pe.jrivera6.reconocimientofacialapp.services;

import java.util.List;

import pe.jrivera6.reconocimientofacialapp.models.Captura;
import pe.jrivera6.reconocimientofacialapp.models.ResponseMessage;
import pe.jrivera6.reconocimientofacialapp.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    String API_BASE_URL = "http://10.200.171.132:8888";


    //GET
    @GET("/api/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("/api/capturas")
    Call<List<Captura>> getCapturas();


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
            @Field("id_estado")Long id_estado,
            @Field("id_captura")Long id_captura

    );


}
