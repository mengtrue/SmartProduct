package com.gtk.smartmanagement.data;

public class RemoteToken {
    public class TokenJsonBean {
        public String status;
        public String msg;
        public Data data;

        public class Data {
            public String token0;
            public String token1;

            @Override
            public String toString() {
                return "Data : token0 = " + token0 + ", token1 = " + token1;
            }
        }

        @Override
        public String toString() {
            return "Token respone : status = " + status + ", msg = " + msg + "\n" + data.toString();
        }
    }
}
