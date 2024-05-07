package weblogic.jms.forwarder.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jms.common.PasswordStore;
import weblogic.jms.forwarder.SessionRuntimeContext;
import weblogic.security.subject.AbstractSubject;

public final class SessionRuntimeContextImpl implements SessionRuntimeContext {
   private static final char[] key = new char[]{'S', 'r', 'C', 'i', ' ', 'S', 'a', 'F', '5', ' ', 'n'};
   private String name;
   private Context providerContext;
   private Connection connection;
   private Session session;
   private boolean isForLocalCluster;
   private String loginUrl;
   private AbstractSubject subject;
   private boolean forceResolveDNS = false;
   private String username;
   private PasswordStore passwordStore;
   private Object passwordHandle;

   public SessionRuntimeContextImpl(String var1, Context var2, String var3, Connection var4, Session var5, boolean var6, AbstractSubject var7) {
      this.passwordStore = new PasswordStore(key);
      this.name = var1;
      this.providerContext = var2;
      this.loginUrl = var3;
      this.connection = var4;
      this.session = var5;
      this.isForLocalCluster = var6;
      this.subject = var7;
   }

   public SessionRuntimeContextImpl(String var1, String var2, String var3, String var4) throws NamingException, JMSException {
      this.passwordStore = new PasswordStore(key);
      if (var1 == null) {
         this.providerContext = new InitialContext();
         this.isForLocalCluster = true;
      } else {
         Hashtable var5 = new Hashtable();
         var5.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         var5.put("java.naming.provider.url", var1);
         var5.put("java.naming.security.principal", var2);
         var5.put("java.naming.security.credentials", var3);
         this.providerContext = new InitialContext(var5);
      }

      this.connection = (Connection)this.providerContext.lookup(var4);
      this.session = this.connection.createSession(false, 1);
   }

   public SessionRuntimeContextImpl(String var1, Context var2, String var3, Connection var4, Session var5, boolean var6, AbstractSubject var7, String var8, String var9, boolean var10) {
      this.passwordStore = new PasswordStore(key);
      this.name = var1;
      this.providerContext = var2;
      this.loginUrl = var3;
      this.connection = var4;
      this.session = var5;
      this.isForLocalCluster = var6;
      this.subject = var7;
      this.username = var8;
      this.setPassword(var9);
      this.forceResolveDNS = var10;
   }

   private void setPassword(String var1) {
      if (this.passwordHandle != null) {
         this.passwordStore.removePassword(this.passwordHandle);
         this.passwordHandle = null;
      }

      try {
         this.passwordHandle = this.passwordStore.storePassword(var1);
      } catch (GeneralSecurityException var3) {
         throw new AssertionError(var3);
      }
   }

   public String getPassword() {
      String var1 = null;
      if (this.passwordHandle != null) {
         try {
            var1 = (String)this.passwordStore.retrievePassword(this.passwordHandle);
         } catch (GeneralSecurityException var3) {
         } catch (IOException var4) {
         }
      }

      return var1;
   }

   public String getUsername() {
      return this.username;
   }

   public String getName() {
      return this.name;
   }

   public boolean getForceResolveDNS() {
      return this.forceResolveDNS;
   }

   public synchronized Context getProviderContext() {
      return this.providerContext;
   }

   public synchronized void setProviderContext(Context var1) {
      this.providerContext = var1;
   }

   public String getLoginUrl() {
      return this.loginUrl;
   }

   public synchronized Connection getJMSConnection() {
      return this.connection;
   }

   public synchronized Session getJMSSession() {
      return this.session;
   }

   public boolean isForLocalCluster() {
      return this.isForLocalCluster;
   }

   public synchronized void refresh(Context var1, Connection var2, Session var3, AbstractSubject var4) {
      this.providerContext = var1;
      this.connection = var2;
      this.session = var3;
      this.subject = var4;
   }

   public synchronized AbstractSubject getSubject() {
      return this.subject;
   }

   public synchronized void setSubject(AbstractSubject var1) {
      this.subject = var1;
   }
}
