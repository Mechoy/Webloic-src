package weblogic.wsee.reliability2.api;

import com.sun.xml.ws.api.WSBinding;
import javax.xml.ws.BindingProvider;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.reliability2.tube.WsrmClientImpl;
import weblogic.wsee.reliability2.tube.WsrmClientRuntimeFeature;

public class WsrmClientFactory {
   public static boolean isWsrmClientEnabledOnPort(Object var0) {
      BindingProvider var1 = (BindingProvider)var0;
      WSBinding var2 = (WSBinding)var1.getBinding();
      boolean var3 = var2.isFeatureEnabled(WsrmClientRuntimeFeature.class);
      return var3;
   }

   public static WsrmClient getWsrmClientFromPort(Object var0) {
      BindingProvider var1 = (BindingProvider)var0;
      WSBinding var2 = (WSBinding)var1.getBinding();
      boolean var3 = var2.isFeatureEnabled(WsrmClientRuntimeFeature.class);
      if (!var3) {
         throw new IllegalStateException(WseeRmLogger.logWsrmClientNotEnabledLoggable().getMessage());
      } else {
         WsrmClientRuntimeFeature var4 = (WsrmClientRuntimeFeature)var2.getFeature(WsrmClientRuntimeFeature.class);

         try {
            return new WsrmClientImpl(var1, var4);
         } catch (Exception var6) {
            throw new RuntimeException(var6.toString(), var6);
         }
      }
   }
}
