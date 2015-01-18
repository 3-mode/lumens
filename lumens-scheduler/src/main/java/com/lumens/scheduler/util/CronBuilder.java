/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.scheduler.util;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class CronBuilder {
    private long second;
    private long minute;
    private long hour;
    private long dayOfMonth;
    private long dayOfWeek;
    private long month;
    private long year;  // optional
    
    public CronBuilder(long hour, long minute, long second, long month, long dayOfMonth, long dayOfWeek, long year){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
    }
    
    public String getCronExpression(){
        return null;
    }
}
