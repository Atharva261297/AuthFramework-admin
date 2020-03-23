package com.atharva.auth.adminservice.client;

import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "EmailClient", url = "${url.email}")
public interface EmailFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/verify-email/{email}/{userId}/{projectName}")
    ErrorCodes senVerificationEmail(@PathVariable(name = "email") String email, @PathVariable(name = "userId") String userId,
                                    @PathVariable(name = "projectName") String projectName);

}
