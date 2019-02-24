package com.simple.reaz.fffcom.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */


public class ApiClient{

    private  static  final String BASE_URL = "http://fff-bd.com/freelancing/";
    private  static Retrofit retrofit;


    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

}