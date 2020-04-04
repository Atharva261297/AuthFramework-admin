package com.atharva.auth.adminservice.client;

import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "AuthClient", url = "${url.auth}")
public interface AuthFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    ErrorCodes register(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    ErrorCodes login(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.POST, value = "/reset")
    ErrorCodes resetPassword(@RequestHeader String auth);
}
