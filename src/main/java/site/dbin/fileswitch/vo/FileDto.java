package site.dbin.fileswitch.vo;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class FileDto {
    private String filename;
    @NotNull
    private Long index;
    @NotNull
    private MultipartFile file;
    private String sonPath;
    public void setSonPath(String sonPath){

        this.sonPath = sonPath;
    }

    /*public boolean getSonPath(String path){
        // 正则校验字符串是否是合法的文件路径
        if(path.matches("")){
            return true;
        }
        else{
            return false;
        }
    }*/

}
