package com.kuroko.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mirai")
@Data
public class MiraiProperties {

    private String protocol = "ANDROID_PHONE";
    private Long account;
    private String password;
    private String device;
    private boolean disableContactCache;

}
