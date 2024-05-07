package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.sql.NClob;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class OracleTNClobStub extends OracleTClobStub {
   private static final long serialVersionUID = -3757015803239435378L;

   public OracleTNClobStub() {
   }

   public OracleTNClobStub(OracleTNClob var1, RmiDriverSettings var2) {
      super(var1, var2);
   }

   public static NClob makeOracleTNClobStub(NClob var0, RmiDriverSettings var1) {
      OracleTNClobStub var2 = (OracleTNClobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTNClobStub", var0, false);
      var2.init((OracleTNClob)var0, var1);
      return (NClob)var2;
   }

   public Object readResolve() throws ObjectStreamException {
      OracleTNClobStub var1 = null;

      try {
         var1 = (OracleTNClobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTNClobStub", this.getStubDelegate(), false);
         var1.init((OracleTNClob)this.getStubDelegate(), this.rmiSettings);
         return (NClob)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.getStubDelegate();
      }
   }
}
