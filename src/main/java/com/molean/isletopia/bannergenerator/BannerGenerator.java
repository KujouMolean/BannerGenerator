package com.molean.isletopia.bannergenerator;

import com.google.gson.Gson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class BannerGenerator {

    private static final Gson GSON = new Gson();

    public static ServerInfo getServerInfo(String server) {
        try {
            URL url = new URL("http://mcsm.molean.com/api/status/" + server);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            String s = new String(bytes, StandardCharsets.UTF_8);
            return GSON.fromJson(s, ServerInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage generate(String version, String players) {

        BufferedImage image;
        try {
            Resource resource = new ClassPathResource("banner.png");
            InputStream resourceAsStream = resource.getInputStream();
            image = ImageIO.read(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setFont(new Font("微软雅黑", Font.PLAIN, 28));
        g.drawString(players + "(" + version + ")", 635, 200);
        g.dispose();
        return image;
    }

    public static BufferedImage generate() {
        Random random = new Random();
        int i = random.nextInt(8) + 1;
        ServerInfo backendServer = getServerInfo("server" + i);
        ServerInfo waterfall = getServerInfo("waterfall");
        String version = "unknown";
        String currentPlayers = "unknown";
        String maxPlayers = "unknown";
        try {
            assert backendServer != null;
            assert waterfall != null;
            version = backendServer.getVersion();
            currentPlayers = waterfall.getCurrent_players() + "";
            maxPlayers = waterfall.getMax_players() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generate(version, currentPlayers + "/" + maxPlayers);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage generate = generate();
        ImageIO.write(generate, "png", new File("test.png"));
    }
}
