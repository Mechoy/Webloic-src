package weblogic.ejb.container.cache;

import java.util.HashSet;
import java.util.Set;
import weblogic.ejb.container.manager.TTLManager;

public class QueryCacheKey {
   public static final int RET_TYPE_UNKNOWN = 0;
   public static final int RET_TYPE_SET = 1;
   public static final int RET_TYPE_COLLECTION = 2;
   public static final int RET_TYPE_SINGLETON = 3;
   private final String methodIdOrQueryString;
   private final Object[] arguments;
   private final int maxelements;
   private final TTLManager manager;
   private int timeoutMillis = 0;
   private int hash;
   private int returnType;
   private Set sourceQueries;
   private Set destinationQueries;
   private Set dependentQueries;

   public QueryCacheKey(String var1, int var2, TTLManager var3, int var4) {
      this.methodIdOrQueryString = var1;
      this.arguments = new Object[0];
      this.maxelements = var2;
      this.manager = var3;
      this.returnType = var4;
      this.hash = this.methodIdOrQueryString.hashCode() ^ this.maxelements ^ this.manager.hashCode();
   }

   public QueryCacheKey(String var1, Object[] var2, TTLManager var3, int var4) {
      this.methodIdOrQueryString = var1;
      this.arguments = var2 == null ? new Object[0] : var2;
      this.maxelements = 0;
      this.manager = var3;
      this.returnType = var4;
      this.hash = this.methodIdOrQueryString.hashCode() ^ this.manager.hashCode();

      for(int var5 = 0; var5 < this.arguments.length; ++var5) {
         this.hash ^= (var5 + 1) * this.arguments[var5].hashCode();
      }

   }

   public Object[] getArguments() {
      return this.arguments;
   }

   public String getMethodId() {
      return this.methodIdOrQueryString;
   }

   public void setTimeoutMillis(int var1) {
      this.timeoutMillis = var1;
   }

   public int getTimeoutMillis() {
      return this.timeoutMillis;
   }

   public int getReturnType() {
      return this.returnType;
   }

   public TTLManager getOwnerManager() {
      return this.manager;
   }

   public void addSourceQuery(QueryCacheKey var1) {
      if (this.sourceQueries == null) {
         this.sourceQueries = new HashSet();
      }

      if (!var1.equals(this)) {
         this.sourceQueries.add(var1);
      }

   }

   public void addDestinationQuery(QueryCacheKey var1) {
      if (this.destinationQueries == null) {
         this.destinationQueries = new HashSet();
      }

      if (!var1.equals(this)) {
         this.destinationQueries.add(var1);
      }

   }

   public void addDependentQuery(QueryCacheKey var1) {
      if (this.dependentQueries == null) {
         this.dependentQueries = new HashSet();
      }

      if (!var1.equals(this)) {
         this.dependentQueries.add(var1);
      }

   }

   protected Set getSourceQueries() {
      return this.sourceQueries;
   }

   protected Set getDestinationQueries() {
      return this.destinationQueries;
   }

   protected Set getDependentQueries() {
      return this.dependentQueries;
   }

   public int hashCode() {
      return this.hash;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof QueryCacheKey) {
         QueryCacheKey var2 = (QueryCacheKey)var1;
         if (this.arguments.length != var2.arguments.length) {
            return false;
         } else if (!this.manager.equals(var2.manager)) {
            return false;
         } else if (!this.methodIdOrQueryString.equals(var2.methodIdOrQueryString)) {
            return false;
         } else if (this.maxelements != var2.maxelements) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.arguments.length; ++var3) {
               if (!this.arguments[var3].equals(var2.arguments[var3])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public String toString() {
      String var1 = this.methodIdOrQueryString;

      for(int var2 = 0; var2 < this.arguments.length; ++var2) {
         if (var2 == 0) {
            var1 = var1 + ", L[";
         }

         var1 = var1 + this.arguments[var2] + ";";
      }

      var1 = var1 + "], " + this.maxelements + ", " + this.timeoutMillis + ", " + this.manager;
      return var1;
   }
}
