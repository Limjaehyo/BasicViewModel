package com.example.jaehyolim.viewmodel.repository;

import android.support.annotation.NonNull;

import com.example.jaehyolim.viewmodel.BuildConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ViewModel -> Repository 동신을위한 최상위 Repository
 * @param <T>
 */
public abstract class BaseRepository<T extends BaseModel > implements getResponseInterFace<T> {

    protected enum TYPE {
        HOTEL("호텔"), GAJAGO("액티비티"), TABLE("다이닝");
        final private String name;

        TYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
    protected String isTest() {
        return  BuildConfig.DEBUG ? "Y" : "N";
    }

    /**
     * 기본적인 커리를 완성하여 리턴하며 추가적으로 Map 형태의 Values 를 받아 세팅한다.
     * 기본적인 값 추가
     *
     * @param type   Query 형태 타잎을 받는다
     * @param apiKey //apiKey를 받아 세팅한다.
     * @return 기본 QueryMap + Values Map
     */
    protected Map<String, String> getQuery(TYPE type, String apiKey) {
        final Map<String, String> map = getDefaultMap(type, apiKey);


        //추가적으로 자식에서 구현받은 map key Values 룰 저장한다. key 값으로 받아온 values 값이 빈경우 넣지 않는ㄴ다.
        for (String key : queryValuesMap().keySet()) {
            if (!queryValuesMap().get(key).isEmpty()) {
                map.put(key, (queryValuesMap().get(key)));
            }
        }
        return map;
    }
    @NonNull
    private Map<String, String> getDefaultMap(TYPE type, String apiKey) {
        final Map<String, String> map = new LinkedHashMap<>();
        //기본적으로 들어갈 키
        map.put("apiKey", (apiKey));

        return map;
    }
    protected Map<String, String> getValuesMap() {
        return new LinkedHashMap<>();
    }
}
