package weblogic.xml.dom;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.utils.NestedException;

public class DOMParserException extends NestedException {
   private SAXException saxException;
   private SAXParseException saxParseException;
   static final long serialVersionUID = 3947439951925250986L;

   public DOMParserException(SAXException var1) {
      super(var1);
      this.saxException = var1;
      if (this.saxException instanceof SAXParseException) {
         this.saxParseException = (SAXParseException)var1;
      }

   }

   public int getErrorLine() {
      return this.saxParseException != null ? this.saxParseException.getLineNumber() : -1;
   }

   public int getErrorColumn() {
      return this.saxParseException != null ? this.saxParseException.getColumnNumber() : -1;
   }

   public String toString() {
      String var1 = new String();
      if (this.saxParseException != null) {
         var1 = var1 + "Received SAXParseException from Sun Parser at line " + this.getErrorLine() + ", column " + this.getErrorColumn() + ": " + this.saxParseException;
      } else {
         var1 = var1 + "Received SAXException from Sun Parser: " + this.saxException;
      }

      return var1;
   }
}
