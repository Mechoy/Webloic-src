package weblogic.wsee.wsdl.soap11;

import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.soap12.Soap12Body;

public class SoapBindingUtil {
   public static final String RPC = "rpc";
   public static final String DOCUMENT = "document";
   public static final String ENCODED = "encoded";

   public static String getStyle(SoapBindingOperation var0, SoapBinding var1) {
      String var2 = null;
      if (var0 != null) {
         var2 = var0.getStyle();
      }

      if (var2 != null && !"".equals(var2)) {
         return var2;
      } else {
         if (var1 != null) {
            var2 = var1.getStyle();
         }

         return var2 != null && !"".equals(var2) ? var2 : "document";
      }
   }

   public static String getUse(WsdlBindingMessage var0, WsdlBindingMessage var1) {
      String var3 = null;
      Object var2;
      if (var0 != null) {
         var2 = SoapBody.narrow(var0);
         if (var2 == null) {
            var2 = Soap12Body.narrow(var0);
         }

         var3 = ((SoapBody)var2).getUse();
         if (var3 != null) {
            return var3;
         }
      }

      if (var1 != null) {
         var2 = SoapBody.narrow(var1);
         if (var2 == null) {
            var2 = Soap12Body.narrow(var1);
         }

         var3 = ((SoapBody)var2).getUse();
         if (var3 != null) {
            return var3;
         }
      }

      return var3;
   }
}
