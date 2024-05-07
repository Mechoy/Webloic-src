package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class ArrayImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private transient ArrayStub arrayStub = null;
   private RmiDriverSettings rmiDriverSettings = null;
   private java.sql.Array t2Array = null;
   private transient Object interop = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof java.sql.ResultSet) {
               java.sql.ResultSet var4 = (java.sql.ResultSet)var3;
               ResultSetImpl var5 = (ResultSetImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ResultSetImpl", var4, true);
               var5.init(var4, this.rmiDriverSettings);
               var3 = (java.sql.ResultSet)var5;
            }
         } catch (Exception var6) {
            JDBCLogger.logStackTrace(var6);
            throw var6;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(java.sql.Array var1, RmiDriverSettings var2) {
      this.t2Array = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.Array makeArrayImpl(java.sql.Array var0, RmiDriverSettings var1) {
      ArrayImpl var2 = (ArrayImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ArrayImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.Array)var2;
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new ArrayStub((Array)var2, this.rmiDriverSettings);
      }

      return this.interop;
   }

   public void internalClose() {
      try {
         UnicastRemoteObject.unexportObject(this, true);
         this.t2Array = null;
         this.rmiDriverSettings = null;
      } catch (NoSuchObjectException var2) {
      }

   }
}
