package weblogic.rmi.cluster;

import java.rmi.RemoteException;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.extensions.server.InvokableServerReference;
import weblogic.rmi.extensions.server.StubReference;

public class ClusterableActivatableServerRef extends ClusterableServerRef implements ActivatableServerReference {
   public ClusterableActivatableServerRef(ActivatableServerReference var1) {
      super((InvokableServerReference)var1);
   }

   public Object getImplementation(Object var1) throws RemoteException {
      return ((ActivatableServerReference)this.getDelegate()).getImplementation(var1);
   }

   public StubReference getStubReference(Object var1) throws RemoteException {
      throw new UnsupportedOperationException("getStubReference()");
   }

   public Activator getActivator() {
      return ((ActivatableServerReference)this.getDelegate()).getActivator();
   }
}
