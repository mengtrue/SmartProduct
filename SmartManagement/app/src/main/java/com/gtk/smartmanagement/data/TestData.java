package com.gtk.smartmanagement.data;

public class TestData {
    public static final String STAR_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String ORG_CODE = "ORG_CODE";
    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String IS_STANDARD = "IS_STANDARD";
    public static final String LINE_CODE = "LINE_CODE";

    public class TestDataJsonBean {
        public String status;
        public String msg;
        public Data data;

        public class Data {
            public String CIRCLE_LOSS_EFFICIENCY;
            public String LINE_END_LOSS_EFFICIENCY;
            public String FIRT_PRODUCT_LOSS_EFFICIENCY;
            public String CHANGE_PN_LOSS_EFFICIENCY;
            public String CHANGE_WO_LOSS_EFFICIENCY;
            public String FAULT_LOSS_EFFICIENCY;
            public String TOTAL_EFFICIENCY;

            @Override
            public String toString() {
                return "Data is " + "CIRCE_LOSS_EFFICIENCY = " + CIRCLE_LOSS_EFFICIENCY +
                        "\n LINE_END_LOSS_EFFICIENCY = " + LINE_END_LOSS_EFFICIENCY +
                        "\n FIRT_PRODUCT_LOSS_EFFICIENCY = " + FIRT_PRODUCT_LOSS_EFFICIENCY +
                        "\n CHANGE_PN_LOSS_EFFICIENCY = " + CHANGE_PN_LOSS_EFFICIENCY +
                        "\n CHANGE_WO_LOSS_EFFICIENCY = " + CHANGE_WO_LOSS_EFFICIENCY +
                        "\n FAULT_LOSS_EFFICIENCY = " + FAULT_LOSS_EFFICIENCY +
                        "\n TOTAL_EFFICIENCY = " + TOTAL_EFFICIENCY;

            }
        }

        @Override
        public String toString() {
            return "Receive : " + "status = " + status +
                    ", msg = " + msg +
                    ", data : " + data.toString();
        }
    }
}
