package weblogic.jndi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.List;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.common.internal.ReplacerObjectInputStream;
import weblogic.common.internal.ReplacerObjectOutputStream;
import weblogic.corba.idl.CorbaStub;
import weblogic.corba.server.naming.ReferenceHelperImpl;
import weblogic.iiop.IIOPReplacer;
import weblogic.jndi.internal.BuiltinTransportableObjectFactory;
import weblogic.jndi.internal.JNDIEnvironment;
import weblogic.jndi.internal.JNDIHelper;
import weblogic.jndi.internal.RemoteContextFactory;
import weblogic.jndi.internal.RemoteContextFactoryImpl;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.jndi.internal.SSL.SSLProxy;
import weblogic.jndi.internal.SSL.WLSSSLProxyImpl;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.acl.internal.Security;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SubjectManagerImpl;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.internal.TransactionHelperImpl;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.Resolver;

public class WLSJNDIEnvironmentImpl extends JNDIEnvironment {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Replacer iiopReplacer = IIOPReplacer.getReplacer();

   public SSLProxy getSSLProxy() {
      return new WLSSSLProxyImpl();
   }

   public boolean isCorbaObject(Object var1) {
      return JNDIHelper.isCorbaObject(var1);
   }

   public Object copyObject(Object var1) throws IOException, ClassNotFoundException {
      return JNDIHelper.copyObject(var1);
   }

   public void prepareKernel() {
      Kernel.ensureInitialized();
   }

   public void prepReferenceHelper() {
      ReferenceHelper.setReferenceHelper(new ReferenceHelperImpl());
   }

   public void nullSSLClientCertificate() {
      Security.setSSLClientCertificate((InputStream[])null);
   }

   public Object iiopReplaceObject(Object var1) throws IOException {
      return iiopReplacer.replaceObject(var1);
   }

   public Object iiopResolveObject(Object var1) throws IOException {
      return iiopReplacer.resolveObject(var1);
   }

   public boolean isCorbaStub(Object var1) {
      return var1 instanceof CorbaStub;
   }

   public void loadTransportableFactories(List var1) throws ConfigurationException {
      String[] var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getJNDITransportableObjectFactoryList();
      BuiltinTransportableObjectFactory var3 = new BuiltinTransportableObjectFactory();
      synchronized(var1) {
         if (var2 != null) {
            for(int var5 = 0; var5 < var2.length; ++var5) {
               String var6 = var2[var5];

               ConfigurationException var8;
               try {
                  var1.add(Class.forName(var6).newInstance());
               } catch (ClassNotFoundException var10) {
                  var8 = new ConfigurationException("Failed to find class \"" + var6);
                  var8.setRootCause(var10);
                  throw var8;
               } catch (InstantiationException var11) {
                  var8 = new ConfigurationException("Failed to instantiate \"" + var6 + ".  Make sure it has a public default constructor.");
                  var8.setRootCause(var11);
                  throw var8;
               } catch (IllegalAccessException var12) {
                  var8 = new ConfigurationException("Failed to instantiate \"" + var6 + " because the default constuctor is not public.");
                  var8.setRootCause(var12);
                  throw var8;
               }
            }
         }

         if (var3 != null) {
            var1.add(var3);
         }

      }
   }

   public Context getDelegateContext(ServerIdentity var1, Environment var2, String var3) throws RemoteException, NamingException {
      RemoteContextFactory var4 = (RemoteContextFactory)StubFactory.getStub(RemoteContextFactoryImpl.class, var1);
      return var4.getContext(var2.getRemoteProperties(), var3);
   }

   public void prepareSubjectManager() {
      SubjectManagerImpl.ensureInitialized();
   }

   public void activateTransactionHelper() {
      TransactionHelper.pushTransactionHelper(new TransactionHelperImpl());
   }

   public void deactivateTransactionHelper() {
      TransactionHelper.popTransactionHelper();
   }

   public void pushThreadEnvironment(Environment var1) {
      ThreadEnvironment.push(var1);
   }

   public Environment popThreadEnvironment() {
      return ThreadEnvironment.pop();
   }

   public void pushSubject(AuthenticatedSubject var1, AuthenticatedSubject var2) {
      SecurityServiceManager.pushSubject(var1, var2);
   }

   public void popSubject(AuthenticatedSubject var1) {
      SecurityServiceManager.popSubject(var1);
   }

   public AuthenticatedSubject getCurrentSubject(AuthenticatedSubject var1) {
      return SecurityServiceManager.getCurrentSubject(var1);
   }

   public AuthenticatedSubject getASFromAU(AuthenticatedUser var1) {
      return SecurityServiceManager.getASFromAU(var1);
   }

   public ObjectOutput getReplacerObjectOutputStream(ObjectOutput var1) throws IOException {
      ReplacerObjectOutputStream var2 = new ReplacerObjectOutputStream((OutputStream)var1, RemoteObjectReplacer.getReplacer());
      var2.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
      return var2;
   }

   public ObjectInput getReplacerObjectInputStream(ObjectInput var1) throws IOException {
      return new ReplacerObjectInputStream((InputStream)var1, RemoteObjectReplacer.getReplacer(), (Resolver)null);
   }
}
