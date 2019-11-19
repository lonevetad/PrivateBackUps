package test1;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Telly {
    public static void main(String[] args) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        ImageIO.write(webcam.getImage(),"PNG",new File("foto.png"));
        webcam.close();
    }
}
