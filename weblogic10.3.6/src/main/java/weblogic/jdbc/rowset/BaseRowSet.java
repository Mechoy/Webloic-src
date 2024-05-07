package weblogic.jdbc.rowset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.Joinable;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialDatalink;
import javax.sql.rowset.serial.SerialRef;
import javax.sql.rowset.spi.SyncFactory;
import javax.sql.rowset.spi.SyncProvider;
import weblogic.jdbc.JDBCLogger;
import weblogic.utils.StackTraceUtils;

public abstract class BaseRowSet implements Serializable, Joinable {
   private static final long serialVersionUID = 496582906625949131L;
   String url;
   String userName;
   String password;
   String dataSourceName;
   boolean preferDataSource;
   transient DataSource dataSource;
   transient Connection cachedConnection;
   String command;
   transient ArrayList params = new ArrayList();
   int isolationLevel;
   int fetchDirection;
   int fetchSize;
   int concurrency = 1008;
   int resultSetType = 1004;
   Map typeMap;
   int queryTimeout;
   int maxRows;
   int maxFieldSize;
   boolean escapeProcessing;
   boolean showDeleted;
   boolean isComplete;
   boolean wasNull;
   transient String providerID;
   transient SyncProvider provider = new WLSyncProvider();
   transient RowSetWriter writer = new CachedRowSetJDBCWriter();
   transient RowSetReader reader = new CachedRowSetJDBCReader();
   transient boolean locked = false;
   CachedRowSetMetaData metaData;
   LifeCycle.State state;
   List rowSetListeners = new ArrayList();
   int rowIndex;
   boolean isClosed = false;
   private static final int BUFFER_SIZE = 8192;

   protected void setIsClosed(boolean var1) {
      this.isClosed = var1;
   }

   void keepRowIndexValid() {
      if (this.rowIndex < -1) {
         this.rowIndex = -1;
      }

      if (this.rowIndex > this.size()) {
         this.rowIndex = this.size();
      }

   }

   public abstract int size();

   public int getRow() {
      return this.rowIndex + 1;
   }

   public boolean absolute(int var1) throws SQLException {
      this.checkIterator();
      int var2 = this.rowIndex;
      if (var1 < 0) {
         this.rowIndex = this.size() + var1;
      } else {
         this.rowIndex = var1 - 1;
      }

      this.keepRowIndexValid();
      if (var2 != this.rowIndex) {
         this.cursorMoved();
      }

      return this.rowIndex >= 0 && this.rowIndex < this.size();
   }

   public boolean relative(int var1) throws SQLException {
      this.checkIterator();
      int var2 = this.rowIndex;
      this.rowIndex += var1;
      this.keepRowIndexValid();
      if (var2 != this.rowIndex) {
         this.cursorMoved();
      }

      return this.rowIndex >= 0 && this.rowIndex < this.size();
   }

   public void beforeFirst() throws SQLException {
      this.absolute(0);
   }

   public boolean first() throws SQLException {
      return this.absolute(1);
   }

   public boolean next() throws SQLException {
      return this.relative(1);
   }

   public boolean previous() throws SQLException {
      return this.relative(-1);
   }

   public boolean last() throws SQLException {
      return this.absolute(this.size());
   }

   public void afterLast() throws SQLException {
      this.absolute(this.size() + 1);
   }

   public boolean isBeforeFirst() throws SQLException {
      this.checkIterator();
      return this.rowIndex == -1;
   }

   public boolean isAfterLast() throws SQLException {
      this.checkIterator();
      return this.size() > 0 && this.rowIndex >= this.size();
   }

   public boolean isFirst() throws SQLException {
      this.checkIterator();
      return this.size() > 0 && this.rowIndex == 0;
   }

   public boolean isLast() throws SQLException {
      this.checkIterator();
      return this.size() > 0 && this.rowIndex == this.size() - 1;
   }

   public boolean rowUpdated() throws SQLException {
      return this.currentRow().isUpdatedRow();
   }

   public boolean rowInserted() throws SQLException {
      return this.currentRow().isInsertRow();
   }

   public boolean rowDeleted() throws SQLException {
      return this.currentRow().isDeletedRow();
   }

   public boolean columnUpdated(int var1) throws SQLException {
      return this.currentRow().isModified(var1);
   }

   public boolean columnUpdated(String var1) throws SQLException {
      return this.currentRow().isModified(this.findColumn(var1));
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      return this.metaData;
   }

   public void setMetaData(RowSetMetaData var1) throws SQLException {
      this.checkOp();
      if (var1 != null) {
         if (var1 instanceof CachedRowSetMetaData) {
            this.metaData = (CachedRowSetMetaData)((CachedRowSetMetaData)var1).clone();
         } else {
            this.metaData = new CachedRowSetMetaData();
            DatabaseMetaData var2 = null;

            try {
               var2 = this.getConnection().getMetaData();
            } catch (Exception var4) {
               JDBCLogger.logStackTrace(var4);
            }

            this.metaData.initialize(var1, var2);
         }

      }
   }

   public boolean isReadOnly() {
      return this.metaData.isReadOnly();
   }

   public void setReadOnlyInternal(boolean var1) {
      this.checkOp();
      this.metaData.setReadOnly(var1);
   }

   public void setReadOnly(boolean var1) {
      this.checkOp(3);
      this.metaData.setReadOnly(var1);
   }

   public void setTableName(String var1) throws SQLException {
      this.checkOp();
      this.metaData.setWriteTableName(var1);
   }

   public String getTableName() throws SQLException {
      return this.metaData.getWriteTableName();
   }

   public int[] getKeyColumns() throws SQLException {
      return this.metaData.getKeyColumns();
   }

   public void setKeyColumns(int[] var1) throws SQLException {
      this.checkOp();
      this.metaData.setKeyColumns(var1);
   }

   public boolean getShowDeleted() throws SQLException {
      return this.showDeleted;
   }

   public void setShowDeleted(boolean var1) throws SQLException {
      this.checkOp();
      this.showDeleted = var1;
   }

   public SQLWarning getWarnings() throws SQLException {
      return null;
   }

   public void clearWarnings() throws SQLException {
   }

   public RowSetWarning getRowSetWarnings() {
      return null;
   }

   public String getCursorName() throws SQLException {
      return null;
   }

   public Statement getStatement() throws SQLException {
      return null;
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
      this.checkOp();
      this.dataSourceName = var1;
      this.preferDataSource = true;
   }

   private DataSource lookupDataSource() throws SQLException {
      InitialContext var1 = null;

      try {
         Hashtable var2 = new Hashtable();
         if (this.userName != null) {
            var2.put("java.naming.security.principal", this.userName);
         }

         if (this.password != null) {
            var2.put("java.naming.security.credentials", this.password);
         }

         var1 = new InitialContext(var2);
      } catch (NamingException var5) {
         throw new SQLException("Unable to access JNDI with username: " + this.userName + ". Please ensure that your username and password are correct.", StackTraceUtils.throwable2StackTrace(var5));
      }

      try {
         this.dataSource = (DataSource)var1.lookup(this.dataSourceName);
         return this.dataSource;
      } catch (ClassCastException var3) {
         throw new SQLException("The name: " + this.dataSourceName + " was found in the JNDI tree, but this is not a javax.sql.DataSource.");
      } catch (NamingException var4) {
         throw new SQLException("Unable to locate a DataSource with the name: " + this.dataSourceName + ".  Please check your configuration and make sure that this DataSource has been deployed.", StackTraceUtils.throwable2StackTrace(var4));
      }
   }

   public DataSource getDataSource() throws SQLException {
      if (this.dataSource != null) {
         return this.dataSource;
      } else {
         return this.dataSourceName != null ? this.lookupDataSource() : null;
      }
   }

   public void setDataSource(DataSource var1) {
      this.checkOp();
      this.dataSource = var1;
      this.preferDataSource = true;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String var1) {
      this.checkOp();
      this.url = var1;
      this.preferDataSource = false;
   }

   boolean isPreferDataSource() {
      return this.preferDataSource;
   }

   public String getUsername() {
      return this.userName;
   }

   public void setUsername(String var1) {
      this.checkOp();
      this.userName = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.checkOp();
      this.password = var1;
   }

   public Connection getConnection() throws SQLException {
      if (this.cachedConnection == null) {
         if (this.preferDataSource) {
            if (this.dataSource == null) {
               if (this.dataSourceName == null) {
                  throw new SQLException("You must call setDataSourceName and provide a valid DataSource JNDI name before attempting JDBC commands.");
               }

               this.lookupDataSource();
            }

            this.cachedConnection = this.dataSource.getConnection();
         } else {
            Properties var1 = new Properties();
            var1.setProperty("user", this.userName);
            var1.setProperty("password", this.password);
            if (this.url != null && this.url.toLowerCase().matches(".*protocol\\s*=\\s*sdp.*")) {
               var1.put("oracle.net.SDP", "true");
            }

            this.cachedConnection = DriverManager.getConnection(this.url, var1);
         }
      }

      if (this.isolationLevel != -1) {
         this.cachedConnection.setTransactionIsolation(this.isolationLevel);
      }

      return this.cachedConnection;
   }

   public int getTransactionIsolation() {
      return this.isolationLevel;
   }

   public void setTransactionIsolation(int var1) throws SQLException {
      this.toDesign();
      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 4:
         case 8:
            this.isolationLevel = var1;
         case -1:
            return;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new SQLException("Invalid value for setTransactionIsolation: " + var1);
      }
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String var1) {
      this.toDesign();
      this.command = var1;
      this.clearParameters();
   }

   public int getQueryTimeout() {
      return this.queryTimeout;
   }

   public void setQueryTimeout(int var1) {
      this.toDesign();
      this.queryTimeout = var1;
   }

   public int getMaxRows() {
      return this.maxRows;
   }

   public void setMaxRows(int var1) {
      this.toDesign();
      this.maxRows = var1;
   }

   public int getMaxFieldSize() {
      return this.maxFieldSize;
   }

   public void setMaxFieldSize(int var1) {
      this.toDesign();
      this.maxFieldSize = var1;
   }

   public boolean getEscapeProcessing() {
      return this.escapeProcessing;
   }

   public void setEscapeProcessing(boolean var1) {
      this.toDesign();
      this.escapeProcessing = var1;
   }

   public int getConcurrency() {
      return this.concurrency;
   }

   public void setConcurrency(int var1) {
      this.toDesign();
      this.concurrency = var1;
   }

   public int getType() {
      return this.resultSetType;
   }

   public void setType(int var1) {
      this.toDesign();
   }

   public int getFetchDirection() {
      return this.fetchDirection;
   }

   public void setFetchDirection(int var1) throws SQLException {
      this.toDesign();
      switch (var1) {
         case 1001:
            if (this.getType() == 1003) {
               throw new SQLException("Cannot set FETCH_REVERSE on a TYPE_FORWARD_ONLY RowSet.");
            }
         case 1000:
         case 1002:
            this.fetchDirection = var1;
            return;
         default:
            throw new SQLException("Unknown value for setFetchDirection:" + var1);
      }
   }

   public int getFetchSize() {
      return this.fetchSize;
   }

   public void setFetchSize(int var1) {
      this.toDesign();
      this.fetchSize = var1;
   }

   public Map getTypeMap() {
      return this.typeMap;
   }

   public void setTypeMap(Map var1) {
      this.toDesign();
      this.typeMap = var1;
   }

   public boolean isComplete() {
      return this.isComplete;
   }

   public void setIsComplete(boolean var1) {
      this.checkOp();
      this.isComplete = var1;
   }

   ArrayList getParameters() {
      return this.params;
   }

   public void clearParameters() {
      this.checkOp();
      this.params.clear();
   }

   public Object[] getParams() throws SQLException {
      Object[] var1 = new Object[this.params.size()];

      for(int var2 = 0; var2 < this.params.size(); ++var2) {
         var1[var2] = ((WLParameter)this.params.get(var2)).getObject();
      }

      return var1;
   }

   public void setString(int var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 39, new Object[]{var2}));
   }

   public void setBoolean(int var1, boolean var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 10, new Object[]{new Boolean(var2)}));
   }

   public void setLong(int var1, long var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 23, new Object[]{new Long(var2)}));
   }

   public void setInt(int var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 22, new Object[]{new Integer(var2)}));
   }

   public void setShort(int var1, short var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 37, new Object[]{new Short(var2)}));
   }

   public void setDouble(int var1, double var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 20, new Object[]{new Double(var2)}));
   }

   public void setFloat(int var1, float var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 21, new Object[]{new Float(var2)}));
   }

   public void setBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 4, new Object[]{var2}));
   }

   public void setBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 6, new Object[]{var2, new Integer(var3)}));
   }

   public void setByte(int var1, byte var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 11, new Object[]{new Byte(var2)}));
   }

   public void setBytes(int var1, byte[] var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 12, new Object[]{var2}));
   }

   public void setNull(int var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 30, new Object[]{new Integer(var2)}));
   }

   public void setNull(int var1, int var2, String var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 31, new Object[]{new Integer(var2), var3}));
   }

   public void setObject(int var1, Object var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 32, new Object[]{var2}));
   }

   public void setObject(int var1, Object var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 33, new Object[]{var2, new Integer(var3)}));
   }

   public void setObject(int var1, Object var2, int var3, int var4) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 34, new Object[]{var2, new Integer(var3), new Integer(var4)}));
   }

   public void setDate(int var1, Date var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 18, new Object[]{var2}));
   }

   public void setDate(int var1, Date var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 19, new Object[]{var2, var3}));
   }

   public void setTime(int var1, Time var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 40, new Object[]{var2}));
   }

   public void setTime(int var1, Time var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 41, new Object[]{var2, var3}));
   }

   public void setTimestamp(int var1, Timestamp var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 42, new Object[]{var2}));
   }

   public void setTimestamp(int var1, Timestamp var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 43, new Object[]{var2, var3}));
   }

   public void setAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 3, new Object[]{var2, new Integer(var3)}));
   }

   public void setCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 14, new Object[]{var2, new Integer(var3)}));
   }

   public void setBlob(int var1, Blob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 7, new Object[]{var2}));
   }

   public void setClob(int var1, Clob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 15, new Object[]{var2}));
   }

   public void setArray(int var1, Array var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 1, new Object[]{var2}));
   }

   public void setRef(int var1, Ref var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 35, new Object[]{var2}));
   }

   public int findColumn(String var1) throws SQLException {
      return this.metaData.findColumn(var1);
   }

   abstract CachedRow currentRow() throws SQLException;

   public boolean wasNull() {
      return this.wasNull;
   }

   public String getString(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof String) {
            this.wasNull = false;
            return (String)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return null;
         } else {
            this.wasNull = false;
            return var2.toString().trim();
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a String");
      }
   }

   public String getString(String var1) throws SQLException {
      return this.getString(this.findColumn(var1));
   }

   public boolean getBoolean(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Boolean) {
            this.wasNull = false;
            return (Boolean)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return false;
         } else {
            this.wasNull = false;
            return Boolean.getBoolean(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a boolean");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a boolean");
      }
   }

   public boolean getBoolean(String var1) throws SQLException {
      return this.getBoolean(this.findColumn(var1));
   }

   public long getLong(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Long) {
            this.wasNull = false;
            return (Long)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0L;
         } else {
            this.wasNull = false;
            return Long.parseLong(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a long");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a long");
      }
   }

   public long getLong(String var1) throws SQLException {
      return this.getLong(this.findColumn(var1));
   }

   public int getInt(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Integer) {
            this.wasNull = false;
            return (Integer)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0;
         } else {
            this.wasNull = false;
            return Integer.parseInt(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to an int");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to an int");
      }
   }

   public int getInt(String var1) throws SQLException {
      return this.getInt(this.findColumn(var1));
   }

   public short getShort(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Short) {
            this.wasNull = false;
            return (Short)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0;
         } else {
            this.wasNull = false;
            return Short.parseShort(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a short");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a short");
      }
   }

   public short getShort(String var1) throws SQLException {
      return this.getShort(this.findColumn(var1));
   }

   public byte getByte(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Byte) {
            this.wasNull = false;
            return (Byte)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0;
         } else {
            this.wasNull = false;
            return Byte.parseByte(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a byte");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a byte");
      }
   }

   public byte getByte(String var1) throws SQLException {
      return this.getByte(this.findColumn(var1));
   }

   public double getDouble(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Double) {
            this.wasNull = false;
            return (Double)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0.0;
         } else {
            this.wasNull = false;
            return Double.parseDouble(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a double");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a double");
      }
   }

   public double getDouble(String var1) throws SQLException {
      return this.getDouble(this.findColumn(var1));
   }

   public float getFloat(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof Float) {
            this.wasNull = false;
            return (Float)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return 0.0F;
         } else {
            this.wasNull = false;
            return Float.parseFloat(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a float");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a float");
      }
   }

   public float getFloat(String var1) throws SQLException {
      return this.getFloat(this.findColumn(var1));
   }

   public BigDecimal getBigDecimal(int var1) throws SQLException {
      this.checkOp();

      try {
         Object var2 = this.currentRow().getColumn(var1);
         if (var2 instanceof BigDecimal) {
            this.wasNull = false;
            return (BigDecimal)var2;
         } else if (var2 == null) {
            this.wasNull = true;
            return null;
         } else {
            this.wasNull = false;
            return new BigDecimal(var2.toString().trim());
         }
      } catch (ClassCastException var3) {
         throw new SQLException("This column cannot be converted to a boolean");
      } catch (NumberFormatException var4) {
         throw new SQLException("This column cannot be converted to a boolean");
      }
   }

   public BigDecimal getBigDecimal(String var1) throws SQLException {
      return this.getBigDecimal(this.findColumn(var1));
   }

   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      BigDecimal var3 = this.getBigDecimal(var1);
      if (var3 != null) {
         var3 = var3.setScale(var2);
      }

      return var3;
   }

   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      return this.getBigDecimal(this.findColumn(var1), var2);
   }

   public byte[] getBytes(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof byte[]) {
         this.wasNull = false;
         return (byte[])((byte[])var2);
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else {
         throw new SQLException("This column cannot be converted to a byte[]");
      }
   }

   public byte[] getBytes(String var1) throws SQLException {
      return this.getBytes(this.findColumn(var1));
   }

   public Object getObject(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      this.wasNull = var2 == null;
      return var2;
   }

   public Object getObject(String var1) throws SQLException {
      return this.getObject(this.findColumn(var1));
   }

   public Date getDate(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof Date) {
         this.wasNull = false;
         return (Date)var2;
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else if (var2 instanceof java.util.Date) {
         this.wasNull = false;
         return new Date(((java.util.Date)var2).getTime());
      } else {
         this.wasNull = false;

         try {
            java.util.Date var3 = DateFormat.getDateInstance().parse(var2.toString());
            return new Date(var3.getTime());
         } catch (java.text.ParseException var4) {
            throw new SQLException(var4.getMessage());
         }
      }
   }

   public Date getDate(String var1) throws SQLException {
      return this.getDate(this.findColumn(var1));
   }

   public Time getTime(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof Time) {
         this.wasNull = false;
         return (Time)var2;
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else if (var2 instanceof java.util.Date) {
         this.wasNull = false;
         return new Time(((java.util.Date)var2).getTime());
      } else {
         try {
            java.util.Date var3 = DateFormat.getDateInstance().parse(var2.toString());
            return new Time(var3.getTime());
         } catch (java.text.ParseException var4) {
            throw new SQLException(var4.getMessage());
         }
      }
   }

   public Time getTime(String var1) throws SQLException {
      return this.getTime(this.findColumn(var1));
   }

   public Timestamp getTimestamp(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof Timestamp) {
         this.wasNull = false;
         return (Timestamp)var2;
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else if (var2 instanceof java.util.Date) {
         this.wasNull = false;
         return new Timestamp(((java.util.Date)var2).getTime());
      } else {
         try {
            java.util.Date var3 = DateFormat.getDateInstance().parse(var2.toString());
            return new Timestamp(var3.getTime());
         } catch (java.text.ParseException var4) {
            throw new SQLException(var4.getMessage());
         }
      }
   }

   public Timestamp getTimestamp(String var1) throws SQLException {
      return this.getTimestamp(this.findColumn(var1));
   }

   public Date getDate(int var1, Calendar var2) throws SQLException {
      this.checkOp();
      Date var3 = this.getDate(var1);
      Calendar var4 = Calendar.getInstance();
      var4.setTime(var3);
      var4.set(1, var2.get(1));
      var4.set(2, var2.get(2));
      var4.set(5, var2.get(5));
      return new Date(var4.getTime().getTime());
   }

   public Date getDate(String var1, Calendar var2) throws SQLException {
      return this.getDate(this.findColumn(var1), var2);
   }

   public Time getTime(int var1, Calendar var2) throws SQLException {
      this.checkOp();
      Time var3 = this.getTime(var1);
      Calendar var4 = Calendar.getInstance();
      var4.setTime(var3);
      var4.set(1, var2.get(1));
      var4.set(2, var2.get(2));
      var4.set(5, var2.get(5));
      return new Time(var4.getTime().getTime());
   }

   public Time getTime(String var1, Calendar var2) throws SQLException {
      return this.getTime(this.findColumn(var1), var2);
   }

   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      this.checkOp();
      Timestamp var3 = this.getTimestamp(var1);
      Calendar var4 = Calendar.getInstance();
      var4.setTime(var3);
      var4.set(1, var2.get(1));
      var4.set(2, var2.get(2));
      var4.set(5, var2.get(5));
      return new Timestamp(var4.getTime().getTime());
   }

   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      return this.getTimestamp(this.findColumn(var1), var2);
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof byte[]) {
         this.wasNull = false;
         return new ByteArrayInputStream((byte[])((byte[])var2));
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else {
         throw new SQLException("This column cannot be converted to a BinaryStream");
      }
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      return this.getBinaryStream(this.findColumn(var1));
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof char[]) {
         this.wasNull = false;
         return new CharArrayReader((char[])((char[])var2));
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else {
         this.wasNull = false;
         return new StringReader(var2.toString());
      }
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      return this.getCharacterStream(this.findColumn(var1));
   }

   public Blob getBlob(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      this.wasNull = var2 == null;
      if (!this.wasNull && var2 instanceof Blob) {
         return (Blob)var2;
      } else if (var2 instanceof String) {
         return new RowSetBlob(((String)var2).getBytes());
      } else if (var2 instanceof byte[]) {
         return new RowSetBlob((byte[])((byte[])var2));
      } else {
         this.currentRow().getColumn(var1).getClass();
         throw new SQLException("This column values are " + var2.getClass().getName() + " by default. " + "This column cannot be converted to a java.sql.Blob");
      }
   }

   public Blob getBlob(String var1) throws SQLException {
      return this.getBlob(this.findColumn(var1));
   }

   public Clob getClob(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof Clob) {
         return (Clob)var2;
      } else if (var2 instanceof String) {
         return new RowSetClob(var2.toString());
      } else {
         throw new SQLException("This column values are " + var2.getClass().getName() + " by default. " + "This column cannot be converted to a java.sql.Clob");
      }
   }

   public Object getObject(int var1, Map var2) throws SQLException {
      this.checkOp();
      Object var3 = this.currentRow().getColumn(var1);
      return var3;
   }

   public Object getObject(String var1, Map var2) throws SQLException {
      return this.getObject(this.findColumn(var1), var2);
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof byte[]) {
         this.wasNull = false;

         try {
            return new ByteArrayInputStream((new String((byte[])((byte[])var2))).getBytes("US-ASCII"));
         } catch (UnsupportedEncodingException var4) {
            throw new SQLException(var4.toString());
         }
      } else if (var2 instanceof char[]) {
         this.wasNull = false;

         try {
            return new ByteArrayInputStream((new String((char[])((char[])var2))).getBytes("US-ASCII"));
         } catch (UnsupportedEncodingException var5) {
            throw new SQLException(var5.toString());
         }
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else {
         throw new SQLException("This column cannot be converted to a AsciiStream");
      }
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      return this.getAsciiStream(this.findColumn(var1));
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof char[]) {
         this.wasNull = false;
         return new ByteArrayInputStream((new String((char[])((char[])var2))).getBytes());
      } else if (var2 == null) {
         this.wasNull = true;
         return null;
      } else {
         this.wasNull = false;
         return new ByteArrayInputStream(var2.toString().getBytes());
      }
   }

   public InputStream getUnicodeStream(String var1) throws SQLException {
      return this.getUnicodeStream(this.findColumn(var1));
   }

   public Clob getClob(String var1) throws SQLException {
      return this.getClob(this.findColumn(var1));
   }

   public Array getArray(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (!(var2 instanceof Array) && !(var2 instanceof SerialArray)) {
         throw new SQLException("This column cannot be converted to a Array");
      } else {
         this.wasNull = false;
         return (Array)var2;
      }
   }

   public Array getArray(String var1) throws SQLException {
      return this.getArray(this.findColumn(var1));
   }

   public Ref getRef(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (!(var2 instanceof Ref) && !(var2 instanceof SerialRef)) {
         if (var2 == null) {
            this.wasNull = true;
            return null;
         } else {
            throw new SQLException("This column cannot be converted to a Ref");
         }
      } else {
         this.wasNull = false;
         return (Ref)var2;
      }
   }

   public Ref getRef(String var1) throws SQLException {
      return this.getRef(this.findColumn(var1));
   }

   public URL getURL(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (!(var2 instanceof URL) && !(var2 instanceof SerialDatalink)) {
         if (var2 instanceof SerialDatalink) {
            this.wasNull = false;
            return ((SerialDatalink)var2).getDatalink();
         } else {
            throw new SQLException("This column cannot be converted to a URL");
         }
      } else {
         this.wasNull = false;
         return (URL)var2;
      }
   }

   public URL getURL(String var1) throws SQLException {
      return this.getURL(this.findColumn(var1));
   }

   abstract void updateCurrent(int var1, Object var2) throws SQLException;

   public void updateNull(int var1) throws SQLException {
      this.updateCurrent(var1, (Object)null);
   }

   public void updateNull(String var1) throws SQLException {
      this.updateNull(this.findColumn(var1));
   }

   public void updateObject(int var1, Object var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateObject(String var1, Object var2) throws SQLException {
      this.updateObject(this.findColumn(var1), var2);
   }

   public void updateObject(int var1, Object var2, int var3) throws SQLException {
      this.metaData.setColumnType(var1, var3);
      this.updateCurrent(var1, var2);
   }

   public void updateObject(String var1, Object var2, int var3) throws SQLException {
      this.updateObject(this.findColumn(var1), var2, var3);
   }

   public void updateString(int var1, String var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateString(String var1, String var2) throws SQLException {
      this.updateString(this.findColumn(var1), var2);
   }

   public void updateBoolean(int var1, boolean var2) throws SQLException {
      this.updateCurrent(var1, new Boolean(var2));
   }

   public void updateBoolean(String var1, boolean var2) throws SQLException {
      this.updateBoolean(this.findColumn(var1), var2);
   }

   public void updateLong(int var1, long var2) throws SQLException {
      this.updateCurrent(var1, new Long(var2));
   }

   public void updateLong(String var1, long var2) throws SQLException {
      this.updateLong(this.findColumn(var1), var2);
   }

   public void updateInt(int var1, int var2) throws SQLException {
      this.updateCurrent(var1, new Integer(var2));
   }

   public void updateInt(String var1, int var2) throws SQLException {
      this.updateInt(this.findColumn(var1), var2);
   }

   public void updateShort(int var1, short var2) throws SQLException {
      this.updateCurrent(var1, new Short(var2));
   }

   public void updateShort(String var1, short var2) throws SQLException {
      this.updateShort(this.findColumn(var1), var2);
   }

   public void updateByte(int var1, byte var2) throws SQLException {
      this.updateCurrent(var1, new Byte(var2));
   }

   public void updateByte(String var1, byte var2) throws SQLException {
      this.updateByte(this.findColumn(var1), var2);
   }

   public void updateDouble(int var1, double var2) throws SQLException {
      this.updateCurrent(var1, new Double(var2));
   }

   public void updateDouble(String var1, double var2) throws SQLException {
      this.updateDouble(this.findColumn(var1), var2);
   }

   public void updateFloat(int var1, float var2) throws SQLException {
      this.updateCurrent(var1, new Float(var2));
   }

   public void updateFloat(String var1, float var2) throws SQLException {
      this.updateFloat(this.findColumn(var1), var2);
   }

   public void updateBytes(int var1, byte[] var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateBytes(String var1, byte[] var2) throws SQLException {
      this.updateBytes(this.findColumn(var1), var2);
   }

   public void updateBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateBigDecimal(String var1, BigDecimal var2) throws SQLException {
      this.updateBigDecimal(this.findColumn(var1), var2);
   }

   public void updateDate(int var1, Date var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateDate(String var1, Date var2) throws SQLException {
      this.updateDate(this.findColumn(var1), var2);
   }

   public void updateTime(int var1, Time var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateTime(String var1, Time var2) throws SQLException {
      this.updateTime(this.findColumn(var1), var2);
   }

   public void updateTimestamp(int var1, Timestamp var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateTimestamp(String var1, Timestamp var2) throws SQLException {
      this.updateTimestamp(this.findColumn(var1), var2);
   }

   private byte[] drain(InputStream var1, byte[] var2) throws SQLException {
      int var3 = 0;

      while(var3 < var2.length) {
         try {
            int var4 = var1.read(var2, var3, var2.length - var3);
            if (var4 == -1) {
               break;
            }

            var3 += var4;
         } catch (IOException var5) {
            JDBCLogger.logStackTrace(var5);
            throw new SQLException(var5.getMessage());
         }
      }

      return var2;
   }

   private char[] drain(Reader var1, char[] var2) throws SQLException {
      int var3 = 0;

      while(var3 < var2.length) {
         try {
            int var4 = var1.read(var2, var3, var2.length - var3);
            if (var4 == -1) {
               break;
            }

            var3 += var4;
         } catch (IOException var5) {
            JDBCLogger.logStackTrace(var5);
            throw new SQLException(var5.getMessage());
         }
      }

      return var2;
   }

   private byte[] drain(InputStream var1) throws SQLException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[8192];

      try {
         while(true) {
            int var4 = var1.read(var3);
            if (var4 == -1) {
               byte[] var15 = var2.toByteArray();
               return var15;
            }

            var2.write(var3, 0, var4);
         }
      } catch (IOException var13) {
         JDBCLogger.logStackTrace(var13);
         throw new SQLException(var13.getMessage());
      } finally {
         try {
            var2.close();
         } catch (IOException var12) {
         }

      }
   }

   private char[] drain(Reader var1) throws SQLException {
      CharArrayWriter var2 = new CharArrayWriter();
      char[] var3 = new char[8192];

      try {
         while(true) {
            int var4 = var1.read(var3);
            if (var4 == -1) {
               char[] var11 = var2.toCharArray();
               return var11;
            }

            var2.write(var3, 0, var4);
         }
      } catch (IOException var9) {
         JDBCLogger.logStackTrace(var9);
         throw new SQLException(var9.getMessage());
      } finally {
         var2.close();
      }
   }

   private String drainContent(InputStream var1) throws SQLException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[8192];

      try {
         while(true) {
            int var4 = var1.read(var3);
            if (var4 == -1) {
               String var15 = var2.toString();
               return var15;
            }

            var2.write(var3, 0, var4);
         }
      } catch (IOException var13) {
         JDBCLogger.logStackTrace(var13);
         throw new SQLException(var13.getMessage());
      } finally {
         try {
            var2.close();
         } catch (IOException var12) {
         }

      }
   }

   public void updateAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateAsciiStream's InputStream was null");
      } else if (var3 <= 0) {
         throw new SQLException("updateAsciiStream's length must be > 0");
      } else {
         this.updateCurrent(var1, new String(this.drain(var2, new byte[var3]), 0, var3));
      }
   }

   public void updateAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.updateAsciiStream(this.findColumn(var1), var2, var3);
   }

   public void updateBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateBinaryStream's InputStream was null");
      } else if (var3 <= 0) {
         throw new SQLException("updateBinaryStream's length must be > 0");
      } else {
         this.updateCurrent(var1, this.drain(var2, new byte[var3]));
      }
   }

   public void updateBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.updateBinaryStream(this.findColumn(var1), var2, var3);
   }

   public void updateCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateCharacterStream's reader was null");
      } else if (var3 <= 0) {
         throw new SQLException("updateCharacterStream's length must be > 0");
      } else {
         this.updateCurrent(var1, this.drain(var2, new char[var3]));
      }
   }

   public void updateCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.updateCharacterStream(this.findColumn(var1), var2, var3);
   }

   public void updateBlob(int var1, Blob var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateBlob(String var1, Blob var2) throws SQLException {
      this.updateBlob(this.findColumn(var1), var2);
   }

   public void updateClob(int var1, Clob var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateClob(String var1, Clob var2) throws SQLException {
      this.updateClob(this.findColumn(var1), var2);
   }

   public void updateRef(int var1, Ref var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateRef(String var1, Ref var2) throws SQLException {
      this.updateRef(this.findColumn(var1), var2);
   }

   public void updateArray(int var1, Array var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateArray(String var1, Array var2) throws SQLException {
      this.updateArray(this.findColumn(var1), var2);
   }

   public void addRowSetListener(RowSetListener var1) {
      this.rowSetListeners.add(var1);
   }

   public void removeRowSetListener(RowSetListener var1) {
      this.rowSetListeners.remove(var1);
   }

   void cursorMoved() {
      RowSetEvent var1 = new RowSetEvent((RowSet)this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.cursorMoved(var1);
      }

   }

   void rowChanged() {
      RowSetEvent var1 = new RowSetEvent((RowSet)this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.rowChanged(var1);
      }

   }

   void rowSetChanged() {
      RowSetEvent var1 = new RowSetEvent((RowSet)this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.rowSetChanged(var1);
      }

   }

   public void setWriter(RowSetWriter var1) {
      this.writer = var1;
   }

   public SyncProvider getSyncProvider() throws SQLException {
      return this.provider;
   }

   public void setSyncProvider(String var1) throws SQLException {
      if (var1 == null) {
         this.provider = new WLSyncProvider();
      } else {
         this.provider = SyncFactory.getInstance(var1);
         if (this.provider == null) {
            throw new SQLException("Can not find SyncProvider for " + var1 + ".");
         }
      }

      this.reader = this.provider.getRowSetReader();
      this.writer = this.provider.getRowSetWriter();
   }

   public int[] getMatchColumnIndexes() throws SQLException {
      return this.metaData.getMatchColumns();
   }

   public String[] getMatchColumnNames() throws SQLException {
      int[] var1 = this.metaData.getMatchColumns();
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.metaData.getColumnName(var1[var3]);
      }

      return var2;
   }

   public void setMatchColumn(int var1) throws SQLException {
      this.checkOp();
      this.metaData.setMatchColumn(var1, true);
   }

   public void setMatchColumn(String var1) throws SQLException {
      this.metaData.setMatchColumn(this.findColumn(var1), true);
   }

   public void setMatchColumn(int[] var1) throws SQLException {
      this.checkOp();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.setMatchColumn(var1[var2]);
      }

   }

   public void setMatchColumn(String[] var1) throws SQLException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.setMatchColumn(var1[var2]);
      }

   }

   public void unsetMatchColumn(int var1) throws SQLException {
      this.checkOp();
      this.metaData.setMatchColumn(var1, false);
   }

   public void unsetMatchColumn(String var1) throws SQLException {
      this.metaData.setMatchColumn(this.findColumn(var1), false);
   }

   public void unsetMatchColumn(int[] var1) throws SQLException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.unsetMatchColumn(var1[var2]);
      }

   }

   public void unsetMatchColumn(String[] var1) throws SQLException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.unsetMatchColumn(var1[var2]);
      }

   }

   void checkOp() {
      if (this.locked) {
         throw new RuntimeException("This operation is disabled because there are open SharedRowSet objects associated with this CachedRowSet object. Please call SharedRowSet.close() to detach it.");
      }
   }

   void checkOp(int var1) {
      this.checkOp();
      this.state = this.state.checkOp(var1);
   }

   void checkIterator() throws SQLException {
      this.checkOp(4);
   }

   void toConfigQuery() {
      this.checkOp(1);
   }

   void toDesign() {
      this.checkOp(0);
   }

   void isJoinable(Class var1) throws SQLException {
      if (!Number.class.isAssignableFrom(var1) && !Boolean.class.isAssignableFrom(var1) && !java.util.Date.class.isAssignableFrom(var1) && !String.class.isAssignableFrom(var1)) {
         throw new SQLException(var1 + " is not a supported Joinable data type");
      }
   }

   void isJoinable(Class var1, Class var2) throws SQLException {
      if (Number.class.isAssignableFrom(var1)) {
         if (!Boolean.class.isAssignableFrom(var2) && !java.util.Date.class.isAssignableFrom(var2)) {
            if (!Number.class.isAssignableFrom(var2) && !String.class.isAssignableFrom(var2)) {
               throw new SQLException(var2 + " is not a supported Joinable data type");
            }
         } else {
            throw new SQLException(var2 + " can not be joined with " + var1);
         }
      } else if (Boolean.class.isAssignableFrom(var1)) {
         if (!Number.class.isAssignableFrom(var2) && !java.util.Date.class.isAssignableFrom(var2)) {
            if (!Boolean.class.isAssignableFrom(var2) && !String.class.isAssignableFrom(var2)) {
               throw new SQLException(var2 + " is not a supported Joinable data type");
            }
         } else {
            throw new SQLException(var2 + " can not be compared with " + var1);
         }
      } else if (java.util.Date.class.isAssignableFrom(var1)) {
         if (!Number.class.isAssignableFrom(var2) && !Boolean.class.isAssignableFrom(var2)) {
            if (!java.util.Date.class.isAssignableFrom(var2) && !String.class.isAssignableFrom(var2)) {
               throw new SQLException(var2 + " is not a supported Joinable data type");
            }
         } else {
            throw new SQLException(var2 + " can not be compared with " + var1);
         }
      } else if (!String.class.isAssignableFrom(var1)) {
         throw new SQLException(var1 + " is not a supported Joinable data type");
      }
   }

   public RowId getRowId(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof RowId) {
         this.wasNull = false;
         return (RowId)var2;
      } else {
         throw new SQLException("This column cannot be converted to a RowId");
      }
   }

   public RowId getRowId(String var1) throws SQLException {
      return this.getRowId(this.findColumn(var1));
   }

   public int getHoldability() throws SQLException {
      return 1;
   }

   public boolean isClosed() throws SQLException {
      return this.isClosed;
   }

   public NClob getNClob(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof NClob) {
         this.wasNull = false;
         return (NClob)var2;
      } else if (var2 instanceof String) {
         return new RowSetNClob(var2.toString());
      } else {
         throw new SQLException("This column values are " + var2.getClass().getName() + " by default. " + "This column cannot be converted to a NClob");
      }
   }

   public NClob getNClob(String var1) throws SQLException {
      return this.getNClob(this.findColumn(var1));
   }

   public SQLXML getSQLXML(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof SQLXML) {
         this.wasNull = false;
         return (SQLXML)var2;
      } else {
         throw new SQLException("This column cannot be converted to a SQLXML");
      }
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      return this.getCharacterStream(var1);
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      return this.getNCharacterStream(this.findColumn(var1));
   }

   public String getNString(int var1) throws SQLException {
      this.checkOp();
      Object var2 = this.currentRow().getColumn(var1);
      if (var2 instanceof String) {
         this.wasNull = false;
         return (String)var2;
      } else {
         throw new SQLException("This column cannot be converted to a String");
      }
   }

   public String getNString(String var1) throws SQLException {
      return this.getNString(this.findColumn(var1));
   }

   public SQLXML getSQLXML(String var1) throws SQLException {
      return this.getSQLXML(this.findColumn(var1));
   }

   public void updateRowId(int var1, RowId var2) throws SQLException {
      throw new SQLException("RowId is only valid within the transaction it is created.");
   }

   public void updateRowId(String var1, RowId var2) throws SQLException {
      throw new SQLException("RowId is only valid within the transaction it is created.");
   }

   public void updateNString(int var1, String var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateNString(String var1, String var2) throws SQLException {
      this.updateNString(this.findColumn(var1), var2);
   }

   public void updateNClob(int var1, NClob var2) throws SQLException {
      this.updateClob(var1, (Clob)var2);
   }

   public void updateNClob(String var1, NClob var2) throws SQLException {
      this.updateNClob(this.findColumn(var1), var2);
   }

   public void updateSQLXML(int var1, SQLXML var2) throws SQLException {
      this.updateCurrent(var1, var2);
   }

   public void updateSQLXML(String var1, SQLXML var2) throws SQLException {
      this.updateSQLXML(this.findColumn(var1), var2);
   }

   public void updateNCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateCharacterStream's reader was null");
      } else if (var3 <= 0) {
         throw new SQLException("updateCharacterStream's length must be > 0");
      } else {
         this.updateCurrent(var1, this.drain(var2, new char[var3]));
      }
   }

   public void updateNCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.updateNCharacterStream(this.findColumn(var1), var2, var3);
   }

   public void setNClob(int var1, NClob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 26, new Object[]{var2}));
   }

   public void setNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 25, new Object[]{var2, var3}));
   }

   public void setNString(int var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 29, new Object[]{var2}));
   }

   public void setSQLXML(int var1, SQLXML var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 38, new Object[]{var2}));
   }

   public void setRowId(int var1, RowId var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 36, new Object[]{var2}));
   }

   public void setSQLXML(String var1, SQLXML var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 38, new Object[]{var2}));
   }

   public void setRowId(String var1, RowId var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 36, new Object[]{var2}));
   }

   public void setNString(String var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 29, new Object[]{var2}));
   }

   public void setNClob(String var1, NClob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 26, new Object[]{var2}));
   }

   public void setNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 25, new Object[]{var2, new Long(var3)}));
   }

   public void updateAsciiStream(int var1, InputStream var2) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateAsciiStream's InputStream was null");
      } else {
         this.updateCurrent(var1, this.drainContent(var2));
      }
   }

   public void updateAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      if (var3 > 2147483647L) {
         throw new SQLException("updateAsciiStream's length must be <= 2147483647");
      } else {
         this.updateAsciiStream(var1, var2, (int)var3);
      }
   }

   public void updateAsciiStream(String var1, InputStream var2) throws SQLException {
      this.updateAsciiStream(this.findColumn(var1), var2);
   }

   public void updateAsciiStream(String var1, InputStream var2, long var3) throws SQLException {
      this.updateAsciiStream(this.findColumn(var1), var2, var3);
   }

   public void updateBinaryStream(int var1, InputStream var2) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateBinaryStream's InputStream was null");
      } else {
         this.updateCurrent(var1, this.drain(var2));
      }
   }

   public void updateBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      if (var3 > 2147483647L) {
         throw new SQLException("updateBinaryStream's length must be <= 2147483647");
      } else {
         this.updateBinaryStream(var1, var2, (int)var3);
      }
   }

   public void updateBinaryStream(String var1, InputStream var2) throws SQLException {
      this.updateBinaryStream(this.findColumn(var1), var2);
   }

   public void updateBinaryStream(String var1, InputStream var2, long var3) throws SQLException {
      this.updateBinaryStream(this.findColumn(var1), var2, var3);
   }

   public void updateBlob(int var1, InputStream var2) throws SQLException {
      this.updateBinaryStream(var1, var2);
   }

   public void updateBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.updateBinaryStream(var1, var2, var3);
   }

   public void updateBlob(String var1, InputStream var2) throws SQLException {
      this.updateBlob(this.findColumn(var1), var2);
   }

   public void updateBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.updateBlob(this.findColumn(var1), var2, var3);
   }

   public void updateCharacterStream(int var1, Reader var2) throws SQLException {
      if (var2 == null) {
         throw new SQLException("updateCharacterStream's reader was null");
      } else {
         this.updateCurrent(var1, this.drain(var2));
      }
   }

   public void updateCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      if (var3 > 2147483647L) {
         throw new SQLException("updateCharacterStream's length must be <= 2147483647");
      } else {
         this.updateCharacterStream(var1, var2, (int)var3);
      }
   }

   public void updateCharacterStream(String var1, Reader var2) throws SQLException {
      this.updateCharacterStream(this.findColumn(var1), var2);
   }

   public void updateCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.updateCharacterStream(this.findColumn(var1), var2, var3);
   }

   public void updateClob(int var1, Reader var2) throws SQLException {
      this.updateCharacterStream(var1, var2);
   }

   public void updateClob(int var1, Reader var2, long var3) throws SQLException {
      this.updateCharacterStream(var1, var2, var3);
   }

   public void updateClob(String var1, Reader var2) throws SQLException {
      this.updateClob(this.findColumn(var1), var2);
   }

   public void updateClob(String var1, Reader var2, long var3) throws SQLException {
      this.updateClob(this.findColumn(var1), var2, var3);
   }

   public void updateNCharacterStream(int var1, Reader var2) throws SQLException {
      this.updateCharacterStream(var1, var2);
   }

   public void updateNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.updateCharacterStream(var1, var2, var3);
   }

   public void updateNCharacterStream(String var1, Reader var2) throws SQLException {
      this.updateNCharacterStream(this.findColumn(var1), var2);
   }

   public void updateNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.updateNCharacterStream(this.findColumn(var1), var2, var3);
   }

   public void updateNClob(int var1, Reader var2) throws SQLException {
      this.updateClob(var1, var2);
   }

   public void updateNClob(int var1, Reader var2, long var3) throws SQLException {
      this.updateClob(var1, var2, var3);
   }

   public void updateNClob(String var1, Reader var2) throws SQLException {
      this.updateNClob(this.findColumn(var1), var2);
   }

   public void updateNClob(String var1, Reader var2, long var3) throws SQLException {
      this.updateNClob(this.findColumn(var1), var2, var3);
   }

   public void setAsciiStream(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 2, new Object[]{var2}));
   }

   public void setAsciiStream(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 2, new Object[]{var2}));
   }

   public void setAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 3, new Object[]{var2, new Integer(var3)}));
   }

   public void setBigDecimal(String var1, BigDecimal var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 4, new Object[]{var2}));
   }

   public void setBinaryStream(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 5, new Object[]{var2}));
   }

   public void setBinaryStream(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 5, new Object[]{var2}));
   }

   public void setBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 6, new Object[]{var2, new Integer(var3)}));
   }

   public void setBlob(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 8, new Object[]{var2}));
   }

   public void setBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 9, new Object[]{var2, new Long(var3)}));
   }

   public void setBlob(String var1, Blob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 7, new Object[]{var2}));
   }

   public void setBlob(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 8, new Object[]{var2}));
   }

   public void setBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 9, new Object[]{var2, new Long(var3)}));
   }

   public void setBoolean(String var1, boolean var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 10, new Object[]{new Boolean(var2)}));
   }

   public void setByte(String var1, byte var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 11, new Object[]{new Byte(var2)}));
   }

   public void setBytes(String var1, byte[] var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 12, new Object[]{var2}));
   }

   public void setCharacterStream(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 13, new Object[]{var2}));
   }

   public void setCharacterStream(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 13, new Object[]{var2}));
   }

   public void setCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 14, new Object[]{var2, new Integer(var3)}));
   }

   public void setClob(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 16, new Object[]{var2}));
   }

   public void setClob(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 17, new Object[]{var2, new Long(var3)}));
   }

   public void setClob(String var1, Clob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 15, new Object[]{var2}));
   }

   public void setClob(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 16, new Object[]{var2}));
   }

   public void setClob(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 17, new Object[]{var2, new Long(var3)}));
   }

   public void setDate(String var1, Date var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 18, new Object[]{var2}));
   }

   public void setDate(String var1, Date var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 19, new Object[]{var2, var3}));
   }

   public void setDouble(String var1, double var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 20, new Object[]{new Double(var2)}));
   }

   public void setFloat(String var1, float var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 21, new Object[]{new Float(var2)}));
   }

   public void setInt(String var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 22, new Object[]{new Integer(var2)}));
   }

   public void setLong(String var1, long var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 23, new Object[]{new Long(var2)}));
   }

   public void setNCharacterStream(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 24, new Object[]{var2}));
   }

   public void setNCharacterStream(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 24, new Object[]{var2}));
   }

   public void setNClob(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 27, new Object[]{var2}));
   }

   public void setNClob(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 28, new Object[]{var2, new Long(var3)}));
   }

   public void setNClob(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 27, new Object[]{var2}));
   }

   public void setNClob(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 28, new Object[]{var2, new Long(var3)}));
   }

   public void setNull(String var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 30, new Object[]{new Integer(var2)}));
   }

   public void setNull(String var1, int var2, String var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 31, new Object[]{new Integer(var2), var3}));
   }

   public void setObject(String var1, Object var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 32, new Object[]{var2}));
   }

   public void setObject(String var1, Object var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 33, new Object[]{var2, new Integer(var3)}));
   }

   public void setObject(String var1, Object var2, int var3, int var4) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 34, new Object[]{var2, new Integer(var3), new Integer(var4)}));
   }

   public void setShort(String var1, short var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 37, new Object[]{new Short(var2)}));
   }

   public void setString(String var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 39, new Object[]{var2}));
   }

   public void setTime(String var1, Time var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 40, new Object[]{var2}));
   }

   public void setTime(String var1, Time var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 41, new Object[]{var2, var3}));
   }

   public void setTimestamp(String var1, Timestamp var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 42, new Object[]{var2}));
   }

   public void setTimestamp(String var1, Timestamp var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(0, var1, 43, new Object[]{var2, var3}));
   }

   public void setURL(int var1, URL var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 44, new Object[]{var2}));
   }
}
