package com.myprojects.bety2.api.auth;

public class Authentication {

//    public static User user = new User();
//
//    public static User getCurrentUser(String token) {
//
//        ApiManager.connectToApi();
//
//        Call<JsonObject> call = ApiManager.apiUrl.getCurrentUser(token);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject jsonObject = response.body();
//                if (response.code() == 200) {
//                    Gson gson = new GsonBuilder().serializeNulls().create();
//                    Authentication.user = gson.fromJson(jsonObject.get("user").getAsJsonObject(), User.class);
//                }
//                else Authentication.user = null;
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//        return user;
//    }
}
