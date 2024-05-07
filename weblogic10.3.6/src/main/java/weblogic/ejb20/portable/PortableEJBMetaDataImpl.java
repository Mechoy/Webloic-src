package weblogic.ejb20.portable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.HomeHandle;

public final class PortableEJBMetaDataImpl implements EJBMetaData, Serializable {
   static final long serialVersionUID = 2161422698991859090L;
   private Class keyClass;
   private Class homeClass;
   private Class remoteClass;
   private boolean isSessionBean;
   private boolean isStatelessSessionBean;
   private HomeHandle homeHandle;

   public PortableEJBMetaDataImpl(EJBMetaData var1) {
      this.homeHandle = new PortableHomeHandleImpl(var1.getEJBHome());
      this.isStatelessSessionBean = var1.isStatelessSession();
      this.isSessionBean = var1.isSession();
      if (!this.isSessionBean) {
         this.keyClass = var1.getPrimaryKeyClass();
      } else {
         this.keyClass = null;
      }

      this.homeClass = var1.getHomeInterfaceClass();
      this.remoteClass = var1.getRemoteInterfaceClass();
   }

   public Class getHomeInterfaceClass() {
      return this.homeClass;
   }

   public Class getRemoteInterfaceClass() {
      return this.remoteClass;
   }

   public EJBHome getEJBHome() {
      try {
         return this.homeHandle.getEJBHome();
      } catch (RemoteException var2) {
         throw new RuntimeException("Could not get ejbHome from HomeHandle " + var2.getMessage());
      }
   }

   public Class getPrimaryKeyClass() {
      if (this.keyClass == null) {
         throw new RuntimeException("SessionBeans do not have a primary key");
      } else {
         return this.keyClass;
      }
   }

   public boolean isSession() {
      return this.isSessionBean;
   }

   public boolean isStatelessSession() {
      return this.isStatelessSessionBean;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.isSessionBean = var1.readBoolean();
      this.isStatelessSessionBean = var1.readBoolean();
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      this.remoteClass = var2.loadClass(var1.readUTF());
      this.homeClass = var2.loadClass(var1.readUTF());
      if (!this.isSessionBean) {
         this.keyClass = var2.loadClass(var1.readUTF());
      }

      this.homeHandle = (HomeHandle)var1.readObject();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeBoolean(this.isSessionBean);
      var1.writeBoolean(this.isStatelessSessionBean);
      var1.writeUTF(this.remoteClass.getName());
      var1.writeUTF(this.homeClass.getName());
      if (!this.isSessionBean) {
         var1.writeUTF(this.keyClass.getName());
      }

      var1.writeObject(this.homeHandle);
   }
}
