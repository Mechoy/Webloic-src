package weblogic.ejb.container.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.ejb.FinderException;
import weblogic.ejb.PreparedQuery;
import weblogic.ejb.container.interfaces.LocalQueryHandler;
import weblogic.ejb20.internal.WLQueryPropertiesImpl;
import weblogic.utils.StackTraceUtils;

public class LocalPreparedQueryImpl extends WLQueryPropertiesImpl implements PreparedQuery {
   private LocalQueryHandler handler;
   private String sql;
   private String ejbql;
   private Map arguments = new TreeMap();
   private Map flattenedArguments = new TreeMap();

   public LocalPreparedQueryImpl(String var1, LocalQueryHandler var2, Properties var3) throws FinderException {
      this.ejbql = var1;
      this.handler = var2;
      this.setProperties(var3);
   }

   public String getEjbql() {
      return this.ejbql;
   }

   public Collection find() throws FinderException {
      try {
         Object[] var1 = (Object[])((Object[])this.handler.executePreparedQuery(this.sql, this, this.arguments, this.flattenedArguments, false));
         this.sql = (String)var1[0];
         this.flattenedArguments = (Map)var1[1];
         this.arguments.clear();
         return (Collection)var1[2];
      } catch (FinderException var2) {
         throw var2;
      } catch (Throwable var3) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var3));
      }
   }

   public ResultSet execute() throws FinderException {
      try {
         Object[] var1 = (Object[])((Object[])this.handler.executePreparedQuery(this.sql, this, this.arguments, this.flattenedArguments, true));
         this.sql = (String)var1[0];
         this.flattenedArguments = (Map)var1[1];
         this.arguments.clear();
         return (ResultSet)var1[2];
      } catch (FinderException var2) {
         throw var2;
      } catch (Throwable var3) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var3));
      }
   }

   public void setString(int var1, String var2) {
      this.setParameter(var1, var2);
   }

   public void setBigDecimal(int var1, BigDecimal var2) {
      this.setParameter(var1, var2);
   }

   public void setBigInteger(int var1, BigInteger var2) {
      this.setParameter(var1, var2);
   }

   public void setBinary(int var1, byte[] var2) {
      this.setParameter(var1, var2);
   }

   public void setBoolean(int var1, boolean var2) {
      this.setParameter(var1, new Boolean(var2));
   }

   public void setByte(int var1, byte var2) {
      this.setParameter(var1, new Byte(var2));
   }

   public void setCharacter(int var1, char var2) {
      this.setParameter(var1, new Character(var2));
   }

   public void setShort(int var1, short var2) {
      this.setParameter(var1, new Short(var2));
   }

   public void setInt(int var1, int var2) {
      this.setParameter(var1, new Integer(var2));
   }

   public void setLong(int var1, long var2) {
      this.setParameter(var1, new Long(var2));
   }

   public void setFloat(int var1, float var2) {
      this.setParameter(var1, new Float(var2));
   }

   public void setDouble(int var1, double var2) {
      this.setParameter(var1, new Double(var2));
   }

   public void setDate(int var1, Date var2) {
      this.setParameter(var1, var2);
   }

   public void setDate(int var1, java.util.Date var2) {
      this.setParameter(var1, var2);
   }

   public void setTime(int var1, Time var2) {
      this.setParameter(var1, var2);
   }

   public void setTime(int var1, java.util.Date var2) {
      this.setParameter(var1, var2);
   }

   public void setTimestamp(int var1, Timestamp var2) {
      this.setParameter(var1, var2);
   }

   public void setTimestamp(int var1, java.util.Date var2) {
      this.setParameter(var1, var2);
   }

   public void setCalender(int var1, Calendar var2) {
      this.setParameter(var1, var2);
   }

   public void setObject(int var1, Object var2) {
      this.setParameter(var1, var2);
   }

   private void setParameter(int var1, Object var2) {
      Integer var3 = new Integer(var1);
      Object var4 = this.arguments.put(var3, var2);
      if (var4 != null) {
         this.arguments.put(var3, var4);
         throw new RuntimeException("Attempt to set more than one value for query parameter '" + var3 + "'.  Previous value was '" + var4 + "'.");
      }
   }

   public String toString() {
      return "PreparedQueryImpl: [ \nejbql: " + this.ejbql + "\n" + "sql: " + this.sql + "\n" + "arguments: " + this.arguments + "\n" + "flattenedArguments: " + this.flattenedArguments + "\n" + "]";
   }
}
