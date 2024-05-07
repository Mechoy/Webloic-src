package weblogic.nodemanager.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class LogFormatter extends Formatter {
   private Date date;
   private MessageFormat mf;
   private Object[] args;
   private String lineSeparator;
   private boolean showSource;

   LogFormatter(String var1) {
      this.date = new Date();
      this.args = new Object[2];
      this.lineSeparator = var1;
   }

   LogFormatter() {
      this(System.getProperty("line.separator"));
   }

   public synchronized String format(LogRecord var1) {
      StringBuffer var2 = new StringBuffer();
      this.date.setTime(var1.getMillis());
      this.args[0] = this.date;
      this.args[1] = var1.getLevel().getName();
      if (this.mf == null) {
         this.mf = new MessageFormat("<{0,date} {0,time}> <{1}>");
      }

      this.mf.format(this.args, var2, (FieldPosition)null);
      if (this.showSource) {
         String var3 = var1.getSourceClassName();
         String var4 = var1.getSourceMethodName();
         if (var3 != null) {
            var2.append(" <");
            var2.append(var3);
            if (var4 != null) {
               var2.append("::");
               var2.append(var4);
            }

            var2.append(">");
         }
      }

      Object[] var5 = var1.getParameters();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            var2.append(" <");
            var2.append(var5[var6].toString());
            var2.append(">");
         }
      }

      var2.append(" <");
      var2.append(this.formatMessage(var1));
      var2.append(">");
      var2.append(this.lineSeparator);
      if (var1.getThrown() != null) {
         appendThrowable(var2, var1.getThrown());
         var2.append(this.lineSeparator);
      }

      return var2.toString();
   }

   public String format(Level var1, String var2, Throwable var3) {
      LogRecord var4 = new LogRecord(var1, var2);
      if (var3 != null) {
         var4.setThrown(var3);
      }

      return this.format(var4);
   }

   public String format(Level var1, String var2) {
      return this.format(var1, var2, (Throwable)null);
   }

   synchronized void setShowSource(boolean var1) {
      this.showSource = var1;
   }

   private static void appendThrowable(StringBuffer var0, Throwable var1) {
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2);
      var1.printStackTrace(var3);
      var3.close();
      var0.append(var2.toString());
   }
}
