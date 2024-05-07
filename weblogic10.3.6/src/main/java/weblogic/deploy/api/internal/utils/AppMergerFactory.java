package weblogic.deploy.api.internal.utils;

import javax.enterprise.deploy.shared.ModuleType;
import weblogic.deploy.api.shared.WebLogicModuleType;

public final class AppMergerFactory {
   public static AppMerger getAppMerger(ModuleType var0) {
      int var1 = var0.getValue();
      return var1 != ModuleType.EAR.getValue() && var1 != ModuleType.WAR.getValue() && var1 != ModuleType.EJB.getValue() && var1 != WebLogicModuleType.SCA_COMPOSITE.getValue() ? null : new EarMerger();
   }
}
