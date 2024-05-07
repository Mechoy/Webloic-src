package weblogic.wsee.tools.build.logging;

import weblogic.utils.StackTraceUtils;

public class LogEvent {
   private int lineNo;
   private int columnNo;
   private String filePath;
   private Throwable exception;
   private String message = new String();

   public LogEvent() {
   }

   public LogEvent(int var1, int var2, String var3, String var4, Throwable var5) {
      this.lineNo = var1;
      this.columnNo = var2;
      this.filePath = var3;
      if (var4 != null) {
         this.message = var4;
      }

      this.exception = var5;
   }

   public LogEvent(String var1, Throwable var2) {
      if (var1 != null) {
         this.message = var1;
      }

      this.exception = var2;
   }

   public LogEvent(String var1) {
      if (var1 != null) {
         this.message = var1;
      }

   }

   public int getLineNo() {
      return this.lineNo;
   }

   public void setLineNo(int var1) {
      this.lineNo = var1;
   }

   public int getColumnNo() {
      return this.columnNo;
   }

   public void setColumnNo(int var1) {
      this.columnNo = var1;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String var1) {
      this.filePath = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      if (var1 != null) {
         this.message = var1;
      }

   }

   public Throwable getException() {
      return this.exception;
   }

   public void setException(Throwable var1) {
      this.exception = var1;
   }

   public String toString() {
      String var1 = this.message;
      if (this.exception != null) {
         var1 = this.message + StackTraceUtils.throwable2StackTrace(this.exception);
      }

      return var1;
   }
}
