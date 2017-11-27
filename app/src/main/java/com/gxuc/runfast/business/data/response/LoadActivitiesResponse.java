package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Activity;
import com.gxuc.runfast.business.data.bean.ActivityDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动列表
 * Created by Berial on 2017/9/1.
 */
public class LoadActivitiesResponse extends BaseResponse implements Mapper<List<Activity>> {

    public List<ActivityDTO> activity;

    @SerializedName(value = "totalpage", alternate = "totalPage")
    public int totalPage;

    @Override
    public List<Activity> map() {
        ArrayList<Activity> activities = new ArrayList<>();
        if (activity != null && !activity.isEmpty()) {
            for (ActivityDTO dto : activity) {
                activities.add(dto.map());
            }
        }
        return activities;
    }
}
