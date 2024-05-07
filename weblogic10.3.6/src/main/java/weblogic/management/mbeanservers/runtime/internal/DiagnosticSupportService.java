package weblogic.management.mbeanservers.runtime.internal;

import java.beans.BeanInfo;
import java.security.AccessController;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.harvester.internal.TreeBeanHarvestableDataProviderHelper;
import weblogic.management.jmx.ObjectNameManager;
import weblogic.management.mbeanservers.internal.WLSObjectNameManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class DiagnosticSupportService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   boolean trackingInstances = true;
   BeanInfoAccess beanInfoAccess;
   ObjectNameManager objectNameManager;

   public void start() throws ServiceFailureException {
      this.beanInfoAccess = ManagementService.getBeanInfoAccess();
      this.objectNameManager = new WLSObjectNameManager(ManagementService.getRuntimeAccess(kernelId).getDomainName());
      TreeBeanHarvestableDataProviderHelper.setSupportService(this);
      RuntimeServerService.setSupportService(this);
   }

   public BeanInfo getBeanInfo(String var1) {
      return this.beanInfoAccess.getBeanInfoForInterface(var1, true, (String)null);
   }

   public void unregisterInstance(Object var1) {
      if (this.trackingInstances) {
         this.objectNameManager.unregisterObjectInstance(var1);
      }

   }

   public String getInterfaceClassForObjectIdentifier(String var1) {
      try {
         Object var2 = this.objectNameManager.lookupObject(new ObjectName(var1));
         return var2 == null ? null : this.beanInfoAccess.getInterfaceForInstance(var2).getName();
      } catch (MalformedObjectNameException var3) {
         return null;
      }
   }

   public String getObjectIdentifier(Object var1) {
      ObjectName var2 = this.objectNameManager.lookupObjectName(var1);
      return var2 == null ? null : var2.getCanonicalName();
   }

   public String getRegisteredObjectIdentifier(Object var1) {
      ObjectName var2 = this.objectNameManager.lookupRegisteredObjectName(var1);
      if (var2 == null) {
         var2 = this.objectNameManager.buildObjectName(var1);
      }

      return var2 == null ? null : var2.getCanonicalName();
   }

   ObjectNameManager getObjectNameManager() {
      this.trackingInstances = false;
      return this.objectNameManager;
   }

   public Object getInstanceForObjectIdentifier(String var1) {
      try {
         return this.objectNameManager.lookupObject(new ObjectName(var1));
      } catch (MalformedObjectNameException var3) {
         return null;
      }
   }
}
