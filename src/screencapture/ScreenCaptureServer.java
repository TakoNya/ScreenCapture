package screencapture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

public class ScreenCaptureServer extends Thread{
    private ServerSocket serverSocket;
    Socket server;
    JFrame frame;
    BufferedImage image;
    
    public ScreenCaptureServer(int port) throws IOException, SQLException, ClassNotFoundException, Exception{
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(180000);
    }
    public void run(){
        try{
            while (true){
                try{
                    server = serverSocket.accept();
                    image = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                    frame = new JFrame();
                    frame.add(new JScrollPane(new JLabel(new ImageIcon(image))), BorderLayout.CENTER);
                    frame.setJMenuBar(createMenuBar());
                    frame.setSize(800,600);
                    frame.setLocationRelativeTo(null);
                    frame.setTitle("Pantalla capturada");
                    frame.setVisible(true);
                }catch(SocketTimeoutException st){
                    System.out.println("Tiempo cumplido!");
                    break;
                }catch(Exception ex){
                    System.out.println(ex);
                    break;
                }
            }
            server.close();
            serverSocket.close();
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        JMenuItem menuItem = new JMenuItem("Guardar como");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveFile();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        return menuBar;
    }
    private void saveFile(){
        JFileChooser fileChooser = new JFileChooser();
        if(fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try{
                ImageIO.write(image, "jpg", file);
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }
            

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, Exception{
        Thread thread = new ScreenCaptureServer(6066);
        thread.start();
        // TODO code application logic here
    }
    
}
