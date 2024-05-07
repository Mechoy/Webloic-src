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

public class ResultSetStraightReader implements ResultSetReader, Serializable {
   private static final long serialVersionUID = 2887163503487096990L;
   private java.sql.ResultSet rs;

   public ResultSetStraightReader(java.sql.ResultSet var1) {
      this.rs = var1;
   }

   public int getRmiFetchSize() throws SQLException {
      return 0;
   }

   public void setRmiFetchSize(int var1) throws SQLException {
   }

   public boolean next() throws SQLException {
      return this.rs.next();
   }

   public boolean wasNull() throws SQLException {
      return this.rs.wasNull();
   }

   public String getString(int var1) throws SQLException {
      return this.rs.getString(var1);
   }

   public boolean getBoolean(int var1) throws SQLException {
      return this.rs.getBoolean(var1);
   }

   public byte getByte(int var1) throws SQLException {
      return this.rs.getByte(var1);
   }

   public short getShort(int var1) throws SQLException {
      return this.rs.getShort(var1);
   }

   public int getInt(int var1) throws SQLException {
      return this.rs.getInt(var1);
   }

   public long getLong(int var1) throws SQLException {
      return this.rs.getLong(var1);
   }

   public float getFloat(int var1) throws SQLException {
      return this.rs.getFloat(var1);
   }

   public double getDouble(int var1) throws SQLException {
      return this.rs.getDouble(var1);
   }

   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      return this.rs.getBigDecimal(var1, var2);
   }

   public byte[] getBytes(int var1) throws SQLException {
      return this.rs.getBytes(var1);
   }

   public Date getDate(int var1) throws SQLException {
      return this.rs.getDate(var1);
   }

   public Time getTime(int var1) throws SQLException {
      return this.rs.getTime(var1);
   }

   public Timestamp getTimestamp(int var1) throws SQLException {
      return this.rs.getTimestamp(var1);
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      return this.rs.getAsciiStream(var1);
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      return this.rs.getUnicodeStream(var1);
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      return this.rs.getBinaryStream(var1);
   }

   public String getString(String var1) throws SQLException {
      return this.rs.getString(var1);
   }

   public boolean getBoolean(String var1) throws SQLException {
      return this.rs.getBoolean(var1);
   }

   public byte getByte(String var1) throws SQLException {
      return this.rs.getByte(var1);
   }

   public short getShort(String var1) throws SQLException {
      return this.rs.getShort(var1);
   }

   public int getInt(String var1) throws SQLException {
      return this.rs.getInt(var1);
   }

   public long getLong(String var1) throws SQLException {
      return this.rs.getLong(var1);
   }

   public float getFloat(String var1) throws SQLException {
      return this.rs.getFloat(var1);
   }

   public double getDouble(String var1) throws SQLException {
      return this.rs.getDouble(var1);
   }

   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      return this.rs.getBigDecimal(var1);
   }

   public byte[] getBytes(String var1) throws SQLException {
      return this.rs.getBytes(var1);
   }

   public Date getDate(String var1) throws SQLException {
      return this.rs.getDate(var1);
   }

   public Time getTime(String var1) throws SQLException {
      return this.rs.getTime(var1);
   }

   public Timestamp getTimestamp(String var1) throws SQLException {
      return this.rs.getTimestamp(var1);
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      return this.rs.getAsciiStream(var1);
   }

   public InputStream getUnicodeStream(String var1) throws SQLException {
      return this.rs.getUnicodeStream(var1);
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      return this.rs.getBinaryStream(var1);
   }

   public Object getObject(int var1) throws SQLException {
      return this.rs.getObject(var1);
   }

   public Object getObject(String var1) throws SQLException {
      return this.rs.getObject(var1);
   }

   public int findColumn(String var1) throws SQLException {
      return this.rs.findColumn(var1);
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      return this.rs.getCharacterStream(var1);
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      return this.rs.getCharacterStream(var1);
   }

   public BigDecimal getBigDecimal(int var1) throws SQLException {
      return this.rs.getBigDecimal(var1);
   }

   public BigDecimal getBigDecimal(String var1) throws SQLException {
      return this.rs.getBigDecimal(var1);
   }

   public boolean isBeforeFirst() throws SQLException {
      return this.rs.isBeforeFirst();
   }

   public boolean isAfterLast() throws SQLException {
      return this.rs.isAfterLast();
   }

   public boolean isFirst() throws SQLException {
      return this.rs.isFirst();
   }

   public boolean isLast() throws SQLException {
      return this.rs.isLast();
   }

   public int getRow() throws SQLException {
      return this.rs.getRow();
   }

   public Object getObject(int var1, Map var2) throws SQLException {
      return this.rs.getObject(var1, var2);
   }

   public java.sql.Ref getRef(int var1) throws SQLException {
      return this.rs.getRef(var1);
   }

   public java.sql.Blob getBlob(int var1) throws SQLException {
      return this.rs.getBlob(var1);
   }

   public java.sql.Clob getClob(int var1) throws SQLException {
      return this.rs.getClob(var1);
   }

   public java.sql.Array getArray(int var1) throws SQLException {
      return this.rs.getArray(var1);
   }

   public Object getObject(String var1, Map var2) throws SQLException {
      return this.rs.getObject(var1, var2);
   }

   public java.sql.Ref getRef(String var1) throws SQLException {
      return this.rs.getRef(var1);
   }

   public java.sql.Blob getBlob(String var1) throws SQLException {
      return this.rs.getBlob(var1);
   }

   public java.sql.Clob getClob(String var1) throws SQLException {
      return this.rs.getClob(var1);
   }

   public java.sql.Array getArray(String var1) throws SQLException {
      return this.rs.getArray(var1);
   }

   public Date getDate(int var1, Calendar var2) throws SQLException {
      return this.rs.getDate(var1, var2);
   }

   public Date getDate(String var1, Calendar var2) throws SQLException {
      return this.rs.getDate(var1, var2);
   }

   public Time getTime(int var1, Calendar var2) throws SQLException {
      return this.rs.getTime(var1, var2);
   }

   public Time getTime(String var1, Calendar var2) throws SQLException {
      return this.rs.getTime(var1, var2);
   }

   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      return this.rs.getTimestamp(var1, var2);
   }

   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      return this.rs.getTimestamp(var1, var2);
   }

   public URL getURL(int var1) throws SQLException {
      return this.rs.getURL(var1);
   }

   public URL getURL(String var1) throws SQLException {
      return this.rs.getURL(var1);
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      return this.rs.getNCharacterStream(var1);
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      return this.rs.getNCharacterStream(var1);
   }

   public NClob getNClob(int var1) throws SQLException {
      return this.rs.getNClob(var1);
   }

   public NClob getNClob(String var1) throws SQLException {
      return this.rs.getNClob(var1);
   }

   public String getNString(int var1) throws SQLException {
      return this.rs.getNString(var1);
   }

   public String getNString(String var1) throws SQLException {
      return this.rs.getNString(var1);
   }

   public RowId getRowId(int var1) throws SQLException {
      return this.rs.getRowId(var1);
   }

   public RowId getRowId(String var1) throws SQLException {
      return this.rs.getRowId(var1);
   }

   public java.sql.SQLXML getSQLXML(int var1) throws SQLException {
      return this.rs.getSQLXML(var1);
   }

   public java.sql.SQLXML getSQLXML(String var1) throws SQLException {
      return this.rs.getSQLXML(var1);
   }
}
