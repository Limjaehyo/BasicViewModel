package com.example.jaehyolim.viewmodel.repository;

import io.reactivex.Observable;


public class TempRepository extends BaseRepository<BaseModel> {

    private static TempRepository ourInstance;

    public static TempRepository getInstance() {
        if (ourInstance == null) {
            synchronized (TempRepository.class) {
                if (ourInstance == null) {
                    ourInstance = new TempRepository();
                }
            }
        }
        return ourInstance;
    }

    private TempRepository() {
    }

    public Observable<BaseModel> getResponse() {
        /*return getRetrofitAPI()
                .getAreaSelect(apiEncryption("leisure_area"), apiEncryption("hotelnjoy"), apiEncryption("gajago"), apiEncryption(isTest()));*/
    }


}
