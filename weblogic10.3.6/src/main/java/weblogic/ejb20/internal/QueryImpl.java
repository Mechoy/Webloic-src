package weblogic.ejb20.internal;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Properties;
import javax.ejb.FinderException;
import weblogic.ejb.Query;
import weblogic.ejb20.interfaces.QueryHandler;
import weblogic.utils.StackTraceUtils;

public class QueryImpl extends WLQueryPropertiesImpl implements Query, Serializable {
   private static final long serialVersionUID = -6324059333171473291L;
   private QueryHandler handler;
   private boolean isSql = false;

   public QueryImpl(QueryHandler var1) {
      this.handler = var1;
   }

   public QueryImpl(QueryHandler var1, boolean var2) {
      this.handler = var1;
      this.isSql = var2;
   }

   public String getLanguage() {
      return this.isSql ? "SQL" : "EJB QL";
   }

   public Collection find(String var1) throws FinderException {
      try {
         return (Collection)this.handler.executeQuery(var1, this, false, this.isSql);
      } catch (FinderException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var4));
      }
   }

   public Collection find(String var1, Properties var2) throws FinderException {
      this.setProperties(var2);

      try {
         return (Collection)this.handler.executeQuery(var1, this, false, this.isSql);
      } catch (FinderException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var5));
      }
   }

   public ResultSet execute(String var1) throws FinderException {
      try {
         return (ResultSet)this.handler.executeQuery(var1, this, true, this.isSql);
      } catch (FinderException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var4));
      }
   }

   public ResultSet execute(String var1, Properties var2) throws FinderException {
      this.setProperties(var2);

      try {
         return (ResultSet)this.handler.executeQuery(var1, this, true, this.isSql);
      } catch (FinderException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new FinderException(StackTraceUtils.throwable2StackTrace(var5));
      }
   }
}
