package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class StructStub extends RMIStubWrapperImpl implements Serializable {
   private Struct remoteStruct = null;
   private RmiDriverSettings rmiDriverSettings = null;

   public StructStub() {
   }

   public StructStub(Struct var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(Struct var1, RmiDriverSettings var2) {
      this.remoteStruct = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      StructStub var1 = null;

      try {
         var1 = (StructStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.StructStub", this.remoteStruct, false);
         var1.init(this.remoteStruct, this.rmiDriverSettings);
         return (java.sql.Struct)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteStruct;
      }
   }
}
