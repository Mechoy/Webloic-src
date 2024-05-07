package weblogic.servlet.jsp.dd;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.logging.Loggable;
import weblogic.servlet.HTTPLogger;

class JspcLogErrHandler implements ErrorHandler {
   private String location;
   private String ctx;

   public JspcLogErrHandler(String var1, String var2) {
      this.ctx = var1;
      this.location = var2;
   }

   public void warning(SAXParseException var1) throws SAXParseException {
   }

   public void error(SAXParseException var1) throws SAXParseException {
      Loggable var2 = HTTPLogger.logMalformedDescriptorCtxLoggable(this.ctx, this.location, "" + var1.getLineNumber(), "" + var1.getColumnNumber(), var1.getMessage());
      var2.log();
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      Loggable var2 = HTTPLogger.logMalformedDescriptorCtxLoggable(this.ctx, this.location, "" + var1.getLineNumber(), "" + var1.getColumnNumber(), var1.getMessage());
      var2.log();
      throw new SAXException(var2.getMessage());
   }
}
