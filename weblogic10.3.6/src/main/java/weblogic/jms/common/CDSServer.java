package weblogic.jms.common;

import java.security.AccessController;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dd.DDMember;
import weblogic.jms.dd.DDStatusListener;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class CDSServer implements CDSListProvider {
   private final List listeners = Collections.synchronizedList(new LinkedList());
   private static CDSServer singleton = new CDSServer();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ServerRuntimeMBean serverRuntimeMBean;

   public static synchronized CDSServer getSingleton() {
      if (serverRuntimeMBean == null) {
         try {
            serverRuntimeMBean = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         } catch (Exception var1) {
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("Failed to get server runtime mbean:" + var1);
            }
         }
      }

      return singleton;
   }

   public static TargetMBean getDestinationTarget(DomainMBean var0, Object var1) {
      if (!(var1 instanceof DestinationImpl)) {
         return null;
      } else {
         String var2 = ((DestinationImpl)var1).getServerName();
         JMSServerMBean var3 = var0.lookupJMSServer(var2);
         if (var3 == null) {
            return null;
         } else {
            TargetMBean[] var4 = var3.getTargets();
            return var4 == null ? null : var4[0];
         }
      }
   }

   private String getLocalClusterName() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return var1.getCluster() != null ? var1.getCluster().getName() : null;
   }

   public synchronized DDMemberInformation[] registerListener(CDSListListener var1) throws javax.jms.JMSException {
      DDHandlerChangeListener var2 = new DDHandlerChangeListener(var1);
      this.listeners.add(var2);
      return var2.getDDMemberInformation();
   }

   public synchronized void unregisterListener(CDSListListener var1) {
      ListIterator var2 = this.listeners.listIterator();

      while(var2.hasNext()) {
         DDHandlerChangeListener var3 = (DDHandlerChangeListener)var2.next();
         if (var3.getListener() == var1) {
            var3.quit();
            var2.remove();
            break;
         }
      }

   }

   private final class DDHandlerChangeListener implements DDStatusListener {
      private final DDHandler ddHandler;
      private final CDSListListener listener;

      public DDHandlerChangeListener(CDSListListener var2) throws javax.jms.JMSException {
         this.ddHandler = DDManager.findDDHandlerByDDName(var2.getConfigName());
         if (this.ddHandler == null) {
            throw new javax.jms.JMSException("There is no DD named " + var2.getConfigName());
         } else {
            this.listener = var2;
            this.ddHandler.addStatusListener(this, 19);
         }
      }

      public CDSListListener getListener() {
         return this.listener;
      }

      public void quit() {
         this.ddHandler.removeStatusListener(this);
      }

      private void waitForServerUp() {
         int var1 = 0;
         if (CDSServer.serverRuntimeMBean != null) {
            while(CDSServer.serverRuntimeMBean.getStateVal() != 2 && CDSServer.serverRuntimeMBean.getStateVal() != 7 && CDSServer.serverRuntimeMBean.getStateVal() != 0) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var3) {
               }

               ++var1;
               if (var1 >= 10) {
                  break;
               }
            }

         }
      }

      public synchronized DDMemberInformation[] getDDMemberInformation() {
         LinkedList var1 = new LinkedList();
         Iterator var2 = this.ddHandler.memberCloneIterator();

         while(var2.hasNext()) {
            DDMember var3 = (DDMember)var2.next();
            if (var3.isUp()) {
               DestinationImpl var4 = new DestinationImpl(this.ddHandler.isQueue() ? 1 : 2, var3.getJMSServerName(), var3.getPersistentStoreName(), var3.getName(), this.ddHandler.getApplicationName(), this.ddHandler.getEARModuleName(), var3.getBackEndId(), var3.getDestinationId(), var3.getDispatcherId());
               var1.add(new DDMemberInformation(this.ddHandler.getName(), this.ddHandler.isQueue() ? new String("javax.jms.Queue") : new String("javax.jms.Topic"), this.ddHandler.getJNDIName(), this.ddHandler.getForwardingPolicy(), var4, var3.getWLSServerName(), var3.getGlobalJNDIName(), var3.getLocalJNDIName(), CDSServer.this.getLocalClusterName(), var3.getMigratableTargetName(), var3.getDomainName(), var3.isConsumptionPaused(), var3.isInsertionPaused(), var3.isProductionPaused(), true));
            }
         }

         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            if (var1.size() == 0) {
               JMSDebug.JMSCDS.debug("list is null");
            } else {
               JMSDebug.JMSCDS.debug("list has in it:");
               CDS.dumpDDMITable((DDMemberInformation[])((DDMemberInformation[])var1.toArray(new DDMemberInformation[0])));
            }
         }

         return (DDMemberInformation[])((DDMemberInformation[])var1.toArray(new DDMemberInformation[0]));
      }

      public void statusChangeNotification(DDHandler var1, int var2) {
         if ((var2 & 16) != 0) {
            this.quit();
            this.listener.distributedDestinationGone((DispatcherId)null);
         } else {
            this.waitForServerUp();
            this.listener.listChange(this.getDDMemberInformation());
         }

      }
   }
}
