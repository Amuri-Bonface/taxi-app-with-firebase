package ke.co.taxityzltd.driver;

import ke.co.taxityzltd.driver.POJO.Example;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL

     */
    @GET("api/directions/json?key=AIzaSyApC2mtgwE2WJ5lHajG-CnchzGJ-GCphts")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
