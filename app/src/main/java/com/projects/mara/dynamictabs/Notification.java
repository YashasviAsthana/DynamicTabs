package com.projects.mara.dynamictabs;

import java.io.Serializable;

/**
 * Created by Yashasvi on 28-06-2017.
 */

public class Notification implements Serializable{
    String heading,desc;

    public String getDesc() {
        return desc;
    }

    public String getHeading() {
        return heading;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
