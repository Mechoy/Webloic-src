package weblogic.jdbc.rmi;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.internal.ResultSetCachingReader;
import weblogic.jdbc.rmi.internal.ResultSetReader;
import weblogic.jdbc.rmi.internal.ResultSetStraightReader;
import weblogic.jdbc.rmi.internal.ResultSetStub;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialResultSet extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -2720653455103793350L;
   private ResultSet rmi_rs = null;
   private ResultSetReader rs_reader = null;
   private transient SerialStatement parent_stmt = null;
   private boolean closed = false;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         return null;
      } else {
         try {
            if (var3 instanceof Blob) {
               return SerialOracleBlob.makeSerialOracleBlob((Blob)var3);
            }

            if (var3 instanceof Clob) {
               return SerialOracleClob.makeSerialOracleClob((Clob)var3);
            }

            if (var3 instanceof Array) {
               return SerialArray.makeSerialArrayFromStub((Array)var3);
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
         }

         return var3;
      }
   }

   public void init(ResultSet var1, SerialStatement var2) {
      this.parent_stmt = var2;
      this.rmi_rs = var1;
      this.closed = false;

      try {
         if (this.rmi_rs instanceof ResultSetStub) {
            ResultSetStub var3 = (ResultSetStub)this.rmi_rs;
            if (var3.isRowCaching()) {
               this.rs_reader = new ResultSetCachingReader(var3);
               return;
            }
         }
      } catch (Exception var4) {
      }

      this.rs_reader = new ResultSetStraightReader(var1);
   }

   public static ResultSet makeSerialResultSet(ResultSet var0, SerialStatement var1) {
      if (var0 == null) {
         return null;
      } else {
         SerialResultSet var2 = (SerialResultSet)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialResultSet", var0, false);
         var2.init(var0, var1);
         if (var1 != null) {
            var1.addResultSet(var2);
         }

         return (ResultSet)var2;
      }
   }

   public boolean next() throws SQLException {
      boolean var1 = false;
      String var2 = "next";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.next();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public void close() throws SQLException {
      String var1 = "close";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.close(true);
         this.postInvocationHandler(var1, var2, (Object)null);
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

   }

   void close(boolean var1) throws SQLException {
      try {
         if (!this.closed) {
            if (var1 && this.parent_stmt != null) {
               this.parent_stmt.removeResultSet(this);
            }

            this.rmi_rs.close();
            this.closed = true;
         }

      } catch (Exception var4) {
         if (var4 instanceof SQLException) {
            throw (SQLException)var4;
         } else {
            Throwable var3 = (new SQLException(var4.toString())).initCause(var4);
            throw (SQLException)var3;
         }
      }
   }

   public boolean wasNull() throws SQLException {
      boolean var1 = false;
      String var2 = "wasNull";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.wasNull();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public String getString(int var1) throws SQLException {
      String var2 = null;
      String var3 = "getString";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getString(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public boolean getBoolean(int var1) throws SQLException {
      boolean var2 = false;
      String var3 = "getBoolean";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBoolean(var1);
         this.postInvocationHandler(var3, var4, new Boolean(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public byte getByte(int var1) throws SQLException {
      byte var2 = 0;
      String var3 = "getbyte";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getByte(var1);
         this.postInvocationHandler(var3, var4, new Byte(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public short getShort(int var1) throws SQLException {
      short var2 = 0;
      String var3 = "getShort";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getShort(var1);
         this.postInvocationHandler(var3, var4, new Short(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public int getInt(int var1) throws SQLException {
      int var2 = 0;
      String var3 = "getInt";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getInt(var1);
         this.postInvocationHandler(var3, var4, new Integer(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public long getLong(int var1) throws SQLException {
      long var2 = 0L;
      String var4 = "getLong";
      Object[] var5 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var4, var5);
         var2 = this.rs_reader.getLong(var1);
         this.postInvocationHandler(var4, var5, new Long(var2));
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var2;
   }

   public float getFloat(int var1) throws SQLException {
      float var2 = 0.0F;
      String var3 = "getFloat";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getFloat(var1);
         this.postInvocationHandler(var3, var4, new Float(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public double getDouble(int var1) throws SQLException {
      double var2 = 0.0;
      String var4 = "getDouble";
      Object[] var5 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var4, var5);
         var2 = this.rs_reader.getDouble(var1);
         this.postInvocationHandler(var4, var5, new Double(var2));
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var2;
   }

   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      BigDecimal var3 = null;
      String var4 = "getBigDecimal";
      Object[] var5 = new Object[]{new Integer(var1), new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getBigDecimal(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public byte[] getBytes(int var1) throws SQLException {
      byte[] var2 = null;
      String var3 = "getBytes";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBytes(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Date getDate(int var1) throws SQLException {
      Date var2 = null;
      String var3 = "getDate";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getDate(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Time getTime(int var1) throws SQLException {
      Time var2 = null;
      String var3 = "getTime";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getTime(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Timestamp getTimestamp(int var1) throws SQLException {
      Timestamp var2 = null;
      String var3 = "getTimestamp";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getTimestamp(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getAsciiStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getAsciiStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getUnicodeStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getUnicodeStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getBinaryStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBinaryStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public String getString(String var1) throws SQLException {
      String var2 = null;
      String var3 = "getString";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getString(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public boolean getBoolean(String var1) throws SQLException {
      boolean var2 = false;
      String var3 = "getBoolean";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBoolean(var1);
         this.postInvocationHandler(var3, var4, new Boolean(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public byte getByte(String var1) throws SQLException {
      byte var2 = 0;
      String var3 = "getByte";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getByte(var1);
         this.postInvocationHandler(var3, var4, new Byte(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public short getShort(String var1) throws SQLException {
      short var2 = 0;
      String var3 = "getShort";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getShort(var1);
         this.postInvocationHandler(var3, var4, new Short(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public int getInt(String var1) throws SQLException {
      int var2 = 0;
      String var3 = "getInt";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getInt(var1);
         this.postInvocationHandler(var3, var4, new Integer(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public long getLong(String var1) throws SQLException {
      long var2 = 0L;
      String var4 = "getLong";
      Object[] var5 = new Object[]{var1};

      try {
         this.preInvocationHandler(var4, var5);
         var2 = this.rs_reader.getLong(var1);
         this.postInvocationHandler(var4, var5, new Long(var2));
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var2;
   }

   public float getFloat(String var1) throws SQLException {
      float var2 = 0.0F;
      String var3 = "getFloat";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getFloat(var1);
         this.postInvocationHandler(var3, var4, new Float(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public double getDouble(String var1) throws SQLException {
      double var2 = 0.0;
      String var4 = "getDouble";
      Object[] var5 = new Object[]{var1};

      try {
         this.preInvocationHandler(var4, var5);
         var2 = this.rs_reader.getDouble(var1);
         this.postInvocationHandler(var4, var5, new Double(var2));
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var2;
   }

   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      BigDecimal var3 = null;
      String var4 = "getBigDecimal";
      Object[] var5 = new Object[]{var1, new Integer(var2)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getBigDecimal(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public byte[] getBytes(String var1) throws SQLException {
      byte[] var2 = null;
      String var3 = "getBytes";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBytes(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Date getDate(String var1) throws SQLException {
      Date var2 = null;
      String var3 = "getDate";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getDate(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Time getTime(String var1) throws SQLException {
      Time var2 = null;
      String var3 = "getTime";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getTime(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Timestamp getTimestamp(String var1) throws SQLException {
      Timestamp var2 = null;
      String var3 = "getTimestamp";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getTimestamp(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getAsciiStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getAsciiStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getUnicodeStream(String var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getUnicodeStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getUnicodeStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      InputStream var2 = null;
      String var3 = "getBinaryStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBinaryStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      SerialResultSetMetaData var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = new SerialResultSetMetaData(this.rmi_rs.getMetaData());
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public Object getObject(int var1) throws SQLException {
      Object var2 = null;
      String var3 = "getObject";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getObject(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Object getObject(String var1) throws SQLException {
      Object var2 = null;
      String var3 = "getObject";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getObject(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public int findColumn(String var1) throws SQLException {
      int var2 = 0;
      String var3 = "findColumn";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.findColumn(var1);
         this.postInvocationHandler(var3, var4, new Integer(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      Reader var2 = null;
      String var3 = "getCharacterStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getCharacterStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      Reader var2 = null;
      String var3 = "getCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getCharacterStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public BigDecimal getBigDecimal(int var1) throws SQLException {
      BigDecimal var2 = null;
      String var3 = "getBigDecimal";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBigDecimal(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public BigDecimal getBigDecimal(String var1) throws SQLException {
      BigDecimal var2 = null;
      String var3 = "getBigDecimal";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getBigDecimal(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public boolean isBeforeFirst() throws SQLException {
      boolean var1 = false;
      String var2 = "isBeforeFirst";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.isBeforeFirst();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public boolean isAfterLast() throws SQLException {
      boolean var1 = false;
      String var2 = "isAfterLast";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.isAfterLast();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public boolean isFirst() throws SQLException {
      boolean var1 = false;
      String var2 = "isFirst";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.isFirst();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public boolean isLast() throws SQLException {
      boolean var1 = false;
      String var2 = "isLast";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.isLast();
         this.postInvocationHandler(var2, var3, new Boolean(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public int getRow() throws SQLException {
      int var1 = 0;
      String var2 = "getrow";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.rs_reader.getRow();
         this.postInvocationHandler(var2, var3, new Integer(var1));
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public Statement getStatement() throws SQLException {
      String var1 = "getStatement";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, this.parent_stmt);
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

      return (Statement)this.parent_stmt;
   }

   public Object getObject(int var1, Map var2) throws SQLException {
      Object var3 = null;
      String var4 = "getObject";
      Object[] var5 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getObject(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Blob getBlob(int var1) throws SQLException {
      Blob var2 = null;
      String var3 = "getBlob";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         ResultSetMetaData var5 = this.rmi_rs.getMetaData();
         var2 = this.rmi_rs.getBlob(var5.getColumnName(var1));
         if (var2 != null) {
            var2 = SerialOracleBlob.makeSerialOracleBlob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
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
         ResultSetMetaData var5 = this.rmi_rs.getMetaData();
         var2 = this.rmi_rs.getClob(var5.getColumnName(var1));
         if (var2 != null) {
            var2 = SerialOracleClob.makeSerialOracleClob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
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
         var2 = SerialArray.makeSerialArrayFromStub(this.rs_reader.getArray(var1));
         super.postInvocationHandler(var3, var4, var2);
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
         var2 = SerialArray.makeSerialArrayFromStub(this.rs_reader.getArray(var1));
         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Object getObject(String var1, Map var2) throws SQLException {
      Object var3 = null;
      String var4 = "getObject";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getObject(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Ref getRef(String var1) throws SQLException {
      Ref var2 = null;
      String var3 = "getRef";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getRef(var1);
         if (var2 != null) {
            var2 = SerialRef.makeSerialRefFromStub(var2);
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
         var2 = this.rmi_rs.getBlob(var1);
         if (var2 != null) {
            var2 = SerialOracleBlob.makeSerialOracleBlob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
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
         var2 = this.rmi_rs.getClob(var1);
         if (var2 != null) {
            var2 = SerialOracleClob.makeSerialOracleClob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Date getDate(int var1, Calendar var2) throws SQLException {
      Date var3 = null;
      String var4 = "getDate";
      Object[] var5 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getDate(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Date getDate(String var1, Calendar var2) throws SQLException {
      Date var3 = null;
      String var4 = "getDate";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getDate(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Time getTime(int var1, Calendar var2) throws SQLException {
      Time var3 = null;
      String var4 = "getTime";
      Object[] var5 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getTime(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Time getTime(String var1, Calendar var2) throws SQLException {
      Time var3 = null;
      String var4 = "getTime";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getTime(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      Timestamp var3 = null;
      String var4 = "getTimestamp";
      Object[] var5 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getTimestamp(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      Timestamp var3 = null;
      String var4 = "getTimestamp";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         var3 = this.rs_reader.getTimestamp(var1, var2);
         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

      return var3;
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      Reader var2 = null;
      String var3 = "getNCharacterStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getNCharacterStream(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      Reader var2 = null;
      String var3 = "getNCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getNCharacterStream(var1);
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
         var2 = this.rs_reader.getNClob(var1);
         if (var2 != null) {
            var2 = SerialOracleNClob.makeSerialOracleNClob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
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
         var2 = this.rs_reader.getNClob(var1);
         if (var2 != null) {
            var2 = SerialOracleNClob.makeSerialOracleNClob(var2);
            ((SerialConnection)((SerialConnection)this.getStatement().getConnection())).addToLobSet(var2);
         }

         super.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public String getNString(int var1) throws SQLException {
      String var2 = null;
      String var3 = "getNString";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getNString(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public String getNString(String var1) throws SQLException {
      String var2 = null;
      String var3 = "getNString";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getNString(var1);
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
         var2 = this.rs_reader.getRef(var1);
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
         var2 = this.rs_reader.getSQLXML(var1);
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
         var2 = this.rs_reader.getSQLXML(var1);
         if (var2 != null) {
            var2 = SerialSQLXML.makeSerialSQLXMLFromStub(var2);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public URL getURL(int var1) throws SQLException {
      URL var2 = null;
      String var3 = "getURL";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getURL(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public URL getURL(String var1) throws SQLException {
      URL var2 = null;
      String var3 = "getURL";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.rs_reader.getURL(var1);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public boolean isClosed() throws SQLException {
      boolean var1 = this.closed;
      String var2 = "isClosed";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         if (!this.closed) {
            var1 = this.rmi_rs.isClosed();
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }
}
