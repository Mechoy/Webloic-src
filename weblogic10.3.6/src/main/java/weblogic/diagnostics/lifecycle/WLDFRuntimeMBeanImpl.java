package weblogic.diagnostics.lifecycle;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WLDFAccessRuntimeMBean;
import weblogic.management.runtime.WLDFArchiveRuntimeMBean;
import weblogic.management.runtime.WLDFHarvesterRuntimeMBean;
import weblogic.management.runtime.WLDFImageRuntimeMBean;
import weblogic.management.runtime.WLDFInstrumentationRuntimeMBean;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.management.runtime.WLDFWatchNotificationRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WLDFRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WLDFRuntimeMBean {
   private WLDFAccessRuntimeMBean accessRuntime;
   private WLDFArchiveRuntimeMBean archiveRuntime;
   private WLDFHarvesterRuntimeMBean harvesterRuntime;
   private WLDFImageRuntimeMBean imageRuntime;
   private WLDFWatchNotificationRuntimeMBean watchRuntime;
   private List archiveRuntimesList = new ArrayList();
   private List instRuntimesList = new ArrayList();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public WLDFRuntimeMBeanImpl() throws ManagementException {
      super("WLDFRuntime");
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      var1.setWLDFRuntime(this);
   }

   public WLDFAccessRuntimeMBean getWLDFAccessRuntime() {
      return this.accessRuntime;
   }

   public void setWLDFAccessRuntime(WLDFAccessRuntimeMBean var1) {
      this.accessRuntime = var1;
   }

   public WLDFArchiveRuntimeMBean[] getWLDFArchiveRuntimes() {
      synchronized(this.archiveRuntimesList) {
         WLDFArchiveRuntimeMBean[] var2 = new WLDFArchiveRuntimeMBean[this.archiveRuntimesList.size()];
         var2 = (WLDFArchiveRuntimeMBean[])((WLDFArchiveRuntimeMBean[])this.archiveRuntimesList.toArray(var2));
         return var2;
      }
   }

   public boolean addWLDFArchiveRuntime(WLDFArchiveRuntimeMBean var1) {
      synchronized(this.archiveRuntimesList) {
         return this.archiveRuntimesList.contains(var1) ? false : this.archiveRuntimesList.add(var1);
      }
   }

   public boolean removeWLDFArchiveRuntime(WLDFArchiveRuntimeMBean var1) {
      synchronized(this.archiveRuntimesList) {
         return this.archiveRuntimesList.remove(var1);
      }
   }

   public WLDFHarvesterRuntimeMBean getWLDFHarvesterRuntime() {
      return this.harvesterRuntime;
   }

   public void setWLDFHarvesterRuntime(WLDFHarvesterRuntimeMBean var1) {
      this.harvesterRuntime = var1;
   }

   public WLDFImageRuntimeMBean getWLDFImageRuntime() {
      return this.imageRuntime;
   }

   public void setWLDFImageRuntime(WLDFImageRuntimeMBean var1) {
      this.imageRuntime = var1;
   }

   public WLDFInstrumentationRuntimeMBean[] getWLDFInstrumentationRuntimes() {
      synchronized(this.instRuntimesList) {
         WLDFInstrumentationRuntimeMBean[] var2 = new WLDFInstrumentationRuntimeMBean[this.instRuntimesList.size()];
         var2 = (WLDFInstrumentationRuntimeMBean[])((WLDFInstrumentationRuntimeMBean[])this.instRuntimesList.toArray(var2));
         return var2;
      }
   }

   public boolean addWLDFInstrumentationRuntime(WLDFInstrumentationRuntimeMBean var1) {
      synchronized(this.instRuntimesList) {
         return this.instRuntimesList.contains(var1) ? false : this.instRuntimesList.add(var1);
      }
   }

   public boolean removeWLDFInstrumentationRuntime(WLDFInstrumentationRuntimeMBean var1) {
      synchronized(this.instRuntimesList) {
         return this.instRuntimesList.remove(var1);
      }
   }

   public WLDFWatchNotificationRuntimeMBean getWLDFWatchNotificationRuntime() {
      return this.watchRuntime;
   }

   public void setWLDFWatchNotificationRuntime(WLDFWatchNotificationRuntimeMBean var1) {
      this.watchRuntime = var1;
   }

   public WLDFInstrumentationRuntimeMBean lookupWLDFInstrumentationRuntime(String var1) {
      Iterator var2 = this.instRuntimesList.iterator();

      WLDFInstrumentationRuntimeMBean var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFInstrumentationRuntimeMBean)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }
}
