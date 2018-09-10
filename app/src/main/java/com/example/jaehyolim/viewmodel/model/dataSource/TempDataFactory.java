package com.example.jaehyolim.viewmodel.model.dataSource;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class TempDataFactory  extends DataSource.Factory{

    private MutableLiveData<FeedDataSource> mutableLiveData;
    private FeedDataSource feedDataSource;
//    private AppController appController;

    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource();
        mutableLiveData.postValue(feedDataSource);
        return feedDataSource;
    }
    public MutableLiveData<FeedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
