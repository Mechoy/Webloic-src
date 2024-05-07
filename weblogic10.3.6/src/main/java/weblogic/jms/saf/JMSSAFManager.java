package weblogic.jms.saf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import weblogic.application.ModuleException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.utils.BeanListenerCustomizer;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.saf.utils.Util;
import weblogic.work.WorkManager;

public final class JMSSAFManager {
   static final String JMSSAF_WM_NAME = "weblogic.jms.saf.General";
   public static JMSSAFManager manager = new JMSSAFManager();
   private Map remoteSAFContexts = Collections.synchronizedMap(new HashMap());
   private Map errorHandlers = Collections.synchronizedMap(new HashMap());
   private WorkManager workManager;

   private void initializeWorkManagers() {
      this.workManager = Util.findOrCreateWorkManager("weblogic.jms.saf.General", -1, 0, -1);
   }

   public void addErrorHandler(String var1, ErrorHandler var2) {
      this.errorHandlers.put(var1, var2);
   }

   public ErrorHandler getErrorHandler(String var1) {
      return (ErrorHandler)this.errorHandlers.get(var1);
   }

   public void removeErrorHandler(String var1) {
      this.errorHandlers.remove(var1);
   }

   public void addRemoteSAFContext(String var1, RemoteContext var2) {
      this.remoteSAFContexts.put(var1, var2);
   }

   public RemoteContext getRemoteSAFContext(String var1) {
      return (RemoteContext)this.remoteSAFContexts.get(var1);
   }

   public void removeRemoteSAFContext(String var1) {
      this.remoteSAFContexts.remove(var1);
   }

   WorkManager getWorkManager() {
      return this.workManager;
   }

   public static GenericBeanListener initializeGenericBeanListener(DescriptorBean var0, Object var1, BeanListenerCustomizer var2, HashMap var3, HashMap var4) throws ModuleException {
      GenericBeanListener var5 = null;
      if (var4 == null) {
         var5 = new GenericBeanListener(var0, var1, var3);
      } else {
         var5 = new GenericBeanListener(var0, var1, var3, var4);
      }

      if (var2 != null) {
         var5.setCustomizer(var2);
      }

      try {
         var5.initialize();
         return var5;
      } catch (ManagementException var7) {
         throw new ModuleException(var7.getMessage(), var7);
      }
   }

   static {
      manager.initializeWorkManagers();
   }
}
