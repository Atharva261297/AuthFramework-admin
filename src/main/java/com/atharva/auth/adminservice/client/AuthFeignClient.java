package com.atharva.auth.adminservice.client;

import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "AuthClient", url = "${url.auth}")
public interface AuthFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/register")
    ErrorCodes register(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.GET, value = "/auth/admin/login")
    ErrorCodes login(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.GET, value = "/project/register")
    ErrorCodes registerProject(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.GET, value = "/auth/admin/reset")
    ErrorCodes resetPassword(@RequestHeader String auth);
}
