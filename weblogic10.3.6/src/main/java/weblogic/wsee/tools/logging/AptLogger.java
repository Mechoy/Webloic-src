package weblogic.wsee.tools.logging;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.util.SourcePosition;
import java.io.File;

public class AptLogger implements Logger {
   private Messager messager = null;

   public AptLogger(AnnotationProcessorEnvironment var1) {
      this.messager = var1.getMessager();
   }

   public void log(EventLevel var1, LogEvent var2) {
      SourcePos var3 = null;
      String var4 = var2.getText();
      if (var2.getSourceURI() != null) {
         var3 = new SourcePos(var2);
      }

      this.log(var1, var3, var4);
   }

   public void log(EventLevel var1, String var2) {
      this.log(var1, (SourcePosition)null, var2);
   }

   private void log(EventLevel var1, SourcePosition var2, String var3) {
      switch (var1) {
         case WARNING:
            this.printWarning(var2, var3);
            break;
         case ERROR:
            this.printError(var2, var3);
            break;
         default:
            this.printNotice(var2, var3);
      }

   }

   private void printNotice(SourcePosition var1, String var2) {
      if (var1 == null) {
         this.messager.printNotice(var2);
      } else {
         this.messager.printNotice(var1, var2);
      }

   }

   private void printError(SourcePosition var1, String var2) {
      if (var1 == null) {
         this.messager.printError(var2);
      } else {
         this.messager.printError(var1, var2);
      }

   }

   private void printWarning(SourcePosition var1, String var2) {
      if (var1 == null) {
         this.messager.printWarning(var2);
      } else {
         this.messager.printWarning(var1, var2);
      }

   }

   private static class SourcePos implements SourcePosition {
      private LogEvent logEvent = null;

      public SourcePos(LogEvent var1) {
         this.logEvent = var1;
      }

      public File file() {
         return new File(this.logEvent.getSourceURI());
      }

      public int line() {
         return this.logEvent.getLine();
      }

      public int column() {
         return this.logEvent.getColumn();
      }
   }
}
