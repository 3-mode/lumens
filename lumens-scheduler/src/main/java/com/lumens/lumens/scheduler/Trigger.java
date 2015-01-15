/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.lumens.scheduler;

import java.util.Date;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface Trigger {
    public int getRepeatCount();
    public Date getStartTime();
    public Date getEndTime();
    public int getRepeatInterval();
    public Long getId();
}
