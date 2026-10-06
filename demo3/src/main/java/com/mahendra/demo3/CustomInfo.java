package com.mahendra.demo3;

import org.springframework.boot.actuate.info.EnvironmentInfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomInfo implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder){
        builder.withDetails(Map.of(
           "name", "Demo-2",
           "version","1.0",
                "environment","Dev"
        ));
    }
}
