package weblogic.xml.process;

import org.xml.sax.SAXException;

public class SAXProcessorException extends SAXException {
   public SAXProcessorException(String var1) {
      super(var1);
   }

   public SAXProcessorException(Exception var1) {
      super(var1);
   }

   public SAXProcessorException(String var1, Exception var2) {
      super(var1, var2);
   }
}
