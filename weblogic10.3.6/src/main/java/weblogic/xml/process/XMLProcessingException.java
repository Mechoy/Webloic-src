package weblogic.xml.process;

import weblogic.utils.NestedException;

public class XMLProcessingException extends NestedException {
   private static final long serialVersionUID = -9066863319527593494L;
   protected String fileName;

   public XMLProcessingException() {
   }

   public XMLProcessingException(String var1) {
      super(var1);
   }

   public XMLProcessingException(Throwable var1) {
      super(var1);
   }

   public XMLProcessingException(Throwable var1, String var2) {
      super(var1);
      this.fileName = var2;
   }

   public XMLProcessingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String toString() {
      return this.fileName == null ? super.toString() : "Error processing file '" + this.fileName + "'. " + super.toString();
   }
}
