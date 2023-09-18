package site.dbin.fileswitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController
public class Run {
    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }
}
