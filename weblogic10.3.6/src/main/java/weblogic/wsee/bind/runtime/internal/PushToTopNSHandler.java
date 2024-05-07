package weblogic.wsee.bind.runtime.internal;

import org.w3c.dom.Element;
import weblogic.xml.dom.NamespaceUtils;
import weblogic.xml.util.WriteNamespaceHandler;

public class PushToTopNSHandler implements WriteNamespaceHandler {
   private static PushToTopNSHandler INSTANCE = new PushToTopNSHandler();
   private static final String SCHEMA_INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance";
   private static final String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
   private static final String SOAP11_ENCODING_NS = "http://schemas.xmlsoap.org/soap/encoding/";

   private PushToTopNSHandler() {
   }

   public static WriteNamespaceHandler getInstance() {
      return INSTANCE;
   }

   public boolean writeNamespaceOnElement(Element var1, String var2, String var3) {
      if (!"http://www.w3.org/2001/XMLSchema".equals(var3) && !"http://www.w3.org/2001/XMLSchema-instance".equals(var3) && !"http://schemas.xmlsoap.org/soap/encoding/".equals(var3)) {
         return false;
      } else {
         Element var4 = var1.getOwnerDocument().getDocumentElement();
         NamespaceUtils.defineNamespace(var4, var2, var3);
         return true;
      }
   }
}
