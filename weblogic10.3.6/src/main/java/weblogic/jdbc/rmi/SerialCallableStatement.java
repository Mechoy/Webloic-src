package weblogic.jdbc.rmi;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.SQLXML;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialCallableStatement extends SerialPreparedStatement {
   private static final long serialVersionUID = 915412605233701370L;
   private CallableStatement rmi_cstmt = null;

   public SerialCallableStatement() {
   }

   public SerialCallableStatement(CallableStatement var1, SerialConnection var2) {
      this.init(var1, var2);
   }

   public void init(CallableStatement var1, SerialConnection var2) {
      super.init(var1, var2);
      this.rmi_cstmt = var1;
   }

   public static CallableStatement makeSerialCallableStatement(CallableStatement var0, SerialConnection var1) {
      if (var0 == null) {
         return null;
      } else {
         SerialCallableStatement var2 = (SerialCallableStatement)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialCallableStatement", var0, false);
         var2.init(var0, var1);
         return (CallableStatement)var2;
      }
   }

   public Blob getBlob(int var1) throws SQLException {
      Blob var2 = null;
      String var3 = "getBlob";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getBlob(var1);
         if (var2 != null) {
            var2 = SerialOracleBlob.makeSerialOracleBlob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Clob getClob(int var1) throws SQLException {
      Clob var2 = null;
      String var3 = "getClob";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getClob(var1);
         if (var2 != null) {
            var2 = SerialOracleClob.makeSerialOracleClob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Array getArray(int var1) throws SQLException {
      Array var2 = null;
      String var3 = "getArray";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getArray(var1);
         if (var2 != null) {
            var2 = SerialArray.makeSerialArrayFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Array getArray(String var1) throws SQLException {
      Array var2 = null;
      String var3 = "getArray";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getArray(var1);
         if (var2 != null) {
            var2 = SerialArray.makeSerialArrayFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Blob getBlob(String var1) throws SQLException {
      Blob var2 = null;
      String var3 = "getBlob";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getBlob(var1);
         if (var2 == null) {
            var2 = SerialOracleBlob.makeSerialOracleBlob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Clob getClob(String var1) throws SQLException {
      Clob var2 = null;
      String var3 = "getClob";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getClob(var1);
         if (var2 == null) {
            var2 = SerialOracleClob.makeSerialOracleClob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public NClob getNClob(int var1) throws SQLException {
      NClob var2 = null;
      String var3 = "getNClob";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getNClob(var1);
         if (var2 == null) {
            var2 = SerialOracleNClob.makeSerialOracleNClob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public NClob getNClob(String var1) throws SQLException {
      NClob var2 = null;
      String var3 = "getNClob";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getNClob(var1);
         if (var2 == null) {
            var2 = SerialOracleNClob.makeSerialOracleNClob(var2);
            ((SerialConnection)this.getConnection()).addToLobSet(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Ref getRef(int var1) throws SQLException {
      Ref var2 = null;
      String var3 = "getRef";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getRef(var1);
         if (var2 != null) {
            var2 = SerialRef.makeSerialRefFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Ref getRef(String var1) throws SQLException {
      Ref var2 = null;
      String var3 = "getRef";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getRef(var1);
         if (var2 != null) {
            var2 = SerialRef.makeSerialRefFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public SQLXML getSQLXML(int var1) throws SQLException {
      SQLXML var2 = null;
      String var3 = "getSQLXML";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getSQLXML(var1);
         if (var2 != null) {
            var2 = SerialSQLXML.makeSerialSQLXMLFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public SQLXML getSQLXML(String var1) throws SQLException {
      SQLXML var2 = null;
      String var3 = "getSQLXML";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rmi_cstmt.getSQLXML(var1);
         if (var2 != null) {
            var2 = SerialSQLXML.makeSerialSQLXMLFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }
}
