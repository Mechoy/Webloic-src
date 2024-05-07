package weblogic.wsee.bind.runtime;

import javax.xml.soap.SOAPMessage;

public interface BindingContext {
   int LITERAL = 0;
   int SOAP11 = 1;
   int SOAP12 = 2;
   boolean force_v91_xmlbean_marshalling = Boolean.getBoolean("FORCE_v91_XMLBEANS_MARSHALLING");

   void setEncoding(int var1);

   int getEncoding();

   SOAPMessage getMessage();

   void setMessage(SOAPMessage var1);
}
