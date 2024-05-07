package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ResultSetCachingReader implements ResultSetReader, Serializable {
   private static final long serialVersionUID = 3906341926472915638L;
   private ResultSetStub rs;
   private transient ResultSetRowCache rowCache = null;
   private transient ResultSetMetaDataCache mdCache = null;

   public ResultSetCachingReader(ResultSetStub var1) throws SQLException {
      this.rs = var1;
      this.mdCache = var1.getMetaDataCache();
      this.rowCache = this.rs.getNextRowCache();
      this.rowCache.setMetaDataCache(this.mdCache);
      this.rowCache.beforeFirstRow();
   }

   public synchronized boolean next() throws SQLException {
      if (this.rowCache.next()) {
         return true;
      } else if (this.rowCache.isTrueSetFinished()) {
         return false;
      } else {
         this.rowCache = this.rs.getNextRowCache();
         this.rowCache.setMetaDataCache(this.mdCache);
         return this.rowCache.getRowCount() > 0;
      }
   }

   public boolean wasNull() throws SQLException {
      return this.rowCache.wasNull();
   }

   public String getString(int var1) throws SQLException {
      return this.rowCache.getString(var1);
   }

   public boolean getBoolean(int var1) throws SQLException {
      return this.rowCache.getBoolean(var1);
   }

   public byte getByte(int var1) throws SQLException {
      return this.rowCache.getByte(var1);
   }

   public short getShort(int var1) throws SQLException {
      return this.rowCache.getShort(var1);
   }

   public int getInt(int var1) throws SQLException {
      return this.rowCache.getInt(var1);
   }

   public long getLong(int var1) throws SQLException {
      return this.rowCache.getLong(var1);
   }

   public float getFloat(int var1) throws SQLException {
      return this.rowCache.getFloat(var1);
   }

   public double getDouble(int var1) throws SQLException {
      return this.rowCache.getDouble(var1);
   }

   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      return this.rowCache.getBigDecimal(var1, var2);
   }

   public byte[] getBytes(int var1) throws SQLException {
      return this.rowCache.getBytes(var1);
   }

   public Date getDate(int var1) throws SQLException {
      return this.rowCache.getDate(var1);
   }

   public Time getTime(int var1) throws SQLException {
      return this.rowCache.getTime(var1);
   }

   public Timestamp getTimestamp(int var1) throws SQLException {
      return this.rowCache.getTimestamp(var1);
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      String var2 = "getAsciiStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      String var2 = "getUnicodeStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      String var2 = "getBinaryStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public String getString(String var1) throws SQLException {
      return this.rowCache.getString(this.mdCache.findColumn(var1));
   }

   public boolean getBoolean(String var1) throws SQLException {
      return this.rowCache.getBoolean(this.mdCache.findColumn(var1));
   }

   public byte getByte(String var1) throws SQLException {
      return this.rowCache.getByte(this.mdCache.findColumn(var1));
   }

   public short getShort(String var1) throws SQLException {
      return this.rowCache.getShort(this.mdCache.findColumn(var1));
   }

   public int getInt(String var1) throws SQLException {
      return this.rowCache.getInt(this.mdCache.findColumn(var1));
   }

   public long getLong(String var1) throws SQLException {
      return this.rowCache.getLong(this.mdCache.findColumn(var1));
   }

   public float getFloat(String var1) throws SQLException {
      return this.rowCache.getFloat(this.mdCache.findColumn(var1));
   }

   public double getDouble(String var1) throws SQLException {
      return this.rowCache.getDouble(this.mdCache.findColumn(var1));
   }

   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      return this.rowCache.getBigDecimal(this.mdCache.findColumn(var1), var2);
   }

   public byte[] getBytes(String var1) throws SQLException {
      return this.rowCache.getBytes(this.mdCache.findColumn(var1));
   }

   public Date getDate(String var1) throws SQLException {
      return this.rowCache.getDate(this.mdCache.findColumn(var1));
   }

   public Time getTime(String var1) throws SQLException {
      return this.rowCache.getTime(this.mdCache.findColumn(var1));
   }

   public Timestamp getTimestamp(String var1) throws SQLException {
      return this.rowCache.getTimestamp(this.mdCache.findColumn(var1));
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      String var2 = "getAsciiStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public InputStream getUnicodeStream(String var1) throws SQLException {
      String var2 = "getUnicodeStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      String var2 = "getBinaryStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public Object getObject(int var1) throws SQLException {
      return this.rowCache.getObject(var1);
   }

   public Object getObject(String var1) throws SQLException {
      return this.rowCache.getObject(this.mdCache.findColumn(var1));
   }

   public int findColumn(String var1) throws SQLException {
      return this.mdCache.findColumn(var1);
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      String var2 = "getCharacterStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      String var2 = "getCharacterStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public BigDecimal getBigDecimal(int var1) throws SQLException {
      return this.rowCache.getBigDecimal(var1);
   }

   public BigDecimal getBigDecimal(String var1) throws SQLException {
      return this.rowCache.getBigDecimal(this.mdCache.findColumn(var1));
   }

   public boolean isBeforeFirst() throws SQLException {
      String var1 = "isBeforeFirst is not supported with row caching turned on. ";
      throw new SQLException(var1);
   }

   public boolean isAfterLast() throws SQLException {
      String var1 = "isAfterLast is not supported with row caching turned on. ";
      throw new SQLException(var1);
   }

   public boolean isFirst() throws SQLException {
      String var1 = "isFirst is not supported with row caching turned on. ";
      throw new SQLException(var1);
   }

   public boolean isLast() throws SQLException {
      String var1 = "isLast is not supported with row caching turned on. ";
      throw new SQLException(var1);
   }

   public int getRow() throws SQLException {
      String var1 = "getRow is not supported with row caching turned on. ";
      throw new SQLException(var1);
   }

   public Object getObject(int var1, Map var2) throws SQLException {
      String var3 = "getObject(int col, java.util.Map map) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public java.sql.Ref getRef(int var1) throws SQLException {
      String var2 = "getRef is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Blob getBlob(int var1) throws SQLException {
      String var2 = "getBlob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Clob getClob(int var1) throws SQLException {
      String var2 = "getClob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Array getArray(int var1) throws SQLException {
      String var2 = "getArray is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public Object getObject(String var1, Map var2) throws SQLException {
      String var3 = "getObject(String col, java.util.Map map) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public java.sql.Ref getRef(String var1) throws SQLException {
      String var2 = "getRef is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Blob getBlob(String var1) throws SQLException {
      String var2 = "getBlob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Clob getClob(String var1) throws SQLException {
      String var2 = "getClob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.Array getArray(String var1) throws SQLException {
      String var2 = "getArray is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public Date getDate(int var1, Calendar var2) throws SQLException {
      String var3 = " getDate(int col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Date getDate(String var1, Calendar var2) throws SQLException {
      String var3 = " getDate(String col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Time getTime(int var1, Calendar var2) throws SQLException {
      String var3 = " getTime(int col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Time getTime(String var1, Calendar var2) throws SQLException {
      String var3 = " getTime(String col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      String var3 = " getTimestamp(int col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      String var3 = " getTimestamp(String col, java.util.Calendar cal) is not supported with row caching turned on. ";
      throw new SQLException(var3);
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      String var2 = "getNCharacterStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      String var2 = "getNCharacterStream is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public NClob getNClob(int var1) throws SQLException {
      String var2 = "getNClob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public NClob getNClob(String var1) throws SQLException {
      String var2 = "getNClob is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public String getNString(int var1) throws SQLException {
      return this.rowCache.getNString(var1);
   }

   public String getNString(String var1) throws SQLException {
      return this.rowCache.getNString(this.mdCache.findColumn(var1));
   }

   public RowId getRowId(int var1) throws SQLException {
      String var2 = "getRowId is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public RowId getRowId(String var1) throws SQLException {
      String var2 = "getRowId is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.SQLXML getSQLXML(int var1) throws SQLException {
      String var2 = "getRowId is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public java.sql.SQLXML getSQLXML(String var1) throws SQLException {
      String var2 = "getRowId is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public URL getURL(int var1) throws SQLException {
      String var2 = "getURL is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }

   public URL getURL(String var1) throws SQLException {
      String var2 = "getURL is not supported with row caching turned on. ";
      throw new SQLException(var2);
   }
}
