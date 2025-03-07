package eu.highball.qr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main {

	public static void main(String[] args) {
        try {
            if (args.length < 4) {
                throw new IllegalArgumentException("Usage: java -jar qr.jar [png|svg] <data> <width> <height>");
            }
            QrGenerator qrGenerator = new QrGenerator();
            if (args[0].equals("png")) {
                BufferedImage image = qrGenerator.png(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                ImageIO.write(image, "bmp", System.out);
            } else {
                String svg = qrGenerator.svg(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                System.out.println(svg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
}



