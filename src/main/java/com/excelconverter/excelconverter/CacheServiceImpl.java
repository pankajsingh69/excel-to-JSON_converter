package com.excelconverter.excelconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService{

//    @Autowired
//    CacheManager cacheManager;
//    @Override
    @Cacheable("cacheOne")
    public String getCache1() {
        System.out.println("cache1");
        return "cache1";

    }

//    @Override
//    @Cacheable("cacheOne")
    private String getCache2() {
        System.out.println("cache2");
        return "cache2";
    }

    public String getCache() {

        getCache1();
        getCache2();
        System.out.println("cache2");
        return "cache2";
    }
}
