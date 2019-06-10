package ke.co.taxityzltd.taxityz;

import ke.co.taxityzltd.taxityz.POJO.Example;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL

     */
    @GET("api/directions/json?key=AIzaSyDx3jSUEETZ50b2Wu3L0bJWFJzgLQmfPcQ")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
