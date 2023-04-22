package site.dbin.fileswitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class Run {
    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }
    @GetMapping
    public void Index(HttpServletResponse response){
        // respose 重定向
        response.setHeader("Location", "/front/index.html");

    }
}
