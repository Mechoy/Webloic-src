package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.jndi.Alias;
import weblogic.jndi.JNDILogger;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ProtocolStack;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.server.RemoteWrapper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TransactionHelper;

public class WLContextImpl implements WLInternalContext, RemoteWrapper, Externalizable {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final long serialVersionUID = -7759756621062870766L;
   private NamingNode node;
   private Hashtable env;
   private transient Thread loginThread = null;
   private transient boolean disableWarning = false;
   private transient boolean enableLogout;

   public WLContextImpl(Hashtable var1, NamingNode var2) {
      this.node = var2;
      this.env = var1;
      if (!this.isLocalURL()) {
         this.loginThread = Thread.currentThread();
         if (var1 != null) {
            String var3 = (String)var1.get("weblogic.jndi.disableLoggingOfWarningMsg");
            if (var3 != null) {
               this.disableWarning = Boolean.parseBoolean(var3);
            }
         }
      }

   }

   public Remote getRemoteDelegate() {
      return this.node;
   }

   public void enableLogoutOnClose() {
      this.loginThread = Thread.currentThread();
      this.enableLogout = true;
   }

   /** @deprecated */
   public void disableThreadWarningOnClose() {
      this.disableWarning = true;
   }

   public void close() throws NamingException {
      if (this.loginThread != null) {
         if (this.loginThread != Thread.currentThread()) {
            if (!this.disableWarning) {
               JNDILogger.logDiffThread();
            }
         } else {
            if (!this.isLocalURL()) {
               ProtocolStack.pop();
            }

            if (this.enableLogout) {
               SecurityServiceManager.popSubject(kernelId);
               JNDIEnvironment.getJNDIEnvironment().nullSSLClientCertificate();
            }
         }

         this.loginThread = null;
      }

      TransactionHelper.popTransactionHelper();
      if (RemoteHelper.getClientTimeout() > 0L) {
         RemoteHelper.setClientTimeout(0L);
      }

   }

   private final boolean isLocalURL() {
      return this.env != null && this.env.get("java.naming.provider.url") == null && KernelStatus.isServer();
   }

   public Context createSubcontext(Name var1) throws NamingException {
      return this.createSubcontext(var1.toString());
   }

   public Context createSubcontext(String var1) throws NamingException {
      try {
         return this.node.createSubcontext(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public Object lookupLink(Name var1) throws NamingException {
      return this.lookupLink(var1.toString());
   }

   public Object lookupLink(String var1) throws NamingException {
      try {
         return this.node.lookupLink(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void destroySubcontext(Name var1) throws NamingException {
      this.destroySubcontext(var1.toString());
   }

   public void destroySubcontext(String var1) throws NamingException {
      try {
         this.node.destroySubcontext(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      try {
         this.node.rebind(var1, var2, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public void rebind(String var1, Object var2) throws NamingException {
      try {
         this.node.rebind(var1, var2, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public void rebind(String var1, Object var2, Object var3) throws NamingException {
      try {
         this.node.rebind(var1, var2, var3, this.env);
      } catch (RemoteException var5) {
         throw this.translateException(var5);
      }
   }

   public NameParser getNameParser(Name var1) throws NamingException {
      return this.getNameParser(var1.toString());
   }

   public NameParser getNameParser(String var1) throws NamingException {
      try {
         return this.node.getNameParser(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      return this.list(var1.toString());
   }

   public NamingEnumeration list(String var1) throws NamingException {
      try {
         return this.node.list(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      Object var2 = null;
      if (this.env != null) {
         var2 = this.env.remove(var1);
      }

      if (this.env.size() == 0) {
         this.env = null;
      }

      return var2;
   }

   public void unbind(Name var1) throws NamingException {
      this.unbind(var1.toString());
   }

   public void unbind(String var1) throws NamingException {
      try {
         this.node.unbind(var1, (Object)null, this.env);
      } catch (RemoteException var3) {
         this.translateException(var3);
      }

   }

   public void unbind(Name var1, Object var2) throws NamingException {
      this.unbind(var1.toString(), var2);
   }

   public void unbind(String var1, Object var2) throws NamingException {
      try {
         this.node.unbind(var1, var2, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      return new CompositeName(this.composeName(var1.toString(), var2.toString()));
   }

   public String composeName(String var1, String var2) throws NamingException {
      if (var2.length() == 0) {
         return var2;
      } else {
         try {
            NamingNode var3 = this.node.getParent();
            NameParser var4 = this.getNameParser("");
            if (var3 != null && var3.getNameParser("", this.env).equals(var4)) {
               return var1.length() == 0 ? var2 : var2 + "." + var1;
            } else {
               return var1.length() == 0 ? var2 : var2 + "." + var1;
            }
         } catch (RemoteException var5) {
            throw this.translateException(var5);
         }
      }
   }

   public String getNameInNamespace() throws NamingException {
      try {
         return this.node.getNameInNamespace();
      } catch (RemoteException var2) {
         throw this.translateException(var2);
      }
   }

   public String getNameInNamespace(String var1) throws NamingException {
      try {
         return this.node.getNameInNamespace(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public Hashtable getEnvironment() throws NamingException {
      return this.env;
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      return this.listBindings(var1.toString());
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      try {
         return this.node.listBindings(var1, this.env);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void rename(Name var1, Name var2) throws NamingException {
      this.rename(var1.toString(), var2.toString());
   }

   public void rename(String var1, String var2) throws NamingException {
      try {
         this.node.rename(var1, var2, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public Object lookup(Name var1) throws NamingException {
      Object var2 = this.lookup(var1, var1.toString());
      if (var2 instanceof Alias) {
         String var3 = ((Alias)var2).getRealName();
         var2 = this.lookup(var3);
      }

      return var2;
   }

   public Object lookup(String var1) throws NamingException {
      Object var2 = this.lookup(WLNameParser.defaultParse(var1), var1);
      if (var2 instanceof Alias) {
         var1 = ((Alias)var2).getRealName();
         var2 = this.lookup(var1);
      }

      return var2;
   }

   protected Object lookup(Name var1, String var2) throws NamingException {
      try {
         Object var3 = this.node.lookup(var2, this.env);
         ServerHelper.ensureJNDIName(var3, var2);
         return NamingManager.getObjectInstance(var3, var1, this, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      } catch (NamingException var5) {
         throw var5;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new NamingException(var7.getMessage());
      }
   }

   public void bind(Name var1, Object var2) throws NamingException {
      this.bind(var1.toString(), var2);
   }

   public void bind(String var1, Object var2) throws NamingException {
      try {
         this.node.bind(var1, var2, this.env);
      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      if (this.env == null) {
         this.env = new Hashtable(5);
      }

      return this.env.put(var1, var2);
   }

   public NamingNode getNode() {
      return this.node;
   }

   protected NamingException translateException(RemoteException var1) {
      return ExceptionTranslator.toNamingException(var1);
   }

   public boolean equals(Object var1) {
      return this.node.equals(((WLContextImpl)var1).getNode());
   }

   public int hashCode() {
      return this.node.hashCode();
   }

   public String toString() {
      try {
         return "WLContext (" + this.getNameInNamespace() + ")";
      } catch (NamingException var2) {
         return "WLContext ( NAME UNKNOWN )";
      }
   }

   public WLContextImpl() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (var1 instanceof WLObjectOutput) {
         WLObjectOutput var2 = (WLObjectOutput)var1;
         var2.writeObjectWL(this.node);
         var2.writeObjectWL(this.env);
      } else {
         ObjectOutput var3 = JNDIEnvironment.getJNDIEnvironment().getReplacerObjectOutputStream(var1);
         var3.writeObject(this.node);
         var3.writeObject(this.env);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      if (var1 instanceof WLObjectInput) {
         WLObjectInput var2 = (WLObjectInput)var1;
         this.node = (NamingNode)var2.readObjectWL();
         this.env = (Hashtable)var2.readObjectWL();
      } else {
         ObjectInput var3 = JNDIEnvironment.getJNDIEnvironment().getReplacerObjectInputStream(var1);
         this.node = (NamingNode)var3.readObject();
         this.env = (Hashtable)var3.readObject();
      }

   }
}
