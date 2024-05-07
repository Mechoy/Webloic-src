package weblogic.ejb.container.monitoring;

import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.MessageDrivenControlEJBRuntimeMBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

public final class MessageDrivenControlEJBRuntimeMBeanImpl extends DomainRuntimeMBeanDelegate implements MessageDrivenControlEJBRuntimeMBean {
   boolean verbose = false;
   boolean debug = false;
   long CACHING_STUB_SVUID = 8673161367344012623L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MessageDrivenControlEJBRuntimeMBeanImpl() throws ManagementException {
      super("MessageDrivenControlEJBRuntime");
   }

   public boolean suspendMDBs(String var1, String var2) {
      boolean var3 = true;
      DomainAccess var4 = ManagementService.getDomainAccess(kernelId);
      ServerRuntimeMBean[] var5 = var4.getDomainRuntimeService().getServerRuntimes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         ServerRuntimeMBean var7 = var5[var6];
         Set var8 = this.getAllMessageDrivenEJBRuntimeMBeans(var7);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            MessageDrivenEJBRuntimeMBean var10 = (MessageDrivenEJBRuntimeMBean)var9.next();
            if (var1.equals(var10.getEJBName()) && var2.equals(var10.getApplicationName())) {
               try {
                  var3 &= var10.suspend();
               } catch (Exception var12) {
                  var12.printStackTrace();
               }

               if (this.verbose) {
                  Debug.say("suspendMDBs ejbName=" + var10.getEJBName() + "; MDBStatus=" + var10.getMDBStatus());
               }
            }
         }
      }

      return var3;
   }

   public boolean resumeMDBs(String var1, String var2) {
      boolean var3 = true;
      DomainAccess var4 = ManagementService.getDomainAccess(kernelId);
      ServerRuntimeMBean[] var5 = var4.getDomainRuntimeService().getServerRuntimes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         ServerRuntimeMBean var7 = var5[var6];
         Set var8 = this.getAllMessageDrivenEJBRuntimeMBeans(var7);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            MessageDrivenEJBRuntimeMBean var10 = (MessageDrivenEJBRuntimeMBean)var9.next();
            if (var1.equals(var10.getEJBName()) && var2.equals(var10.getApplicationName())) {
               try {
                  var3 &= var10.resume();
               } catch (Exception var12) {
                  var12.printStackTrace();
               }

               if (this.verbose) {
                  Debug.say("resumeMDBs ejbName=" + var10.getEJBName() + "; MDBStatus=" + var10.getMDBStatus());
               }
            }
         }
      }

      return var3;
   }

   public boolean printMDBStatus(String var1, String var2) {
      boolean var3 = true;
      DomainAccess var4 = ManagementService.getDomainAccess(kernelId);
      ServerRuntimeMBean[] var5 = var4.getDomainRuntimeService().getServerRuntimes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         ServerRuntimeMBean var7 = var5[var6];
         Set var8 = this.getAllMessageDrivenEJBRuntimeMBeans(var7);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            MessageDrivenEJBRuntimeMBean var10 = (MessageDrivenEJBRuntimeMBean)var9.next();
            if (var1.equals(var10.getEJBName()) && var2.equals(var10.getApplicationName()) && this.verbose) {
               Debug.say("printMDBStatus ejbName=" + var10.getEJBName() + "; MDBStatus=" + var10.getMDBStatus());
            }

            if (this.debug) {
               Debug.say("printMDBStatus ejbName=" + var10.getEJBName() + "; MDBStatus=" + var10.getMDBStatus());
            }
         }
      }

      return var3;
   }

   private Set getAllMessageDrivenEJBRuntimeMBeans(ServerRuntimeMBean var1) {
      HashSet var2 = new HashSet(5);
      ApplicationRuntimeMBean[] var3 = var1.getApplicationRuntimes();
      if (var3 != null && var3.length > 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            ApplicationRuntimeMBean var5 = var3[var4];
            ComponentRuntimeMBean[] var6 = var5.getComponentRuntimes();
            if (var6 != null && var6.length > 0) {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  ComponentRuntimeMBean var8 = var6[var7];
                  if (var8 instanceof MessageDrivenEJBRuntimeMBean) {
                     MessageDrivenEJBRuntimeMBean var9 = (MessageDrivenEJBRuntimeMBean)var8;
                     var2.add(var9);
                  }
               }
            }
         }
      }

      return var2;
   }
}
