package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.Remote;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;

public class StructImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private transient StructStub structStub = null;
   private RmiDriverSettings rmiDriverSettings = null;
   private java.sql.Struct t2Struct = null;
   private transient Object interop = null;

   public void init(java.sql.Struct var1, RmiDriverSettings var2) {
      this.t2Struct = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.Struct makeStructImpl(java.sql.Struct var0, RmiDriverSettings var1) {
      StructImpl var2 = (StructImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.StructImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.Struct)var2;
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new StructStub((Struct)var2, this.rmiDriverSettings);
      }

      return this.interop;
   }
}
