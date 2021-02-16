package cn.org.faster.framework.xxl.job.server.controller;

import cn.org.faster.framework.xxl.job.server.controller.annotation.PermissionLimit;
import cn.org.faster.framework.xxl.job.server.core.conf.XxlJobAdminConfig;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobGroup;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobInfo;
import cn.org.faster.framework.xxl.job.server.core.util.I18nUtil;
import cn.org.faster.framework.xxl.job.server.service.JobApiExtraService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.GsonTool;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by xuxueli on 17/5/10.
 */
@Controller
@RequestMapping("/api")
public class JobApiController {

    @Autowired
    private AdminBiz adminBiz;
    @Autowired
    private JobApiExtraService jobApiExtraService;

    /**
     * api
     *
     * @param uri
     * @param data
     * @return
     */
    @RequestMapping("/{uri}")
    @ResponseBody
    @PermissionLimit(limit=false)
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) String data) {

        // valid
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
        }
        if (uri==null || uri.trim().length()==0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
        }
        if (XxlJobAdminConfig.getAdminConfig().getAccessToken()!=null
                && XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN))) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // services mapping
        if ("callback".equals(uri)) {
            List<HandleCallbackParam> callbackParamList = GsonTool.fromJson(data, List.class, HandleCallbackParam.class);
            return adminBiz.callback(callbackParamList);
        } else if ("registry".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registry(registryParam);
        } else if ("registryRemove".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registryRemove(registryParam);
        }else if("jobGroupList".equals(uri)){
            XxlJobGroup xxlJobGroup = GsonTool.fromJson(data, XxlJobGroup.class);
            return jobApiExtraService.jobGroupList(xxlJobGroup);
        }else if("createJobGroup".equals(uri)){
            XxlJobGroup xxlJobGroup = GsonTool.fromJson(data, XxlJobGroup.class);
            return jobApiExtraService.createJobGroup(xxlJobGroup);
        }else if("removeJobGroup".equals(uri)){
            XxlJobGroup xxlJobGroup = GsonTool.fromJson(data, XxlJobGroup.class);
            return jobApiExtraService.removeJobGroup(xxlJobGroup.getId());
        }else if("jobInfoList".equals(uri)){
            XxlJobInfo xxlJobInfo = GsonTool.fromJson(data, XxlJobInfo.class);
            return jobApiExtraService.jobInfoList(xxlJobInfo);
        }else if("createJobInfo".equals(uri)){
            XxlJobInfo xxlJobInfo = GsonTool.fromJson(data, XxlJobInfo.class);
            return jobApiExtraService.createJobInfo(xxlJobInfo);
        }else if("removeJobInfo".equals(uri)){
            XxlJobInfo xxlJobInfo = GsonTool.fromJson(data, XxlJobInfo.class);
            return jobApiExtraService.removeJobInfo(xxlJobInfo);
        }else if("startJobInfo".equals(uri)){
            XxlJobInfo xxlJobInfo = GsonTool.fromJson(data, XxlJobInfo.class);
            return jobApiExtraService.startJobInfo(xxlJobInfo);
        } else if("stopJobInfo".equals(uri)){
            XxlJobInfo xxlJobInfo = GsonTool.fromJson(data, XxlJobInfo.class);
            return jobApiExtraService.stopJobInfo(xxlJobInfo);
        } else {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping("+ uri +") not found.");
        }

    }

}
