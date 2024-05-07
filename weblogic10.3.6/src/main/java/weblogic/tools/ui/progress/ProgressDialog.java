package weblogic.tools.ui.progress;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgressDialog extends JDialog implements ProgressListener {
   private JTextArea textArea = new JTextArea();
   private ProgressEvent event = new ProgressEvent();
   private ProgressProducer progressProducer;

   public ProgressDialog(Frame var1, String var2, boolean var3) {
      super(var1, var2, var3);
      Container var4 = this.getContentPane();
      this.textArea.setEditable(false);
      JPanel var5 = new JPanel();
      var5.add(new JScrollPane(this.textArea));
      var5.add(new JButton("OK"));
      var4.add(var5);
   }

   public void updateProgress(ProgressEvent var1) {
      this.textArea.append(var1.getMessage() + "\n");
   }

   public void update(String var1) {
      this.event.setEventInfo(var1, 1);
      this.updateProgress(this.event);
   }

   public void update(String var1, int var2) {
      this.event.setEventInfo(var1, var2);
      this.updateProgress(this.event);
   }

   public void setProgressProducer(ProgressProducer var1) {
      this.progressProducer = var1;
   }
}
