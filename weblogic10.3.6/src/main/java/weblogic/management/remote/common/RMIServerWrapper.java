package weblogic.management.remote.common;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.management.remote.rmi.RMIConnection;
import javax.management.remote.rmi.RMIServer;
import javax.naming.InitialContext;
import javax.security.auth.Subject;

class RMIServerWrapper implements RMIServer {
   RMIServer rmiServer;
   private Subject subject;
   private Locale locale;
   List connectionList = new ArrayList();
   InitialContext ctx;

   public RMIServerWrapper(RMIServer var1, Subject var2, InitialContext var3, Locale var4) {
      this.rmiServer = var1;
      this.subject = var2;
      this.ctx = var3;
      this.locale = var4;
   }

   public String getVersion() throws RemoteException {
      return this.rmiServer.getVersion();
   }

   public RMIConnection newClient(Object var1) throws IOException {
      this.clearClientConnection((RMIConnectionWrapper)null);
      RMIConnection var2 = this.rmiServer.newClient(var1);
      RMIConnectionWrapper var3 = new RMIConnectionWrapper(var2, this.subject, this.locale, this);
      synchronized(this.connectionList) {
         this.connectionList.add(new WeakReference(var3));
         return var3;
      }
   }

   public void disconnected() {
      synchronized(this.connectionList) {
         Iterator var2 = this.connectionList.iterator();

         while(var2.hasNext()) {
            RMIConnectionWrapper var3 = (RMIConnectionWrapper)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.disconnected();
            }
         }

         this.connectionList.clear();
      }
   }

   void clearClientConnection(RMIConnectionWrapper var1) {
      synchronized(this.connectionList) {
         Iterator var3 = this.connectionList.iterator();

         while(true) {
            RMIConnectionWrapper var4;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (RMIConnectionWrapper)((WeakReference)var3.next()).get();
            } while(var4 != null && !var4.equals(var1));

            var3.remove();
         }
      }
   }
}
