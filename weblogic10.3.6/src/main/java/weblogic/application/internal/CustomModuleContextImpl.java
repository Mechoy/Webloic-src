package weblogic.application.internal;

import weblogic.application.CustomModuleContext;
import weblogic.j2ee.descriptor.wl.ModuleProviderBean;

public class CustomModuleContextImpl implements CustomModuleContext {
   private final String parentModuleUri;
   private final String parentModuleId;
   private final ModuleProviderBean moduleProviderBean;

   public CustomModuleContextImpl(ModuleProviderBean var1) {
      this(var1, (String)null, (String)null);
   }

   public CustomModuleContextImpl(ModuleProviderBean var1, String var2, String var3) {
      this.moduleProviderBean = var1;
      this.parentModuleId = var2;
      this.parentModuleUri = var3;
   }

   public ModuleProviderBean getModuleProviderBean() {
      return this.moduleProviderBean;
   }

   public String getParentModuleId() {
      return this.parentModuleId;
   }

   public String getParentModuleUri() {
      return this.parentModuleUri;
   }
}
