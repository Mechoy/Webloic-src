package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.Remote;
import java.sql.NClob;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;

public class OracleTNClobImpl extends OracleTClobImpl {
   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new OracleTNClobStub((OracleTNClob)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public static NClob makeOracleTNClobImpl(NClob var0, RmiDriverSettings var1) {
      OracleTNClobImpl var2 = (OracleTNClobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTNClobImpl", var0, true);
      var2.init(var0, var1);
      return (NClob)var2;
   }
}
