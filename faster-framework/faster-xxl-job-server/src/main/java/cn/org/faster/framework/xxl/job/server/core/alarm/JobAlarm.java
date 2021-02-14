package cn.org.faster.framework.xxl.job.server.core.alarm;

import cn.org.faster.framework.xxl.job.server.core.model.XxlJobInfo;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
