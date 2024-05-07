package weblogic.jms.common;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.Request;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.spi.HostID;
import weblogic.security.HMAC;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.SubjectManager;
import weblogic.utils.StringUtils;

public final class JMSServerUtilities {
   private static final String APPSCOPED_JNDI_PREFIX = "weblogic.applications";
   private static final AuthenticatedSubject kernelID = getKernelIdentity();

   private static final AuthenticatedSubject getKernelIdentity() {
      try {
         return (AuthenticatedSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public static Context getLocalJNDIContext() {
      try {
         javaURLContextFactory var0 = new javaURLContextFactory();
         return (Context)var0.getObjectInstance((Object)null, (Name)null, (Context)null, (Hashtable)null);
      } catch (NamingException var1) {
         return null;
      }
   }

   public static void pushLocalJNDIContext(Context var0) {
      javaURLContextFactory.pushContext(var0);
   }

   public static void popLocalJNDIContext() {
      javaURLContextFactory.popContext();
   }

   public static HostID getHostId(Context var0, DestinationImpl var1) {
      try {
         DispatcherId var2 = var1.getDispatcherId();
         String var3 = JMSDispatcherManager.getDispatcherJNDIName(var2);
         DispatcherWrapper var4 = (DispatcherWrapper)var0.lookup(var3);
         return RemoteHelper.getHostID(var4.getRemoteDispatcher());
      } catch (NamingException var5) {
         return null;
      }
   }

   public static String getAppscopedGlobalJNDIName(String var0, String var1, NamedEntityBean var2) {
      return var0 != null && var1 != null && var2 != null ? new String("weblogic.applications" + var0 + "." + var1 + "#" + var2.getName()) : null;
   }

   public static ConnectionFactory getXAConnectionFactory() {
      return JMSService.getJMSService().getDefaultConnectionFactory("DefaultXAConnectionFactory").getJMSConnectionFactory();
   }

   public static ConnectionFactory getXAConnectionFactory0() {
      return JMSService.getJMSService().getDefaultConnectionFactory("DefaultXAConnectionFactory0").getJMSConnectionFactory();
   }

   public static ConnectionFactory getXAConnectionFactory1() {
      return JMSService.getJMSService().getDefaultConnectionFactory("DefaultXAConnectionFactory1").getJMSConnectionFactory();
   }

   public static ConnectionFactory getXAConnectionFactory2() {
      return JMSService.getJMSService().getDefaultConnectionFactory("DefaultXAConnectionFactory2").getJMSConnectionFactory();
   }

   public static ConnectionFactory getConnectionFactory() {
      return JMSService.getJMSService().getDefaultConnectionFactory("DefaultConnectionFactory").getJMSConnectionFactory();
   }

   public static BEDestinationImpl findBEDestinationByJNDIName(String var0) {
      BackEnd[] var1 = JMSService.getJMSService().getBEDeployer().getBackEnds();

      int var4;
      for(int var2 = 0; var2 < var1.length; ++var2) {
         BEDestinationImpl[] var3 = var1[var2].getBEDestinations();

         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] != null && var0.equals(var3[var4].getJNDIName())) {
               return var3[var4];
            }

            if (var3[var4] != null && var0.equals(var3[var4].getLocalJNDIName())) {
               return var3[var4];
            }
         }
      }

      BackEnd[] var13 = var1;
      int var14 = var1.length;

      for(var4 = 0; var4 < var14; ++var4) {
         BackEnd var5 = var13[var4];
         BEDestinationImpl[] var6 = var5.getBEDestinations();
         BEDestinationImpl[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BEDestinationImpl var10 = var7[var9];
            String var11 = var10.getJNDIName();
            int var12 = var11 != null ? var11.lastIndexOf(64) : -1;
            if (var12 != -1 && var0.equals(var11.substring(var12 + 1))) {
               return var10;
            }
         }
      }

      return null;
   }

   public static String transformJNDIName(String var0) {
      try {
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("In JMSServerUtilities.transformJNDIName with jndiName: " + var0);
         }

         String var1 = getCurrentApplicationContext().getApplicationId();
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("In JMSServerUtilities.transformJNDIName. Found app name: " + var1);
         }

         var0 = transformJNDIName(var0, var1);
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("In JMSServerUtilities.transformJNDIName. Calculated final jndiName: " + var0);
         }
      } catch (IllegalStateException var2) {
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("In JMSServerUtilities.transformJNDIName. Didn't get app name due to exception: " + var2.toString());
         }
      }

      return var0;
   }

   public static String transformJNDIName(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         return var0.indexOf("${APPNAME}") != -1 ? StringUtils.replaceGlobal(var0, "${APPNAME}", var1) : var0;
      }
   }

   public static ApplicationContext getCurrentApplicationContext() throws IllegalStateException {
      ApplicationContextInternal var0 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      if (var0 == null) {
         throw new IllegalStateException("Attempt to access current application context from a component which has no application context");
      } else {
         return var0;
      }
   }

   public static boolean verifySignature(byte[] var0, byte[] var1, byte[] var2) {
      return HMAC.verify(var0, var1, var2, SerializedSystemIni.getSalt());
   }

   public static byte[] digest(byte[] var0, byte[] var1) {
      return HMAC.digest(var0, var1, SerializedSystemIni.getSalt());
   }

   public static byte[] generateSecret(String var0) {
      byte[] var1 = SerializedSystemIni.getEncryptedSecretKey();
      byte[] var2 = var0.getBytes();
      byte[] var3 = new byte[var1.length + var2.length];
      System.arraycopy(var1, 0, var3, 0, var1.length);
      System.arraycopy(var2, 0, var3, var1.length, var2.length);
      return var3;
   }

   public static void anonDispatchNoReply(Request var0, JMSDispatcher var1) throws javax.jms.JMSException {
      AuthenticatedSubject var2 = null;

      try {
         if (!var1.isLocal()) {
            AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(kernelID);
            if (SecurityServiceManager.isKernelIdentity(var3) || SecurityServiceManager.isServerIdentity(var3)) {
               var2 = SubjectUtils.getAnonymousSubject();
            }
         }

         if (var2 != null) {
            SubjectManager.getSubjectManager().pushSubject(kernelID, var2);
         }

         var1.dispatchNoReply(var0);
      } finally {
         if (var2 != null) {
            SubjectManager.getSubjectManager().popSubject(kernelID);
         }

      }

   }
}
