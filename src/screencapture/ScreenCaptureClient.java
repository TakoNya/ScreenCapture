package screencapture;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ScreenCaptureClient {

    Image newimg;
    static BufferedImage bimg;
    byte[] bytes;
    static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame();
        JButton button = new JButton("Capturar pantalla");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenCapture();
            }
        });
        frame.add(button, BorderLayout.CENTER);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setTitle("elija su pantalla");
        frame.setVisible(true);
    }

    private static void screenCapture() {
        String serverName = "192.168.12.58";
        int port = 6066;
        try{
            Socket client = new Socket(serverName, port);
            Robot bot;
            bot = new Robot();
            frame.setExtendedState(Frame.ICONIFIED);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;
            bimg = bot.createScreenCapture(new Rectangle(0,0,screenWidth, screenHeight));
            ImageIO.write(bimg, "JPG",client.getOutputStream());
            client.close();
        }catch(IOException | AWTException e){
            e.printStackTrace();
        }finally{
            frame.setExtendedState(Frame.NORMAL);
        }
    }

}
