package weblogic.jms.deployer;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.frontend.FEConnectionFactory;
import weblogic.jms.frontend.FrontEnd;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class FEDeployer implements DeployerConstants {
   private Object shutdownLock;
   private boolean initialized;
   private HashMap defaultConnectionFactories;
   private FrontEnd frontEnd;
   private JMSService jmsService;

   public FEDeployer(JMSService var1) throws ManagementException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Constructing JMS FEDeployer");
      }

      this.jmsService = var1;
      this.allocate();
   }

   public void initialize(FrontEnd var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Initializing JMS FEDeployer");
      }

      this.frontEnd = var1;
      this.initializeConnectionFactories();
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (ManagementService.getRuntimeAccess(var2).getServer().isJMSDefaultConnectionFactoriesEnabled()) {
         this.deployDefaultConnectionFactories();
      }

   }

   public void shutdown() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Shutting down JMS FEDeployer");
      }

      synchronized(this.shutdownLock) {
         try {
            this.undeployDefaultConnectionFactories();
         } catch (JMSException var4) {
         }

      }
   }

   public void allocate() {
      this.defaultConnectionFactories = new HashMap();
   }

   public FrontEnd getFrontEnd() {
      return this.frontEnd;
   }

   public void setFrontEnd(FrontEnd var1) {
      this.frontEnd = var1;
   }

   public Object getShutdownLock() {
      synchronized(this.shutdownLock) {
         return this.shutdownLock;
      }
   }

   public void setShutdownLock(Object var1) {
      this.shutdownLock = var1;
   }

   public void initializeConnectionFactories() {
      FEConnectionFactory var1 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[0][0], DEFAULT_FACTORY_NAMES[0][1], false, false, "All");
      FEConnectionFactory var2 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[1][0], DEFAULT_FACTORY_NAMES[1][1], false, true, "All");
      FEConnectionFactory var3 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[2][0], DEFAULT_FACTORY_NAMES[2][1], false, true, "All", false, false);
      FEConnectionFactory var4 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[3][0], DEFAULT_FACTORY_NAMES[3][1], false, true, "All", false, true);
      FEConnectionFactory var5 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[4][0], DEFAULT_FACTORY_NAMES[4][1], false, true, "All", true, false);
      FEConnectionFactory var6 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[5][0], DEFAULT_FACTORY_NAMES[5][1], false, true, "Previous");
      FEConnectionFactory var7 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[6][0], DEFAULT_FACTORY_NAMES[6][1], true, true, "Previous");
      FEConnectionFactory var8 = new FEConnectionFactory(this.frontEnd, DEFAULT_FACTORY_NAMES[7][0], DEFAULT_FACTORY_NAMES[7][1], true, true, "Previous");
      synchronized(this.shutdownLock) {
         if (!this.jmsService.isShutdown()) {
            this.defaultConnectionFactories.put("DefaultConnectionFactory", var1);
            this.defaultConnectionFactories.put("DefaultXAConnectionFactory", var2);
            this.defaultConnectionFactories.put("DefaultXAConnectionFactory0", var3);
            this.defaultConnectionFactories.put("DefaultXAConnectionFactory1", var4);
            this.defaultConnectionFactories.put("DefaultXAConnectionFactory2", var5);
            this.defaultConnectionFactories.put("MessageDrivenBeanConnectionFactory", var6);
            this.defaultConnectionFactories.put("QueueConnectionFactory", var7);
            this.defaultConnectionFactories.put("TopicConnectionFactory", var8);
         }
      }
   }

   public void deployDefaultConnectionFactories() {
      int var1 = 0;
      Iterator var2;
      synchronized(this.shutdownLock) {
         var2 = ((HashMap)this.defaultConnectionFactories.clone()).values().iterator();
      }

      try {
         while(var2.hasNext()) {
            FEConnectionFactory var3 = (FEConnectionFactory)var2.next();
            if (this.jmsService.isActive()) {
               var3.bind();
            }

            this.frontEnd.connectionFactoryAdd(var3);
            ++var1;
         }
      } catch (JMSException var7) {
         JMSLogger.logConnFactoryFailed(this.jmsService.getMbeanName(), var7);
      }

      JMSLogger.logCntDefCFactory(var1);
   }

   public void undeployDefaultConnectionFactories() throws JMSException {
      Iterator var1;
      synchronized(this.shutdownLock) {
         var1 = ((HashMap)this.defaultConnectionFactories.clone()).values().iterator();
      }

      while(var1.hasNext()) {
         try {
            FEConnectionFactory var2 = (FEConnectionFactory)var1.next();
            var2.shutdown();
            synchronized(this.shutdownLock) {
               this.defaultConnectionFactories.remove(var2);
            }

            this.frontEnd.connectionFactoryRemove(var2);
         } catch (Throwable var6) {
         }
      }

      JMSLogger.logCntDefCFactoryUndeployed(this.defaultConnectionFactories.size());
   }

   public FEConnectionFactory[] getDefaultConnectionFactories() {
      synchronized(this.shutdownLock) {
         FEConnectionFactory[] var2 = new FEConnectionFactory[this.defaultConnectionFactories.size()];
         return (FEConnectionFactory[])((FEConnectionFactory[])this.defaultConnectionFactories.values().toArray(var2));
      }
   }

   public FEConnectionFactory getDefaultConnectionFactory(String var1) {
      return (FEConnectionFactory)this.defaultConnectionFactories.get(var1);
   }
}
