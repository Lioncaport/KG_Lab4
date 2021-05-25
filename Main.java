package workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JDialog
{
    public Main()
    {
        place myPlace = new place();
        add(myPlace);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });
    }

    public static void main(String[] args)
    {
        Main dialog = new Main();
        dialog.setTitle("8307 -- Tkachev Igor, Guseinov Akshin -- Lab 4, Var 6");
        dialog.setSize(1000,700);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
