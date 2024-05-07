package weblogic.corba.idl;

import org.omg.CORBA.portable.Delegate;
import weblogic.iiop.IOR;

public final class DelegateFactory {
   public static final Delegate createDelegate(IOR var0) {
      return (Delegate)(var0.isLocal() ? new DelegateImpl(var0) : new RemoteDelegateImpl(var0));
   }
}
