package com.example.sadovod;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//
@FeignClient(name = "vk", url = "https://api.vk.com/")
public interface VkClient {
    @RequestMapping(
            value = "method/wall.get?domain={partline}&count=100&offset={offset}" +
                    "&access_token={token}&v=5.131"
    )
    String detPage(
            @PathVariable("partline") String partline,//часть ссылки вк
            @PathVariable("offset") int offset,//c какого момента
            @PathVariable("token") String token
    );
}
