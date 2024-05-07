package weblogic.management.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import javax.management.AttributeChangeNotification;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import weblogic.management.RemoteNotificationListener;

public class WatchListener implements RemoteNotificationListener {
   String logFile = null;
   Object stdOutputMedium = null;
   boolean logToStandardOut = true;
   private static String WATCH = "<listener> ";
   String watchName = null;
   FileOutputStream fos = null;

   public WatchListener(String logFile, Object stdOutputMedium, boolean logToStandardOut, String watchName, WLScriptContext ctx) throws ScriptException {
      try {
         this.logFile = logFile;
         ctx.stdOutputMedium = stdOutputMedium;
         ctx.logToStandardOut = logToStandardOut;
         this.watchName = watchName;
         if (logFile != null) {
            this.fos = new FileOutputStream(new File(logFile));
            ctx.stdOutputMedium = this.fos;
            ctx.logToStandardOut = false;
         }
      } catch (FileNotFoundException var7) {
         this.println("File could not be located for listener " + logFile);
         var7.printStackTrace();
      }

   }

   public void handleNotification(Notification notification, Object handback) {
      if (notification instanceof AttributeChangeNotification) {
         AttributeChangeNotification acn = (AttributeChangeNotification)notification;
         this.println(this.getChangeInfo(acn));
      } else if (notification instanceof MBeanServerNotification) {
         this.println(this.getUnregInfo(notification));
      }

   }

   String getChangeInfo(AttributeChangeNotification acn) {
      String s = "\n##################################################################\n";
      s = s + "Listener Name       : " + this.watchName + "\n";
      s = s + "MBean Changed    : " + acn.getSource() + "\n";
      s = s + "Attribute Changed: " + acn.getAttributeName() + "\n";
      s = s + "Attribute value changed from " + this.format(acn.getOldValue()) + " to " + this.format(acn.getNewValue()) + "\n";
      s = s + "###################################################################\n";
      return s;
   }

   String getUnregInfo(Notification notif) {
      MBeanServerNotification msn = (MBeanServerNotification)notif;
      String s = "\n##################################################################\n";
      s = s + "Listener Name       : " + this.watchName + "\n";
      s = s + "MBean Changed    : " + msn.getSource() + "\n";
      s = s + "MBean Name    : " + msn.getMBeanName() + "\n";
      s = s + "This MBean has been unregistered\n";
      s = s + "\n###################################################################\n";
      return s;
   }

   private void println(String s) {
      try {
         if (this.stdOutputMedium == null) {
            System.out.println(s);
            return;
         }

         if (this.logToStandardOut) {
            System.out.println(s);
         }

         if (this.stdOutputMedium instanceof OutputStream) {
            ((OutputStream)this.stdOutputMedium).write(s.getBytes());
            ((OutputStream)this.stdOutputMedium).write("\n".getBytes());
            ((OutputStream)this.stdOutputMedium).flush();
         } else if (this.stdOutputMedium instanceof Writer) {
            if (this.stdOutputMedium instanceof PrintWriter) {
               ((PrintWriter)this.stdOutputMedium).println(s);
               ((PrintWriter)this.stdOutputMedium).flush();
            } else {
               ((Writer)this.stdOutputMedium).write(s);
               ((Writer)this.stdOutputMedium).write("\n");
               ((Writer)this.stdOutputMedium).flush();
            }
         }
      } catch (IOException var12) {
         var12.printStackTrace();
      } finally {
         try {
            if (this.stdOutputMedium != null) {
               if (this.stdOutputMedium instanceof OutputStream) {
                  ((OutputStream)this.stdOutputMedium).flush();
               } else if (this.stdOutputMedium instanceof Writer) {
                  if (this.stdOutputMedium instanceof PrintWriter) {
                     ((PrintWriter)this.stdOutputMedium).flush();
                  } else {
                     ((Writer)this.stdOutputMedium).flush();
                  }
               }
            }
         } catch (IOException var11) {
            var11.printStackTrace();
         }

      }

   }

   String format(Object o) {
      if (o == null) {
         return "(null)";
      } else {
         String result = new String(o.getClass().getName());
         result = result + "{ ";
         if (o.getClass().isArray()) {
            Object[] members = (Object[])((Object[])o);
            if (members.length < 1) {
               result = result + "(empty)";
            } else {
               Object[] arr$ = members;
               int len$ = members.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Object member = arr$[i$];
                  result = result + this.format(member) + ", ";
               }

               result = result.substring(0, result.length() - 2);
            }
         } else {
            result = result + o.toString();
         }

         result = result + " }";
         return result;
      }
   }
}
