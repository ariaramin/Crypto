package com.ariaramin.crypto.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllMarket {

    @SerializedName("data")
    private Data data;

    @SerializedName("status")
    private Status status;

    public Data getData(){
        return data;
    }

    public Status getStatus(){
        return status;
    }


    public static class Data{

        @SerializedName("cryptoCurrencyList")
        private List<DataItem> cryptoCurrencyList;

        @SerializedName("totalCount")
        private int totalCount;

        public List<DataItem> getCryptoCurrencyList() {
            return cryptoCurrencyList;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static class Status{

        @SerializedName("timestamp")
        private String timestamp;

        @SerializedName("error_code")
        private int errorCode;

        @SerializedName("error_message")
        private String errorMessage;

        @SerializedName("elapsed")
        private int elapsed;

        @SerializedName("credit_count")
        private int creditCount;

        public String getTimestamp() {
            return timestamp;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getElapsed() {
            return elapsed;
        }

        public int getCreditCount() {
            return creditCount;
        }
    }
}
