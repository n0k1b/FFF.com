package com.simple.reaz.fffcom.API;

import com.simple.reaz.fffcom.Home.ModelCategories;
import com.simple.reaz.fffcom.JobPost.Model_Jobpost;
import com.simple.reaz.fffcom.LoginRegisterPanel.ModelUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */
public interface ApiInterface {

    @GET("get_cata_list.php")
    Call<List<ModelCategories>> get_cata_list();


    @FormUrlEncoded
    @POST("job_post.php")
    Call<Model_Jobpost> job_post(@Field("mobile") String mobile,
                                           @Field("description") String description ,
                                           @Field("category") String category,
                                           @Field("sub_category") String sub_category ,
                                           @Field("image") String image,
                                           @Field("price") String price,
                                           @Field("location") String location);

    @GET("get_job_list.php")
    Call<List<Model_Jobpost>> get_job_list();

    Call<ModelUser> user_registration(String user_mobile, String user_name, String password);

    Call<ModelUser> user_login(String mobile_id, String password_login);

//    @GET("something.php")
//    Call<List<ModelSomething>> getRSomethig(@Query("something") String something);
//
//

//
//

}