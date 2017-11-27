package com.gxuc.runfast.business.data.repo;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.gxuc.runfast.business.data.ApiService;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.DataLayer;
import com.gxuc.runfast.business.data.bean.Activity;
import com.gxuc.runfast.business.data.bean.BusinessStatistics;
import com.gxuc.runfast.business.data.bean.CashInfo;
import com.gxuc.runfast.business.data.bean.CashRecord;
import com.gxuc.runfast.business.data.bean.Comment;
import com.gxuc.runfast.business.data.bean.DayOrder;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.bean.GoodsSell;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.data.bean.IncomeRecord;
import com.gxuc.runfast.business.data.bean.Message;
import com.gxuc.runfast.business.data.bean.Monthly;
import com.gxuc.runfast.business.data.bean.Operation;
import com.gxuc.runfast.business.data.bean.Option;
import com.gxuc.runfast.business.data.bean.Standard;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.LoadActivityGoodsResponse;
import com.gxuc.runfast.business.data.response.LoadBusinessStatisticsResponse;
import com.gxuc.runfast.business.data.response.LoadCashInfoResponse;
import com.gxuc.runfast.business.data.response.LoadCommentResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsDetailResponse;
import com.gxuc.runfast.business.data.response.LoadGoodsSortsResponse;
import com.gxuc.runfast.business.data.response.LoadOperationInfoResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 运营
 * Created by Berial on 2017/8/25.
 */
public class OperationRepo {

    private int incomeRecordPages = -1;
    private int cashRecordPages = -1;
    private int messagePages = -1;
    private int dayOrderPages = -1;
    private int commentPages = -1;
    private int activityPages = -1;
    private int activityGoodsPages = -1;
    private int goodsSellPages = -1;
    private int monthliesPages = -1;

    private List<GoodsSort> goodsSorts = new ArrayList<>();

    private OperationRepo() {}

    public static OperationRepo get() {
        return OperationRepoHolder.sInstance;
    }

    private static class OperationRepoHolder {
        private static final OperationRepo sInstance = new OperationRepo();
    }

    private ApiService getApi() {
        return ApiServiceFactory.getApi();
    }

    private long getId() {
        return Paper.book().read("id", 0L);
    }

    private long getBusinessId() {
        return Paper.book().read("businessId", 0L);
    }

    public Observable<Operation> loadOperationInfo() {
        return getApi().loadOperationInfo(getBusinessId())
                .map(LoadOperationInfoResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BusinessStatistics> loadBusinessStatistics() {
        return getApi().loadBusinessStatistics(getId())
                .map(LoadBusinessStatisticsResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<CashInfo> loadCashInfo() {
        return getApi().loadCashInfo(getId())
                .map(LoadCashInfoResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> applyForCash() {
        return getApi().applyForCash(getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<IncomeRecord>> loadIncomeRecords(int page) {
        if (incomeRecordPages != -1 && page > incomeRecordPages) {
            return Observable.just(Collections.<IncomeRecord>emptyList());
        }
        return getApi().loadIncomeRecords(getId(), page, 10)
                .map(response -> {
                    incomeRecordPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<CashRecord>> loadCashRecords(int page) {
        if (cashRecordPages != -1 && page > cashRecordPages) {
            return Observable.just(Collections.<CashRecord>emptyList());
        }
        return getApi().loadCashRecords(getId(), page, 10)
                .map(response -> {
                    cashRecordPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Message>> loadMessages(int page) {
        if (messagePages != -1 && page > messagePages) {
            return Observable.just(Collections.<Message>emptyList());
        }
        return getApi().loadMessages(getId(), 2, page, 10)
                .map(response -> {
                    messagePages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<DayOrder>> loadDayOrders(int page, String startTime, String endTime) {
        if (dayOrderPages != -1 && page > dayOrderPages) {
            return Observable.just(Collections.<DayOrder>emptyList());
        }
        return getApi().loadDayOrders(getId(), page, 5, startTime, endTime)
                .map(response -> {
                    dayOrderPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<GoodsSell>> loadGoodsSellList(int page, String startTime, String endTime) {
        if (goodsSellPages != -1 && page > goodsSellPages) {
            return Observable.just(Collections.<GoodsSell>emptyList());
        }
        return getApi().loadGoodsSellList(getId(), page, 5, startTime, endTime)
                .map(response -> {
                    goodsSellPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Monthly>> loadMonthlies(int page, String startTime) {
        if (monthliesPages != -1 && page > monthliesPages) {
            return Observable.just(Collections.<Monthly>emptyList());
        }
        return getApi().loadMonthlies(getId(), page, 5, startTime)
                .map(response -> {
                    monthliesPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<GoodsSort>> loadGoods(String status) {
        return getApi().loadGoods(getId(), status)
                .map(response -> {
                    List<GoodsSort> sorts = response.map();
                    goodsSorts.clear();
                    goodsSorts.addAll(sorts);
                    return sorts;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<GoodsSort>> loadGoodsSorts() {
        return getApi().loadGoodsSorts(getId())
                .map(LoadGoodsSortsResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Goods> loadGoodsDetail(long goodsId) {
        return getApi().loadGoodsDetail(getId(), goodsId)
                .map(LoadGoodsDetailResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> manageGoodsStatus(long goodsId, int status) {
        return getApi().manageGoodsStatus(getId(), goodsId, status == 0 ? 2 : 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Comment>> loadComments(int page) {
        if (commentPages != -1 && page > commentPages) {
            return Observable.just(Collections.<Comment>emptyList());
        }
        return getApi().loadComments(getId(), page, 10)
                .map(response -> {
                    commentPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Comment> loadCommentDetail(long id) {
        return getApi().loadCommentDetail(id)
                .map(LoadCommentResponse::map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> replyComment(long id, String reply) {
        return getApi().replyComment(id, reply)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Activity>> loadActivities(int page) {
        if (activityPages != -1 && page > activityPages) {
            return Observable.just(Collections.<Activity>emptyList());
        }
        return getApi().loadActivities(getId(), page, 10)
                .map(response -> {
                    activityPages = response.totalPage;
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Goods>> loadActivityGoods(int page, int type) {
        if (activityPages != -1 && page > activityPages) {
            return Observable.just(Collections.<Goods>emptyList());
        }
        Observable<LoadActivityGoodsResponse> ob;
        if (type == 4) {
            ob = getApi().loadActivityGoodsForSpecialOffer(getId(), page, 10);
        } else {
            ob = getApi().loadActivityGoods(getId(), page, 10);
        }
        return ob.map(response -> {
            activityGoodsPages = response.totalPage;
            return response.map();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> saveOrUpdateActivity(String name, String startTime, String endTime, int type,
                                                         String full, String less, String discount, String disprice,
                                                         String goodsId, String goods, String standardId) {
        return getApi().saveOrUpdateActivity(getId(), name, startTime, endTime, type, full, less, discount, disprice,
                goodsId, goods, standardId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> changeActivityStatus(long activityId) {
        return getApi().changeActivityStatus(getId(), activityId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> addGoodsSort(String name, String describe) {
        return getApi().addGoodsSort(getId(), name, describe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> updateGoodsSort(long sortId, String name, String describe) {
        return getApi().updateGoodsSort(getId(), sortId, name, describe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> deleteGoodsSort(long sortId) {
        return getApi().deleteGoodsSort(getId(), sortId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> saveOrUpdateGoods(
            int status,
            long goodsId,
            String name,
            long sortId,
            String sortName,
            String imagePath,
            String thumbnailPath,
            boolean isLimited,
            boolean needFullPrice,
            String limitCount,
            String startTime,
            String endTime,
            boolean needPackingCharge,
            String count,
            String describe,
            List<Standard> standards,
            List<Option> options
    ) {
        Gson gson = DataLayer.getGson();
        LinkedTreeMap<String, Object> map = new LinkedTreeMap<>();
        map.put("status", status);
        map.put("name", name);
        map.put("sellTypeId", sortId);
        map.put("sellTypeName", sortName);
        map.put("imgPath", imagePath);
        map.put("mini_imgPath", thumbnailPath);
        map.put("ptype", needPackingCharge ? 0 : 1);
        map.put("num", count);
        map.put("content", describe);
        map.put("standardList", standards);
        map.put("optionList", options);
        map.put("islimited", isLimited ? 1 : 0);
        if (isLimited) {
            map.put("limiStartTime", startTime.replace(".", "-") + " 00:00:00");
            map.put("limiEndTime", endTime.replace(".", "-") + " 00:00:00");
            map.put("limittype", needFullPrice ? 1 : 0);
            map.put("limitNum", limitCount);
        }

        Observable<BaseResponse> obs;
        if (goodsId != 0) {
            map.put("id", goodsId);
            obs = getApi().updateGoods(getId(), gson.toJson(map));
        } else {
            obs = getApi().addGoods(getId(), gson.toJson(map));
        }

        return obs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void resetIncomeRecordPages(){
        incomeRecordPages = -1;
    }

    public int getIncomeRecordPages() {
        return incomeRecordPages;
    }

    public void resetCashRecordPages(){
        cashRecordPages = -1;
    }

    public int getCashRecordPages() {
        return cashRecordPages;
    }

    public void resetMessagePages() {
        messagePages = -1;
    }

    public int getMessagePages() {
        return messagePages;
    }

    public void resetCommentPages() {
        commentPages = -1;
    }

    public int getCommentPages() {
        return commentPages;
    }

    public int getActivityPages() {
        return activityPages;
    }

    public int getActivityGoodsPages() {
        return activityGoodsPages;
    }

    public void resetActivityGoodsPages() {
        activityGoodsPages = -1;
    }

    public int getDayOrderPages() {
        return dayOrderPages;
    }

    public int getGoodsSellPages() {
        return goodsSellPages;
    }

    public int getMonthliesPages() {
        return monthliesPages;
    }

    public void resetActivityPages() {
        activityPages = -1;
    }

    public void resetDayOrderPages() {
        dayOrderPages = -1;
    }

    public void resetGoodsSellPages() {
        goodsSellPages = -1;
    }

    public void resetMonthlyPages() {
        monthliesPages = -1;
    }

    public List<Goods> getGoodsList() {
        List<Goods> goods = new ArrayList<>();
        goods.addAll(goodsSorts.get(0).goods);
        return goods;
    }

    public List<Goods> getGoodsList(long sortId) {
        List<Goods> goods = new ArrayList<>();
        for (GoodsSort sort : goodsSorts) {
            sort.selected = sort.id == sortId;
            if (sort.id == sortId) {
                goods.addAll(sort.goods);
            }
        }
        return goods;
    }
}
