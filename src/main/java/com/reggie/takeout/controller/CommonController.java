package com.reggie.takeout.controller;

import com.reggie.takeout.common.R;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    private static final String BASE_PATH = "D:\\Project\\Springboot\\reggie\\src\\main\\resources\\static/";

    /**
     * 上传文件
     * @param file 上传的文件
     * @return
     */
    @PostMapping("/upload")
    public R<Object> upload(MultipartFile file) {

        String originalFilename = file.getOriginalFilename(); // 获取源文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 获取文件后缀

        String fileName = UUID.randomUUID() + suffix; // 生成新文件名


        File dir = new File(BASE_PATH);
        if (!dir.exists()) { // 如果路径不存在
            dir.mkdirs(); // 创建路径
        }

        try {
            file.transferTo(new File(BASE_PATH + fileName)); // 将文件移动到指定路径
        } catch (IOException e) {
            return R.error(e.getMessage());
        }

        return R.success(fileName);
    }

    /**
     * 下载文件
     * @param response
     * @param name 文件名
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {

        try {
            FileInputStream fileInputStream = new FileInputStream(BASE_PATH + name); // 获取文件字节输入流

            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg"); // 设置响应类型为image/jpeg

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            outputStream.close(); // 关闭输出流
            fileInputStream.close(); // 关闭文件字节输入流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
