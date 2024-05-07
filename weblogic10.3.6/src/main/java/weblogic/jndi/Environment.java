package weblogic.jndi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jndi.internal.JNDIEnvironment;
import weblogic.jndi.internal.SSL.SSLProxy;
import weblogic.jndi.spi.EnvironmentManager;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.ServerURL;
import weblogic.rmi.spi.HostID;
import weblogic.security.SSL.TrustManager;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;

public final class Environment implements Externalizable, ClientEnvironment {
   private static final long serialVersionUID = 6539137427459606294L;
   public static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   public static final String LOCAL_URL = "local://";
   public static final String LOCAL_URL_PROTOCOL = "t3";
   private static final boolean cantReadSystemProperties = KernelStatus.isApplet();
   private transient boolean copyOnWrite;
   private transient Context initialContext = null;
   private Hashtable env;
   private boolean enableDefaultUser = false;
   private static final SSLProxy sslProxy = JNDIEnvironment.getJNDIEnvironment().getSSLProxy();
   private static Class factoryReference = WLInitialContextFactory.class;
   private UserInfo securityUser = null;
   private AuthenticatedSubject subject;
   private static final String TRUE;
   private static final String FALSE;

   public Environment() {
      this.env = new Hashtable();
      this.copyOnWrite = false;
   }

   public Environment(Hashtable var1) {
      this.env = var1;
      if (var1 != null) {
         try {
            if (var1.get("weblogic.jndi.ssl.root.ca.fingerprints") != null) {
               Object var2 = var1.get("weblogic.jndi.ssl.root.ca.fingerprints");
               if (var2 instanceof String) {
                  this.setSSLRootCAFingerprints((String)var2);
               } else if (var2 instanceof byte[][]) {
                  this.setSSLRootCAFingerprints((byte[][])((byte[][])var2));
               }
            }

            if (var1.get("weblogic.jndi.ssl.server.name") != null) {
               this.setSSLServerName((String)var1.get("weblogic.jndi.ssl.server.name"));
            }

            if (var1.get("weblogic.jndi.ssl.client.certificate") != null) {
               this.setSSLClientCertificate((InputStream[])((InputStream[])var1.get("weblogic.jndi.ssl.client.certificate")));
            }

            if (var1.get("weblogic.jndi.enableDefaultUser") != null) {
               this.setEnableDefaultUser(true);
            }
         } catch (ClassCastException var3) {
         }
      }

      this.copyOnWrite = true;
   }

   private SSLProxy findOrCreateSSLProxy() {
      return sslProxy;
   }

   public Hashtable getProperties() {
      return this.env;
   }

   public final Context getInitialContext() throws NamingException {
      return this.getInitialContext(true);
   }

   public final Context getInitialContext(boolean var1) throws NamingException {
      Context var2 = this.initialContext;
      if (this.initialContext == null) {
         this.initialContext = this.createInitialContext();
         return this.initialContext;
      } else {
         return var1 ? var2 : this.createInitialContext();
      }
   }

   private Context createInitialContext() throws NamingException {
      String var1 = this.getInitialContextFactory();
      return (Context)(var1.equals("weblogic.jndi.WLInitialContextFactory") ? this.getContext((String)null) : new InitialContext(this.env));
   }

   public final Context getContext() throws NamingException {
      return this.getInitialContext();
   }

   public final Remote getInitialReference(Class var1) throws NamingException {
      String var3 = this.getString("java.naming.provider.url");
      String var2;
      if (var3 != null) {
         try {
            var2 = var3.substring(0, var3.indexOf(58));
         } catch (IndexOutOfBoundsException var5) {
            throw new NamingException(var5.getMessage());
         }
      } else if (KernelStatus.isServer()) {
         var2 = "local";
      } else {
         var2 = ServerURL.DEFAULT_URL.getProtocol();
      }

      return EnvironmentManager.getInstance(var2).getInitialReference(this, var1);
   }

   public final void setProviderChannel(String var1) {
      if (var1 != null) {
         this.setProperty("weblogic.jndi.provider.channel", var1);
      }

   }

   public final String getProviderChannel() {
      return this.getString("weblogic.jndi.provider.channel");
   }

   public final Context getContext(String var1) throws NamingException {
      return this.getContext(var1, (HostID)null);
   }

   public final Context getContext(String var1, HostID var2) throws NamingException {
      String var4 = this.getString("java.naming.provider.url");
      String var3;
      if (var4 != null) {
         try {
            var3 = var4.substring(0, var4.indexOf(58));
         } catch (IndexOutOfBoundsException var6) {
            throw new NamingException(var6.getMessage());
         }
      } else if (KernelStatus.isServer()) {
         var3 = "local";
      } else {
         var3 = ServerURL.DEFAULT_URL.getProtocol();
      }

      return EnvironmentManager.getInstance(var3).getInitialContext(this, var1, var2);
   }

   public String getInitialContextFactory() throws IllegalArgumentException {
      String var1 = this.getString("java.naming.factory.initial");
      return var1 != null ? var1 : "weblogic.jndi.WLInitialContextFactory";
   }

   public final void setInitialContextFactory(String var1) {
      this.setProperty("java.naming.factory.initial", var1);
   }

   public final String getProviderUrl() {
      String var1 = this.getString("java.naming.provider.url");
      if (var1 != null) {
         return var1;
      } else {
         return KernelStatus.isServer() ? "local://" : ServerURL.DEFAULT_URL.toString();
      }
   }

   public final void setProviderUrl(String var1) {
      if (var1 != null) {
         this.setProperty("java.naming.provider.url", var1);
      }

   }

   public final void setProviderURL(String var1) {
      this.setProviderUrl(var1);
   }

   public final Hashtable getDelegateEnvironment() throws IllegalArgumentException {
      try {
         Hashtable var1 = (Hashtable)this.getProperty("weblogic.jndi.delegate.environment");
         if (var1 == null) {
            var1 = (Hashtable)this.getObsoleteProperty("java.naming.provider.delegate.environment", "WLContext.DELEGATE_ENVIRONMENT");
         }

         return var1;
      } catch (ClassCastException var2) {
         throw new IllegalArgumentException("Value of 'weblogic.jndi.delegate.environment' is not a Hashtable as expected");
      }
   }

   public final void setDelegateEnvironment(Hashtable var1) {
      this.setProperty("weblogic.jndi.delegate.environment", var1);
   }

   public final boolean getForceResolveDNSName() throws IllegalArgumentException {
      return this.getBoolean("weblogic.jndi.forceResolveDNSName", false);
   }

   public final String getSecurityPrincipal() throws IllegalArgumentException {
      return (String)this.getPropertyFromEnv("java.naming.security.principal");
   }

   public final void setSecurityPrincipal(String var1) {
      this.setProperty("java.naming.security.principal", var1);
   }

   public final Object getSecurityCredentials() throws IllegalArgumentException {
      return this.getPropertyFromEnv("java.naming.security.credentials");
   }

   public final void setSecurityCredentials(Object var1) {
      this.setProperty("java.naming.security.credentials", var1);
   }

   public final UserInfo getSecurityUser() throws IllegalArgumentException {
      if (this.securityUser != null) {
         return this.securityUser;
      } else {
         Object var1 = this.getSecurityCredentials();
         if (var1 instanceof UserInfo) {
            this.securityUser = (UserInfo)var1;
         } else {
            String var2;
            if (var1 instanceof String) {
               var2 = this.getSecurityPrincipal();
               if (var2 == null) {
                  throw new IllegalArgumentException("The 'java.naming.security.principal' property has not been specified");
               }

               this.securityUser = new DefaultUserInfoImpl(var2, var1);
            } else if (var1 == null) {
               var2 = this.getSecurityPrincipal();
               if (var2 == null) {
                  return this.securityUser;
               }

               this.securityUser = new DefaultUserInfoImpl(var2, var1);
            } else if (var1 != null) {
               throw new IllegalArgumentException("The 'java.naming.security.credentials' property must be either a password String or an instance of UserInfo.");
            }
         }

         return this.securityUser;
      }
   }

   public final void setSecurityUser(UserInfo var1) {
      this.securityUser = var1;
      this.setSecurityCredentials(var1);
   }

   public void setSecuritySubject(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   public AuthenticatedSubject getSecuritySubject() {
      return this.subject;
   }

   public AbstractSubject getSubject() {
      return this.subject;
   }

   public final boolean getCreateIntermediateContexts() throws IllegalArgumentException {
      return this.getBoolean("weblogic.jndi.createIntermediateContexts", false);
   }

   public final void setCreateIntermediateContexts(boolean var1) throws IllegalArgumentException {
      this.setBoolean("weblogic.jndi.createIntermediateContexts", var1);
   }

   public final boolean getReplicateBindings() throws IllegalArgumentException {
      return this.getBoolean("weblogic.jndi.replicateBindings", true);
   }

   public final void setReplicateBindings(boolean var1) {
      this.setBoolean("weblogic.jndi.replicateBindings", var1);
   }

   public final boolean getPinToPrimaryServer() throws IllegalArgumentException {
      return this.getReplicateBindings() && this.getProviderIdentity() == null ? this.getBoolean("weblogic.jndi.pinToPrimaryServer", false) : true;
   }

   public final void setPinToPrimaryServer(boolean var1) {
      this.setBoolean("weblogic.jndi.pinToPrimaryServer", var1);
   }

   public final void setEnableServerAffinity(boolean var1) {
      this.setBoolean("weblogic.jndi.enableServerAffinity", var1);
   }

   public final boolean getEnableServerAffinity() {
      return this.getBoolean("weblogic.jndi.enableServerAffinity", false);
   }

   public final void setRequestTimeout(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Cannot have -ve timeout");
      } else {
         this.env.put("weblogic.jndi.requestTimeout", new Long(var1));
      }
   }

   public final long getRequestTimeout() {
      if (this.env == null) {
         return 0L;
      } else {
         Object var1 = this.env.get("weblogic.jndi.requestTimeout");
         long var2;
         if (var1 == null) {
            var2 = 0L;
         } else if (var1 instanceof String) {
            var2 = Long.parseLong((String)var1);
         } else {
            var2 = (Long)var1;
         }

         return var2;
      }
   }

   public final void setRMIClientTimeout(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Cannot have -ve timeout");
      } else {
         this.env.put("weblogic.rmi.clientTimeout", new Long(var1));
      }
   }

   public final long getRMIClientTimeout() {
      if (this.env == null) {
         return 0L;
      } else {
         Object var1 = this.env.get("weblogic.rmi.clientTimeout");
         long var2;
         if (var1 == null) {
            var2 = 0L;
         } else if (var1 instanceof String) {
            var2 = Long.parseLong((String)var1);
         } else {
            var2 = (Long)var1;
         }

         return var2;
      }
   }

   public final String getString(String var1) throws IllegalArgumentException {
      try {
         return (String)this.getProperty(var1);
      } catch (ClassCastException var3) {
         throw new IllegalArgumentException("Value of '" + var1 + "' is not a String as expected");
      }
   }

   public final boolean getBoolean(String var1, boolean var2) throws IllegalArgumentException {
      String var3 = this.getString(var1);
      if (var3 == null) {
         return var2;
      } else if (var3.equalsIgnoreCase(TRUE)) {
         return true;
      } else if (var3.equalsIgnoreCase(FALSE)) {
         return false;
      } else {
         throw new IllegalArgumentException("Value of '" + var1 + "' is not \"" + TRUE + "\" or \"" + FALSE + "\" as expected");
      }
   }

   public final void setSSLRootCAFingerprints(String var1) {
      this.findOrCreateSSLProxy().setRootCAfingerprints(var1);
   }

   public final void setSSLRootCAFingerprints(byte[][] var1) {
      this.findOrCreateSSLProxy().setRootCAfingerprints(var1);
   }

   public final byte[][] getSSLRootCAFingerprints() {
      return this.findOrCreateSSLProxy().getRootCAfingerprints();
   }

   public final void setSSLServerName(String var1) {
      this.findOrCreateSSLProxy().setExpectedName(var1);
   }

   public final String getSSLServerName() {
      return this.findOrCreateSSLProxy().getExpectedName();
   }

   public final Object getSSLClientCertificate() throws IOException {
      return this.findOrCreateSSLProxy().getSSLClientCertificate();
   }

   public final void setSSLClientCertificate(InputStream[] var1) {
      this.findOrCreateSSLProxy().setSSLClientCertificate(var1);
   }

   public final void setSSLClientKeyPassword(String var1) {
      this.findOrCreateSSLProxy().setSSLClientKeyPassword(var1);
   }

   /** @deprecated */
   public final Object getSSLClientKeyPassword() {
      return this.findOrCreateSSLProxy().getSSLClientKeyPassword();
   }

   public final void setSSLClientTrustManager(TrustManager var1) {
      this.findOrCreateSSLProxy().setTrustManager(var1);
   }

   public final TrustManager getSSLClientTrustManager() {
      return this.findOrCreateSSLProxy().getTrustManager();
   }

   public final void setBoolean(String var1, boolean var2) {
      this.setProperty(var1, var2 ? TRUE : FALSE);
   }

   public final Object getProperty(String var1) {
      Object var2 = null;
      if (this.env != null) {
         var2 = this.env.get(var1);
      }

      if (var2 == null && !cantReadSystemProperties) {
         try {
            var2 = System.getProperty(var1);
         } catch (SecurityException var4) {
         }
      }

      return var2;
   }

   public final Object getPropertyFromEnv(String var1) {
      return this.env != null ? this.env.get(var1) : null;
   }

   private final Object getObsoleteProperty(String var1, String var2) {
      Object var3 = this.getPropertyFromEnv(var1);
      if (var3 != null) {
         JNDILogger.logObsoleteProp(var1, var2);
      }

      return var3;
   }

   public final Object setProperty(String var1, Object var2) {
      if (this.env == null && var2 == null) {
         return null;
      } else {
         this.initialContext = null;
         if (this.copyOnWrite) {
            Hashtable var3 = this.env;
            this.env = new Hashtable();
            if (var3 != null) {
               Enumeration var4 = var3.keys();

               while(var4.hasMoreElements()) {
                  Object var5 = var4.nextElement();
                  this.env.put(var5, var3.get(var5));
               }
            }

            this.copyOnWrite = false;
         }

         if (var2 == null) {
            return this.env.remove(var1);
         } else {
            Object var6 = this.env.put(var1, var2);
            switch (var1.length()) {
               case 29:
                  if (var1.equals("weblogic.jndi.ssl.server.name") && var2 instanceof String) {
                     this.setSSLServerName((String)var2);
                  }
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               default:
                  break;
               case 36:
                  if (var1.equals("weblogic.jndi.ssl.client.certificate") && var2 instanceof InputStream[]) {
                     this.setSSLClientCertificate((InputStream[])((InputStream[])var2));
                  }
                  break;
               case 37:
                  if (var1.equals("weblogic.jndi.ssl.client.key_password") && var2 instanceof String) {
                     this.setSSLServerName((String)var2);
                  }
                  break;
               case 38:
                  if (var1.equals("weblogic.jndi.ssl.root.ca.fingerprints")) {
                     if (var2 instanceof String) {
                        this.setSSLRootCAFingerprints((String)var2);
                     } else if (var2 instanceof byte[][]) {
                        this.setSSLRootCAFingerprints((byte[][])((byte[][])var2));
                     }
                  }
            }

            return var6;
         }
      }
   }

   public final Object removeProperty(String var1) {
      return this.setProperty(var1, (Object)null);
   }

   /** @deprecated */
   public final ServerIdentity getProviderIdentity() throws IllegalArgumentException {
      try {
         return (ServerIdentity)this.getPropertyFromEnv("weblogic.jndi.provider.rjvm");
      } catch (ClassCastException var2) {
         throw new IllegalArgumentException("Value of 'weblogic.jndi.provider.rjvm' is not a ServerIdentity object as expected");
      }
   }

   /** @deprecated */
   public final void setProviderIdentity(ServerIdentity var1) {
      this.setProperty("weblogic.jndi.provider.rjvm", var1);
   }

   Hashtable getRemoteProperties() {
      if (this.env == null) {
         return this.env;
      } else {
         Object var1 = this.env.get("weblogic.jndi.ssl.client.certificate");
         if (this.env.get("java.naming.security.principal") == null && this.env.get("java.naming.security.credentials") == null && this.env.get("weblogic.jndi.provider.rjvm") == null && var1 == null) {
            return this.env;
         } else {
            Hashtable var2 = (Hashtable)this.env.clone();
            var2.remove("weblogic.jndi.provider.rjvm");
            var2.remove("java.naming.security.principal");
            var2.remove("java.naming.security.credentials");
            var2.remove("weblogic.jndi.ssl.client.certificate");
            return var2;
         }
      }
   }

   public void setEnableDefaultUser(boolean var1) {
      this.enableDefaultUser = var1;
   }

   public boolean getEnableDefaultUser() {
      return this.enableDefaultUser;
   }

   public final void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      this.findOrCreateSSLProxy().loadLocalIdentity(var1, var2);
   }

   public final boolean isClientCertAvailable() {
      return this.findOrCreateSSLProxy().isClientCertAvailable();
   }

   public final boolean isLocalIdentitySet() {
      return this.findOrCreateSSLProxy().isLocalIdentitySet();
   }

   public final void setDisableLoggingOfWarningMsg(boolean var1) {
      this.setBoolean("weblogic.jndi.disableLoggingOfWarningMsg", var1);
   }

   public final boolean getDisableLoggingOfWarningMsg() {
      return this.getBoolean("weblogic.jndi.disableLoggingOfWarningMsg", false);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.getRemoteProperties());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.env = (Hashtable)var1.readObject();
   }

   static {
      TRUE = Boolean.TRUE.toString();
      FALSE = Boolean.FALSE.toString();
   }
}
