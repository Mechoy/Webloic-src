package weblogic.xml.security.wsse;

import weblogic.xml.schema.binding.util.StdNamespace;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.utils.XMLOutputStreamBase;
import weblogic.xml.security.wsse.v200207.SecureOutputPipelineFactory;
import weblogic.xml.security.wsse.v200207.SecurityImpl;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class SecureSoapOutputStream extends XMLOutputStreamBase implements weblogic.xml.security.wsse.v200207.WSSEConstants, XMLOutputStream {
   public SecureSoapOutputStream(Security var1, XMLOutputStream var2) throws XMLStreamException, SecurityProcessingException {
      this(var1, var2, "UTF-8");
   }

   public SecureSoapOutputStream(Security var1, XMLOutputStream var2, String var3) throws SecurityProcessingException {
      this(var1, var2, var3, StdNamespace.instance().soapEnvelope());
   }

   public SecureSoapOutputStream(Security var1, XMLOutputStream var2, String var3, String var4) throws SecurityProcessingException {
      super((XMLOutputStream)null);
      XMLOutputStream var5 = SecureOutputPipelineFactory.createStream((SecurityImpl)var1, var2, var3, var4);
      this.setDestination(var5);
      this.addPrefix(WSSE_URI, "wsse");
   }

   /** @deprecated */
   public static void setSoapEnvPrefix(String var0) {
      throw new UnsupportedOperationException("Set the namespace for SOAP Envelope on the constructor; prefix will be auto-detected");
   }

   protected void addXMLEvent(XMLEvent var1) throws XMLStreamException {
      this.dest.add(var1);
   }
}
