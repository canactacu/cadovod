package com.example.sadovod;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// https://tk-sad.ru/odezhda?page=5
@FeignClient(name = "tk-sad", url = "https://tk-sad.ru/")
public interface SiteClient {

    @RequestMapping(
            value = "odezhda?page={pageId}",
            headers = {
//            "GET /odezhda?page=1 HTTP/1.1",
                    "Accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                    "Accept-Encoding=gzip, deflate, br",
                    "Accept-Language=ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
                    "Cache-Control=max-age=0",
                    "Connection=keep-alive",
//            "Cookie=ID=MsCjLvswXQDCxzElVVaOFUd335725422; _ym_uid=16628086101059579540; _ym_d=1662808610; _ym_isad=2; _ym_visorc=w; viewed=11819413%2C11818552%2C11800576",
//            "Host=tk-sad.ru",
                    "Sec-Fetch-Dest=document",
                    "Sec-Fetch-Mode=navigate",
                    "Sec-Fetch-Site=same-origin",
                    "Sec-Fetch-User=?1",
                    "Upgrade-Insecure-Requests=1",
                    "User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36",
                    "sec-ch-ua=\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\"",
                    "sec-ch-ua-mobile=?0",
                    "sec-ch-ua-platform=\"Windows\""
            }
    )
    String getPage(@PathVariable("pageId") int page);

    @RequestMapping(
            value = "{part}",
            headers = {
//            "GET /odezhda?page=1 HTTP/1.1",
                    "Accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                    "Accept-Encoding=gzip, deflate, br",
                    "Accept-Language=ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
                    "Cache-Control=max-age=0",
                    "Connection=keep-alive",
//            "Cookie=ID=MsCjLvswXQDCxzElVVaOFUd335725422; _ym_uid=16628086101059579540; _ym_d=1662808610; _ym_isad=2; _ym_visorc=w; viewed=11819413%2C11818552%2C11800576",
//            "Host=tk-sad.ru",
                    "Sec-Fetch-Dest=document",
                    "Sec-Fetch-Mode=navigate",
                    "Sec-Fetch-Site=same-origin",
                    "Sec-Fetch-User=?1",
                    "Upgrade-Insecure-Requests=1",
                    "User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36",
                    "sec-ch-ua=\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\"",
                    "sec-ch-ua-mobile=?0",
                    "sec-ch-ua-platform=\"Windows\""
            }

    )
    byte[] getImg(@PathVariable("part") String value);
}
