package weblogic.wsee.jaxws.injection;

import com.oracle.pitchfork.interfaces.inject.EnricherI;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainsBean;
import weblogic.j2ee.injection.PitchforkContext;

public class WSEEClientComponentContributor extends WSEEComponentContributor {
   private final ServiceRefHandlerChainsBean handlerChainsBean;

   public WSEEClientComponentContributor(ServiceRefHandlerChainsBean var1, PitchforkContext var2) {
      super(var2);
      this.handlerChainsBean = var1;
   }

   public void contribute(EnricherI var1) {
      if (this.handlerChainsBean != null) {
         ServiceRefHandlerChainBean[] var2 = this.handlerChainsBean.getHandlerChains();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ServiceRefHandlerChainBean var5 = var2[var4];
            ServiceRefHandlerBean[] var6 = var5.getHandlers();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               ServiceRefHandlerBean var9 = var6[var8];
               this.contribute(var1, var9.getHandlerClass(), var9.getHandlerClass(), var9);
            }
         }
      }

   }
}
