package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class ArrayStub extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -3520000790072523739L;
   private Array remoteArray = null;
   private RmiDriverSettings rmiDriverSettings = null;

   public ArrayStub() {
   }

   public ArrayStub(Array var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(Array var1, RmiDriverSettings var2) {
      this.remoteArray = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      ArrayStub var1 = null;

      try {
         var1 = (ArrayStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ArrayStub", this.remoteArray, false);
         var1.init(this.remoteArray, this.rmiDriverSettings);
         return (java.sql.Array)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteArray;
      }
   }

   public void internalClose() {
      this.remoteArray.internalClose();
   }
}
