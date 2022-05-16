package developer.citypalestine8936ps.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class City implements Serializable {
    public  String cityName  ;

    @DocumentId
   public String   id ;

    public Double lat,lng ;

}
