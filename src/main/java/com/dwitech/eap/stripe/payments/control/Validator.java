package com.dwitech.eap.stripe.payments.control;

public class Validator {
    public static boolean emptyParameter(final String param) {
        return param == null || param.isEmpty();
    }
    public static boolean emptyParameter(final Double param) {
        return param == null || param == 0;
    }
}