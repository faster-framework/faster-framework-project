package cn.org.faster.framework.xxl.job.server.service;

import cn.org.faster.framework.xxl.job.server.core.model.XxlJobGroup;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobInfo;
import cn.org.faster.framework.xxl.job.server.core.util.I18nUtil;
import cn.org.faster.framework.xxl.job.server.mapper.XxlJobGroupMapper;
import cn.org.faster.framework.xxl.job.server.mapper.XxlJobInfoMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.GsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbowen
 * @since 2021-02-15 21:33
 */
@Service
public class JobApiExtraService {
    @Autowired
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Autowired
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Autowired
    private XxlJobService xxlJobService;

    public ReturnT<String> createJobGroup(XxlJobGroup xxlJobGroup) {

        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            return new ReturnT<String>(500, "AppName" + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_title") + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList") + I18nUtil.getString("system_unvalid"));
            }

            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupMapper.save(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    public ReturnT<String> removeJobGroup(int id) {
        // valid
        int count = xxlJobInfoMapper.pageListCount(0, 10, id, -1, null, null, null);
        if (count > 0) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<XxlJobGroup> allList = xxlJobGroupMapper.findAll();
        if (allList.size() == 1) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = xxlJobGroupMapper.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    public ReturnT<String> jobGroupList(XxlJobGroup xxlJobGroup) {
        List<XxlJobGroup> list = xxlJobGroupMapper.list(xxlJobGroup.getAppname(), xxlJobGroup.getTitle());
        ReturnT<String> returnT = ReturnT.SUCCESS;
        returnT.setContent(GsonTool.toJson(list));
        return returnT;
    }

    public ReturnT<String> jobInfoList(XxlJobInfo xxlJobInfo) {
        List<XxlJobInfo> list = xxlJobInfoMapper.list(xxlJobInfo);
        ReturnT<String> returnT = ReturnT.SUCCESS;
        returnT.setContent(GsonTool.toJson(list));
        return returnT;
    }

    public ReturnT<String> createJobInfo(XxlJobInfo xxlJobInfo) {
        return xxlJobService.add(xxlJobInfo);
    }

    public ReturnT<String> removeJobInfo(XxlJobInfo xxlJobInfo) {
        return xxlJobService.remove(xxlJobInfo.getId());
    }

    public ReturnT<String> startJobInfo(XxlJobInfo xxlJobInfo) {
        return xxlJobService.start(xxlJobInfo.getId());
    }

    public ReturnT<String> stopJobInfo(XxlJobInfo xxlJobInfo) {
        return xxlJobService.stop(xxlJobInfo.getId());
    }
}
