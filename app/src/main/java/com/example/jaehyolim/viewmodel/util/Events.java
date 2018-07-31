package com.example.jaehyolim.viewmodel.util;


public class Events {
    public static class Query {

        public static class GuestCard {

            public GuestCard(String id) {
                this.id = id;
            }

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }

    public static class Insert {

        public static class GuestCardValidation {
            public boolean valid = false;
            public String name;
            public String phoneNumber;
            public String note;

            public GuestCardValidation() {
            }
        }
    }
    public static class Update {

        public static class GuestCard {

            public GuestCard(String id , String name , String phone , String note) {

                this.id = id;
                this.name = name;
                this.phoneNumber = phone;
                this.note = note;
            }

            public String id;
            public String name;
            public String phoneNumber;
            public String note;
            public boolean valid = false;

        }

        public static class GuestCardValid {

            public String id;
            public boolean valid;
        }
        public static  class TabBarDateUpDate{
            public int position;
            public String data;
        }
    }

    public static class View {

        public Object model;
    }

    public static class ReservationDetail {

        public Object model;
    }

}
