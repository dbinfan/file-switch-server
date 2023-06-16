package site.dbin.fileswitch.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    // 上传文件
    @PostMapping
    public Result upload(FileDto fileDto) throws IOException {
        fileUploadService.saveFile(
                fileDto.getFile().getInputStream(),
                fileDto.getFilename(),
                fileDto.getIndex()
        );
        boolean is = fileUploadService.check(fileDto.getFilename());
        return Result.ok()
                .add("over",is);
    }

    @GetMapping("/getTrunk")
    public Result getTrunk(){
        return Result.ok()
                .add("trunk",fileUploadService.getTrunk());
    }

    @GetMapping("/getTask")
    // 获取上传任务
    public Result getTask(@RequestParam Long size, @RequestParam String filename){
        FileInfo fileInfo = fileUploadService.getTask(filename,size);
        if(fileInfo==null){
            return Result.error("文件名重复了或者文件太大了");
        }
        else{
            return Result.ok()
                    .add("fileInfo",fileInfo);
        }
    }

    //
    @GetMapping("/getFileInfo")
    public Result getFileInfo(@RequestParam String filename){
        FileInfo fileInfo =fileUploadService.getTask(filename);
        if(fileInfo.getFilename()==null){
            return Result.ok()
                    .add("status",0);
        }else{
            return Result.ok()
                    .add("status",1)
                    .add("fileInfo",fileInfo);
        }
    }

}
