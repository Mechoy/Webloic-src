package weblogic.jms.saf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.SAFLoginContextBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.jms.forwarder.Forwarder;
import weblogic.jms.forwarder.ReplyHandler;
import weblogic.jms.forwarder.RuntimeHandler;
import weblogic.jndi.ClientEnvironmentFactory;
import weblogic.management.EncryptionHelper;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.kernel.Queue;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.t3.srvr.T3Srvr;
import weblogic.work.WorkManager;

public class RemoteContextAgent {
   private static final AbstractSubject kernelID = getKernelIdentity();
   private final String rcBeanFullyQualifiedName;
   private final SAFRemoteContextBean rcBean;
   private static final HashMap RC_ATTRIBUTES = new HashMap();
   private static final HashMap LC_ATTRIBUTES = new HashMap();
   private GenericBeanListener rcChangeListener;
   private GenericBeanListener lcChangeListener;
   Forwarder forwarder;
   ReplyHandler replyHandler;
   private boolean isInitialized;
   private static AuthenticatedSubject KERNEL_ID;
   private static RuntimeAccess runtimeAccess;
   private ServerStateChangeListener stateChangeListener;

   private static final AbstractSubject getKernelIdentity() {
      try {
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public RemoteContextAgent(String var1, SAFRemoteContextBean var2, ReplyHandler var3, ClientEnvironmentFactory var4) {
      this.rcBeanFullyQualifiedName = var1;
      this.rcBean = var2;
      this.replyHandler = var3;
      this.forwarder = new Forwarder(var2 == null, this.replyHandler, var4);
      this.initializeListeners();
      if (T3Srvr.getT3Srvr().getRunState() == 2) {
         this.forwarder.start();
      } else {
         this.stateChangeListener = new ServerStateChangeListener();
         runtimeAccess.getServerRuntime().addPropertyChangeListener(this.stateChangeListener);
      }

   }

   private void initializeListeners() {
      if (this.rcBean == null) {
         this.isInitialized = true;
      } else {
         this.rcChangeListener = new GenericBeanListener((DescriptorBean)this.rcBean, this, RC_ATTRIBUTES, (Map)null);
         SAFLoginContextBean var1 = this.rcBean.getSAFLoginContext();
         this.lcChangeListener = new GenericBeanListener((DescriptorBean)var1, this, LC_ATTRIBUTES, (Map)null);

         try {
            this.rcChangeListener.initialize();
            this.lcChangeListener.initialize();
            this.isInitialized = true;
         } catch (ManagementException var3) {
            throw new AssertionError(var3);
         }
      }
   }

   public String toString() {
      return this.rcBeanFullyQualifiedName;
   }

   public String getRcBeanFullyQualifiedName() {
      return this.rcBeanFullyQualifiedName;
   }

   public SAFRemoteContextBean getRcBean() {
      return this.rcBean;
   }

   public boolean isLocalServerContext() {
      return this.rcBean == null;
   }

   public void addForwarder(PersistentStoreXA var1, WorkManager var2, RuntimeHandler var3, Queue var4, String var5, int var6) {
      this.forwarder.addSubforwarder(var1, var2, var3, var4, var5, var6);
   }

   public void addForwarder(PersistentStoreXA var1, WorkManager var2, RuntimeHandler var3, Queue var4, String var5, int var6, int var7) {
      this.forwarder.addSubforwarder(var1, var2, var3, var4, var5, var6, var7);
   }

   public void removeForwarder(Queue var1, String var2) {
      this.forwarder.removeSubforwarder(var1);
   }

   public void setCompressionThreshold(int var1) {
      this.forwarder.setCompressionThreshold(var1);
   }

   public void setReplyToSAFRemoteContextName(String var1) {
      this.replyHandler.setReplyToSAFRemoteContextName(var1);
   }

   public void setLoginURL(String var1) {
      if (this.isInitialized) {
         this.forwarder.stop();
      }

      this.forwarder.setLoginURL(var1);
      if (this.isInitialized) {
         this.forwarder.start();
      }

   }

   public void setUsername(String var1) {
      if (this.isInitialized) {
         this.forwarder.stop();
      }

      this.forwarder.setUsername(var1);
      if (this.isInitialized) {
         this.forwarder.start();
      }

   }

   public void setPassword(String var1) {
      if (this.isInitialized) {
         this.forwarder.stop();
      }

      this.forwarder.setPassword(var1);
      if (this.isInitialized) {
         this.forwarder.start();
      }

   }

   public void setPasswordEncrypted(byte[] var1) {
      if (this.isInitialized) {
         this.forwarder.stop();
      }

      this.forwarder.setPassword(EncryptionHelper.decryptString(var1, (AuthenticatedSubject)kernelID));
      if (this.isInitialized) {
         this.forwarder.start();
      }

   }

   public void setRetryDelayBase(long var1) {
      this.forwarder.setRetryDelayBase(var1);
   }

   public void setRetryDelayMaximum(long var1) {
      this.forwarder.setRetryDelayMaximum(var1);
   }

   public void setRetryDelayMultiplier(double var1) {
      this.forwarder.setRetryDelayMultiplier(var1);
   }

   public void setWindowSize(int var1) {
      this.forwarder.setWindowSize(var1);
   }

   public void setWindowInterval(long var1) {
      this.forwarder.setWindowInterval(var1);
   }

   static {
      RC_ATTRIBUTES.put("CompressionThreshold", Integer.TYPE);
      RC_ATTRIBUTES.put("ReplyToSAFRemoteContextName", String.class);
      LC_ATTRIBUTES.put("LoginURL", String.class);
      LC_ATTRIBUTES.put("Username", String.class);
      LC_ATTRIBUTES.put("Password", String.class);
      LC_ATTRIBUTES.put("PasswordEncrypted", byte[].class);
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      runtimeAccess = ManagementService.getRuntimeAccess(KERNEL_ID);
   }

   class ServerStateChangeListener implements PropertyChangeListener {
      public void propertyChange(PropertyChangeEvent var1) {
         if ("State".equals(var1.getPropertyName()) && "RUNNING".equals((String)var1.getNewValue())) {
            RemoteContextAgent.this.forwarder.start();
         }

      }
   }
}
