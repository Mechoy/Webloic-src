package weblogic.wsee.tools.logging;

import java.net.URI;
import weblogic.utils.StackTraceUtils;

public class LogEvent {
   private int line;
   private int column;
   private URI sourceURI;
   private Throwable exception;
   private String text = new String();

   public int getLine() {
      return this.line;
   }

   public void setLine(int var1) {
      this.line = var1;
   }

   public int getColumn() {
      return this.column;
   }

   public void setColumn(int var1) {
      this.column = var1;
   }

   public URI getSourceURI() {
      return this.sourceURI;
   }

   public void setSourceURI(URI var1) {
      this.sourceURI = var1;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      if (var1 != null) {
         this.text = var1;
      }

   }

   public Throwable getException() {
      return this.exception;
   }

   public void setException(Throwable var1) {
      this.exception = var1;
   }

   public String toString() {
      String var1 = this.text;
      if (this.exception != null) {
         var1 = this.text + StackTraceUtils.throwable2StackTrace(this.exception);
      }

      return var1;
   }
}
