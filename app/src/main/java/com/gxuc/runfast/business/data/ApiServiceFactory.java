package com.gxuc.runfast.business.data;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工厂, 提供 ApiService 对象
 * Created by Berial on 16/8/18.
 */
public final class ApiServiceFactory {

    //    public static final String HOST = "http://www.gxptkc.com"; // 正式服
//        public static final String HOST = "http://120.77.70.27"; // 测试服
    public static final String HOST = "http://192.168.2.221:8080/runfast"; // 测试服

    private static final String BASE_URL = HOST + "/business1/";

    private final ApiService mApiService;

    private ApiServiceFactory() {
        // 实例化 ApiService 对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(DataLayer.getClient())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    private static class ApiServiceFactoryHolder {
        private static final ApiServiceFactory INSTANCE = new ApiServiceFactory();
    }

    /**
     * 获取 ApiService 对象
     *
     * @return Api 接口对象
     */
    public static ApiService getApi() {
        return ApiServiceFactoryHolder.INSTANCE.mApiService;
    }
}
