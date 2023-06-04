package com.excelconverter.excelconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
    @Autowired
    CacheService cacheService;
    @GetMapping("/getCache")
    public String getCache(){
//        cacheService.getCache();
        return cacheService.getCache();

    }
}
