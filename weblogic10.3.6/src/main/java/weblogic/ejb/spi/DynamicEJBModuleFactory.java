package weblogic.ejb.spi;

import weblogic.application.ApplicationContextInternal;

public class DynamicEJBModuleFactory {
   public static DynamicEJBModule createDynamicEJBModule(String var0) {
      return new weblogic.ejb.container.deployer.DynamicEJBModule(var0);
   }

   public static DynamicEJBModule createDynamicEJBModule(String var0, ApplicationContextInternal var1) {
      return new weblogic.ejb.container.deployer.DynamicEJBModule(var0, var1);
   }
}
