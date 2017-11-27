package com.gxuc.runfast.business.data.repo;

import com.gxuc.runfast.business.data.ApiService;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.bean.Archive;
import com.gxuc.runfast.business.data.bean.Business;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.LoadArchivesResponse;
import com.gxuc.runfast.business.data.response.LoadBusinessInfoResponse;
import com.gxuc.runfast.business.data.response.UploadFileResponse;

import java.io.File;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 商铺
 * Created by Berial on 2017/8/25.
 */
public class BusinessRepo {

    private BusinessRepo() {}

    public static BusinessRepo get() {
        return BusinessRepoHolder.sInstance;
    }

    private static class BusinessRepoHolder {
        private static final BusinessRepo sInstance = new BusinessRepo();
    }

    private ApiService getApi() {
        return ApiServiceFactory.getApi();
    }

    private long getId() {
        return Paper.book().read("id", 0L);
    }

    public Observable<Business> loadBusinessInfo() {
        return getApi().loadBusinessInfo(getId())
                .map(LoadBusinessInfoResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> changeBusinessState(String state) {
        return getApi().changeBusinessState(getId(), state)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> updateShopInfo(int status, String startTime, String endTime,
                                                   String nextStartTime, String nextEndTime,
                                                   String packingCharge, String deliveryCost, String describe,
                                                   boolean hasSubsidy, String subsidy) {
        return getApi().updateShopInfo(getId(), status, startTime, endTime, nextStartTime, nextEndTime,
                packingCharge, deliveryCost, describe, hasSubsidy ? 1 : 0, subsidy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> updateShopLogoPath(String logoPath, String thumbnailPath) {
        return getApi().updateShopLogoPath(getId(), logoPath, thumbnailPath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> updateArchivePath(String imagePath) {
        return getApi().updateArchivePath(getId(), imagePath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> deleteArchive(long archiveId) {
        return getApi().deleteArchive(getId(), archiveId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> changePassword(String oldPassword, String password) {
        return getApi().changePassword(getId(), oldPassword, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> submitFeedback(String describe, String email) {
        return getApi().submitFeedback(getId(), describe, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> changeShopName(String name) {
        return getApi().changeShopName(getId(), name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Archive>> loadArchives() {
        return getApi().loadArchives(getId())
                .map(LoadArchivesResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<UploadFileResponse> uploadImage(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return Observable.just(new UploadFileResponse());
        }

        RequestBody body = RequestBody
                .create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part part =
                MultipartBody.Part.createFormData("upfile", file.getName(), body);

        return getApi().uploadImage(1, part)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void setAutomatic(boolean isAutomatic) {
        Paper.book().write("isAutomatic", isAutomatic);
    }

    public boolean isAutomatic() {
        return Paper.book().read("isAutomatic", false);
    }

    public void savePhone(String phone) {
        Paper.book().write("phone", phone);
    }

    public String getPhone() {
        return Paper.book().read("phone", "");
    }
}
