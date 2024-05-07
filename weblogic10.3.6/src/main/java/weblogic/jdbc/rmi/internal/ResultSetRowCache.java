package weblogic.jdbc.rmi.internal;

import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class ResultSetRowCache implements Serializable {
   static final long serialVersionUID = 819366818399178928L;
   private boolean isTrueSetFinished = false;
   private int cacheRows;
   private final int cacheCols;
   private Object[] recordCache;
   private transient int currRowIdx = 0;
   private transient boolean wasNullFlag = false;
   private transient boolean haveCalledGet = false;
   private transient ResultSetMetaDataCache mdCache = null;
   private static final String CONVERSIONERR = "this type conversion is unsupported when row caching is on";

   public ResultSetRowCache() {
      this.cacheCols = 0;
   }

   public ResultSetRowCache(int var1, java.sql.ResultSet var2, ResultSetMetaDataCache var3) throws SQLException {
      this.cacheRows = 0;
      this.cacheCols = var3.getColumnCount();
      this.recordCache = new Object[var1 * this.cacheCols];

      for(int var4 = 0; var4 < var1; ++var4) {
         if (!var2.next()) {
            this.isTrueSetFinished = true;
            break;
         }

         int var5 = var4 * this.cacheCols;

         for(int var6 = 0; var6 < this.cacheCols; ++var6) {
            this.recordCache[var5 + var6] = getFieldAsObject(var3.getColumnTypeZeroBased(var6), var6 + 1, var2);
         }

         ++this.cacheRows;
      }

   }

   static boolean isCacheable(ResultSetMetaDataCache var0) {
      int var1 = var0.getColumnCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = var0.getColumnTypeZeroBased(var2);
         if (!isCacheable(var3)) {
            return false;
         }
      }

      return true;
   }

   void setMetaDataCache(ResultSetMetaDataCache var1) {
      this.mdCache = var1;
   }

   synchronized void beforeFirstRow() {
      this.currRowIdx = -1;
   }

   synchronized int getRowCount() {
      return this.cacheRows;
   }

   synchronized boolean wasNull() throws SQLException {
      if (!this.haveCalledGet) {
         throw new SQLException("No getXXX() has been called on a column of the current row");
      } else {
         return this.wasNullFlag;
      }
   }

   String getNString(int var1) throws SQLException {
      return this.getString(var1);
   }

   synchronized String getString(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      return this.checkNull(var2) ? null : var2.toString();
   }

   synchronized boolean getBoolean(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return false;
      } else {
         String var3 = var2.toString();
         if (var3 == null) {
            return false;
         } else if (var3.length() == 0) {
            return false;
         } else {
            var3 = var3.trim();
            if (var3.equalsIgnoreCase("true")) {
               return true;
            } else if (var3.equalsIgnoreCase("yes")) {
               return true;
            } else if (var3.equalsIgnoreCase("no")) {
               return false;
            } else if (var3.equalsIgnoreCase("false")) {
               return false;
            } else {
               try {
                  if (var3.indexOf(".") != -1) {
                     double var7 = new Double(var3);
                     return var7 != 0.0;
                  } else {
                     long var4 = new Long(var3);
                     return var4 != 0L;
                  }
               } catch (Exception var6) {
                  return false;
               }
            }
         }
      }
   }

   synchronized byte getByte(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1;
               }

               return 0;
            case -6:
            case 4:
            case 5:
               return ((Integer)var2).byteValue();
            case -5:
               return ((Long)var2).byteValue();
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Byte.parseByte((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).byteValue();
         }
      }
   }

   synchronized short getShort(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1;
               }

               return 0;
            case -6:
            case 4:
            case 5:
               return ((Integer)var2).shortValue();
            case -5:
               return ((Long)var2).shortValue();
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Short.parseShort((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).shortValue();
         }
      }
   }

   synchronized int getInt(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1;
               }

               return 0;
            case -6:
            case 4:
            case 5:
               return (Integer)var2;
            case -5:
               return ((Long)var2).intValue();
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Integer.parseInt((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).intValue();
         }
      }
   }

   synchronized long getLong(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0L;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1L;
               }

               return 0L;
            case -6:
            case 4:
            case 5:
               return ((Integer)var2).longValue();
            case -5:
               return (Long)var2;
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Long.parseLong((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).longValue();
         }
      }
   }

   synchronized float getFloat(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0.0F;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1.0F;
               }

               return 0.0F;
            case -6:
            case 4:
            case 5:
               return ((Integer)var2).floatValue();
            case -5:
               return ((Long)var2).floatValue();
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Float.parseFloat((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).floatValue();
         }
      }
   }

   synchronized double getDouble(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return 0.0;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return 1.0;
               }

               return 0.0;
            case -6:
            case 4:
            case 5:
               return ((Integer)var2).doubleValue();
            case -5:
               return ((Long)var2).doubleValue();
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return Double.parseDouble((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return ((BigDecimal)var2).doubleValue();
         }
      }
   }

   synchronized BigDecimal getBigDecimal(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return null;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -7:
               if ((Boolean)var2) {
                  return new BigDecimal(1.0);
               }

               return new BigDecimal(0.0);
            case -6:
            case 4:
            case 5:
               return new BigDecimal(((Integer)var2).doubleValue());
            case -5:
               return new BigDecimal(((Long)var2).doubleValue());
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 9:
            case 10:
            case 11:
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
            case 1:
            case 12:
               return new BigDecimal((String)var2);
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
               return (BigDecimal)var2;
         }
      }
   }

   synchronized BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      BigDecimal var3 = this.getBigDecimal(var1);
      return var3.setScale(var2);
   }

   synchronized byte[] getBytes(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return null;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case -3:
            case -2:
               return (byte[])((byte[])var2);
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
         }
      }
   }

   synchronized Date getDate(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return null;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case 1:
            case 12:
               return Date.valueOf((String)var2);
            case 91:
               return (Date)var2;
            case 93:
               return new Date(((Timestamp)var2).getTime());
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
         }
      }
   }

   synchronized Time getTime(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return null;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case 1:
            case 12:
               return Time.valueOf((String)var2);
            case 92:
               return (Time)var2;
            case 93:
               return new Time(((Timestamp)var2).getTime());
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
         }
      }
   }

   synchronized Timestamp getTimestamp(int var1) throws SQLException {
      Object var2 = this.getObject(var1);
      if (this.checkNull(var2)) {
         return null;
      } else {
         switch (this.mdCache.getColumnType(var1)) {
            case 1:
            case 12:
               return Timestamp.valueOf((String)var2);
            case 91:
               if (var2 instanceof Timestamp) {
                  return (Timestamp)var2;
               }

               return new Timestamp(((Date)var2).getTime());
            case 92:
               return new Timestamp(((Time)var2).getTime());
            case 93:
               return (Timestamp)var2;
            default:
               throw new SQLException("this type conversion is unsupported when row caching is on");
         }
      }
   }

   synchronized Object getObject(int var1) throws SQLException {
      if (var1 >= 1 && var1 <= this.cacheCols) {
         this.haveCalledGet = true;
         int var3 = this.currRowIdx * this.cacheCols + (var1 - 1);
         return this.recordCache[var3];
      } else {
         String var2 = "Invalid column index: " + var1;
         throw new SQLException(var2);
      }
   }

   synchronized boolean isTrueSetFinished() {
      return this.isTrueSetFinished;
   }

   synchronized void setTrueSetFinished(boolean var1) {
      this.isTrueSetFinished = var1;
   }

   synchronized boolean next() {
      this.haveCalledGet = false;
      ++this.currRowIdx;
      return this.currRowIdx < this.cacheRows;
   }

   public synchronized void dumpCache(PrintStream var1) {
      for(int var2 = 0; var2 < this.cacheRows; ++var2) {
         for(int var3 = 0; var3 < this.cacheCols; ++var3) {
            var1.print("ROW: " + var2 + "\tCOL: " + var3 + "\tOBJ: ");
            if (this.recordCache[var2 * this.cacheCols + var3] != null) {
               System.out.println(this.recordCache[var2 * this.cacheCols + var3]);
            } else {
               System.out.println("NULL");
            }
         }
      }

   }

   private static Object getFieldAsObject(int var0, int var1, java.sql.ResultSet var2) throws SQLException {
      Object var3;
      switch (var0) {
         case -16:
         case -8:
         case -4:
         case -1:
         case 0:
         case 70:
         case 1111:
         case 2000:
         case 2001:
         case 2002:
         case 2003:
         case 2004:
         case 2005:
         case 2006:
         case 2009:
         case 2011:
         default:
            var3 = null;
            break;
         case -15:
         case -9:
            var3 = var2.getNString(var1);
            break;
         case -7:
         case 16:
            var3 = new Boolean(var2.getBoolean(var1));
            break;
         case -6:
         case 4:
         case 5:
            var3 = new Integer(var2.getInt(var1));
            break;
         case -5:
            var3 = new Long(var2.getLong(var1));
            break;
         case -3:
         case -2:
            var3 = var2.getBytes(var1);
            break;
         case 1:
         case 12:
            var3 = var2.getString(var1);
            break;
         case 2:
         case 3:
         case 6:
         case 7:
         case 8:
            try {
               var3 = var2.getBigDecimal(var1);
            } catch (SQLException var5) {
               var3 = new BigDecimal(var2.getString(var1));
            }
            break;
         case 91:
            var3 = var2.getDate(var1);
            Timestamp var4 = var2.getTimestamp(var1);
            if (var3 != null && var4 != null && var4.getTime() != ((Date)var3).getTime()) {
               var3 = var4;
            }
            break;
         case 92:
            var3 = var2.getTime(var1);
            break;
         case 93:
            var3 = var2.getTimestamp(var1);
      }

      if (var2.wasNull()) {
         var3 = null;
      }

      return var3;
   }

   private static boolean isCacheable(int var0) {
      switch (var0) {
         case -15:
         case -9:
         case -7:
         case -6:
         case -5:
         case -3:
         case -2:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 12:
         case 16:
         case 91:
         case 92:
         case 93:
            return true;
         default:
            return false;
      }
   }

   private boolean checkNull(Object var1) {
      this.wasNullFlag = var1 == null;
      return this.wasNullFlag;
   }
}
