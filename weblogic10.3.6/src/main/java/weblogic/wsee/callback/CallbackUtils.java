package weblogic.wsee.callback;

import com.bea.xbean.common.NameUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.namespace.QName;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class CallbackUtils {
   private static final String CALLBACK_SERVICE_URI = "/CallbackService.jws";

   public static boolean isCallbackService(WsPort var0) {
      if (var0 == null) {
         return false;
      } else {
         WsdlPartnerLinkType var1 = (WsdlPartnerLinkType)var0.getWsdlPort().getDefinitions().getExtension("PartnerLinkType");
         if (var1 == null) {
            return false;
         } else {
            QName var2 = null;

            try {
               var2 = var1.getPortTypeName("Callback");
            } catch (WsdlException var4) {
               var4.printStackTrace();
               return false;
            }

            if (var2 == null) {
               return false;
            } else {
               return var2.equals(var0.getWsdlPort().getPortType().getName());
            }
         }
      }
   }

   public static String getServiceUri(QName var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append('/');
      var2.append(NameUtil.getClassNameFromQName(var0).replace(".", "/"));
      var2.append('/');
      var2.append(var1);
      var2.append("/CallbackService.jws");
      return var2.toString();
   }

   public static String getCallbackPortName(String var0) {
      return var0 + "Callback";
   }

   public static boolean has81StyleCallback(JsService var0) {
      WsdlService var1 = var0.getWsdlService();
      Iterator var2 = var1.getPortTypes().iterator();

      while(var2.hasNext()) {
         WsdlPortType var3 = (WsdlPortType)var2.next();
         Iterator var4 = var3.getOperations().values().iterator();

         while(var4.hasNext()) {
            WsdlOperation var5 = (WsdlOperation)var4.next();
            if (var5.getType() == 2 || var5.getType() == 3) {
               return true;
            }
         }
      }

      return false;
   }

   public static String parseSendingSideConvId(String var0) {
      if (!var0.startsWith("[")) {
         return null;
      } else {
         int var1 = var0.indexOf("]", 1);
         return var1 > 1 ? var0.substring(1, var1) : null;
      }
   }

   public static String escapeValueForWlw81ControlCallback(String var0) {
      return var0 == null ? null : var0.replace(";", "\\;");
   }

   public static String unEscapeValueForWlw81ControlCallback(String var0) {
      return var0 == null ? null : var0.replace("\\;", ";");
   }

   public static LinkedHashMap<String, String> keyValuePairsForWlw81ControlsCallbackEncodedConvId(String var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0 != null) {
         int var2 = var0.length();
         int var3 = var0.indexOf(93) + 1;
         if (var2 > var3) {
            while(var3 < var2 && var3 > 0) {
               int var4 = var0.indexOf(61, var3);
               if (var4 < 0) {
                  break;
               }

               int var5 = var0.indexOf(59, var4);
               if (var5 <= var4) {
                  break;
               }

               String var6 = var0.substring(var3, var4);
               String var7 = var0.substring(var4 + 1, var5);
               var1.put(var6, unEscapeValueForWlw81ControlCallback(var7));
               var3 = var5 + 1;
            }
         }
      }

      return var1;
   }

   public static String encodeConversationIdForWlw81ControlsCallback(String var0, String var1, LinkedHashMap<String, String> var2) {
      StringBuffer var3 = new StringBuffer();
      if (var0 != null && !var0.equals("")) {
         ContinueHeader var4 = new ContinueHeader(var0, var1);
         var3.append("[");
         var3.append(var4.convertToWlw81StringForm());
         var3.append("]");
      } else {
         var3.append("[]");
      }

      Iterator var7 = var2.keySet().iterator();

      while(var7.hasNext()) {
         String var5 = (String)var7.next();
         String var6 = (String)var2.get(var5);
         var3.append(var5);
         var3.append("=");
         var3.append(escapeValueForWlw81ControlCallback(var6));
         var3.append(";");
      }

      return var3.toString();
   }
}
