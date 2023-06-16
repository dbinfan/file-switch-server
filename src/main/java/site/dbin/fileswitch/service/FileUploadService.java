package site.dbin.fileswitch.service;

import site.dbin.fileswitch.vo.DownloadFile;
import site.dbin.fileswitch.vo.FileInfo;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface FileUploadService {
    /**
     * 分片保存文件
     * @param is 文件输入流
     * @param fileName 文件名
     * @param index 文件起始分片
     * @return 是否保存成功0 成功，1 失败,2 超出分片,3 没有任务
     */
    int saveFile(InputStream is, String fileName, long index);

    // 检查文件是否已经上传完毕
    boolean check(String filePath);

    Long getTrunk();

    FileInfo getTask(String filename, Long size);

    FileInfo getTask(String filename);

    List<DownloadFile> getFileList();

    File download(String filename);

    void delete(String filename);

}
