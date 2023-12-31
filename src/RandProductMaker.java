import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RandProductMaker extends JFrame
{
    //Declarations

    //JPanel
    JPanel mainPnl;
    JPanel buttonPnl;
    JPanel countPnl;
    JPanel searchPnl;

    //JButtons
    JButton quitBtn;
    JButton addBtn;
    JButton createBtn;

    //JLabels
    JLabel nameLabel;
    JLabel desLabel;
    JLabel idLabel;
    JLabel costLabel;
    JLabel countLabel;

    //JTextFields
    JTextField nameInput;
    JTextField desInput;
    JTextField idInput;
    JTextField costInput;
    JTextField countOutput;

    //Forms Product
    String ID = "";
    String name ="";
    String description = "";
    String strCost;
    double cost = 0;
    int objCount = 0;
    Product item;
    String finalName ="";
    String finalDes = "";
    String finalID ="";
    double price =0;
    ArrayList<Product> products = new ArrayList<>();


    //others
    long pos;
    DataOutputStream output;



    public RandProductMaker()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createSearchPnl();
        mainPnl.add(searchPnl, BorderLayout.BEFORE_FIRST_LINE);


        createButtonPnl();
        mainPnl.add(buttonPnl, BorderLayout.AFTER_LAST_LINE);

        add(mainPnl);
        setTitle("Product Maker");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createSearchPnl()
    {
        searchPnl = new JPanel();
        searchPnl.setLayout(new GridLayout(5, 2));

        nameLabel = new JLabel("Name: ");
        nameInput = new JTextField();

        desLabel = new JLabel("Description: ");
        desInput = new JTextField();

        idLabel = new JLabel("ID: ");
        idInput = new JTextField();

        costLabel = new JLabel("Cost: ");
        costInput = new JTextField();

        countLabel = new JLabel("Object Count: ");
        countOutput = new JTextField();
        countOutput.setEditable(false);

        searchPnl.add(nameLabel);
        searchPnl.add(nameInput);

        searchPnl.add(desLabel);
        searchPnl.add(desInput);

        searchPnl.add(idLabel);
        searchPnl.add(idInput);

        searchPnl.add(costLabel);
        searchPnl.add(costInput);

        searchPnl.add(countLabel);
        searchPnl.add(countOutput);

    }


    public void createButtonPnl()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1,2));

        addBtn = new JButton("Add Object");
        createBtn = new JButton("Create File");
        quitBtn = new JButton("Quit");

        addBtn.addActionListener((ActionEvent ae) -> writeBinaryFile());
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));


        buttonPnl.add(addBtn);
        buttonPnl.add(quitBtn);
    }

    public void writeBinaryFile()
    {
        File workingDirectory= new File(System.getProperty("user.dir")); //selects directory of intellij
        Path outFile = Paths.get(workingDirectory.getPath() + "\\src\\data.bin"); //creates path

        getProduct();

            try
            {
                RandomAccessFile file = new RandomAccessFile("data.bin", "rw");

                //reads file
                file.seek(objCount * 124);

                byte[] nameBytes = new byte[35];
                file.read(nameBytes);
                finalName = new String(nameBytes, StandardCharsets.UTF_8).trim();

                byte[] desByte = new byte[75];
                file.read(desByte);
                finalDes = new String(desByte, StandardCharsets.UTF_8).trim();

                byte[] IDByte = new byte[6];
                file.read(IDByte);
                finalID = new String(IDByte, StandardCharsets.UTF_8).trim();

                price = file.readDouble();

                Product item = new Product(finalID,finalName,finalDes,price); // saves data read from a file to a Product Object
                products.add(item);

                //writes to file
                file.seek(objCount * 124);
                file.write(String.format("%-35s", name).getBytes(StandardCharsets.UTF_8));
                file.write(String.format("%-75s", description).getBytes(StandardCharsets.UTF_8));
                file.write(String.format("%-6s", ID).getBytes(StandardCharsets.UTF_8));

                file.writeDouble(cost);

                System.out.println(finalName + " " + finalDes + " " + finalID + " " + price);


            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }


        idInput.setText("");
        nameInput.setText("");
        costInput.setText("");
        desInput.setText("");

        for (Product i : products)
        {
            System.out.println(i);
        }



        objCount = objCount + 1;

        String objStr = String.valueOf(objCount);
        countOutput.setText(objStr);
    }


    private void getProduct()
    {
        //gets user input
        name = nameInput.getText();
        description = desInput.getText();
        ID = idInput.getText();

        //converts cost to double
        strCost = costInput.getText();
        cost = Double.parseDouble(strCost);



        //creates input to product
        item = new Product(ID, name, description, cost);

        //sets name of the product
        item.setName(name);




    }




}
