package com.kuroko.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mirai.proxy")
public class HttpClientProperties {
    String host;
    Integer port;
    Integer timeout = 20;
}
