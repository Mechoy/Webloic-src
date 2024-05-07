package weblogic.jndi.internal;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jndi.JNDILogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJNDIProviderMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

public class ForeignJNDIManager implements BeanUpdateListener {
   private static ForeignJNDIManager singleton = null;
   private Map<ForeignJNDIProviderMBean, ForeignJNDILinkManager> jndiLinkMngrs = new HashMap();
   private InitialContext ic = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private ForeignJNDIManager() {
      try {
         Hashtable var1 = new Hashtable();
         var1.put("weblogic.jndi.createIntermediateContexts", "true");
         var1.put("weblogic.jndi.replicateBindings", "false");
         this.ic = new InitialContext(var1);
      } catch (NamingException var2) {
         JNDILogger.logCannotCreateInitialContext(StackTraceUtils.throwable2StackTrace(var2));
      }

   }

   static void initialize() {
      singleton = new ForeignJNDIManager();
      singleton.processForeignJNDIProviderLinks();
      ManagementService.getRuntimeAccess(kernelId).getDomain().addBeanUpdateListener(singleton);
   }

   private void processForeignJNDIProviderLinks() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      String var2 = var1.getServer().getName();
      ForeignJNDIProviderMBean[] var3 = var1.getDomain().getForeignJNDIProviders();
      if (var3 != null) {
         ForeignJNDIProviderMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ForeignJNDIProviderMBean var7 = var4[var6];
            if (this.isForeignJNDILinkTargettedTo(var2, var7)) {
               this.jndiLinkMngrs.put(var7, new ForeignJNDILinkManager(var7, var7.getForeignJNDILinks(), this.ic));
            }
         }
      }

   }

   private boolean isForeignJNDILinkTargettedTo(String var1, ForeignJNDIProviderMBean var2) {
      TargetMBean[] var3 = var2.getTargets();
      if (var3 != null && var3.length != 0) {
         TargetMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            TargetMBean var7 = var4[var6];
            if (var7.getServerNames().contains(var1)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      if (var1.getSourceBean() instanceof DomainMBean) {
         String var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         BeanUpdateEvent.PropertyUpdate[] var5 = var1.getUpdateList();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            BeanUpdateEvent.PropertyUpdate var8 = var5[var7];
            Object var3;
            ForeignJNDIProviderMBean var4;
            switch (var8.getUpdateType()) {
               case 2:
                  var3 = var8.getAddedObject();
                  if (var3 instanceof ForeignJNDIProviderMBean) {
                     var4 = (ForeignJNDIProviderMBean)var3;
                     if (this.isForeignJNDILinkTargettedTo(var2, var4)) {
                        this.jndiLinkMngrs.put(var4, new ForeignJNDILinkManager(var4, var4.getForeignJNDILinks(), this.ic));
                     }
                  }
                  break;
               case 3:
                  var3 = var8.getRemovedObject();
                  if (var3 instanceof ForeignJNDIProviderMBean) {
                     var4 = (ForeignJNDIProviderMBean)var3;
                     if (this.isForeignJNDILinkTargettedTo(var2, var4)) {
                        ((ForeignJNDILinkManager)this.jndiLinkMngrs.remove(var4)).unbindAll();
                     }
                  }
            }
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
