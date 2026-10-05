package com.mahendra.bootdemo1;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class DataSource {

    @Value("jdbc:mysql://${db.host}:${db.port}/${db.name}?useSSL=false")
    private String url;

    // Using Spring EL to inject values from application.properties
    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;
    

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
