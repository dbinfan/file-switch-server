package site.dbin.fileswitch.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import site.dbin.fileswitch.config.UploadConfig;
import site.dbin.fileswitch.service.FileUploadService;
import site.dbin.fileswitch.vo.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.*;

@Service
@AllArgsConstructor
public class FileUploadServiceImpl  implements FileUploadService {
    private final UploadConfig uploadConfig;
    public static final Map<String, FileInfo> map = Collections.synchronizedMap(new HashMap<>());
    public int saveFile(InputStream is, String fileName, long index) {
        FileInfo fileInfo = map.get(fileName);
        if(fileInfo==null)
            return 3;
        if(index>=fileInfo.getAllPage()){
            return 2;
        }
        int bufferSize = uploadConfig.getBufferSize();
        long trunk = uploadConfig.getTrunk();
        String filePath = uploadConfig.getPath()+fileName;
        RandomAccessFile os;
        try {
            os = new RandomAccessFile(filePath, "rw");
        } catch (IOException e) {
            close(is);
            return 1;
        }
        try {
            byte[] buffer = new byte[bufferSize];
            long lenCount = 0;
            int len;
            os.seek(index * trunk);
            while (lenCount < trunk && (len = is.read(buffer)) != -1) {
                lenCount += len;
                os.write(buffer, 0, len);
            }
            os.close();
            is.close();
            fileInfo.getPageSet().add(index);
            return 0;
        } catch (IOException e) {
            close(is);
            return 1;
        }
    }

    @Override
    public void check(String fileName) {
        FileInfo fileInfo = map.get(fileName);
        if(fileInfo!=null){
            if(fileInfo.getPageSet().size()==fileInfo.getAllPage()){
                map.remove(fileName);
            }
        }
    }



    @Override
    public Long getTrunk() {
        return uploadConfig.getTrunk();
    }

    @Override
    public FileInfo getTask(String filename, Long allPage) {
        if(map.containsKey(filename))
            return map.get(filename);
        if(new File(uploadConfig.getPath()+filename).exists())
            return null;
        else{
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(filename);
            fileInfo.setAllPage(allPage);
            fileInfo.setPageSet(Collections.synchronizedSet(new HashSet<>()));
            map.put(filename,fileInfo);
            return fileInfo;
        }
    }

    private void close(InputStream inputStream){
        try {
            if(inputStream!=null)
                inputStream.close();
        } catch (IOException ignored) {
        }
    }
}
