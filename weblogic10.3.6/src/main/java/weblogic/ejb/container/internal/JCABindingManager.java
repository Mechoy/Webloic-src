package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.resource.spi.ActivationSpec;
import weblogic.connector.external.EndpointActivationException;
import weblogic.connector.external.EndpointActivationUtils;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.ActivationConfigPropertyBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.bean.BeanInitializer;
import weblogic.utils.bean.ConversionException;

public final class JCABindingManager extends MDConnectionManager {
   private MessageDrivenManagerIntf mdManager;
   private Context environmentContext;
   private MessageEndpointFactoryImpl factory;
   private ActivationSpec activationSpec;

   public JCABindingManager(MessageDrivenBeanInfo var1, Context var2, MessageDrivenEJBRuntimeMBean var3) throws WLDeploymentException {
      super(var1, var2, var3);
      this.mdManager = (MessageDrivenManagerIntf)this.info.getBeanManager();
      this.environmentContext = var2;
   }

   protected void disconnect(boolean var1) throws EndpointActivationException {
      if (this.factory != null) {
         this.factory.setReady(false);
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         if (debugLogger.isDebugEnabled()) {
            debug("activationSpec=" + this.activationSpec);
         }

         if (this.activationSpec == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("The activationSpec is null, deActivation is skipped");
            }
         } else {
            EndpointActivationUtils.accessor.deActivateEndpoint(this.info.getDisplayName(), this.mdManager.getResourceAdapterJndiName(), this.info.getMessagingTypeInterfaceName(), this.activationSpec, this.factory, this.runtimeMBean);
         }

         this.setState(1);
      } catch (EndpointActivationException var3) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED to deactivate endpoint: " + var3);
            var3.printStackTrace(System.err);
         }

         throw var3;
      }
   }

   protected void connect() throws EndpointActivationException {
      assert this.getState() != 2;

      ActivationConfigBean var1 = this.info.getActivationConfigBean();

      try {
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         ++this.reconnectionCount;
         this.activationSpec = (ActivationSpec)EndpointActivationUtils.accessor.getActivationSpec(this.mdManager.getResourceAdapterJndiName(), this.info.getMessagingTypeInterfaceName());
         if (this.activationSpec == null) {
            throw new RuntimeException("Problem during connect, activationSpec is null");
         }

         this.setActivationSpec(this.activationSpec, var1);
         if (debugLogger.isDebugEnabled()) {
            debug("activationSpec=" + this.activationSpec);
         }

         this.factory = new MessageEndpointFactoryImpl(this.info);
         if (this.factory != null) {
            this.factory.setReady(true);
         }

         EndpointActivationUtils.accessor.activateEndpoint(this.info.getDisplayName(), this.mdManager.getResourceAdapterJndiName(), this.info.getMessagingTypeInterfaceName(), this.activationSpec, this.factory, this.runtimeMBean);
         if (this.state != 3) {
            this.setState(2);
         }

         this.runtimeMBean.setMDBStatus("running");
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         if (debugLogger.isDebugEnabled()) {
            debug("\n\n +++++++++++  Got Connection ++++++++++\n");
         }
      } catch (EndpointActivationException var8) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED to activate endpoint: " + var8);
            var8.printStackTrace(System.err);
         }

         throw var8;
      } catch (RuntimeException var9) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED to activate endpoint with: " + var9);
            var9.printStackTrace(System.err);
         }

         throw var9;
      } finally {
         this.runtimeMBean.setJMSConnectionAlive(this.getState() == 2);
         if (this.getState() == 2) {
            this.runtimeMBean.setConnectionStatus("Connected");
         } else {
            this.runtimeMBean.setConnectionStatus("re-connecting");
         }

      }

   }

   protected void logException(Exception var1) {
      if (var1 instanceof EndpointActivationException) {
         EJBLogger.logMDBUnableToConnectToJCA(this.info.getEJBName(), this.mdManager.getResourceAdapterJndiName(), ((EndpointActivationException)var1).getMessage());
      } else if (var1 instanceof RuntimeException) {
         EJBLogger.logMDBUnableToConnectToJCA(this.info.getEJBName(), this.mdManager.getResourceAdapterJndiName(), var1.getMessage() == null ? StackTraceUtils.throwable2StackTrace(var1) : var1.getMessage());
      }

   }

   private void setActivationSpec(ActivationSpec var1, ActivationConfigBean var2) {
      if (var2 != null) {
         Hashtable var3 = new Hashtable();
         ActivationConfigPropertyBean[] var4 = var2.getActivationConfigProperties();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5].getActivationConfigPropertyName();
            String var7 = var4[var5].getActivationConfigPropertyValue();
            if ((!"MESSAGESELECTOR".equalsIgnoreCase(var6) || var7 != "") && var6 != null && var7 != null) {
               var3.put(var6, var7);
            }
         }

         BeanInitializer var9 = new BeanInitializer();

         try {
            var9.initializeBean(var1, var3);
         } catch (ConversionException var8) {
            Debug.say("Fail to set ActivationSpec. " + var8.getMessage());
         }

      }
   }

   public void onRAUndeploy() {
      this.setState(6);
      ClassLoader var1 = this.info.getClassLoader();
      Thread var2 = Thread.currentThread();
      ClassLoader var3 = var2.getContextClassLoader();
      var2.setContextClassLoader(var1);
      this.activationSpec = null;

      try {
         this.scheduleReconnection();
      } finally {
         if (var3 != null) {
            var2.setContextClassLoader(var3);
         }

      }

   }

   public synchronized void signalBackgroundThreads() {
      Debug.say("This operation is not supported for JCA based MDB");
   }

   public synchronized boolean suspend(boolean var1) {
      try {
         EndpointActivationUtils.accessor.suspendInbound(this.info.getResourceAdapterJndiName(), this.factory, (Properties)null);
         this.factory.setReady(false);
         this.runtimeMBean.setMDBStatus("Suspended at " + new Date(System.currentTimeMillis()) + " by the user.");
         return true;
      } catch (EndpointActivationException var3) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED to suspend endpoint: " + var3);
            var3.printStackTrace(System.err);
         }

         throw new RuntimeException(var3);
      }
   }

   public synchronized boolean resume(boolean var1) {
      try {
         EndpointActivationUtils.accessor.resumeInbound(this.info.getResourceAdapterJndiName(), this.factory, (Properties)null);
         this.factory.setReady(true);
         this.runtimeMBean.setMDBStatus("running");
         return true;
      } catch (EndpointActivationException var3) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED to suspend endpoint: " + var3);
            var3.printStackTrace(System.err);
         }

         throw new RuntimeException(var3);
      }
   }

   public void shutdown() {
      Debug.say("This operation is not supported for JCA based MDB");
   }

   protected List getMessagingTypeMethods() {
      ArrayList var1 = new ArrayList();
      Class var2 = this.info.getMessagingTypeInterfaceClass();
      if (var2 != null) {
         Method[] var3 = var2.getMethods();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1.add(var3[var4]);
         }
      }

      return var1;
   }

   private void testRA() {
      Debug.say("isDeliveryTransacted started");
      boolean var1 = false;

      Method var3;
      for(Iterator var2 = this.getMessagingTypeMethods().iterator(); var2.hasNext(); Debug.say("isDeliveryTransacted for " + var3.getName() + " = " + var1)) {
         var3 = (Method)var2.next();

         try {
            var1 = this.factory.isDeliveryTransacted(var3);
         } catch (NoSuchMethodException var5) {
            Debug.say("NoSuchMethodException= " + var5);
         }
      }

      Debug.say("isDeliveryTransacted ended");
   }

   private static void debug(String var0) {
      debugLogger.debug("[JCABindingManager] " + var0);
   }
}
