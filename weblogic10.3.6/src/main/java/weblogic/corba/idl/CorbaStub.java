package weblogic.corba.idl;

import java.rmi.Remote;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.Stub;

public class CorbaStub extends Stub implements Remote {
   public CorbaStub(StubReference var1) {
      super(var1);
   }
}
