package weblogic.servlet.jsp.dd;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

class StderrHandler implements ErrorHandler {
   public void warning(SAXParseException var1) throws SAXParseException {
      System.err.println("[JSP]: warning " + var1);
   }

   public void error(SAXParseException var1) throws SAXParseException {
      System.err.println("[JSP]: got error " + var1 + " at line " + var1.getLineNumber() + ", column " + var1.getColumnNumber());
   }

   public void fatalError(SAXParseException var1) throws SAXParseException {
      System.err.println("[JSP]: FATAL ERROR " + var1);
      throw var1;
   }
}
