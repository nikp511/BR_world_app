package network;


import constants.ConstantCodes;
import pojo.PojoCommon;
import pojo.PojoDetail;
import pojo.PojoHome;
import pojo.PojoLogin;
import pojo.PojoNotification;
import pojo.PojoAllCategory;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Retrofit interface for ws.wolfsoft.propertyplanetapp.network calls
 */
public interface RetrofitInterface {

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_REGISTER)
    Call<PojoCommon> RegisterUser(@Field("username") String username,
                                  @Field("mobile") String mobile,
                                  @Field("email") String email,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_GOOGLE_LOGIN)
    Call<PojoLogin> GoogleLogin(@Field("username") String username,
                                @Field("google_token") String google_token,
                                @Field("email") String email);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_LOGIN)
    Call<PojoLogin> LoginUser(@Field("email") String email,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_NOTIFICATION)
    Call<PojoNotification> Notification(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_SEARCH)
    Call<PojoAllCategory> Search(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_ALL_CATEGORY)
    Call<PojoAllCategory> AllCategory(@Field("menu_category") String menu_category);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_ALL_CATEGORY)
    Call<PojoDetail> DetailInfo(@Field("movie_id") String movie_id);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_HOME)
    Call<PojoHome> Home(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_FORGOT_PASSWORD)
    Call<PojoCommon> ForgotPassword(@Field("key") String key,
                                    @Field("mobile") String mobile,
                                    @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST(ConstantCodes.METHOD_UPDATE_PROFILE)
    Call<PojoCommon> UpdateProfile(@Field("key") String key,
                                   @Field("user_id") String userId,
                                   @Field("username") String name,
                                   @Field("address") String address,
                                   @Field("oldPwd") String oldPwd,
                                   @Field("newPwd") String newPwd);


}