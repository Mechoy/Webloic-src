package weblogic.connector.external;

import weblogic.connector.external.impl.RAInfoImpl;
import weblogic.security.service.EISResource;

public class ConnectorUtils {
   public static final EndpointActivationUtils endpointActivation = weblogic.connector.external.impl.EndpointActivationUtils.getAccessor();
   public static final RAInfo raInfo;

   public static String getEISResourceId(String var0, String var1, String var2, String var3) {
      EISResource var4;
      if (var3 == null) {
         var4 = new EISResource(var0, var1, var2);
      } else {
         var4 = new EISResource(var0, var1, var2, var3);
      }

      return var4.toString();
   }

   static {
      raInfo = RAInfoImpl.factoryHelper;
   }
}
