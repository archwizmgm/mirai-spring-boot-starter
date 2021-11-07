package com.kuroko.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mirai")
@Data
public class MiraiProperties {

    private String protocol;
    private Long account;
    private String password;
    private boolean disableContactCache;

}
