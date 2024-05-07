package weblogic.jndi.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import java.util.List;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.SSL.SSLProxy;
import weblogic.protocol.ServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;

public abstract class JNDIEnvironment {
   private static JNDIEnvironment singleton;

   public static JNDIEnvironment getJNDIEnvironment() {
      if (singleton == null) {
         try {
            singleton = (JNDIEnvironment)Class.forName("weblogic.jndi.WLSJNDIEnvironmentImpl").newInstance();
         } catch (Exception var5) {
            try {
               singleton = (JNDIEnvironment)Class.forName("weblogic.jndi.WLSClientJNDIEnvironmentImpl").newInstance();
            } catch (Exception var4) {
               try {
                  singleton = (JNDIEnvironment)Class.forName("weblogic.jndi.CEJNDIEnvironmentImpl").newInstance();
               } catch (Exception var3) {
                  throw new IllegalArgumentException(var3.toString());
               }
            }
         }
      }

      return singleton;
   }

   static void setJNDIEnvironment(JNDIEnvironment var0) {
      singleton = var0;
   }

   public abstract SSLProxy getSSLProxy();

   public abstract boolean isCorbaObject(Object var1);

   public abstract Object copyObject(Object var1) throws IOException, ClassNotFoundException;

   public abstract void prepareKernel();

   public abstract void prepReferenceHelper();

   public abstract void nullSSLClientCertificate();

   public abstract Object iiopReplaceObject(Object var1) throws IOException;

   public abstract Object iiopResolveObject(Object var1) throws IOException;

   public abstract boolean isCorbaStub(Object var1);

   public abstract void loadTransportableFactories(List var1) throws ConfigurationException;

   public abstract Context getDelegateContext(ServerIdentity var1, Environment var2, String var3) throws RemoteException, NamingException;

   public abstract void prepareSubjectManager();

   public abstract void activateTransactionHelper();

   public abstract void deactivateTransactionHelper();

   public abstract void pushThreadEnvironment(Environment var1);

   public abstract Environment popThreadEnvironment();

   public abstract void pushSubject(AuthenticatedSubject var1, AuthenticatedSubject var2);

   public abstract void popSubject(AuthenticatedSubject var1);

   public abstract AuthenticatedSubject getCurrentSubject(AuthenticatedSubject var1);

   public abstract AuthenticatedSubject getASFromAU(AuthenticatedUser var1);

   public abstract ObjectOutput getReplacerObjectOutputStream(ObjectOutput var1) throws IOException;

   public abstract ObjectInput getReplacerObjectInputStream(ObjectInput var1) throws IOException;
}
