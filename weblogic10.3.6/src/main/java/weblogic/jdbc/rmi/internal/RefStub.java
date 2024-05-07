package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class RefStub extends RMIStubWrapperImpl implements Serializable {
   private Ref remoteRef = null;
   private RmiDriverSettings rmiDriverSettings = null;

   public RefStub() {
   }

   public RefStub(Ref var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(Ref var1, RmiDriverSettings var2) {
      this.remoteRef = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      RefStub var1 = null;

      try {
         var1 = (RefStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.RefStub", this.remoteRef, false);
         var1.init(this.remoteRef, this.rmiDriverSettings);
         return (java.sql.Ref)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteRef;
      }
   }
}
