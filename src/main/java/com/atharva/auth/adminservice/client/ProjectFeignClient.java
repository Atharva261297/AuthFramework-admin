package com.atharva.auth.adminservice.client;

import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ProjectClient", url = "${url.auth.project}")
public interface ProjectFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    ErrorCodes registerProject(@RequestHeader String auth);

    @RequestMapping(method = RequestMethod.GET, value = "/getCreds")
    String getCreds(@RequestParam String id);

}
