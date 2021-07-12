package com.example.store_webApp;

public enum OrderStatus {
    IN_PROCESS{
        @Override
        public String toString() {
            return "In Process";
        }
    },
    SENT{
        @Override
        public String toString() {
            return "Sent";
        }
    },
    ARRIVED{
        @Override
        public String toString() {
            return "Arrived";
        }
    },
    CANCELED{
        @Override
        public String toString() {
            return "Canceled";
        }
    }

}
