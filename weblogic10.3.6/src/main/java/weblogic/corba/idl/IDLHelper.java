package weblogic.corba.idl;

import java.rmi.RemoteException;
import weblogic.corba.utils.RemoteInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.rmi.cluster.ClusterableServerRef;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.ServerReference;
import weblogic.utils.Debug;

public final class IDLHelper {
   public static final ServerReference exportObject(org.omg.CORBA.portable.ObjectImpl var0) throws RemoteException {
      String var1 = var0._ids()[0];
      RepositoryId var2 = new RepositoryId(var1);
      RemoteInfo var3 = RemoteInfo.findRemoteInfo(var2, var0.getClass());
      RuntimeDescriptor var4 = var3.getDescriptor();
      return var4 != null ? var4.createServerReference(var0).exportObject() : (new CorbaServerRef(var0)).exportObject();
   }

   public static final ServerReference exportObject(org.omg.CORBA.portable.ObjectImpl var0, String var1) throws RemoteException {
      ServerReference var2 = exportObject(var0);
      RuntimeDescriptor var3 = var2.getDescriptor();
      Debug.assertion(var3.isClusterable(), "Cannot export non-clusterable object with jndiName");
      ((ClusterableServerRef)var2).initialize(var1);
      return var2;
   }
}
