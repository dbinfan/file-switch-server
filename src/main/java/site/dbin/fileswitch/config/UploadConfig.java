package site.dbin.fileswitch.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "site.dbin.fileswitch")
@Getter
@Setter
@Slf4j
public class UploadConfig {
    private String staticPath;
    private String path;
    private List<String> typeList;
    private Long trunk=1024*1024*5L;
    private Integer bufferSize=1024*1024;
    private Integer maxConnection=3;
    private Long maxFileSize=1024*1024*100L;
    // 存储策略
    private String strategy="yyyy-MM-dd";

    public void setTrunk(Long trunk) {
        this.trunk = trunk*1024*1024;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize*1024;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize*1024*1024;
    }

    public void setPath(String path) {
        if(!path.endsWith("/"))
            path = path + "/";
        File file = new File(path);
        boolean is = false;
        if(file.exists()&&file.isDirectory())
            is = true;
        else
            is = file.mkdirs();
        if(!is){
            log.error("创建文件夹"+path+"失败");
        }
        this.path = path;
    }
}
