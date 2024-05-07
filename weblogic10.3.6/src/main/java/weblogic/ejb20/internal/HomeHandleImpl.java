package weblogic.ejb20.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.ejb.EJBHome;
import javax.ejb.HomeHandle;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.internal.URLDelegate;
import weblogic.ejb.spi.PortableReplaceable;
import weblogic.protocol.ChannelHelperBase;
import weblogic.rmi.extensions.PortableRemoteObject;

public final class HomeHandleImpl implements HomeHandle, Externalizable, PortableReplaceable {
   private static final long serialVersionUID = -4517252809192149290L;
   private String serverURL;
   private Name jndiName;
   private transient EJBHome home = null;
   private transient URLDelegate urlDelegate = null;

   public HomeHandleImpl() {
   }

   public HomeHandleImpl(EJBHome var1, Name var2, URLDelegate var3) {
      this.home = var1;
      this.jndiName = var2;
      this.urlDelegate = var3;
   }

   public String toString() {
      return this.jndiName + ":" + this.serverURL;
   }

   public EJBHome getEJBHome() throws RemoteException {
      if (this.home == null) {
         String var1 = this.serverURL;
         if (var1 == null) {
            var1 = ChannelHelperBase.getDefaultURL();
         }

         try {
            Hashtable var2 = new Hashtable();
            var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var2.put("java.naming.provider.url", var1);
            InitialContext var3 = new InitialContext(var2);
            this.home = (EJBHome)PortableRemoteObject.narrow(var3.lookup(this.jndiName), EJBHome.class);

            assert this.home != null;
         } catch (ClassCastException var4) {
            EJBLogger.logStackTrace(var4);
         } catch (NamingException var5) {
            throw new NoSuchObjectException("Unable to locate EJBHome: '" + this.jndiName + "' on server: '" + var1);
         }
      }

      return this.home;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (this.serverURL == null) {
         var1.writeObject(this.urlDelegate.getURL(var1));
      } else {
         var1.writeObject(this.serverURL);
      }

      var1.writeObject(this.jndiName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.serverURL = (String)var1.readObject();
      this.jndiName = (Name)var1.readObject();
      this.urlDelegate = URLDelegate.CHANNEL_URL_DELEGATE;
   }
}
