package cn.org.faster.framework.xxl.job.server.controller;

import cn.org.faster.framework.xxl.job.server.controller.annotation.PermissionLimit;
import cn.org.faster.framework.xxl.job.server.core.conf.XxlJobAdminConfig;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobGroup;
import cn.org.faster.framework.xxl.job.server.core.model.XxlJobInfo;
import cn.org.faster.framework.xxl.job.server.service.JobApiExtraService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xuxueli on 17/5/10.
 */
@RestController
@RequestMapping("/api")
public class JobApiController {

    @Autowired
    private AdminBiz adminBiz;
    @Autowired
    private JobApiExtraService jobApiExtraService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/callback")
    @PermissionLimit(limit = false)
    public ReturnT<String> callback(@RequestBody List<HandleCallbackParam> callbackParamList) {
        if (errorToken()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return adminBiz.callback(callbackParamList);
    }

    @PostMapping("/registry")
    @PermissionLimit(limit = false)
    public ReturnT<String> registry(@RequestBody RegistryParam registryParam) {
        if (errorToken()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return adminBiz.registry(registryParam);
    }

    @PostMapping("/registryRemove")
    @PermissionLimit(limit = false)
    public ReturnT<String> registryRemove(@RequestBody RegistryParam registryParam) {
        if (errorToken()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return adminBiz.registryRemove(registryParam);
    }

    @PostMapping("/jobGroupList")
    @PermissionLimit(limit = false)
    public ReturnT<Object> jobGroupList(@RequestBody XxlJobGroup xxlJobGroup) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.jobGroupList(xxlJobGroup);
    }

    @PostMapping("/createJobGroup")
    @PermissionLimit(limit = false)
    public ReturnT<String> createJobGroup(@RequestBody XxlJobGroup xxlJobGroup) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.createJobGroup(xxlJobGroup);
    }

    @PostMapping("/removeJobGroup")
    @PermissionLimit(limit = false)
    public ReturnT<String> removeJobGroup(@RequestBody XxlJobGroup xxlJobGroup) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.removeJobGroup(xxlJobGroup.getId());
    }

    @PostMapping("/jobInfoList")
    @PermissionLimit(limit = false)
    public ReturnT<Object> jobInfoList(@RequestBody XxlJobInfo xxlJobInfo) {
        if (errorToken()) {
            return new ReturnT<Object>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.jobInfoList(xxlJobInfo);
    }

    @PostMapping("/createJobInfo")
    @PermissionLimit(limit = false)
    public ReturnT<String> createJobInfo(@RequestBody XxlJobInfo xxlJobInfo) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.createJobInfo(xxlJobInfo);
    }

    @PostMapping("/removeJobInfo")
    @PermissionLimit(limit = false)
    public ReturnT<String> removeJobInfo(@RequestBody XxlJobInfo xxlJobInfo) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.removeJobInfo(xxlJobInfo);
    }

    @PostMapping("/startJobInfo")
    @PermissionLimit(limit = false)
    public ReturnT<String> startJobInfo(@RequestBody XxlJobInfo xxlJobInfo) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.startJobInfo(xxlJobInfo);
    }


    @PostMapping("/stopJobInfo")
    @PermissionLimit(limit = false)
    public ReturnT<String> stopJobInfo(@RequestBody XxlJobInfo xxlJobInfo) {
        if (errorToken()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return jobApiExtraService.stopJobInfo(xxlJobInfo);
    }


    /**
     * 检验token
     *
     * @return true：成功 false：失败
     */
    private boolean errorToken() {
        return XxlJobAdminConfig.getAdminConfig().getAccessToken() != null
                && XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().length() > 0
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN));
    }


}
