package com.example.jaehyolim.viewmodel.util;


public class TempRxEvent implements RxEventBus.OnEventListener<Events.ReservationDetail> {

    public TempRxEvent() {
        RxEventBus.getInstance().registerListener(this, Events.ReservationDetail.class);
    }


    private void evnerPost() {
        Events.ReservationDetail reservationDetail = new Events.ReservationDetail();
        reservationDetail.model = "sdfsdf";
        RxEventBus.getInstance().postEvent(reservationDetail);
    }

    @Override
    public void onEvent(Events.ReservationDetail reservationDetail) {
        //ㅇㅣ벤트 받는다.
    }
}
