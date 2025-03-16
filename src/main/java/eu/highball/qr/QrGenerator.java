package eu.highball.qr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrGenerator {


    public String svg(String payload,int size) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, size, size, hints);
        StringBuilder sbPath = new StringBuilder();
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BitArray row = new BitArray(width);
        for(int y = 0; y < height; ++y) {
            row = bitMatrix.getRow(y, row);
            for(int x = 0; x < width; ++x) {
                if (row.get(x)) {
                    sbPath.append(" M").append(x).append(",").append(y).append("h1v1h-1z");
                }
            }
        }
        return "<div style=\"width:"+ size +"px;height:"+size+"px\"><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" viewBox=\"0 0 " + width + " " + height + "\">" +
                "<path class=\"black\"  d=\"" + sbPath + "\"/></svg></div>";
    }


}
