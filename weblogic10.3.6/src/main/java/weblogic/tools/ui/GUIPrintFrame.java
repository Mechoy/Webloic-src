package weblogic.tools.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

class GUIPrintFrame extends JFrame implements Runnable, ActionListener {
   JButton goAway;

   public GUIPrintFrame(String var1) {
      super(var1);
   }

   public void run() {
      this.getContentPane().setLayout(new BorderLayout());
      JTextArea var1 = new JTextArea("", 40, 12);
      var1.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new BevelBorder(1, Color.lightGray, Color.darkGray)));
      GUIPrintStream var2 = new GUIPrintStream(var1);
      PrintStream var3 = new PrintStream(var2);
      this.setSize(400, 700);
      this.getContentPane().add("North", var1);
      JPanel var4 = new JPanel();
      this.goAway = new JButton("GO AWAY");
      this.goAway.addActionListener(this);
      var4.add(this.goAway);
      this.getContentPane().add("South", var4);
      System.setOut(var3);
      System.setErr(var3);
      this.setVisible(true);
      var2.println("HelloWorld!");
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.goAway) {
         System.exit(0);
      }

   }
}
