package weblogic.connector.inbound;

import com.bea.connector.diagnostic.EndpointType;
import com.bea.connector.diagnostic.InboundAdapterType;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.SystemException;
import weblogic.connector.common.ConnectorDiagnosticImageSource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.exception.RAInboundException;
import weblogic.connector.external.ElementNotFoundException;
import weblogic.connector.external.InboundInfo;
import weblogic.connector.external.SuspendableEndpointFactory;
import weblogic.connector.monitoring.ConnectorComponentRuntimeMBeanImpl;
import weblogic.connector.monitoring.inbound.ConnectorInboundRuntimeMBeanImpl;
import weblogic.connector.transaction.inbound.InboundRecoveryManager;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class RAInboundManager {
   private RAInstanceManager raInstanceMgr;
   private InboundRecoveryManager recoveryMgr;
   private Hashtable msgListenerToEndptFactoryMap;
   private Hashtable endptFactorySuspendStateMap;
   private Hashtable msgListenerToEJBMap;
   private Hashtable msgListenerToInboundRuntimeMap;
   private String state;
   private static final String RUNNING = Debug.getStringRunning();
   private static final String SUSPENDED = Debug.getStringSuspended();
   private boolean isInbound;

   public RAInboundManager(RAInstanceManager var1) throws RAInboundException {
      this.raInstanceMgr = var1;
      this.msgListenerToEndptFactoryMap = new Hashtable();
      this.msgListenerToEJBMap = new Hashtable();
      this.msgListenerToInboundRuntimeMap = new Hashtable();
      this.endptFactorySuspendStateMap = new Hashtable();
      this.recoveryMgr = new InboundRecoveryManager();
      this.state = RUNNING;
   }

   public void activate() throws RAInboundException {
   }

   public void deactivate() throws RAInboundException {
      this.stop();
   }

   public void rollback() throws RAInboundException {
   }

   public void suspend(Properties var1) throws RAInboundException {
      Debug.enter(this, "suspend(properties)");

      try {
         this.suspendOrResumeMEF(1, var1);
         this.state = SUSPENDED;
      } finally {
         Debug.exit(this, "suspend(properties)");
      }

   }

   public void resume(Properties var1) throws RAInboundException {
      Debug.enter(this, "resume(properties)");

      try {
         this.suspendOrResumeMEF(2, var1);
         this.state = RUNNING;
      } finally {
         Debug.exit(this, "resume(properties)");
      }

   }

   public void stop() throws RAInboundException {
      Debug.enter(this, "stop()");
      RAInboundException var1 = new RAInboundException();

      try {
         this.recoveryMgr.onRAStop(var1);
         this.disconnectMEFs(var1);
         if (!var1.isEmpty()) {
            throw var1;
         }
      } finally {
         this.msgListenerToEndptFactoryMap.clear();
         this.removeAllMDBRuntimeBeans();
         Debug.exit(this, "stop()");
      }

   }

   private void removeAllMDBRuntimeBeans() {
      Iterator var1 = this.msgListenerToInboundRuntimeMap.values().iterator();

      while(var1.hasNext()) {
         ConnectorInboundRuntimeMBeanImpl var2 = (ConnectorInboundRuntimeMBeanImpl)var1.next();
         var2.removeAllMDBRuntimes();
      }

   }

   public void addEndpointFactory(String var1, MessageEndpointFactory var2, MessageDrivenEJBRuntimeMBean var3) {
      Object var4 = null;
      if (this.msgListenerToEndptFactoryMap.containsKey(var1)) {
         var4 = (List)this.msgListenerToEndptFactoryMap.get(var1);
      } else {
         var4 = new Vector();
         this.msgListenerToEndptFactoryMap.put(var1, var4);
      }

      ((List)var4).add(var2);
      this.endptFactorySuspendStateMap.put(var2, Boolean.FALSE);
      ConnectorInboundRuntimeMBeanImpl var5 = (ConnectorInboundRuntimeMBeanImpl)this.msgListenerToInboundRuntimeMap.get(var1);
      if (var5 != null) {
         var5.addMDBRuntime(var3);
      }

   }

   public void removeEndpointFactory(String var1, MessageEndpointFactory var2, MessageDrivenEJBRuntimeMBean var3) throws SystemException {
      List var4 = null;
      if (this.msgListenerToEndptFactoryMap.containsKey(var1)) {
         var4 = (List)this.msgListenerToEndptFactoryMap.get(var1);
         var4.remove(var2);
         if (var4.isEmpty()) {
            this.msgListenerToEndptFactoryMap.remove(var1);
         }

         this.endptFactorySuspendStateMap.remove(var2);
         ConnectorInboundRuntimeMBeanImpl var5 = (ConnectorInboundRuntimeMBeanImpl)this.msgListenerToInboundRuntimeMap.get(var1);
         if (var5 != null) {
            var5.removeMDBRuntime(var3);
         }
      }

   }

   public void setupForRecovery(ActivationSpec var1, String var2) throws SystemException {
      this.recoveryMgr.onActivateEndpoint(this.raInstanceMgr, var1, var2);
   }

   public void cleanupForRecovery(ActivationSpec var1) throws SystemException {
      this.recoveryMgr.onDeActivateEndpoint(var1);
   }

   public void addEJB(String var1, String var2) {
      Object var3 = null;
      if (this.msgListenerToEJBMap.containsKey(var1)) {
         var3 = (List)this.msgListenerToEJBMap.get(var1);
      } else {
         var3 = new Vector();
         this.msgListenerToEJBMap.put(var1, var3);
      }

      ((List)var3).add(var2);
   }

   public void removeEJB(String var1, String var2) {
      List var3 = null;
      if (this.msgListenerToEJBMap.containsKey(var1)) {
         var3 = (List)this.msgListenerToEJBMap.get(var1);
         var3.remove(var2);
         if (var3.isEmpty()) {
            this.msgListenerToEJBMap.remove(var1);
         }
      }

   }

   private void suspendOrResumeMEF(int var1, Properties var2) throws RAInboundException {
      Debug.enter(this, "suspendOrResumeMEF( action , props )");

      try {
         Iterator var3 = this.getEndpointFactories().iterator();
         MessageEndpointFactory var4 = null;
         RAInboundException var5 = null;

         while(true) {
            do {
               do {
                  if (!var3.hasNext()) {
                     if (var5 != null) {
                        throw var5;
                     }

                     return;
                  }

                  var4 = (MessageEndpointFactory)var3.next();
               } while(var4 == null);
            } while(!(var4 instanceof SuspendableEndpointFactory));

            AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

            try {
               if (var1 == 1) {
                  this.raInstanceMgr.getAdapterLayer().suspend((SuspendableEndpointFactory)var4, var2, var6);
               } else {
                  this.raInstanceMgr.getAdapterLayer().resume((SuspendableEndpointFactory)var4, var2, var6);
               }
            } catch (ResourceException var12) {
               if (var5 == null) {
                  var5 = new RAInboundException();
               }

               Utils.consolidateException(var5, var12);
            }
         }
      } finally {
         Debug.exit(this, "suspendOrResumeMEF( action , props )");
      }
   }

   private void disconnectMEFs(RAInboundException var1) {
      Debug.enter(this, "disconnect()");

      try {
         Iterator var2 = this.getEndpointFactories().iterator();
         MessageEndpointFactory var3 = null;

         while(var2.hasNext()) {
            var3 = (MessageEndpointFactory)var2.next();
            if (var3 != null && var3 instanceof SuspendableEndpointFactory) {
               AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

               try {
                  this.raInstanceMgr.getAdapterLayer().disconnect((SuspendableEndpointFactory)var3, var4);
               } catch (ResourceException var10) {
                  Utils.consolidateException(var1, var10);
               }
            }
         }
      } finally {
         Debug.exit(this, "suspendOrResumeMEF( action , props )");
      }

   }

   public RAInstanceManager getRA() {
      return this.raInstanceMgr;
   }

   public List getEndpointFactories() {
      Vector var1 = new Vector();
      Iterator var2 = this.msgListenerToEndptFactoryMap.values().iterator();
      MessageEndpointFactory var3 = null;

      while(var2.hasNext()) {
         List var4 = (List)var2.next();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            var3 = (MessageEndpointFactory)var5.next();
            if (var3 != null) {
               var1.add(var3);
            }
         }
      }

      return var1;
   }

   public List getEndpointFactories(String var1) {
      return (List)this.msgListenerToEndptFactoryMap.get(var1);
   }

   public List getEJBs(String var1) {
      return (List)this.msgListenerToEJBMap.get(var1);
   }

   public int getAvailableInboundConnectionsCount() {
      int var1 = 0;

      try {
         var1 = this.raInstanceMgr.getRAInfo().getInboundInfos().size();
      } catch (ElementNotFoundException var3) {
      }

      return var1;
   }

   public int getSubscribedInboundConnectionsCount() {
      return this.msgListenerToEJBMap.size();
   }

   public int getActiveInboundConnectionsCount() {
      return 0;
   }

   public InboundAdapterType[] getXMLBeans(ConnectorDiagnosticImageSource var1) {
      boolean var2 = var1 != null ? var1.timedout() : false;
      int var3 = this.getAvailableInboundConnectionsCount();
      InboundAdapterType[] var4 = new InboundAdapterType[var3];
      if (var3 > 0 && !var2) {
         Iterator var5 = null;

         try {
            var5 = this.raInstanceMgr.getRAInfo().getInboundInfos().iterator();
         } catch (ElementNotFoundException var14) {
         }

         for(int var6 = 0; var5.hasNext(); ++var6) {
            InboundInfo var7 = (InboundInfo)var5.next();
            InboundAdapterType var8 = InboundAdapterType.Factory.newInstance();
            var8.setName(var7.getDisplayName());
            List var9 = (List)this.msgListenerToEJBMap.get(var7.getMsgType());
            if (var9 != null && var9.size() > 0) {
               EndpointType[] var10 = new EndpointType[var9.size()];
               Iterator var11 = var9.iterator();

               for(int var12 = 0; var11.hasNext(); ++var12) {
                  EndpointType var13 = EndpointType.Factory.newInstance();
                  var13.setMsgType(var7.getMsgType());
                  var13.setName((String)var11.next());
                  var10[var12] = var13;
               }

               var8.addNewEndpoints().setEndpointArray(var10);
            }

            var4[var6] = var8;
         }
      }

      return var4;
   }

   public void setupRuntimes(ConnectorComponentRuntimeMBeanImpl var1) {
      List var2 = null;

      try {
         var2 = this.raInstanceMgr.getRAInfo().getInboundInfos();
      } catch (ElementNotFoundException var9) {
      }

      if (var2 != null) {
         Iterator var3 = var2.iterator();
         InboundInfo var4 = null;

         while(var3.hasNext()) {
            this.isInbound = true;
            var4 = (InboundInfo)var3.next();
            String var6 = var4.getMsgType();

            try {
               Debug.deployment("Creating a new ConnectorInboundRuntimeMBeanImpl for " + var6);
               ConnectorInboundRuntimeMBeanImpl var5 = new ConnectorInboundRuntimeMBeanImpl(var6, var4.getMsgType(), var4.getActivationSpec().getActivationSpecClass(), var1, this);
               this.msgListenerToInboundRuntimeMap.put(var4.getMsgType(), var5);
               var1.addConnInboundRuntime(var5);
            } catch (Exception var8) {
               Debug.logCreateInboundRuntimeMBeanFailed(var6, var8.toString());
               Debug.deployment("Exception Creating a new ConnectorInboundRuntimeMBeanImpl for " + var6, var8);
            }
         }
      }

   }

   public String getState() {
      return this.state;
   }

   public boolean isInboundRA() {
      return this.isInbound;
   }

   public void setEndpointFactorySuspendedState(MessageEndpointFactory var1, boolean var2) {
      if (this.endptFactorySuspendStateMap.containsKey(var1)) {
         this.endptFactorySuspendStateMap.put(var1, var2);
      } else {
         throw new AssertionError("Attempt to set suspend state for endpoint factory not in map");
      }
   }
}
