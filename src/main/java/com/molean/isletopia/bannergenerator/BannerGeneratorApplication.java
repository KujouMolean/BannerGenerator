package com.molean.isletopia.bannergenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
@RestController
@EnableScheduling
public class BannerGeneratorApplication {

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(65536));
        SpringApplication.run(BannerGeneratorApplication.class, args);

    }

    private static byte[] bytes;

    @Scheduled(fixedRate = 60000)
    public void update() throws IOException {
        BufferedImage generate = BannerGenerator.generate();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(generate, "png", byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
    }

    @GetMapping(value = "/", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] root() throws IOException {
        return bytes;
    }

}
