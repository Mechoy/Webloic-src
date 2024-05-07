package weblogic.tools.ui;

import java.io.OutputStream;
import javax.swing.JTextArea;

public class GUIPrintStream extends OutputStream {
   static final String nl = "\n";
   JTextArea t;
   private static final int MAX_CHARS = 50000;
   private int maxChars;

   void newLine() {
      this.write("\n");
   }

   protected void write(String var1) {
      if (this.t.getText().length() + var1.length() >= this.maxChars) {
         this.t.setText("****<truncated buffer>***\n");
      }

      this.t.append(var1);
   }

   public static void main(String[] var0) throws Exception {
      (new GUIPrintFrame("Stdout/Stderr")).run();
   }

   public GUIPrintStream(JTextArea var1) {
      this.t = var1;
      this.maxChars = 50000;
   }

   public void setMaxChars(int var1) {
      this.maxChars = var1;
   }

   public void write(int var1) {
      this.write(String.valueOf((char)var1));
   }

   public void write(byte[] var1, int var2, int var3) {
      String var4 = new String(var1, 0, var2, var3);
      this.write(var4);
   }

   public void print(boolean var1) {
      this.write(var1 ? "true" : "false");
   }

   public void print(char var1) {
      this.write(String.valueOf(var1));
   }

   public void print(int var1) {
      this.write(String.valueOf(var1));
   }

   public void print(long var1) {
      this.write(String.valueOf(var1));
   }

   public void print(float var1) {
      this.write(String.valueOf(var1));
   }

   public void print(double var1) {
      this.write(String.valueOf(var1));
   }

   public void print(char[] var1) {
      this.print(new String(var1));
   }

   public void print(String var1) {
      if (var1 == null) {
         var1 = "null";
      }

      this.write(var1);
   }

   public void print(Object var1) {
      this.write(String.valueOf(var1));
   }

   public void println() {
      this.write("\n");
   }

   public void println(boolean var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(char var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(int var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(long var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(float var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(double var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(char[] var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(String var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }

   public void println(Object var1) {
      synchronized(this) {
         this.print(var1);
         this.newLine();
      }
   }
}
