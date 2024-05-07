package weblogic.auddi.uddi.soap;

import java.io.IOException;
import java.net.URL;
import weblogic.auddi.soap.SOAPWrapperException;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;

public class SOAPClient {
   private static final int BUFF_SIZE = 2048;

   private SOAPClient() {
   }

   public static String sendUDDIRequest(String var0) throws IOException, FatalErrorException, UDDIException {
      String var1 = System.getProperty("auddi.provider.url");
      String var2 = System.getProperty("PROXY_HOST");
      String var3 = System.getProperty("PROXY_PORT");
      if (var1 != null && var1.compareTo("") != 0) {
         String var4 = null;
         if (var2 != null && var2.compareTo("") != 0 && var3 != null && var3.compareTo("") != 0) {
            var4 = sendUDDIRequest(var1, var2, Integer.parseInt(var3), var0);
         } else {
            var4 = sendUDDIRequest(var1, var0);
         }

         return var4;
      } else {
         Logger.Log(3, (String)"SOAPClientDataSource error: auddi.provider.url must exist in uddi.properties");
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.property", "auddi.provider.url"));
      }
   }

   /** @deprecated */
   public static String sendUDDIRequest(String var0, String var1) throws IOException, UDDIException {
      return sendUDDIRequest((String)var0, "", -1, var1);
   }

   public static String sendUDDIRequest(URL var0, String var1) throws IOException, UDDIException {
      return sendUDDIRequest((URL)var0, "", -1, var1);
   }

   /** @deprecated */
   public static String sendUDDIRequest(String var0, String var1, int var2, String var3) throws IOException, UDDIException {
      Logger.trace("+SOAPClient.sendUDDIRequest() : URL = '" + var0 + "'");
      URL var4 = new URL(var0);
      Logger.Log(3, (String)("URL = '" + var4 + "'"));

      String var6;
      try {
         String var5 = sendUDDIRequest(var4, var1, var2, var3);
         var6 = var5;
      } finally {
         Logger.trace("-SOAPClient.sendUDDIRequest()");
      }

      return var6;
   }

   public static String sendUDDIRequest(URL var0, String var1, int var2, String var3) throws IOException, UDDIException {
      Logger.trace("+SOAPClient.sendUDDIRequest(URL)");
      Logger.Log(3, (String)("URL = '" + var0 + "'"));

      String var5;
      try {
         String var4 = UDDISOAPWrapper.sendUDDIRequest(var3, var0, var1, var2);
         var5 = var4;
      } catch (SOAPWrapperException var9) {
         Logger.error((Throwable)var9);
         throw new IOException(var9.getMessage());
      } finally {
         Logger.trace("-SOAPClient.sendUDDIRequest(URL)");
      }

      return var5;
   }
}
