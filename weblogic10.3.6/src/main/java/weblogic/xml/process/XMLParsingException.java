package weblogic.xml.process;

import org.xml.sax.SAXParseException;
import weblogic.utils.NestedException;

public class XMLParsingException extends NestedException {
   private static final long serialVersionUID = -6330961353409169997L;
   protected String fileName;

   public XMLParsingException(String var1) {
      super(var1);
   }

   public XMLParsingException(Throwable var1) {
      super(var1);
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getMessage() {
      return this.getMessagePrefix() + super.getMessage();
   }

   public String toString() {
      return this.getMessagePrefix() + super.toString();
   }

   private String getMessagePrefix() {
      StringBuffer var1 = new StringBuffer();
      Throwable var2 = this.getNestedException();
      boolean var3 = var2 instanceof SAXParseException;
      if (this.fileName != null || var3) {
         var1.append("Error parsing file ");
         if (this.fileName != null) {
            var1.append('\'');
            var1.append(this.fileName);
            var1.append('\'');
            if (var3) {
               var1.append(" ");
            } else {
               var1.append(".  ");
            }
         }

         if (var3) {
            SAXParseException var4 = (SAXParseException)var2;
            var1.append("at line: ");
            var1.append(var4.getLineNumber());
            var1.append(" column: ");
            var1.append(var4.getColumnNumber());
            var1.append(".  ");
         }
      }

      return var1.toString();
   }
}
