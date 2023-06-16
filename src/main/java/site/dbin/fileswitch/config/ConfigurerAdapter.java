package site.dbin.fileswitch.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableWebMvc
public class ConfigurerAdapter implements WebMvcConfigurer {
    private final UploadConfig uploadConfig;
    @Autowired
    public ConfigurerAdapter(UploadConfig uploadConfig){
        this.uploadConfig = uploadConfig;
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 创建与配置 Fastjson 对象
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置序列化方式 官方文档:https://alibaba.github.io/fastjson2/features_cn.html
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.PrettyFormat,
                JSONWriter.Feature.WriteNullListAsEmpty,//将List类型字段的空值序列化输出为空数组”[]”
                JSONWriter.Feature.WriteNullStringAsEmpty);//将String类型字段的空值序列化输出为空字符串””
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 解决中文乱码
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setDefaultCharset(StandardCharsets.UTF_8);
        // 将convert 添加到 converters
        converters.add(fastConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 前端路径
        //registry.addResourceHandler("/static/**").addResourceLocations("file:"+uploadConfig.getStaticPath()+"dist/").setCachePeriod(360000);
        // 将/** 访问映射到 file:/home/riverchiefs/fileswitch/upload/
        registry.addResourceHandler("/**").addResourceLocations("file:"+uploadConfig.getStaticPath()+"dist/").setCachePeriod(360000);
    }

    /**
     * 解决跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
