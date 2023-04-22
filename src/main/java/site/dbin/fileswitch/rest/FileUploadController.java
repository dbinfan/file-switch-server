package site.dbin.fileswitch.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dbin.fileswitch.config.UploadConfig;
import site.dbin.fileswitch.service.FileUploadService;
import site.dbin.fileswitch.vo.FileDto;
import site.dbin.fileswitch.vo.FileInfo;
import site.dbin.fileswitch.vo.Result;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/")
    public Result upload(FileDto fileDto) throws IOException {
        fileUploadService.saveFile(
                fileDto.getFile().getInputStream(),
                fileDto.getFilename(),
                fileDto.getIndex()
        );
        fileUploadService.check(fileDto.getFilename());
        return null;
    }

    @GetMapping("/getTrunk")
    public Result getTrunk(){
        return Result.ok()
                .add("trunk",fileUploadService.getTrunk());
    }

    @GetMapping("/getTask")
    public Result getTask(Long size,String filename){
        FileInfo fileInfo = fileUploadService.getTask(filename,size);
        if(fileInfo==null){
            return Result.error("文件名重复了");
        }
        else{
            return Result.ok()
                    .add("fileInfo",fileInfo);
        }

    }
}
