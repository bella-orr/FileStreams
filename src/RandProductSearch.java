import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.awt.Color.black;

public class RandProductSearch extends JFrame
{
    //Declarations

    //Jpanel
    JPanel mainPnl;
    JPanel buttonPnl;
    JPanel searchPnl;
    JPanel returnPnl;

    //JButtons
    JButton quitBtn;
    JButton searchBtn;
    JButton fileBtn;

    //scroller
    JScrollPane returnScroller;

    ArrayList products = new ArrayList<>();
    long pos = 0;

    //JTextArea
    JTextArea returnArea;

    //JTextField
    JTextField searchArea;

    //JLabel
    JLabel searchLabel;

    double objCount = 0;

    public RandProductSearch()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createSearchPnl();
        mainPnl.add(searchPnl, BorderLayout.NORTH);

        createReturnPnl();
        mainPnl.add(returnPnl, BorderLayout.CENTER);


        creatButtonPnl();
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);

        add(mainPnl);
        setTitle("Data Search");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }


    private void createSearchPnl()
    {
        searchPnl = new JPanel();
        searchPnl.setLayout(new GridLayout(1,3));

        searchLabel = new JLabel("Search Here: ");
        searchArea= new JTextField();
        searchBtn = new JButton("Search");

        searchBtn.addActionListener((ActionEvent ae) -> searchFile());

        searchPnl.add(searchLabel);
        searchPnl.add(searchArea);
        searchPnl.add(searchBtn);

    }


    private void createReturnPnl()
    {
        returnPnl = new JPanel();

        returnPnl.setBorder(new TitledBorder((new LineBorder(black, 4 )), "Search Results:"));

        returnArea = new JTextArea(20,30);
        returnArea.setEditable(false);

        returnScroller = new JScrollPane(returnArea);
        returnPnl.add(returnScroller);
    }

    private void creatButtonPnl()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 2));

        fileBtn = new JButton("Choose File");
        quitBtn = new JButton("Quit");

        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));
        fileBtn.addActionListener((ActionEvent ae) -> readfile());

        buttonPnl.add(fileBtn);
        buttonPnl.add(quitBtn);


    }

    private static void readfile()
    {
        File workingDirectory = new File(System.getProperty("user.dir"));
        Path path = Paths.get(workingDirectory.getPath() + "\\src\\data.bin");

        long objCount = 0;

        try
        {
            RandomAccessFile file = new RandomAccessFile("data.bin", "r");

            file.seek(0);
            long current = 0;

            while (current < file.length())

            {
            byte[] nameBytes = new byte[35];
            file.read(nameBytes);
            String finalName = new String(nameBytes, StandardCharsets.UTF_8).trim();

            byte[] desByte = new byte[75];
            file.read(desByte);
            String finalDes = new String(desByte, StandardCharsets.UTF_8).trim();

            byte[] IDByte = new byte[6];
            file.read(IDByte);
            String finalID = new String(IDByte, StandardCharsets.UTF_8).trim();

            double price = file.readDouble();


            System.out.println(file.getFilePointer());

            Product item = new Product(finalID,finalName,finalDes,price);// saves data read from a file to a Product Object

            System.out.println(item);

            }

        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);

        }

        catch (IOException e)
        {
            throw new RuntimeException(e);
        }


        objCount = objCount + 1;

    }

    private void searchFile()
    {


        try
        {
            RandomAccessFile file = new RandomAccessFile("data.bin", "r");

            file.seek(0);

            long current = 0;

            while(current < file.length())
            {
                byte[] nameBytes = new byte[35];
                file.read(nameBytes);
                String finalName = new String(nameBytes, StandardCharsets.UTF_8).trim();

                byte[] desByte = new byte[75];
                file.read(desByte);
                String finalDes = new String(desByte, StandardCharsets.UTF_8).trim();

                byte[] IDByte = new byte[6];
                file.read(IDByte);
                String finalID = new String(IDByte, StandardCharsets.UTF_8).trim();

                double price = file.readDouble();

                Product newProduct = new Product(finalID,finalName,finalDes,price);

                newProduct.setName(finalName);

                products.add(newProduct);

                if(newProduct.getName().equals(searchArea.getText()))
                {
                    String priStr = String.valueOf(price);
                    returnArea.append(finalName + ", " + finalDes + ", " + finalID + ", " + priStr + "\n");
                }


            }

        }

        catch(IOException e)
        {
            e.printStackTrace();
        }




    }



}
