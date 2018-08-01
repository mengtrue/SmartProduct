package com.gtk.smartmanagement.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultData {
    public static final String SERIES = "SERIES";
    public static final String AREA_NAME = "AREA_NAME";
    public static final String ORG_NAME = "ORG_NAME";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";

    public static class OrgMap {
        private String sTime;
        private String eTime;
        private String serie;
        private String area;

        public OrgMap(String sTime, String eTime, String area, String serie) {
            this.sTime = sTime;
            this.eTime = eTime;
            this.area = area;
            this.serie = serie;
        }

        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put(START_TIME, sTime);
            map.put(END_TIME, eTime);
            map.put(AREA_NAME, area);
            map.put(SERIES, serie);
            return map;
        }
    }

    public static class OrgDataMap {
        private String sTime;
        private String eTime;
        private String serie;
        private String area;
        private String org;

        public OrgDataMap(String sTime, String eTime, String serie, String area, String org) {
            this.sTime = sTime;
            this.eTime = eTime;
            this.serie = serie;
            this.area = area;
            this.org = org;
        }

        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put(START_TIME, sTime);
            map.put(END_TIME, eTime);
            map.put(SERIES, serie);
            map.put(AREA_NAME, area);
            map.put(ORG_NAME, org);
            return map;
        }
    }

    public static class OrgData {
        private String name;
        private String subName;
        private String value;

        public OrgData(String name, String subName, String value) {
            this.name = name;
            this.subName = subName;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getSubName() {
            return subName;
        }

        public String getValue() {
            return value;
        }
    }

    public class OrgJsonBean {
        private String status;
        private String msg;
        private List<String> data = new ArrayList<String>();

        @Override
        public String toString() {
            return String.valueOf(data.size());
        }

        public OrgData[] getOrgData() {
            int size = data.size();
            OrgData[] orgs = new OrgData[size/2];
            for (int i = 0; i < size/2; i++) {
                orgs[i] = new OrgData(data.get(i), data.get(i), data.get(size/2 + i) + "%");
            }
            return orgs;
        }
    }

    public class OrgDetailDataJsonBean {
        private String status;
        private String msg;
        private Data data;

        public class Data {
            public String CIRCLE_LOSS_EFFICIENCY;
            public String LINE_END_LOSS_EFFICIENCY;
            public String FIRT_PRODUCT_LOSS_EFFICIENCY;
            public String CHANGE_PN_LOSS_EFFICIENCY;
            public String CHANGE_WO_LOSS_EFFICIENCY;
            public String FAULT_LOSS_EFFICIENCY;
            public String TOTAL_EFFICIENCY;
        }

        @Override
        public String toString() {
            return "data total : " + data.TOTAL_EFFICIENCY;
        }
    }

}
