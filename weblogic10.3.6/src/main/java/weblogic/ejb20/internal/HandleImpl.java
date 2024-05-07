package weblogic.ejb20.internal;

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
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import org.omg.CORBA.MARSHAL;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.ReplacerObjectInputStream;
import weblogic.common.internal.ReplacerObjectOutputStream;
import weblogic.ejb.spi.PortableReplaceable;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.Resolver;

public final class HandleImpl implements Handle, Externalizable, PortableReplaceable {
   private static final long serialVersionUID = 5484839489411820318L;
   private transient EJBObject ejbObject = null;
   private HomeHandleImpl homeHandle = null;
   private Object primaryKey = null;
   private Object stubInfo = null;

   public HandleImpl() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.homeHandle.writeExternal(var1);
      var1.writeObject(this.primaryKey);
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

   public HandleImpl(EJBObject var1) throws RemoteException {
      this.ejbObject = var1;
      EJBHome var2 = var1.getEJBHome();

      assert var2.getEJBMetaData().isStatelessSession();

      this.primaryKey = null;
      this.homeHandle = (HomeHandleImpl)var2.getHomeHandle();
   }

   public HandleImpl(EJBObject var1, Object var2) throws RemoteException {
      assert var1 != null;

      assert var2 != null;

      this.ejbObject = var1;
      EJBHome var3 = var1.getEJBHome();

      assert !var3.getEJBMetaData().isStatelessSession();

      this.primaryKey = var2;
      this.homeHandle = (HomeHandleImpl)var3.getHomeHandle();
      if (var3.getEJBMetaData().isSession()) {
         this.stubInfo = var1;
      }

   }

   private EJBObject allocateEO(EJBHome var1, Class[] var2, Object[] var3) throws RemoteException {
      Class var4 = var1.getClass();

      try {
         Method var5 = var4.getMethod("allocateEJBObject", var2);
         return (EJBObject)var5.invoke(var1, var3);
      } catch (NoSuchMethodException var7) {
         throw new AssertionError("Class " + var4.getName() + " did not have allocateEO method");
      } catch (InvocationTargetException var8) {
         Throwable var6 = var8.getTargetException();
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         } else {
            throw new RemoteException("Exception re-establishing handle", var6);
         }
      } catch (IllegalAccessException var9) {
         throw new RemoteException("Exception re-establishing handle", var9);
      }
   }

   public EJBObject getEJBObject() throws RemoteException {
      if (this.ejbObject != null) {
         return this.ejbObject;
      } else if (this.primaryKey == null) {
         EJBHome var3 = this.homeHandle.getEJBHome();

         assert var3.getEJBMetaData().isStatelessSession();

         return this.allocateEO(var3, (Class[])null, (Object[])null);
      } else {
         EJBObject var1 = null;
         if (this.stubInfo != null) {
            var1 = (EJBObject)this.stubInfo;
         } else {
            EJBHome var2 = this.homeHandle.getEJBHome();

            assert !var2.getEJBMetaData().isStatelessSession();

            var1 = this.allocateEO(var2, new Class[]{Object.class}, new Object[]{this.primaryKey});
         }

         return var1;
      }
   }
}
