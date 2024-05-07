package weblogic.corba.idl.poa;

import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.portable.Delegate;
import weblogic.corba.utils.RemoteInfo;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.iiop.spi.IORDelegate;

class POADelegateImpl implements Delegate, IORDelegate {
   private final POAImpl poa;
   private final IOR ior;
   private final RemoteInfo rinfo;
   private final byte[] oid;

   POADelegateImpl(POAImpl var1, IOR var2, RemoteInfo var3, byte[] var4) {
      this.poa = var1;
      this.ior = var2;
      this.rinfo = var3;
      this.oid = var4;
   }

   public final IOR getIOR() {
      return this.ior;
   }

   public boolean non_existent(Servant var1) {
      return false;
   }

   public byte[] object_id(Servant var1) {
      return this.oid;
   }

   public boolean is_a(Servant var1, String var2) {
      String[] var3 = var1._all_interfaces(this.poa, this.oid);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var2.equals(var3[var4])) {
            return true;
         }
      }

      return var2.equals("IDL:omg.org/CORBA/Object:1.0");
   }

   public ORB orb(Servant var1) {
      return this.poa._orb();
   }

   public Object get_interface_def(Servant var1) {
      throw new NO_IMPLEMENT();
   }

   public Object this_object(Servant var1) {
      try {
         return IIOPReplacer.createCORBAStub(this.ior, (Class)null, (Class)null);
      } catch (Exception var3) {
         throw (AssertionError)(new AssertionError()).initCause(var3);
      }
   }

   public POA default_POA(Servant var1) {
      return this.poa;
   }

   public POA poa(Servant var1) {
      return this.poa;
   }

   public String toString() {
      return this.ior.getTypeId() + "(" + System.identityHashCode(this) + "): " + this.rinfo;
   }
}
