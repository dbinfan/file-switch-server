package site.dbin.fileswitch.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.dbin.fileswitch.exception.BadRequestException;
import site.dbin.fileswitch.service.FileUploadService;
import site.dbin.fileswitch.vo.Result;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
@AllArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileUploadService fileUploadService;
    @GetMapping("getFileList")
    public Result getFileList(){
        return Result.ok()
                .add("fileList",fileUploadService.getFileList());
    }
    @GetMapping("download")
    public void downloadFile(HttpServletResponse response, HttpServletRequest request, @RequestParam String filename){
        File file = fileUploadService.download(filename);
        InputStream inputStream = null;
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            inputStream = Files.newInputStream(file.toPath());
            int fSize = Integer.parseInt(String.valueOf(file.length()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(fSize));
            response.setHeader("content-type", "application/octet-stream;charset=utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            long pos = 0;
            if (null != request.getHeader("Range")) {
                // 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                try {
                    pos = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
                } catch (NumberFormatException e) {
                    throw new BadRequestException("下载出错");
                }
            }
            response.setHeader("Content-Range", "bytes " + pos + "-" +
                    (fSize - 1) + "/" + fSize);
            inputStream.skip(pos);
            byte[] buffer = new byte[1024 * 10];
            int length = 0;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, length);
            }
            inputStream.close();
        } catch (Exception e) {

        } finally {
            try {
                if (null != out)
                    out.flush();
                if (null != out)
                    out.close();
                if (null != inputStream)
                    inputStream.close();
            } catch (IOException e) {
            }
        }
    }
    @GetMapping("/delFile")
    public Result deleteFile(@RequestParam String filename){
       fileUploadService.delete(filename);
       return Result.ok();
    }
}
