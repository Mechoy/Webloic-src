package weblogic.corba.rmi;

import java.io.IOException;
import java.rmi.Remote;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.Delegate;
import weblogic.corba.idl.DelegateFactory;
import weblogic.iiop.IIOPRemoteRef;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.extensions.server.CollocatedRemoteReference;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.StubInfoIntf;

public class Stub extends javax.rmi.CORBA.Stub implements weblogic.rmi.extensions.server.Stub, Remote {
   private String[] ids;
   private final RemoteReference ror;
   private static final long serialVersionUID = -2889933658618898055L;

   private Stub() {
      this.ror = null;
   }

   public Stub(StubReference var1) {
      this.ror = var1.getRemoteRef();
      RemoteReference var2 = this.ror;
      IOR var3 = null;
      Delegate var4 = null;
      if (var2 instanceof ClusterableRemoteRef) {
         var2 = ((ClusterableRemoteRef)this.ror).getPrimaryRef();
      }

      if (var2 instanceof CollocatedRemoteReference) {
         try {
            var3 = (IOR)IIOPReplacer.getIIOPReplacer().replaceObject(var2);
         } catch (IOException var6) {
            throw new INV_OBJREF(var6.getMessage());
         }

         var4 = DelegateFactory.createDelegate(var3);
      } else {
         var3 = ((IIOPRemoteRef)var2).getIOR();
         var4 = DelegateFactory.createDelegate(var3);
      }

      this._set_delegate(var4);
      this.ids = new String[]{var3.getTypeId().toString()};
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Stub) {
         Stub var3 = (Stub)var1;
         return this.ror.equals(var3.ror);
      } else if (var1 instanceof StubInfoIntf) {
         StubInfoIntf var2 = (StubInfoIntf)var1;
         return this.ror.equals(var2.getStubInfo().getRemoteRef());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.ror.hashCode();
   }

   public void connect(ORB var1) {
   }

   public String[] _ids() {
      return this.ids;
   }

   public String toString() {
      return this._get_delegate().toString();
   }

   public RemoteReference getRemoteRef() {
      return this.ror;
   }
}
