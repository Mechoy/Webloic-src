package weblogic.xml.dtdc;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class NullHandler implements DocumentHandler {
   final boolean verbose;
   private static int depth = 0;

   public NullHandler() {
      this.verbose = false;
   }

   public NullHandler(boolean var1) {
      this.verbose = var1;
   }

   public void setDocumentLocator(Locator var1) {
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      if (this.verbose) {
         format("Start: " + var1);
         ++depth;
      }

   }

   public void endElement(String var1) throws SAXException {
      if (this.verbose) {
         --depth;
         format("End:   " + var1);
      }

   }

   private static void format(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = depth; var2 > 0; --var2) {
         var1.append("  ");
      }

      System.out.println(var1.append(var0));
   }
}
