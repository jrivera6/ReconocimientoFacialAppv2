package pe.jrivera6.reconocimientofacialapp.repositories;

import com.orm.SugarRecord;

import pe.jrivera6.reconocimientofacialapp.models.User;

public class UserRepository {

    public static void create(String nombre, String email, String tokken){

        User user = new User();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setTokken(tokken);
        SugarRecord.save(user);
    }
}
