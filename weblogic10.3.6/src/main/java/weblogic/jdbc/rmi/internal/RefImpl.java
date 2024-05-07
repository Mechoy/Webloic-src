package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.Remote;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;

public class RefImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private transient RefStub refStub = null;
   private RmiDriverSettings rmiDriverSettings = null;
   private java.sql.Ref t2Ref = null;
   private transient Object interop = null;

   public void init(java.sql.Ref var1, RmiDriverSettings var2) {
      this.t2Ref = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.Ref makeRefImpl(java.sql.Ref var0, RmiDriverSettings var1) {
      RefImpl var2 = (RefImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.RefImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.Ref)var2;
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new RefStub((Ref)var2, this.rmiDriverSettings);
      }

      return this.interop;
   }
}
