package com.kuroko.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mirai")
public class MiraiProperties {

    private String protocol = "ANDROID_PAD";
    private Long account;
    private String password;
    private String device;
    private boolean disableContactCache = false;

}
