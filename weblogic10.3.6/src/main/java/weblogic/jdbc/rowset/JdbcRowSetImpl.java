package weblogic.jdbc.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
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
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetWarning;
import weblogic.utils.StackTraceUtils;

public class JdbcRowSetImpl implements JdbcRowSet {
   String url;
   String userName;
   String password;
   String dataSourceName;
   boolean preferDataSource;
   transient DataSource dataSource;
   transient Connection cachedConnection;
   String command;
   transient ArrayList<WLParameter> params = new ArrayList();
   int isolationLevel = -1;
   int fetchDirection;
   int fetchSize;
   int concurrency = 1008;
   int resultSetType = 1004;
   Map<String, Class<?>> typeMap;
   int queryTimeout;
   int maxRows;
   int maxFieldSize;
   boolean escapeProcessing;
   boolean showDeleted;
   boolean isComplete;
   boolean wasNull;
   private Connection conn = null;
   private CallableStatement cstmt = null;
   private ResultSet rs = null;
   CachedRowSetMetaData metaData = null;
   LifeCycle.State state;
   List<RowSetListener> rowSetListeners;

   public JdbcRowSetImpl() {
      this.state = LifeCycle.DESIGNING;
      this.rowSetListeners = new ArrayList();
   }

   public JdbcRowSetImpl(Connection var1) {
      this.state = LifeCycle.DESIGNING;
      this.rowSetListeners = new ArrayList();
      throw new RuntimeException("Method not implemented");
   }

   public JdbcRowSetImpl(ResultSet var1) {
      this.state = LifeCycle.DESIGNING;
      this.rowSetListeners = new ArrayList();
      throw new RuntimeException("Method not implemented");
   }

   public JdbcRowSetImpl(String var1, String var2, String var3) {
      this.state = LifeCycle.DESIGNING;
      this.rowSetListeners = new ArrayList();
      throw new RuntimeException("Method not implemented");
   }

   void checkOp(int var1) {
      this.state = this.state.checkOp(var1);
   }

   public void addRowSetListener(RowSetListener var1) {
      this.rowSetListeners.add(var1);
   }

   public void removeRowSetListener(RowSetListener var1) {
      this.rowSetListeners.remove(var1);
   }

   void cursorMoved() {
      RowSetEvent var1 = new RowSetEvent(this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.cursorMoved(var1);
      }

   }

   void rowChanged() {
      RowSetEvent var1 = new RowSetEvent(this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.rowChanged(var1);
      }

   }

   void rowSetChanged() {
      RowSetEvent var1 = new RowSetEvent(this);
      Iterator var2 = this.rowSetListeners.iterator();

      while(var2.hasNext()) {
         RowSetListener var3 = (RowSetListener)var2.next();
         var3.rowSetChanged(var1);
      }

   }

   public ResultSetMetaData getMetaData() throws SQLException {
      return this.metaData;
   }

   public boolean isReadOnly() {
      return this.metaData.isReadOnly();
   }

   public void setReadOnly(boolean var1) {
      this.metaData.setReadOnly(var1);
   }

   public String getTableName() throws SQLException {
      return this.metaData.getWriteTableName();
   }

   public void setTableName(String var1) throws SQLException {
      this.metaData.setWriteTableName(var1);
   }

   public int[] getKeyColumns() throws SQLException {
      return this.metaData.getKeyColumns();
   }

   public void setKeyColumns(int[] var1) throws SQLException {
      this.metaData.setKeyColumns(var1);
   }

   public boolean getShowDeleted() throws SQLException {
      return this.showDeleted;
   }

   public void setShowDeleted(boolean var1) throws SQLException {
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
      return this.rs.getCursorName();
   }

   public Statement getStatement() throws SQLException {
      return this.rs.getStatement();
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
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
      this.dataSource = var1;
      this.preferDataSource = true;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String var1) {
      this.url = var1;
      this.preferDataSource = false;
   }

   public String getUsername() {
      return this.userName;
   }

   public void setUsername(String var1) {
      this.userName = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public Connection getConnection() throws SQLException {
      if (this.conn == null) {
         if (this.preferDataSource) {
            if (this.dataSource == null) {
               if (this.dataSourceName == null) {
                  throw new SQLException("You must call setDataSourceName and provide a valid DataSource JNDI name before attempting JDBC commands.");
               }

               this.lookupDataSource();
            }

            this.conn = this.dataSource.getConnection();
         } else {
            Properties var1 = new Properties();
            var1.setProperty("user", this.userName);
            var1.setProperty("password", this.password);
            if (this.url != null && this.url.toLowerCase().matches(".*protocol\\s*=\\s*sdp.*")) {
               var1.put("oracle.net.SDP", "true");
            }

            this.conn = DriverManager.getConnection(this.url, var1);
         }
      }

      if (this.isolationLevel != -1) {
         this.conn.setTransactionIsolation(this.isolationLevel);
      }

      return this.conn;
   }

   void toDesign() {
      this.checkOp(0);
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
      this.resultSetType = var1;
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

   public Map<String, Class<?>> getTypeMap() {
      return this.typeMap;
   }

   public void setTypeMap(Map<String, Class<?>> var1) {
      this.toDesign();
      this.typeMap = var1;
   }

   public boolean isComplete() {
      return this.isComplete;
   }

   public void setIsComplete(boolean var1) {
      this.isComplete = var1;
   }

   ArrayList getParameters() {
      return this.params;
   }

   public void clearParameters() {
      this.params.clear();
   }

   public Object[] getParams() throws SQLException {
      Object[] var1 = new Object[this.params.size()];

      for(int var2 = 0; var2 < this.params.size(); ++var2) {
         var1[var2] = ((WLParameter)this.params.get(var2)).getObject();
      }

      return var1;
   }

   public int getRow() throws SQLException {
      return this.rs.getRow();
   }

   public void moveToInsertRow() throws SQLException {
      this.checkOp(5);
      this.rs.moveToInsertRow();
   }

   public void moveToCurrentRow() throws SQLException {
      this.checkOp(6);
      this.rs.moveToCurrentRow();
   }

   public boolean absolute(int var1) throws SQLException {
      this.checkOp(4);
      return this.rs.absolute(var1);
   }

   public void beforeFirst() throws SQLException {
      this.checkOp(4);
      this.rs.beforeFirst();
   }

   public boolean first() throws SQLException {
      this.checkOp(4);
      return this.rs.first();
   }

   public boolean last() throws SQLException {
      this.checkOp(4);
      return this.rs.last();
   }

   public void afterLast() throws SQLException {
      this.checkOp(4);
      this.rs.afterLast();
   }

   public boolean relative(int var1) throws SQLException {
      this.checkOp(4);
      return this.rs.relative(var1);
   }

   public boolean next() throws SQLException {
      this.checkOp(4);
      return this.rs.next();
   }

   public boolean previous() throws SQLException {
      this.checkOp(4);
      return this.rs.previous();
   }

   public boolean isBeforeFirst() throws SQLException {
      this.checkOp(4);
      return this.rs.isBeforeFirst();
   }

   public boolean isAfterLast() throws SQLException {
      this.checkOp(4);
      return this.rs.isAfterLast();
   }

   public boolean isFirst() throws SQLException {
      this.checkOp(4);
      return this.rs.isFirst();
   }

   public boolean isLast() throws SQLException {
      this.checkOp(4);
      return this.rs.isLast();
   }

   public boolean rowUpdated() throws SQLException {
      return this.rs.rowUpdated();
   }

   public boolean rowInserted() throws SQLException {
      return this.rs.rowInserted();
   }

   public boolean rowDeleted() throws SQLException {
      return this.rs.rowDeleted();
   }

   void toConfigQuery() {
      this.checkOp(1);
   }

   public void setArray(int var1, Array var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 1, new Object[]{var2}));
   }

   public void setAsciiStream(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 2, new Object[]{var2}));
   }

   public void setAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 3, new Object[]{var2, new Integer(var3)}));
   }

   public void setAsciiStream(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 2, new Object[]{var2}));
   }

   public void setAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 3, new Object[]{var2, new Integer(var3)}));
   }

   public void setBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 4, new Object[]{var2}));
   }

   public void setBigDecimal(String var1, BigDecimal var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 4, new Object[]{var2}));
   }

   public void setBinaryStream(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 5, new Object[]{var2}));
   }

   public void setBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 6, new Object[]{var2, new Integer(var3)}));
   }

   public void setBinaryStream(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 5, new Object[]{var2}));
   }

   public void setBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 6, new Object[]{var2, new Integer(var3)}));
   }

   public void setBlob(int var1, Blob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 7, new Object[]{var2}));
   }

   public void setBlob(int var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 8, new Object[]{var2}));
   }

   public void setBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 9, new Object[]{var2, new Long(var3)}));
   }

   public void setBlob(String var1, Blob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 7, new Object[]{var2}));
   }

   public void setBlob(String var1, InputStream var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 8, new Object[]{var2}));
   }

   public void setBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 9, new Object[]{var2, new Long(var3)}));
   }

   public void setBoolean(int var1, boolean var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 10, new Object[]{new Boolean(var2)}));
   }

   public void setBoolean(String var1, boolean var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 10, new Object[]{new Boolean(var2)}));
   }

   public void setByte(int var1, byte var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 11, new Object[]{new Byte(var2)}));
   }

   public void setByte(String var1, byte var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 11, new Object[]{new Byte(var2)}));
   }

   public void setBytes(int var1, byte[] var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 12, new Object[]{var2}));
   }

   public void setBytes(String var1, byte[] var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 12, new Object[]{var2}));
   }

   public void setCharacterStream(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 13, new Object[]{var2}));
   }

   public void setCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 14, new Object[]{var2, new Integer(var3)}));
   }

   public void setCharacterStream(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 13, new Object[]{var2}));
   }

   public void setCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 14, new Object[]{var2, new Integer(var3)}));
   }

   public void setClob(int var1, Clob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, (String)null, 15, new Object[]{var2}));
   }

   public void setClob(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 16, new Object[]{var2}));
   }

   public void setClob(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 17, new Object[]{var2, new Long(var3)}));
   }

   public void setClob(String var1, Clob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 15, new Object[]{var2}));
   }

   public void setClob(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 16, new Object[]{var2}));
   }

   public void setClob(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 17, new Object[]{var2, new Long(var3)}));
   }

   public void setDate(int var1, Date var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 18, new Object[]{var2}));
   }

   public void setDate(int var1, Date var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 19, new Object[]{var2, var3}));
   }

   public void setDate(String var1, Date var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 18, new Object[]{var2}));
   }

   public void setDate(String var1, Date var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 19, new Object[]{var2, var3}));
   }

   public void setDouble(int var1, double var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 20, new Object[]{new Double(var2)}));
   }

   public void setDouble(String var1, double var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 20, new Object[]{new Double(var2)}));
   }

   public void setFloat(int var1, float var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 21, new Object[]{new Float(var2)}));
   }

   public void setFloat(String var1, float var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 21, new Object[]{new Float(var2)}));
   }

   public void setInt(int var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 22, new Object[]{new Integer(var2)}));
   }

   public void setInt(String var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 22, new Object[]{new Integer(var2)}));
   }

   public void setLong(int var1, long var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 23, new Object[]{new Long(var2)}));
   }

   public void setLong(String var1, long var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 23, new Object[]{new Long(var2)}));
   }

   public void setNCharacterStream(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 24, new Object[]{var2}));
   }

   public void setNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 25, new Object[]{var2, new Long(var3)}));
   }

   public void setNCharacterStream(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 24, new Object[]{var2}));
   }

   public void setNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 25, new Object[]{var2, new Long(var3)}));
   }

   public void setNClob(int var1, NClob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 26, new Object[]{var2}));
   }

   public void setNClob(int var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 27, new Object[]{var2}));
   }

   public void setNClob(int var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 28, new Object[]{var2, new Long(var3)}));
   }

   public void setNClob(String var1, NClob var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 26, new Object[]{var2}));
   }

   public void setNClob(String var1, Reader var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 27, new Object[]{var2}));
   }

   public void setNClob(String var1, Reader var2, long var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 28, new Object[]{var2, new Long(var3)}));
   }

   public void setNString(int var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 29, new Object[]{var2}));
   }

   public void setNString(String var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 29, new Object[]{var2}));
   }

   public void setNull(int var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 30, new Object[]{new Integer(var2)}));
   }

   public void setNull(int var1, int var2, String var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 31, new Object[]{new Integer(var2), var3}));
   }

   public void setNull(String var1, int var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 30, new Object[]{new Integer(var2)}));
   }

   public void setNull(String var1, int var2, String var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 31, new Object[]{new Integer(var2), var3}));
   }

   public void setObject(int var1, Object var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 32, new Object[]{var2}));
   }

   public void setObject(int var1, Object var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 33, new Object[]{var2, new Integer(var3)}));
   }

   public void setObject(int var1, Object var2, int var3, int var4) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 34, new Object[]{var2, new Integer(var3), new Integer(var4)}));
   }

   public void setObject(String var1, Object var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 32, new Object[]{var2}));
   }

   public void setObject(String var1, Object var2, int var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 33, new Object[]{var2, new Integer(var3)}));
   }

   public void setObject(String var1, Object var2, int var3, int var4) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 34, new Object[]{var2, new Integer(var3), new Integer(var4)}));
   }

   public void setRef(int var1, Ref var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 35, new Object[]{var2}));
   }

   public void setRowId(int var1, RowId var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 36, new Object[]{var2}));
   }

   public void setRowId(String var1, RowId var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 36, new Object[]{var2}));
   }

   public void setShort(int var1, short var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 37, new Object[]{new Short(var2)}));
   }

   public void setShort(String var1, short var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 37, new Object[]{new Short(var2)}));
   }

   public void setSQLXML(int var1, SQLXML var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 38, new Object[]{var2}));
   }

   public void setSQLXML(String var1, SQLXML var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 38, new Object[]{var2}));
   }

   public void setString(int var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 39, new Object[]{var2}));
   }

   public void setString(String var1, String var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 39, new Object[]{var2}));
   }

   public void setTime(int var1, Time var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 40, new Object[]{var2}));
   }

   public void setTime(int var1, Time var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 41, new Object[]{var2, var3}));
   }

   public void setTime(String var1, Time var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 40, new Object[]{var2}));
   }

   public void setTime(String var1, Time var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 41, new Object[]{var2, var3}));
   }

   public void setTimestamp(int var1, Timestamp var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 42, new Object[]{var2}));
   }

   public void setTimestamp(int var1, Timestamp var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 43, new Object[]{var2, var3}));
   }

   public void setTimestamp(String var1, Timestamp var2) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 42, new Object[]{var2}));
   }

   public void setTimestamp(String var1, Timestamp var2, Calendar var3) throws SQLException {
      this.toConfigQuery();
      this.params.add(new WLParameter(var1, 43, new Object[]{var2, var3}));
   }

   public void setURL(int var1, URL var2) throws SQLException {
      this.params.add(new WLParameter(var1, 44, new Object[]{var2}));
   }

   public Array getArray(int var1) throws SQLException {
      return this.rs.getArray(var1);
   }

   public Array getArray(String var1) throws SQLException {
      return this.rs.getArray(var1);
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      return this.rs.getAsciiStream(var1);
   }

   public InputStream getAsciiStream(String var1) throws SQLException {
      return this.rs.getAsciiStream(var1);
   }

   public BigDecimal getBigDecimal(int var1) throws SQLException {
      return this.rs.getBigDecimal(var1);
   }

   /** @deprecated */
   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      return this.rs.getBigDecimal(var1, var2);
   }

   public BigDecimal getBigDecimal(String var1) throws SQLException {
      return this.rs.getBigDecimal(var1);
   }

   /** @deprecated */
   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      return this.rs.getBigDecimal(var1, var2);
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      return this.rs.getBinaryStream(var1);
   }

   public InputStream getBinaryStream(String var1) throws SQLException {
      return this.rs.getBinaryStream(var1);
   }

   public Blob getBlob(int var1) throws SQLException {
      try {
         return this.rs.getBlob(var1);
      } catch (SQLException var4) {
         byte[] var3 = this.rs.getBytes(var1);
         return new RowSetBlob(var3);
      }
   }

   public Blob getBlob(String var1) throws SQLException {
      try {
         return this.rs.getBlob(var1);
      } catch (SQLException var4) {
         byte[] var3 = this.rs.getBytes(var1);
         return new RowSetBlob(var3);
      }
   }

   public boolean getBoolean(int var1) throws SQLException {
      return this.rs.getBoolean(var1);
   }

   public boolean getBoolean(String var1) throws SQLException {
      return this.rs.getBoolean(var1);
   }

   public byte getByte(int var1) throws SQLException {
      return this.rs.getByte(var1);
   }

   public byte getByte(String var1) throws SQLException {
      return this.rs.getByte(var1);
   }

   public byte[] getBytes(int var1) throws SQLException {
      return this.rs.getBytes(var1);
   }

   public byte[] getBytes(String var1) throws SQLException {
      return this.rs.getBytes(var1);
   }

   public Reader getCharacterStream(int var1) throws SQLException {
      return this.rs.getCharacterStream(var1);
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      return this.rs.getCharacterStream(var1);
   }

   public Clob getClob(int var1) throws SQLException {
      try {
         return this.rs.getClob(var1);
      } catch (SQLException var4) {
         String var3 = this.rs.getString(var1);
         return new RowSetClob(var3);
      }
   }

   public Clob getClob(String var1) throws SQLException {
      try {
         return this.rs.getClob(var1);
      } catch (SQLException var4) {
         String var3 = this.rs.getString(var1);
         return new RowSetClob(var3);
      }
   }

   public Date getDate(int var1) throws SQLException {
      return this.rs.getDate(var1);
   }

   public Date getDate(int var1, Calendar var2) throws SQLException {
      return this.rs.getDate(var1, var2);
   }

   public Date getDate(String var1) throws SQLException {
      return this.rs.getDate(var1);
   }

   public Date getDate(String var1, Calendar var2) throws SQLException {
      return this.rs.getDate(var1, var2);
   }

   public double getDouble(int var1) throws SQLException {
      return this.rs.getDouble(var1);
   }

   public double getDouble(String var1) throws SQLException {
      return this.rs.getDouble(var1);
   }

   public float getFloat(int var1) throws SQLException {
      return this.rs.getFloat(var1);
   }

   public float getFloat(String var1) throws SQLException {
      return this.rs.getFloat(var1);
   }

   public int getInt(int var1) throws SQLException {
      return this.rs.getInt(var1);
   }

   public int getInt(String var1) throws SQLException {
      return this.rs.getInt(var1);
   }

   public long getLong(int var1) throws SQLException {
      return this.rs.getLong(var1);
   }

   public long getLong(String var1) throws SQLException {
      return this.rs.getLong(var1);
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      return this.rs.getNCharacterStream(var1);
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      return this.rs.getNCharacterStream(var1);
   }

   public NClob getNClob(int var1) throws SQLException {
      try {
         return this.rs.getNClob(var1);
      } catch (SQLException var4) {
         String var3 = this.rs.getString(var1);
         return new RowSetNClob(var3);
      }
   }

   public NClob getNClob(String var1) throws SQLException {
      try {
         return this.rs.getNClob(var1);
      } catch (SQLException var4) {
         String var3 = this.rs.getString(var1);
         return new RowSetNClob(var3);
      }
   }

   public String getNString(int var1) throws SQLException {
      return this.rs.getNString(var1);
   }

   public String getNString(String var1) throws SQLException {
      return this.rs.getNString(var1);
   }

   public Object getObject(int var1) throws SQLException {
      return this.rs.getObject(var1);
   }

   public Object getObject(int var1, Map<String, Class<?>> var2) throws SQLException {
      return this.rs.getObject(var1, var2);
   }

   public Object getObject(String var1) throws SQLException {
      return this.rs.getObject(var1);
   }

   public Object getObject(String var1, Map<String, Class<?>> var2) throws SQLException {
      return this.rs.getObject(var1, var2);
   }

   public Ref getRef(int var1) throws SQLException {
      return this.rs.getRef(var1);
   }

   public Ref getRef(String var1) throws SQLException {
      return this.rs.getRef(var1);
   }

   public RowId getRowId(int var1) throws SQLException {
      return this.rs.getRowId(var1);
   }

   public RowId getRowId(String var1) throws SQLException {
      return this.rs.getRowId(var1);
   }

   public short getShort(int var1) throws SQLException {
      return this.rs.getShort(var1);
   }

   public short getShort(String var1) throws SQLException {
      return this.rs.getShort(var1);
   }

   public SQLXML getSQLXML(int var1) throws SQLException {
      return this.rs.getSQLXML(var1);
   }

   public SQLXML getSQLXML(String var1) throws SQLException {
      return this.rs.getSQLXML(var1);
   }

   public String getString(int var1) throws SQLException {
      return this.rs.getString(var1);
   }

   public String getString(String var1) throws SQLException {
      return this.rs.getString(var1);
   }

   public Time getTime(int var1) throws SQLException {
      return this.rs.getTime(var1);
   }

   public Time getTime(int var1, Calendar var2) throws SQLException {
      return this.rs.getTime(var1, var2);
   }

   public Time getTime(String var1) throws SQLException {
      return this.rs.getTime(var1);
   }

   public Time getTime(String var1, Calendar var2) throws SQLException {
      return this.rs.getTime(var1, var2);
   }

   public Timestamp getTimestamp(int var1) throws SQLException {
      return this.rs.getTimestamp(var1);
   }

   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      return this.rs.getTimestamp(var1, var2);
   }

   public Timestamp getTimestamp(String var1) throws SQLException {
      return this.rs.getTimestamp(var1);
   }

   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      return this.rs.getTimestamp(var1, var2);
   }

   /** @deprecated */
   public InputStream getUnicodeStream(int var1) throws SQLException {
      return this.rs.getUnicodeStream(var1);
   }

   /** @deprecated */
   public InputStream getUnicodeStream(String var1) throws SQLException {
      return this.rs.getUnicodeStream(var1);
   }

   public URL getURL(int var1) throws SQLException {
      return this.rs.getURL(var1);
   }

   public URL getURL(String var1) throws SQLException {
      return this.rs.getURL(var1);
   }

   public boolean wasNull() throws SQLException {
      return this.rs.wasNull();
   }

   public void updateArray(int var1, Array var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateArray(var1, var2);
   }

   public void updateArray(String var1, Array var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateArray(var1, var2);
   }

   public void updateAsciiStream(int var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2);
   }

   public void updateAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2, var3);
   }

   public void updateAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2, var3);
   }

   public void updateAsciiStream(String var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2);
   }

   public void updateAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2, var3);
   }

   public void updateAsciiStream(String var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateAsciiStream(var1, var2, var3);
   }

   public void updateBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBigDecimal(var1, var2);
   }

   public void updateBigDecimal(String var1, BigDecimal var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBigDecimal(var1, var2);
   }

   public void updateBinaryStream(int var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2);
   }

   public void updateBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2, var3);
   }

   public void updateBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2, var3);
   }

   public void updateBinaryStream(String var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2);
   }

   public void updateBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2, var3);
   }

   public void updateBinaryStream(String var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBinaryStream(var1, var2, var3);
   }

   public void updateBlob(int var1, Blob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2);
   }

   public void updateBlob(int var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2);
   }

   public void updateBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2, var3);
   }

   public void updateBlob(String var1, Blob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2);
   }

   public void updateBlob(String var1, InputStream var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2);
   }

   public void updateBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateBlob(var1, var2, var3);
   }

   public void updateBoolean(int var1, boolean var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBoolean(var1, var2);
   }

   public void updateBoolean(String var1, boolean var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBoolean(var1, var2);
   }

   public void updateByte(int var1, byte var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateByte(var1, var2);
   }

   public void updateByte(String var1, byte var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateByte(var1, var2);
   }

   public void updateBytes(int var1, byte[] var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBytes(var1, var2);
   }

   public void updateBytes(String var1, byte[] var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateBytes(var1, var2);
   }

   public void updateCharacterStream(int var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2);
   }

   public void updateCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2, var3);
   }

   public void updateCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2, var3);
   }

   public void updateCharacterStream(String var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2);
   }

   public void updateCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2, var3);
   }

   public void updateCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateCharacterStream(var1, var2, var3);
   }

   public void updateClob(int var1, Clob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2);
   }

   public void updateClob(int var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2);
   }

   public void updateClob(int var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2, var3);
   }

   public void updateClob(String var1, Clob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2);
   }

   public void updateClob(String var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2);
   }

   public void updateClob(String var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateClob(var1, var2, var3);
   }

   public void updateDate(int var1, Date var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateDate(var1, var2);
   }

   public void updateDate(String var1, Date var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateDate(var1, var2);
   }

   public void updateDouble(int var1, double var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateDouble(var1, var2);
   }

   public void updateDouble(String var1, double var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateDouble(var1, var2);
   }

   public void updateFloat(int var1, float var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateFloat(var1, var2);
   }

   public void updateFloat(String var1, float var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateFloat(var1, var2);
   }

   public void updateInt(int var1, int var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateInt(var1, var2);
   }

   public void updateInt(String var1, int var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateInt(var1, var2);
   }

   public void updateLong(int var1, long var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateLong(var1, var2);
   }

   public void updateLong(String var1, long var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateLong(var1, var2);
   }

   public void updateNCharacterStream(int var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNCharacterStream(var1, var2);
   }

   public void updateNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateNCharacterStream(var1, var2, var3);
   }

   public void updateNCharacterStream(String var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNCharacterStream(var1, var2);
   }

   public void updateNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateNCharacterStream(var1, var2, var3);
   }

   public void updateNClob(int var1, NClob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2);
   }

   public void updateNClob(int var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2);
   }

   public void updateNClob(int var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2, var3);
   }

   public void updateNClob(String var1, NClob var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2);
   }

   public void updateNClob(String var1, Reader var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2);
   }

   public void updateNClob(String var1, Reader var2, long var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateNClob(var1, var2, var3);
   }

   public void updateNString(int var1, String var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNString(var1, var2);
   }

   public void updateNString(String var1, String var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateNString(var1, var2);
   }

   public void updateNull(int var1) throws SQLException {
      this.checkOp(7);
      this.rs.updateNull(var1);
   }

   public void updateNull(String var1) throws SQLException {
      this.checkOp(7);
      this.rs.updateNull(var1);
   }

   public void updateObject(int var1, Object var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateObject(var1, var2);
   }

   public void updateObject(int var1, Object var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateObject(var1, var2, var3);
   }

   public void updateObject(String var1, Object var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateObject(var1, var2);
   }

   public void updateObject(String var1, Object var2, int var3) throws SQLException {
      this.checkOp(7);
      this.rs.updateObject(var1, var2, var3);
   }

   public void updateRef(int var1, Ref var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateRef(var1, var2);
   }

   public void updateRef(String var1, Ref var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateRef(var1, var2);
   }

   public void updateRowId(int var1, RowId var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateRowId(var1, var2);
   }

   public void updateRowId(String var1, RowId var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateRowId(var1, var2);
   }

   public void updateShort(int var1, short var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateShort(var1, var2);
   }

   public void updateShort(String var1, short var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateShort(var1, var2);
   }

   public void updateSQLXML(int var1, SQLXML var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateSQLXML(var1, var2);
   }

   public void updateSQLXML(String var1, SQLXML var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateSQLXML(var1, var2);
   }

   public void updateString(int var1, String var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateString(var1, var2);
   }

   public void updateString(String var1, String var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateString(var1, var2);
   }

   public void updateTime(int var1, Time var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateTime(var1, var2);
   }

   public void updateTime(String var1, Time var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateTime(var1, var2);
   }

   public void updateTimestamp(int var1, Timestamp var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateTimestamp(var1, var2);
   }

   public void updateTimestamp(String var1, Timestamp var2) throws SQLException {
      this.checkOp(7);
      this.rs.updateTimestamp(var1, var2);
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
      this.metaData.setMatchColumn(var1, true);
   }

   public void setMatchColumn(String var1) throws SQLException {
      this.metaData.setMatchColumn(this.findColumn(var1), true);
   }

   public void setMatchColumn(int[] var1) throws SQLException {
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

   public void execute() throws SQLException {
      this.checkOp(2);
      this.cstmt = this.getConnection().prepareCall(this.command, this.resultSetType, this.concurrency);
      Iterator var1 = this.getParameters().iterator();

      while(var1.hasNext()) {
         WLParameter var2 = (WLParameter)var1.next();
         var2.setParam(this.cstmt);
      }

      this.rs = this.cstmt.executeQuery();
      this.metaData = new CachedRowSetMetaData();
      this.metaData.initialize(this.rs.getMetaData(), this.rs.getStatement().getConnection().getMetaData());
   }

   public void insertRow() throws SQLException {
      this.checkOp(8);
      this.rs.insertRow();
      this.rowChanged();
   }

   public void deleteRow() throws SQLException {
      this.checkOp(4);
      this.rs.deleteRow();
      this.rowChanged();
   }

   public void updateRow() throws SQLException {
      this.checkOp(8);
      this.rs.updateRow();
      this.rowChanged();
   }

   public void refreshRow() throws SQLException {
      this.rs.updateRow();
   }

   public void cancelRowUpdates() throws SQLException {
      this.checkOp(8);
      this.rs.cancelRowUpdates();
   }

   public void commit() throws SQLException {
      this.conn.commit();
   }

   public boolean getAutoCommit() throws SQLException {
      return this.conn.getAutoCommit();
   }

   public void setAutoCommit(boolean var1) throws SQLException {
      this.conn.setAutoCommit(var1);
   }

   public void rollback() throws SQLException {
      this.conn.rollback();
   }

   public void rollback(Savepoint var1) throws SQLException {
      this.conn.rollback(var1);
   }

   public void close() throws SQLException {
      try {
         if (this.rs != null) {
            this.rs.close();
         }
      } catch (Throwable var4) {
      }

      try {
         if (this.cstmt != null) {
            this.cstmt.close();
         }
      } catch (Throwable var3) {
      }

      try {
         if (this.conn != null) {
            this.conn.close();
         }
      } catch (Throwable var2) {
      }

   }

   public int findColumn(String var1) throws SQLException {
      return this.metaData.findColumn(var1);
   }

   public int getHoldability() throws SQLException {
      return this.rs.getHoldability();
   }

   public boolean isClosed() throws SQLException {
      return this.rs == null ? false : this.rs.isClosed();
   }

   public <T> T unwrap(Class<T> var1) throws SQLException {
      if (var1.isInstance(this)) {
         return var1.cast(this);
      } else {
         throw new SQLException(this + " is not an instance of " + var1);
      }
   }

   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return var1.isInstance(this);
   }

   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }
}
