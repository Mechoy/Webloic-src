package weblogic.dbeans.internal;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.FinderException;
import weblogic.dbeans.ConversationImpl;
import weblogic.dbeans.DataBeansException;
import weblogic.dbeans.Query;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb20.internal.WLQueryPropertiesImpl;

public class QueryImpl implements Query {
   private ConversationImpl conversation = null;
   private BaseEntityManager manager = null;
   private String sql = null;
   private String sqlShapeName = null;
   private Map arguments = new TreeMap();
   private int maxElements = 0;
   private static Method getCollectionMethod = null;

   public QueryImpl(ConversationImpl var1, BaseEntityManager var2, String var3) {
      this.conversation = var1;
      this.manager = var2;
      this.sql = var3;
   }

   public QueryImpl(ConversationImpl var1, BaseEntityManager var2, String var3, String var4) {
      this.conversation = var1;
      this.manager = var2;
      this.sql = var3;
      this.sqlShapeName = var4;
   }

   public Collection getCollection() {
      Collection var1 = null;
      WLQueryPropertiesImpl var2 = new WLQueryPropertiesImpl();

      try {
         var2.setSqlShapeName(this.sqlShapeName);
         var2.setMaxElements(this.maxElements);
      } catch (FinderException var5) {
         throw new AssertionError("should never get here");
      }

      try {
         var1 = (Collection)this.manager.dynamicSqlQuery(this.sql, this.prepareArguments(this.arguments), var2, getCollectionMethod, true, Collection.class, this.conversation);
      } catch (InternalException var4) {
         DataBeansUtils.throwDataBeansException(var4);
      }

      return var1;
   }

   public void setMaxElements(int var1) {
      this.maxElements = var1;
   }

   public Object getObject() {
      return null;
   }

   public ResultSet getResultSet() {
      return null;
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

   private Object[] prepareArguments(Map var1) {
      Iterator var2 = var1.entrySet().iterator();
      Object[] var3 = new Object[var1.size()];
      int var4 = 1;
      int var5 = -1;

      for(int var6 = 0; var2.hasNext(); var5 = var4++) {
         Map.Entry var7 = (Map.Entry)var2.next();
         int var8 = (Integer)var7.getKey();
         if (var5 == -1 && var8 == 0) {
            var4 = 0;
         }

         if (var8 != var4) {
            if (var5 == -1) {
               throw new DataBeansException("Missing value for first parameter.  The first parameter index specified was at index '" + var8 + "' instead '0' or '1'.");
            }

            throw new DataBeansException("Missing value for parameter '" + var4 + "'.  There is a missing parameter between parameters '" + var5 + "' and '" + var8 + "'.");
         }

         var3[var6++] = var7.getValue();
      }

      return var3;
   }

   private void setParameter(int var1, Object var2) {
      Integer var3 = new Integer(var1);
      Object var4 = this.arguments.put(var3, var2);
      if (var4 != null) {
         this.arguments.put(var3, var4);
         throw new DataBeansException("Attempt to set more than one value for query parameter '" + var3 + "'.  Previous value was '" + var4 + "'.");
      }
   }

   static {
      try {
         getCollectionMethod = Query.class.getMethod("getCollection");
      } catch (NoSuchMethodException var1) {
      }

   }
}
