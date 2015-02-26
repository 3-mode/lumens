/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author shaofeng wang
 */
public class DateTime {

    public final static DateFormat[] DATETIME_PATTERN = {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm"),
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy-MM-ddZZ"),
        new SimpleDateFormat("'T'HH:mm:ss"),
        new SimpleDateFormat("'T'HH:mm:ssZZ"),
        new SimpleDateFormat("HH:mm:ss"),
        new SimpleDateFormat("HH:mm:ssZZ"),
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
    };

    public static Date parse(String datetime) {
        for (DateFormat dateFormat : DATETIME_PATTERN) {
            try {
                return dateFormat.parse(datetime);
            } catch (ParseException ex) {
            }
        }
        throw new RuntimeException("Not support date time '" + datetime + "'");
    }
}
