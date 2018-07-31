package com.example.jaehyolim.viewmodel.repository;

import java.util.Map;

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
        return  null;
      /*  return getRetrofitAPI()
                .getGajagoReserve(getQuery(TYPE.GAJAGO,"leisure_findorder"));*/
        /*return getRetrofitAPI()
                .getAreaSelect(("leisure_area"), ("hotelnjoy"), ("gajago"), (isTest()));*/
    }

    @Override
    public Map<String, String> queryValuesMap() {
        Map<String, String> valuesMap = getValuesMap();
        valuesMap.put("option","list");
        return valuesMap;
    }


}
