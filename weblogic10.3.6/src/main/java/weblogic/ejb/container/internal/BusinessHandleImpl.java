package weblogic.ejb.container.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.MARSHAL;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.ReplacerObjectInputStream;
import weblogic.common.internal.ReplacerObjectOutputStream;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.ejb.spi.BusinessObject;
import weblogic.ejb.spi.PortableReplaceable;
import weblogic.ejb20.internal.HomeHandleImpl;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.Resolver;

public final class BusinessHandleImpl implements BusinessHandle, Externalizable, PortableReplaceable {
   private static final long serialVersionUID = -339809761282601060L;
   private transient BusinessObject businessObject = null;
   private HomeHandleImpl homeHandle = null;
   private static final boolean debug = false;
   private Object primaryKey = null;
   private Object stubInfo = null;
   private Object ifaceName = null;

   public BusinessHandleImpl() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.homeHandle.writeExternal(var1);
      var1.writeObject(this.primaryKey);
      var1.writeObject(this.ifaceName);
      if (var1 instanceof WLObjectOutput) {
         var1.writeObject(this.stubInfo);
      } else {
         ReplacerObjectOutputStream var2 = new ReplacerObjectOutputStream((OutputStream)var1, RemoteObjectReplacer.getReplacer());
         var2.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
         if (this.stubInfo != null) {
            var2.writeObject(this.stubInfo);
         } else {
            var1.writeObject(this.stubInfo);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.homeHandle = new HomeHandleImpl();
      this.homeHandle.readExternal(var1);
      this.primaryKey = var1.readObject();
      this.ifaceName = var1.readObject();

      try {
         if (var1 instanceof WLObjectInput) {
            this.stubInfo = var1.readObject();
         } else {
            Replacer var2 = RemoteObjectReplacer.getReplacer();
            ReplacerObjectInputStream var3 = new ReplacerObjectInputStream((InputStream)var1, var2, (Resolver)null);
            this.stubInfo = var3.readObject();
         }
      } catch (IOException var4) {
      } catch (MARSHAL var5) {
      }

   }

   public String toString() {
      return this.homeHandle + "#" + this.primaryKey;
   }

   public BusinessHandleImpl(EJBHome var1, BusinessObject var2, String var3) throws RemoteException {
      this.businessObject = var2;
      this.ifaceName = var3;
      this.primaryKey = null;
      this.homeHandle = (HomeHandleImpl)var1.getHomeHandle();
   }

   public BusinessHandleImpl(EJBHome var1, BusinessObject var2, Object var3) throws RemoteException {
      this.businessObject = var2;
      this.ifaceName = null;
      this.primaryKey = var3;
      this.homeHandle = (HomeHandleImpl)var1.getHomeHandle();
      this.stubInfo = var2;
   }

   private BusinessObject allocateBO(EJBHome var1, Class[] var2, Object[] var3) throws RemoteException {
      Class var4 = var1.getClass();

      try {
         Method var5 = var4.getMethod("getBusinessImpl", var2);
         Object var10 = var5.invoke(var1, var3);
         return (BusinessObject)PortableRemoteObject.narrow(var10, BusinessObject.class);
      } catch (NoSuchMethodException var7) {
         throw new AssertionError("Class " + var4.getName() + " did not have allocateBO method");
      } catch (InvocationTargetException var8) {
         Throwable var6 = var8.getTargetException();
         EJBRuntimeUtils.throwRemoteException("Exception re-establishing business handle", var6);
         throw new AssertionError("cannot reach here");
      } catch (IllegalAccessException var9) {
         EJBRuntimeUtils.throwRemoteException("Exception re-establishing business handle", var9);
         throw new AssertionError("cannot reach here");
      }
   }

   public BusinessObject getBusinessObject() throws RemoteException {
      if (this.businessObject != null) {
         return this.businessObject;
      } else if (this.primaryKey == null) {
         EJBHome var2 = this.homeHandle.getEJBHome();
         return this.allocateBO(var2, new Class[]{Object.class, String.class}, new Object[]{this.primaryKey, this.ifaceName});
      } else {
         BusinessObject var1 = (BusinessObject)this.stubInfo;
         return var1;
      }
   }
}
