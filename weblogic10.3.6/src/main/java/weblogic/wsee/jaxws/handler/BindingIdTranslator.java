package weblogic.wsee.jaxws.handler;

import java.util.HashMap;
import java.util.Map;

public class BindingIdTranslator {
   private static final String SOAP11_TOKEN = "##SOAP11_HTTP";
   private static final String SOAP12_TOKEN = "##SOAP12_HTTP";
   private static final String SOAP11_MTOM_TOKEN = "##SOAP11_HTTP_MTOM";
   private static final String SOAP12_MTOM_TOKEN = "##SOAP12_HTTP_MTOM";
   private static final String XML_TOKEN = "##XML_HTTP";
   private static final Map<String, String> TRANSLATION_MAP = new HashMap(5);

   private BindingIdTranslator() {
   }

   public static String translate(String var0) {
      String var1 = (String)TRANSLATION_MAP.get(var0);
      if (var1 == null) {
         var1 = var0;
      }

      return var1;
   }

   static {
      TRANSLATION_MAP.put("##SOAP11_HTTP", "http://schemas.xmlsoap.org/wsdl/soap/http");
      TRANSLATION_MAP.put("##SOAP12_HTTP", "http://www.w3.org/2003/05/soap/bindings/HTTP/");
      TRANSLATION_MAP.put("##SOAP11_HTTP_MTOM", "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true");
      TRANSLATION_MAP.put("##SOAP12_HTTP_MTOM", "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true");
      TRANSLATION_MAP.put("##XML_HTTP", "http://www.w3.org/2004/08/wsdl/http");
   }
}
