package site.dbin.fileswitch.vo;

import lombok.Data;

import java.util.Set;
@Data
public class FileInfo {
    String filename;
    Long allPage;
    Set<Long> pageSet;
}
