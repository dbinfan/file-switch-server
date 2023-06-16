package site.dbin.fileswitch.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import site.dbin.fileswitch.config.UploadConfig;
import site.dbin.fileswitch.exception.BadRequestException;
import site.dbin.fileswitch.service.FileUploadService;
import site.dbin.fileswitch.vo.DownloadFile;
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
    private void clear(){
        List<String> list = new LinkedList<>();
        Long time = System.currentTimeMillis();
        for(String s:map.keySet()){
            FileInfo fileInfo = map.get(s);
            if(time > fileInfo.getTime()+36000){
                list.add(s);
            }
        }
        for(String s: list){
            map.remove(s);
        }
    }
    public int saveFile(InputStream is, String fileName, long index) {
        if(map.size()>20){
            clear();
        }
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
    public boolean check(String fileName) {
        FileInfo fileInfo = map.get(fileName);
        if(fileInfo!=null){
            if(fileInfo.getPageSet().size()==fileInfo.getAllPage()){
                map.remove(fileName);
                return true;
            }
        }
        return false;
    }



    @Override
    public Long getTrunk() {
        return uploadConfig.getTrunk();
    }

    @Override
    public FileInfo getTask(String filename, Long size) {
        if(map.containsKey(filename))
            return map.get(filename);
        if(new File(uploadConfig.getPath()+filename).exists())
            return null;
        if(size>uploadConfig.getMaxFileSize()*1024*1024)
            throw new BadRequestException("文件过大");
        long allPage = size/uploadConfig.getTrunk();
        if(size%uploadConfig.getTrunk()!=0)
            allPage++;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(filename);
        fileInfo.setAllPage(allPage);
        fileInfo.setPageSet(Collections.synchronizedSet(new HashSet<>()));
        fileInfo.setTime(System.currentTimeMillis()/1000);
        map.put(filename,fileInfo);
        return fileInfo;

    }

    @Override
    public FileInfo getTask(String filename) {
        FileInfo fileInfo =  map.get(filename);
        if(fileInfo==null&&new File(uploadConfig.getPath()+filename).exists()){
            return new FileInfo();
        }
        return fileInfo;
    }

    @Override
    public List<DownloadFile> getFileList() {
        // 获取文件列表
        List<DownloadFile> list = new ArrayList<>();
        File file = new File(uploadConfig.getPath());
        File[] files = file.listFiles();
        if(files!=null){
            for(File f:files){
                DownloadFile downloadFile = new DownloadFile();
                downloadFile.setFilename(f.getName());
                downloadFile.setSize(f.length()/1024);
                list.add(downloadFile);
            }
        }
        return list;
    }

    @Override
    public File download(String filename) {
        return new File(uploadConfig.getPath()+filename);
    }

    @Override
    public void delete(String filename) {
        // 删除文件
        File file = new File(uploadConfig.getPath()+filename);
        if(file.exists()){
            file.delete();
        }
        else throw new BadRequestException("文件不存在");
    }

    private void close(InputStream inputStream){
        try {
            if(inputStream!=null)
                inputStream.close();
        } catch (IOException ignored) {
        }
    }
}
