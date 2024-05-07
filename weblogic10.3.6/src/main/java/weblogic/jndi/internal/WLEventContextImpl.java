package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;
import javax.naming.spi.NamingManager;
import org.omg.PortableServer.Servant;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.AssertionError;

public final class WLEventContextImpl extends WLContextImpl implements EventContext, Externalizable, InteropWriteReplaceable {
   private List listeners;
   public static final String ENABLE_EVENTS = "weblogic.jndi.events.enable";
   private static final long serialVersionUID = -570490474896887060L;
   private NamingNode node;
   private Hashtable env;
   private boolean copyNotRequired;

   public WLEventContextImpl(Hashtable var1, NamingNode var2, boolean var3) {
      super(var1, var2);
      this.listeners = Collections.synchronizedList(new ArrayList());
      this.node = var2;
      this.env = var1;
      this.copyNotRequired = var3;
   }

   public WLEventContextImpl(Hashtable var1, NamingNode var2) {
      this(var1, var2, false);
   }

   public Context createSubcontext(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.createSubcontext(var1, this.env) : super.createSubcontext(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public Object lookupLink(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.lookupLink(var1, this.env) : super.lookupLink(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void destroySubcontext(String var1) throws NamingException {
      try {
         if (this.node != null) {
            this.node.destroySubcontext(var1, this.env);
         } else {
            super.destroySubcontext(var1);
         }

      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void rebind(String var1, Object var2) throws NamingException {
      try {
         if (this.node != null) {
            Object var3 = this.copyObject(var2);
            this.node.rebind(var1, var3, this.env);
         } else {
            super.rebind(var1, var2);
         }

      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public void rebind(String var1, Object var2, Object var3) throws NamingException {
      try {
         if (this.node != null) {
            Object var4 = this.copyObject(var3);
            Object var5 = this.copyObject(var2);
            this.node.rebind(var1, var5, var4, this.env);
         } else {
            super.rebind(var1, var2, var3);
         }

      } catch (RemoteException var6) {
         throw this.translateException(var6);
      }
   }

   public NameParser getNameParser(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.getNameParser(var1, this.env) : super.getNameParser(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public NamingEnumeration list(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.list(var1, this.env) : super.list(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void unbind(String var1) throws NamingException {
      try {
         if (this.node != null) {
            this.node.unbind(var1, (Object)null, this.env);
         } else {
            super.unbind(var1);
         }
      } catch (RemoteException var3) {
         this.translateException(var3);
      }

   }

   public void unbind(String var1, Object var2) throws NamingException {
      try {
         if (this.node != null) {
            this.node.unbind(var1, var2, this.env);
         } else {
            super.unbind(var1, var2);
         }

      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public String getNameInNamespace() throws NamingException {
      try {
         return this.node != null ? this.node.getNameInNamespace() : super.getNameInNamespace();
      } catch (RemoteException var2) {
         throw this.translateException(var2);
      }
   }

   public String getNameInNamespace(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.getNameInNamespace(var1) : super.getNameInNamespace(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      try {
         return this.node != null ? this.node.listBindings(var1, this.env) : super.listBindings(var1);
      } catch (RemoteException var3) {
         throw this.translateException(var3);
      }
   }

   public void rename(String var1, String var2) throws NamingException {
      try {
         if (this.node != null) {
            this.node.rename(var1, var2, this.env);
         } else {
            super.rename(var1, var2);
         }

      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   protected Object lookup(Name var1, String var2) throws NamingException {
      try {
         if (this.node != null) {
            Object var3 = this.node.lookup(var2, this.env);
            return NamingManager.getObjectInstance(this.copyObject(var3), var1, this, this.env);
         } else {
            return super.lookup(var1, var2);
         }
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

   public void bind(String var1, Object var2) throws NamingException {
      try {
         if (this.node != null) {
            Object var3 = this.copyObject(var2);
            this.node.bind(var1, var3, this.env);
         } else {
            super.bind(var1, var2);
         }

      } catch (RemoteException var4) {
         throw this.translateException(var4);
      }
   }

   public boolean equals(Object var1) {
      return this.node != null ? this.node.equals(((WLEventContextImpl)var1).getNode()) : super.equals(var1);
   }

   public int hashCode() {
      return this.node == null ? super.hashCode() : this.node.hashCode();
   }

   public void addNamingListener(Name var1, int var2, NamingListener var3) throws NamingException {
      this.addNamingListener(var1.toString(), var2, var3);
   }

   public void addNamingListener(String var1, int var2, NamingListener var3) throws NamingException {
      if (this.node != null) {
         this.node.addNamingListener(var1, var2, var3, this.env);
         this.listeners.add(var3);
      } else {
         throw new OperationNotSupportedException("Unsupported operationaddNamingListener");
      }
   }

   public void removeNamingListener(NamingListener var1) throws NamingException {
      if (this.node != null) {
         this.node.removeNamingListener(var1, this.env);
         this.listeners.remove(var1);
      } else {
         throw new OperationNotSupportedException("Unsupported operationaddNamingListener");
      }
   }

   public boolean targetMustExist() {
      return false;
   }

   public void close() throws NamingException {
      super.close();
      synchronized(this.listeners) {
         NamingListener var3;
         for(Iterator var2 = this.listeners.iterator(); var2.hasNext(); this.removeNamingListener(var3)) {
            var3 = (NamingListener)var2.next();
            if (this.node != null) {
               this.node.removeNamingListener(var3, this.env);
            }
         }

      }
   }

   public String toString() {
      try {
         return "EventContext (" + this.getNameInNamespace() + ")";
      } catch (NamingException var2) {
         return "EventContext ( NAME UNKNOWN )";
      }
   }

   private Object copyObject(Object var1) throws RemoteException {
      if (this.copyNotRequired) {
         return var1;
      } else {
         try {
            if (var1 instanceof Proxy) {
               return JNDIEnvironment.getJNDIEnvironment().copyObject(var1);
            } else if (!(var1 instanceof org.omg.CORBA.Object) && !(var1 instanceof Servant) && !JNDIEnvironment.getJNDIEnvironment().isCorbaStub(var1)) {
               if (var1 instanceof Remote) {
                  Remote var5 = (Remote)var1;
                  return ServerHelper.replaceAndResolveRemoteObject(var5);
               } else {
                  return var1 instanceof Serializable ? JNDIEnvironment.getJNDIEnvironment().copyObject(var1) : var1;
               }
            } else {
               Object var2 = JNDIEnvironment.getJNDIEnvironment().iiopReplaceObject(var1);
               return JNDIEnvironment.getJNDIEnvironment().iiopResolveObject(var2);
            }
         } catch (IOException var3) {
            throw new RemoteException(var3.getMessage(), var3);
         } catch (ClassNotFoundException var4) {
            throw new RemoteException(var4.getMessage(), var4);
         }
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      int var2 = var1.getMajor();
      int var3 = var1.getMinor();
      int var4 = var1.getServicePack();
      return (var2 != 7 || var3 != 0 || var4 < 3) && (var2 != 8 || var3 != 1 || var4 < 1) ? new WLContextImpl(this.env, this.node) : new WLEventContextImpl(this.env, this.node);
   }

   public WLEventContextImpl() {
      this.listeners = Collections.synchronizedList(new ArrayList());
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);

      try {
         NamingNode var2 = this.getNode();
         if (ServerHelper.isLocal(var2)) {
            this.node = (NamingNode)ServerHelper.getServerReference(var2).getImplementation();
            this.env = this.getEnvironment();
            this.copyNotRequired = false;
            this.listeners = new ArrayList();
         }

      } catch (NamingException var3) {
         throw new AssertionError("Unexpected exception", var3);
      }
   }

   public Object readResolve() throws ObjectStreamException {
      if (this.node != null) {
         return this;
      } else {
         try {
            return new WLContextImpl(this.getEnvironment(), this.getNode());
         } catch (NamingException var2) {
            throw new AssertionError("Unexpected exception", var2);
         }
      }
   }
}
