package com.study.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.lang.System.in;

@Controller
public class Img {

    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public Object getImage(@RequestParam("filename") String filename) throws Exception {
        String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        Object entity = null;
        Path filePath = Paths.get(uploadPath + filename);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            InputStream in = Files.newInputStream(filePath, StandardOpenOption.READ);
            entity = in.readAllBytes();
            in.close();
            System.out.println("=====1");
        } else {
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
            System.out.println("=====2");
        }

        return entity;
    }
}
