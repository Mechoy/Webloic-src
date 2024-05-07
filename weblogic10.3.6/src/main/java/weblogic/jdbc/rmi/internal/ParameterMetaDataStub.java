package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class ParameterMetaDataStub extends RMIStubWrapperImpl implements Serializable {
   ParameterMetaData remoteParameterMetaData;
   private RmiDriverSettings rmiDriverSettings;

   public ParameterMetaDataStub() {
   }

   public ParameterMetaDataStub(ParameterMetaData var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(ParameterMetaData var1, RmiDriverSettings var2) {
      this.remoteParameterMetaData = var1;
      this.rmiDriverSettings = var2;
   }

   public Object readResolve() throws ObjectStreamException {
      ParameterMetaDataStub var1 = null;

      try {
         var1 = (ParameterMetaDataStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ParameterMetaDataStub", this.remoteParameterMetaData, false);
         var1.init(this.remoteParameterMetaData, this.rmiDriverSettings);
         return (java.sql.ParameterMetaData)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteParameterMetaData;
      }
   }
}
