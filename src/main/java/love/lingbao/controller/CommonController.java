package love.lingbao.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 图片上传到本地
     * @param file 图片文件
     * @return
     */
    @PostMapping("/uploadImage")
    public String uploadImage(HttpServletRequest request, MultipartFile file) {
        log.info("/lose/uploadImage post -> uploadImage: file = {}; 将图片保存在本地，并返回存储路径", file);
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("static/images")).getPath();//本地资源路径
        String uid = (String) request.getSession().getAttribute("user");
        path = path + "/" + uid + "/";
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + suffix;
        try {
            file.transferTo(new File(path, Objects.requireNonNull(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path + fileName;//保存成功后返回路径
    }
}
