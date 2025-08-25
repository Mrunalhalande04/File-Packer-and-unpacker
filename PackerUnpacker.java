import javax.swing.*; 
import java.awt.event.*;
import java.io.*;

public class PackerUnpacker {
    JFrame frame;
    JTextField dirField, packField, unpackField;
    JButton packBtn, unpackBtn;
    JTextArea outputArea;

    public PackerUnpacker() {
        frame = new JFrame("Marvellous Packer GUI");
        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Packing and Unpacking Module");
        title.setBounds(160, 10, 250, 20);
        frame.add(title);

        JLabel dirLabel = new JLabel("Directory to Pack:");
        dirLabel.setBounds(30, 50, 120, 20);
        frame.add(dirLabel);

        dirField = new JTextField();
        dirField.setBounds(160, 50, 200, 25);
        frame.add(dirField);

        JLabel packLabel = new JLabel("Packed File Name:");
        packLabel.setBounds(30, 90, 120, 20);
        frame.add(packLabel);

        packField = new JTextField();
        packField.setBounds(160, 90, 200, 25);
        frame.add(packField);

        packBtn = new JButton("Pack Files");
        packBtn.setBounds(380, 70, 120, 30);
        frame.add(packBtn);

        JLabel unpackLabel = new JLabel("Packed File to Unpack:");
        unpackLabel.setBounds(30, 130, 150, 20);
        frame.add(unpackLabel);

        unpackField = new JTextField();
        unpackField.setBounds(160, 130, 200, 25);
        frame.add(unpackField);

        unpackBtn = new JButton("Unpack Files");
        unpackBtn.setBounds(380, 130, 120, 30);
        frame.add(unpackBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);//we cant edit manually 
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(30, 180, 470, 260);
        frame.add(scroll);

        packBtn.addActionListener(e -> packFiles(dirField.getText(), packField.getText()));
        unpackBtn.addActionListener(e -> unpackFiles(unpackField.getText()));

        frame.setVisible(true);
    }

    public void packFiles(String DirName, String PackName) {
        String Header = "";
        int i = 0, j = 0, iRet = 0, iCount = 0;
        FileOutputStream fopackobj = null;
        FileInputStream fiobj = null;
        File Packobj = null;
        File fobj = null;

        try {
            byte Buffer[] = new byte[1024];
            outputArea.setText("");

            Packobj = new File(PackName);
            boolean bret = Packobj.createNewFile();
            if (!bret) {
                outputArea.append("Pack file could not be created.\n");
                return;
            }
            outputArea.append("Packed file created: " + PackName + "\n");

            fopackobj = new FileOutputStream(Packobj);
            fobj = new File(DirName);

            if (fobj.exists()) {
                File Arr[] = fobj.listFiles();

                for (i = 0; i < Arr.length; i++) {
                    if ((Arr[i].getName()).endsWith(".txt")) {
                        Header = Arr[i].getName() + " " + Arr[i].length();
                        for (j = Header.length(); j < 100; j++) {
                            Header = Header + " ";
                        }
                        fopackobj.write(Header.getBytes(), 0, 100);
                        fiobj = new FileInputStream(Arr[i]);

                        while ((iRet = fiobj.read(Buffer)) != -1) {
                            fopackobj.write(Buffer, 0, iRet);
                        }

                        fiobj.close();
                        outputArea.append("Packed file: " + Arr[i].getName() + "\n");
                        Header = "";
                        iCount++;
                    }
                }

                fopackobj.close();
                outputArea.append("Total files packed: " + iCount + "\n");
            } else {
                outputArea.append("Directory does not exist.\n");
            }

        } catch (Exception eobj) {
            outputArea.append("Exception: " + eobj + "\n");
        }
    }

    public void unpackFiles(String PackName) {
        int FileSize = 0, iRet = 0, iCount = 0;
        String SHeader = null;
        File fobj = null, fobjX = null;
        FileOutputStream foobj = null;
        FileInputStream fiobj = null;

        try {
            outputArea.setText("");
            fobj = new File(PackName);

            if (fobj.exists()) {
                fiobj = new FileInputStream(fobj);
                byte Header[] = new byte[100];

                while ((iRet = fiobj.read(Header)) != -1) {
                    SHeader = new String(Header);
                    SHeader = SHeader.trim();
                    String Arr[] = SHeader.split(" ");

                    fobjX = new File(Arr[0]);
                    fobjX.createNewFile();

                    outputArea.append("File extracted: " + Arr[0] + "\n");
                    foobj = new FileOutputStream(fobjX);

                    FileSize = Integer.parseInt(Arr[1]);
                    byte Buffer[] = new byte[FileSize];

                    fiobj.read(Buffer);
                    foobj.write(Buffer, 0, FileSize);

                    foobj.close();
                    iCount++;
                }
                fiobj.close();
                outputArea.append("Total files unpacked: " + iCount + "\n");
            } else {
                outputArea.append("Packed file not found.\n");
            }

        } catch (Exception eobj) {
            outputArea.append("Exception: " + eobj + "\n");
        }
    }

    public static void main(String[] args) {
        new PackerUnpacker();
    }
}

