package com.example.jaehyolim.viewmodel.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hotelnjoy.R;
import com.hotelnjoy.customview.FontTextView;
import com.hotelnjoy.util.http.API;
import com.hotelnjoy.view.HotelNJoyApplication;
import com.matei.calendar.DayData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sjlee on 2015-11-30.
 */
public class Util {
    public static String sToken = "", sOSVersion = "", sDeviceModel = "", sMode = "", sPhone = "", sVersion = "";
//    public static int sPreAreaPosition = 0;

    public static final String S_TAG_HOTEL_CODE = "tag_hotel_code";
    public static final String S_TAG_LIST_CODE = "tag_list_code";
    public static final String S_TAG_EVENT_CODE = "tag_event_code";
    public static final String S_TAG_MENU_TYPE = "tag_menu_type";
    public static final String S_TAG_WEBVIEW_URL = "tag_webview_url";
    public static final String GAJAGO_DETAIL = "gajago_detail";
    public static final String GAJAGO_KEYWORD_SEARCHES = "gajago_keyword_searches";
    public static final String GAJAGO_AREA_CODE = "gajago_area_code";
    public static final String GAJAGO_AREA_NAME = "gajago_area_name";
    public static final ArrayList<String> S_ARR_EVENT_TAG = new ArrayList<>();

    public static void startAnimationActivity(final Activity activity, final Class<?> clazz, final Bundle bundle) {
        final Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        (activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    /**
     * Android SharedPreferences 에 data 를 저장한다.
     *
     * @param key   저장할 Key
     * @param value 저장할 Value
     * @param <T>   Value 값에 따라 저장을 달리한다.
     */
    public static <T> void setSharedPreferences(Context context,String key, T value) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.apply();
    }


    /**
     * Android SharedPreferences 의 data 를 가져온다.
     *
     * @param key 가져올 Key
     * @param <R> 가져올 Key 에 저장된 Value 값에따라 리턴값이 달라진다.
     * @return 제네릭 값의 Data 값.
     */
    public static <R extends Object> R getSharedPreferences(Context context,String key) throws ClassCastException {
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Object value = settings.getAll().get(key);
        return (R) value;

    }


    /**
     * Android SharedPreferences 의 data 를 지운다.
     *
     * @param key 가져올 Key
     */
    public static void removesSharedPreferences(Context context ,String key) {
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        settings.edit().remove(key).apply();
    }

    /**
     * @param context CacheDir에 접근하기 위한 Context
     * @param bitmap  저장할 Bitmap
     * @param name    저장할 BitmapName
     * @return 저장한 경로
     */
    public static String saveCacheBitmapToJpeg(Context context, Bitmap bitmap, String name) {
        File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로
        String fileName = name + ".png";  // 파일이름 지정
        File tempFile = new File(storage, fileName);

        try {
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            File.createTempFile(fileName, null, context.getCacheDir());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);  // 넘겨 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            out.close(); // 마무리로 닫아줍니다.

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴!
    }





    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context         다이얼로그 생성에 필요한 콘텍스트
     * @param title           타이틀을 세팅할 String
     * @param firstText       contents 첫라인 Text
     * @param secondText      contents 두번째라인 Text
     * @param firstTextColor  contents 첫라인 TextColor
     * @param secondTextColor contents 두번째라인 TextColor
     * @param clickListener   오른쪽 버튼 클릭 리스너
     */
    public static void showContentsDialog(Context context, String title, String firstText, String secondText, int firstTextColor, int secondTextColor, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setTextColor(firstTextColor);
        ((FontTextView) dialog.findViewById(R.id.secondContentText)).setText(secondText);
        ((FontTextView) dialog.findViewById(R.id.secondContentText)).setTextColor(secondTextColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context         다이얼로그 생성에 필요한 콘텍스트
     * @param title           타이틀을 세팅할 String
     * @param firstText       contents 첫라인 Text
     * @param secondText      contents 두번째라인 Text
     * @param firstTextColor  contents 첫라인 TextColor
     * @param secondTextColor contents 두번째라인 TextColor
     * @param doneText        오른쪽 버튼 클릭 Text
     * @param clickListener   오른쪽 버튼 클릭 리스너
     */
    public static MaterialDialog showContentsDialog(Context context, String title, String firstText, String secondText, int firstTextColor, int secondTextColor, String doneText, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((Button) dialog.findViewById(R.id.bt_done)).setText(doneText);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setTextColor(firstTextColor);
        ((FontTextView) dialog.findViewById(R.id.secondContentText)).setText(secondText);
        ((FontTextView) dialog.findViewById(R.id.secondContentText)).setTextColor(secondTextColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context       다이얼로그 생성에 필요한 콘텍스트
     * @param title         타이틀을 세팅할 String
     * @param firstText     contents 첫라인 Text
     * @param secondText    contents 두번째라인 Text
     * @param doneText      오른쪽 버튼 클릭 Text
     * @param clickListener 오른쪽 버튼 클릭 리스너
     */
    public static void showContentsDialog(Context context, String title, String firstText, String secondText, String doneText, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((Button) dialog.findViewById(R.id.bt_done)).setText(doneText);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        ((FontTextView) dialog.findViewById(R.id.secondContentText)).setText(secondText);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context       다이얼로그 생성에 필요한 콘텍스트
     * @param title         타이틀을 세팅할 String
     * @param firstText     contents 첫라인 Text
     * @param clickListener 오른쪽 버튼 클릭 리스너
     */
    public static MaterialDialog showContentsDialog(Context context, String title, String firstText, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        if (clickListener != null) {
            dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        } else {
            dialog.findViewById(R.id.bt_cancel).setVisibility(View.GONE);
            dialog.findViewById(R.id.bt_done).setOnClickListener(v -> dialog.dismiss());
        }
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        dialog.findViewById(R.id.secondContentText).setVisibility(View.GONE);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context       다이얼로그 생성에 필요한 콘텍스트
     * @param title         타이틀을 세팅할 String
     * @param firstText     contents 첫라인 Text
     * @param doneText      오른쪽 버튼 클릭 Text
     * @param clickListener 오른쪽 버튼 클릭 리스너
     */
    public static MaterialDialog showContentsDialog(Context context, String title, String
            firstText, String doneText, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        ((Button) dialog.findViewById(R.id.bt_done)).setText(doneText);
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        dialog.findViewById(R.id.secondContentText).setVisibility(View.GONE);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * 호텔엔조이 디폴트 다이얼로그
     *
     * @param context        다이얼로그 생성에 필요한 콘텍스트
     * @param title          타이틀을 세팅할 String
     * @param firstText      contents 첫라인 Text
     * @param doneText       오른쪽 버튼 클릭 Text
     * @param firstTextColor contents 첫라인 TextColor
     * @param clickListener  오른쪽 버튼 클릭 리스너
     */
    public static void showContentsDialog(Context context, String title, String firstText,
                                          int firstTextColor, String doneText, View.OnClickListener clickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gajago_contact, false)
                .build();

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
        //전화 연결 클릭
        dialog.findViewById(R.id.bt_done).setOnClickListener(clickListener);
        ((Button) dialog.findViewById(R.id.bt_done)).setText(doneText);
        ((FontTextView) dialog.findViewById(R.id.tv_title)).setText(title);
        dialog.findViewById(R.id.secondContentText).setVisibility(View.GONE);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setText(firstText);
        ((FontTextView) dialog.findViewById(R.id.topContentText)).setTextColor(firstTextColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static RetrofitAPI getRetrofitAPI() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.API_BASE)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(RetrofitAPI.class);
    }

    public static String dayofWeek(int nWeek) {
        String strweek = "";
        switch (nWeek) {
            case 1:
                strweek = "일";
                break;
            case 2:
                strweek = "월";
                break;
            case 3:
                strweek = "화";
                break;
            case 4:
                strweek = "수";
                break;
            case 5:
                strweek = "목";
                break;
            case 6:
                strweek = "금";
                break;
            case 7:
                strweek = "토";
                break;

        }
        return strweek;
    }

    public static int hotelNameColor(String title) {
        int rgb = 0;
        if (title.equals("특1급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("특2급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("1급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("2급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("3급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("4급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("5급")) {
            rgb = Color.rgb(0, 58, 136);
        } else if (title.equals("관광")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("부띠끄")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("비지니스") || title.equals("비즈니스")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("콘도")) {
            rgb = Color.rgb(24, 117, 171);
        } else if (title.equals("리조트")) {
            rgb = Color.rgb(24, 117, 171);
        } else if (title.equals("글램핑")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("카라반")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("캠핑카")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("게스트하우스")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("펜션")) {
            rgb = Color.rgb(24, 117, 171);
        } else if (title.equals("유스호스텔")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("민박")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("한옥민박")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("모텔")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("가족호텔")) {
            rgb = Color.rgb(72, 158, 133);
        } else if (title.equals("레지던스")) {
            rgb = Color.rgb(0, 58, 136);
        } else {        //기타
            //error
            rgb = Color.rgb(0, 58, 136);
        }
        return rgb;
    }

    public static void initEventTAG() {
        if (S_ARR_EVENT_TAG == null || S_ARR_EVENT_TAG.size() == 0) {
            S_ARR_EVENT_TAG.add(S_TAG_HOTEL_CODE);
            S_ARR_EVENT_TAG.add(S_TAG_LIST_CODE);
            S_ARR_EVENT_TAG.add(S_TAG_EVENT_CODE);
            S_ARR_EVENT_TAG.add(S_TAG_MENU_TYPE);
            S_ARR_EVENT_TAG.add(S_TAG_WEBVIEW_URL);
        }
    }

    public static String formatDateYMDDash(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateMdSlash(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateMdSlashMore(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateMdDote(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateMdEDote(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd (EEE)", Locale.KOREA);
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateYMdDote(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd(EEE)", Locale.KOREA);
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateyMdDote(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd(EEE)", Locale.KOREA);
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateYMDash(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateMKorY(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM월 yyyy");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateKorYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String formatString = format.format(date);
        return formatString;
    }

    public static String formatDateE(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("(EEE)");
        String formatString = format.format(date);
        return formatString;
    }

    public static String toMoneyFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public static String toMoneyFormat(String num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(Integer.parseInt(num));
    }

    public static String formatDateYMDDash(DayData day) {
        if (day == null) return "";
        String msg = HotelNJoyUtil.formatDateYMDDash(day.getDay().getTime());
        return msg;
    }

    public static void setListcode(String listCode) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("listcd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("v_listcode", listCode);
        editor.commit();
    }

    public static String getListCode() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("listcd", Context.MODE_PRIVATE);
        String lscd = sharedPreferences.getString("v_listcode", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("v_listcode");
        editor.commit();
        return lscd;
    }

    public static String formatDateYMDDot(DayData day) {
        if (day == null) return "";
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        String formatString = format.format(day.getDay().getTime());
        return formatString;
    }

    public static void setPush() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isPush", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ispush", sMode);
        editor.commit();
    }

    public static String getPush() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isPush", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ispush", "regist");
    }

    public static void setFirstInstall(boolean isFirst) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }


    public static boolean isRoomDetail() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("roomDetail", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("roomDetailClick", false);

    }

    public static void get(String v_pCode, String v_offer, String v_startdate, String end_date, String v_Count) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("v_pCode", v_pCode);
        editor.putString("v_offer", v_offer);
        editor.putString("v_startdate", v_startdate);
        editor.putString("end_date", end_date);
        editor.putString("v_Count", v_Count);
        editor.commit();
    }


    public static boolean getFirstInstall() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirst", true);
    }

    public static void setGuide(boolean isGuide) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isGuide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isGuide", isGuide);
        editor.commit();
    }

    public static boolean getGuide() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isGuide", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isGuide", true);
    }

    public static void setMapGuide(boolean isGuide) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isMapGuide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isMapGuide", isGuide);
        editor.commit();
    }

    public static boolean getMapGuide() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isMapGuide", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isMapGuide", true);
    }

    public static void setCommunityGuide(boolean isGuide) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isCommunityGuide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isCommunityGuide", isGuide);
        editor.commit();
    }

    public static boolean getCommunityGuide() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isCommunityGuide", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isCommunityGuide", true);
    }

//    public static void setGPSAgree(boolean isGPSAgree) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isGPSAgree", isGPSAgree);
//        editor.commit();
//    }
//
//    public static boolean isGPSAgree() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
//        return sharedPreferences.getBoolean("isGPSAgree", false);
//    }

    public static void setStartGuide() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isStartGuide", false);
        editor.commit();
    }

    public static boolean isStartGuide() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
        return sharedPreferences.getBoolean("isStartGuide", true);
    }

    public static boolean isPermmision() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isPermmision", Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
        return sharedPreferences.getBoolean("isPermmision", true);
    }

    public static boolean isFirstReview() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirstReview", Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
        return sharedPreferences.getBoolean("isFirstReview", true);
    }

    public static void setFirstReview() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirstReview", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstReview", false);
        editor.commit();
    }

    public static void setPermmision() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isPermmision", Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HotelNJoyApplication.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPermmision", false);
        editor.commit();
    }

    public static void setIntroName(String introUrl) {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("introName", introUrl);
        editor.commit();
    }

    public static String getIntroName() {
        SharedPreferences sharedPreferences = HotelNJoyApplication.getInstance().getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        return sharedPreferences.getString("introName", "");
    }

    public static Object deepCopy(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);

        byte[] buff = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buff);
        ObjectInputStream os = new ObjectInputStream(bais);
        Object copy = os.readObject();
        return copy;
    }

    public static boolean isStringEmpty(@Nullable CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public static Calendar CalendarFromString(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(formatter.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static boolean isMidnightTime() {
        Calendar cal = Calendar.getInstance();
        Calendar standardMidnightTime = (Calendar) cal.clone();
        standardMidnightTime.clear();
        standardMidnightTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        return cal.before(standardMidnightTime);
    }

    public static String isMidnightTimeString() {
        Calendar cal = Calendar.getInstance();
        String month = "";
        String date = "";
        if ((cal.get(Calendar.MONTH) + 1) < 10)
            month = "0";
        if ((cal.get(Calendar.DATE) - 1) < 10)
            date = "0";
        String returnDate = cal.get(Calendar.YEAR) + "-" + month + (cal.get(Calendar.MONTH) + 1) + "-" + date + (cal.get(Calendar.DATE) - 1);
        return returnDate;
    }

    public static String isMidnightTimeTommorow() {
        Calendar cal = Calendar.getInstance();
        Calendar standardMidnightTime = (Calendar) cal.clone();
        standardMidnightTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        String month = "";
        String date = "";
        if ((cal.get(Calendar.MONTH) + 1) < 10)
            month = "0";
        if ((cal.get(Calendar.DATE) - 1) < 10)
            date = "0";
        String returnDate = cal.get(Calendar.YEAR) + "-" + month + (cal.get(Calendar.MONTH) + 1) + "-" + date + (cal.get(Calendar.DATE));
        return returnDate;
    }
}
