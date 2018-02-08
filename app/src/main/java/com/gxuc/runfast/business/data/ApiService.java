package com.gxuc.runfast.business.data;


import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.LoadActivitiesResponse;
import com.gxuc.runfast.business.data.response.LoadActivityGoodsResponse;
import com.gxuc.runfast.business.data.response.LoadArchivesResponse;
import com.gxuc.runfast.business.data.response.LoadBusinessInfoResponse;
import com.gxuc.runfast.business.data.response.LoadBusinessStatisticsResponse;
import com.gxuc.runfast.business.data.response.LoadCashInfoResponse;
import com.gxuc.runfast.business.data.response.LoadCashRecordsResponse;
import com.gxuc.runfast.business.data.response.LoadCommentResponse;
import com.gxuc.runfast.business.data.response.LoadCommentsResponse;
import com.gxuc.runfast.business.data.response.LoadDayOrdersResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsDetailResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsSellListResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsSortsResponse;
import com.gxuc.runfast.business.data.response.LoadIncomeRecordsResponse;
import com.gxuc.runfast.business.data.response.LoadMessagesResponse;
import com.gxuc.runfast.business.data.response.LoadMonthliesResponse;
import com.gxuc.runfast.business.data.response.LoadOperationInfoResponse;
import com.gxuc.runfast.business.data.response.LoadOrdersResponse;
import com.gxuc.runfast.business.data.response.LoginResponse;
import com.gxuc.runfast.business.data.response.UpdateAppResponse;
import com.gxuc.runfast.business.data.response.UploadFileResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.do")
    Observable<LoginResponse> login(
            @Field("userName") String username,
            @Field("password") String password,
            @Field("pushType") String pushType,
            @Field("bdpushUserId") String bdpushUserId,
            @Field("bdpushChannelId") String bdpushChannelId,
            @Field("otherId") String otherId,
            @Field("bptype") String bptype,
            @Field("vercode") int vercode,
            @Field("alias") String alias
    );

    @FormUrlEncoded
    @POST("logout.do")
    Observable<LoginResponse> logout(
            @Field("alias") String alias
    );

    @FormUrlEncoded
    @POST("checkAndroidVersion.do")
    Observable<UpdateAppResponse> updateApp(
            @Field("vercode") int vercode
    );

    @FormUrlEncoded
    @POST("getBusiness.do")
    Observable<LoadBusinessInfoResponse> loadBusinessInfo(
            @Field("id") long id
    );

    @FormUrlEncoded
    @POST("accounts/updatebusname.do")
    Observable<BaseResponse> changeShopName(
            @Field("baId") long id,
            @Field("busname") String name
    );

    @FormUrlEncoded
    @POST("accounts/creatcode.do")
    void sendVerifyCode();

    @FormUrlEncoded
    @POST("changestate.do")
    Observable<BaseResponse> changeBusinessState(
            @Field("baId") long id,
            @Field("statu") String state
    );

    @FormUrlEncoded
    @POST("accounts/mybusiness.do")
    Observable<BaseResponse> updateShopInfo(
            @Field("baId") long id,
            @Field("statu") int status,
            @Field("startwork") String startTime,
            @Field("endwork") String endTime,
            @Field("startwork2") String nextStartTime,
            @Field("endwork2") String nextEndTime,
            @Field("packing") String packingCharge,
            @Field("busshowps") String deliveryCost,
            @Field("content") String describe,
            @Field("issubsidy") int hasSubsidy,
            @Field("subsidy") String subsidy
    );

    @FormUrlEncoded
    @POST("updatepic.do")
    Observable<BaseResponse> updateShopLogoPath(
            @Field("baId") long id,
            @Field("imgPath") String logoPath,
            @Field("mini_imgPath") String thumbnailPath
    );

    @FormUrlEncoded
    @POST("accounts/addfoodsafe.do")
    Observable<BaseResponse> updateArchivePath(
            @Field("baId") long id,
            @Field("imgUrl") String imagePath
    );

    @FormUrlEncoded
    @POST("accounts/detelefood.do")
    Observable<BaseResponse> deleteArchive(
            @Field("baId") long id,
            @Field("id") long archiveId
    );

    @Multipart
    @POST(ApiServiceFactory.HOST + "/business/fileUpload.do")
    Observable<UploadFileResponse> uploadImage(
            @Query("json") int value,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST("accounts/repassword.do")
    Observable<BaseResponse> changePassword(
            @Field("baId") long id,
            @Field("oldpassword") String oldPassword,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("accounts/feedback.do")
    Observable<BaseResponse> submitFeedback(
            @Field("baId") long id,
            @Field("content") String describe,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getValidGoodsSellRecord.do")
    Observable<LoadOperationInfoResponse> loadOperationInfo(
            @Field("businessId") long id
    );

    @FormUrlEncoded
    @POST("statistics/index.do")
    Observable<LoadBusinessStatisticsResponse> loadBusinessStatistics(
            @Field("baId") long id
    );

    @FormUrlEncoded
    @POST("withdrawals/index.do")
    Observable<LoadCashInfoResponse> loadCashInfo(
            @Field("baId") long id
    );

    @FormUrlEncoded
    @POST("withdrawals/apply.do")
    Observable<BaseResponse> applyForCash(
            @Field("baId") long id
    );

    @FormUrlEncoded
    @POST("withdrawals/incomeRecord.do")
    Observable<LoadIncomeRecordsResponse> loadIncomeRecords(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("withdrawals/drawingRecord.do")
    Observable<LoadCashRecordsResponse> loadCashRecords(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("statistics/daydate.do")
    Observable<LoadDayOrdersResponse> loadDayOrders(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size,
            @Field("startTime") String startTime,
            @Field("endTime") String endTime
    );

    @FormUrlEncoded
    @POST("statistics/markgetdate.do")
    Observable<LoadGoodsSellListResponse> loadGoodsSellList(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size,
            @Field("startTime") String startTime,
            @Field("endTime") String endTime
    );

    @FormUrlEncoded
    @POST("statistics/monthdate.do")
    Observable<LoadMonthliesResponse> loadMonthlies(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size,
            @Field("startTime") String startTime
    );

    @FormUrlEncoded
    @POST("messge/list.do")
    Observable<LoadMessagesResponse> loadMessages(
            @Field("baId") long id,
            @Field("tpye") int type,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("goodsSell/goodsSellList.do")
    Observable<LoadGoodsResponse> loadGoods(
            @Field("baId") long id,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("goodsSell/getGoodsType.do")
    Observable<LoadGoodsSortsResponse> loadGoodsSorts(
            @Field("baId") long id
    );

    @FormUrlEncoded
    @POST("goodsSell/getGoodInfo.do")
    Observable<LoadGoodsDetailResponse> loadGoodsDetail(
            @Field("baId") long id,
            @Field("id") long goodsId
    );

    @FormUrlEncoded
    @POST("goodsSell/addGoodsSell.do")
    Observable<BaseResponse> addGoods(
            @Field("baId") long id,
            @Field("t") String json
    );

    @FormUrlEncoded
    @POST("goodsSell/editGoodsSell.do")
    Observable<BaseResponse> updateGoods(
            @Field("baId") long id,
            @Field("t") String json
    );

    @FormUrlEncoded
    @POST("goodsSell/grounding.do")
    Observable<BaseResponse> manageGoodsStatus(
            @Field("baId") long businessId,
            @Field("id") long goodsId,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("goodsSell/delete.do")
    Observable<BaseResponse> deleteGood(
            @Field("id") long goodsId
    );

    @FormUrlEncoded
    @POST("goodsSell/addtype.do")
    Observable<BaseResponse> addGoodsSort(
            @Field("baId") long businessId,
            @Field("name") String sortName,
            @Field("content") String describe
    );

    @FormUrlEncoded
    @POST("goodsSell/editType.do")
    Observable<BaseResponse> updateGoodsSort(
            @Field("baId") long businessId,
            @Field("id") long sortId,
            @Field("name") String sortName,
            @Field("content") String describe
    );

    @FormUrlEncoded
    @POST("goodsSell/remove.do")
    Observable<BaseResponse> deleteGoodsSort(
            @Field("baId") long businessId,
            @Field("id") long sortId
    );

    @FormUrlEncoded
    @POST("buscomment/index.do")
    Observable<LoadCommentsResponse> loadComments(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("buscomment/deail.do")
    Observable<LoadCommentResponse> loadCommentDetail(
            @Field("id") long id
    );

    @FormUrlEncoded
    @POST("buscomment/feedback.do")
    Observable<BaseResponse> replyComment(
            @Field("id") long id,
            @Field("feedback") String reply
    );

    @FormUrlEncoded
    @POST("activity/list.do")
    Observable<LoadActivitiesResponse> loadActivities(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("activity/add.do")
    Observable<LoadActivityGoodsResponse> loadActivityGoods(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("activity/poststops.do")
    Observable<BaseResponse> changeActivityStatus(
            @Field("baId") long id,
            @Field("id") long activityId
    );

    @FormUrlEncoded
    @POST("activity/delete.do")
    Observable<BaseResponse> deleteActivity(
            @Field("baId") long id,
            @Field("id") long activityId
    );

    @FormUrlEncoded
    @POST("activity/getgoodsellview.do")
    Observable<LoadActivityGoodsResponse> loadActivityGoodsForSpecialOffer(
            @Field("baId") long id,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("activity/activitydetail.do")
    Observable<LoadActivityGoodsResponse> loadActivityGoodsForSpecial(
            @Field("id") String id,
            @Field("baId") long baId,
            @Field("page") int page,
            @Field("rows") int size
    );

    @FormUrlEncoded
    @POST("activity/saveactivity.do")
    Observable<BaseResponse> saveOrUpdateActivity(
            @Field("baId") long id,
            @Field("name") String name,
            @Field("startTime1") String startTime,
            @Field("endTime1") String endTime,
            @Field("ptype") int type,
            @Field("fulls") String full,
            @Field("lesss") String less,
            @Field("discount") String discount,
            @Field("disprice") String disprice,
            @Field("goodids") String goodsId,
            @Field("goods") String goods,
            @Field("stanids") String standardId
    );

    @FormUrlEncoded
    @POST("getOrder1.do")
    Observable<LoadOrdersResponse> loadOrders(
            @Field("baId") long id,
            @Field("bId") long businessId,
            @Field("page") int page,
            @Field("type") int status
    );

    @FormUrlEncoded
    @POST("order/loadstatrders.do")
    Observable<BaseResponse> loadOrderStatus(
            @Field("baId") long id,
            @Field("bId") long businessId
    );

    @FormUrlEncoded
    @POST("taking.do")
    Observable<BaseResponse> receive(
            @Field("baId") long id,
            @Field("id") long orderId
    );

    @FormUrlEncoded
    @POST("refuse.do")
    Observable<BaseResponse> refuse(
            @Field("baId") long id,
            @Field("id") long orderId,
            @Field("refundcontext") String reason
    );

    @FormUrlEncoded
    @POST("cancel.do")
    Observable<BaseResponse> applyForCancel(
            @Field("baId") long id,
            @Field("id") long orderId,
            @Field("isCancel") int isAgreed,
            @Field("refundcontext") String reason
    );

    @FormUrlEncoded
    @POST("order/refund.do")
    Observable<BaseResponse> refund(
            @Field("baId") long id,
            @Field("id") long orderId,
            @Field("refund") String amount
    );

    @FormUrlEncoded
    @POST("order/changed.do")
    Observable<BaseResponse> changeOrderStatus(
            @Field("baId") long id,
            @Field("id") long orderId,
            @Field("type") int status
    );

    @FormUrlEncoded
    @POST("accounts/foodsafe.do")
    Observable<LoadArchivesResponse> loadArchives(
            @Field("baId") long id
    );
}
