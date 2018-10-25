package cn.org.faster.framework.sms.modules.sms.service.ali;


import cn.org.faster.framework.sms.modules.sms.service.ISmsService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangbowen
 * @since 2018/9/3
 */
@Slf4j
public class AliSmsService implements ISmsService<SendSmsRequest> {
    private IClientProfile profile;

    public AliSmsService(String accessKeyId, String accessKeySecret) {
        try {
            //初始化ascClient,暂时不支持多region（请勿修改）
            profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean send(SendSmsRequest request) {
        try {
            IAcsClient acsClient = new DefaultAcsClient(profile);
            request.setMethod(MethodType.POST);
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().
                    equals("OK")) {
                return true;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }
}
