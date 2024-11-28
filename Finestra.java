import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Finestra implements ActionListener
{
    private JFrame frame;
    private JTextField text_field_1;
    private JTextArea text_field_2;
    private JButton bottone_1;

    private JPanel panel_1;
    private JPanel panel_2;

    public Finestra()
    {
        frame = new JFrame("");

        panel_1 = new JPanel();
        panel_2 = new JPanel();

        text_field_1 = new JTextField("");
        text_field_2 = new JTextArea("");
        //text_field_2.setEnabled(false);

        bottone_1 = new JButton("Premi");
        bottone_1.addActionListener(this);
        frame.setLayout(new BorderLayout());

        frame.setMinimumSize(new Dimension(450, 250));
        frame.setLocation(700, 300);
        frame.addWindowListener(new GestioneFinestra());

        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
        panel_1.add(text_field_1);
        panel_1.add(bottone_1);

        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
        panel_2.add(text_field_2);

        frame.add(panel_1, BorderLayout.NORTH);
        frame.add(panel_2, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        String text = text_field_1.getText();
        Ip ip;

        if(isValid(text))
        {
            ip = new Ip(text);
            text_field_2.setText("\nIP Class: " + ip.getClasse() + "\nIP Type: " + ip.getType() + "\nIP Address: " + ip.getIpAddress() + "\nNetmask: " + ip.getMask() + "\nWildcard Mask: " + ip.getWildcardMask() + "\nNetwork Address: " + ip.getNetworkAddress() + "\nUsable Host Range: " + ip.getUsableHostRange() + "\nBroadcast Address: " + ip.getBroadcast() + "\nNumber of usable hosts: " + ip.getNumberHosts());
        }
        else
        {
            text_field_2.setText("Errore");
        }
    }

    private boolean isValid(String text)
    {
        if(text.equals("")) return false;
        int index = 0;
        int cnt = 0;
        String num = "";

        if(text.length() < 9) return false;

        for(int i = 0; i < text.length(); i++)
        {
            if(text.charAt(i) == '.' || text.charAt(i) == '/') cnt++;
        }
        if(cnt != 4) return false;
        
        for(int i = 0; i < 4; i++)
        {
            while(text.charAt(index) != '.' && text.charAt(index) != '/')
            {
                num = num + text.charAt(index);
                index++;
            }
            index++;
            try
            {
                if(Integer.parseInt(num) > 255 || Integer.parseInt(num) < 0) return false;
            }
            catch(NumberFormatException e)
            {
                return false;
            }
            num = "";
        }

        num = "";

        while(index < text.length())
        {
            num = num + text.charAt(index);
            index++;
        }
        
        try
        {
            if(Integer.parseInt(num) > 32 || Integer.parseInt(num) < 0) return false;
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }
}
