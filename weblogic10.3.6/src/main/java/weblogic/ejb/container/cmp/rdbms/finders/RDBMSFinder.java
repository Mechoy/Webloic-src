package weblogic.ejb.container.cmp.rdbms.finders;

import java.lang.reflect.Method;
import java.util.Map;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.j2ee.descriptor.wl.MethodParamsBean;
import weblogic.j2ee.descriptor.wl.QueryMethodBean;
import weblogic.j2ee.descriptor.wl.WeblogicQueryBean;

public final class RDBMSFinder {
   private String finderName;
   private String[] finderParams;
   private String ejbQlQuery;
   private String groupName;
   private String cachingName;
   private String sqlShapeName;
   private Map sqlQueries;
   private int maxElements = 0;
   private boolean includeUpdates = true;
   private boolean sqlSelectDistinct = false;
   private boolean queryCachingEnabled = false;
   private boolean enableEagerRefresh = false;
   private boolean includeResultCacheHint = false;

   public void setFinderName(String var1) {
      this.finderName = var1;
   }

   public String getFinderName() {
      return this.finderName;
   }

   public void setFinderParams(String[] var1) {
      this.finderParams = var1;
   }

   public String[] getFinderParams() {
      return this.finderParams;
   }

   public void setEjbQlQuery(String var1) {
      this.ejbQlQuery = var1;
   }

   public String getEjbQlQuery() {
      return this.ejbQlQuery;
   }

   public void setGroupName(String var1) {
      this.groupName = var1;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setCachingName(String var1) {
      this.cachingName = var1;
   }

   public String getCachingName() {
      return this.cachingName;
   }

   public int getMaxElements() {
      return this.maxElements;
   }

   public void setMaxElements(int var1) {
      this.maxElements = var1;
   }

   public void setIncludeUpdates(boolean var1) {
      this.includeUpdates = var1;
   }

   public boolean getIncludeUpdates() {
      return this.includeUpdates;
   }

   public void setSqlSelectDistinct(boolean var1) {
      this.sqlSelectDistinct = var1;
   }

   public boolean getSqlSelectDistinct() {
      return this.sqlSelectDistinct;
   }

   public void setQueryCachingEnabled(boolean var1) {
      this.queryCachingEnabled = var1;
   }

   public boolean isQueryCachingEnabled() {
      return this.queryCachingEnabled;
   }

   public void setEnableEagerRefresh(boolean var1) {
      this.enableEagerRefresh = var1;
   }

   public boolean isEnableEagerRefresh() {
      return this.enableEagerRefresh;
   }

   public void setIncludeResultCacheHint(boolean var1) {
      this.includeResultCacheHint = var1;
   }

   public boolean isIncludeResultCacheHint() {
      return this.includeResultCacheHint;
   }

   public String toString() {
      return DDUtils.getMethodSignature(this.finderName, this.finderParams);
   }

   public String getSqlShapeName() {
      return this.sqlShapeName;
   }

   public void setSqlShapeName(String var1) {
      this.sqlShapeName = var1;
   }

   public Map getSqlQueries() {
      return this.sqlQueries;
   }

   public void setSqlQueries(Map var1) {
      this.sqlQueries = var1;
   }

   public boolean usesSql() {
      return this.sqlQueries != null;
   }

   public static class FinderKey {
      private String finderName;
      private String[] finderParams;

      public FinderKey(String var1, String[] var2) {
         this.finderName = var1;
         this.finderParams = var2;
         if (var1 == null) {
            var1 = "";
         }

         if (var2 == null) {
            var2 = new String[0];
         }

      }

      public FinderKey(RDBMSFinder var1) {
         this.finderName = var1.getFinderName();
         String[] var2 = var1.getFinderParams();
         if (var2 == null) {
            var2 = new String[0];
         }

         this.finderParams = new String[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.finderParams[var3] = MethodUtils.decodePrimitiveTypeArrayMaybe(var2[var3]);
         }

      }

      public FinderKey(Finder var1) {
         this.finderName = var1.getName();
         String[] var2 = var1.getParameterClassNames();
         if (var2 == null) {
            var2 = new String[0];
         }

         this.finderParams = new String[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.finderParams[var3] = MethodUtils.decodePrimitiveTypeArrayMaybe(var2[var3]);
         }

      }

      public FinderKey(Method var1) {
         String var2 = var1.getName();
         if (var2.startsWith("ejbFind")) {
            this.finderName = MethodUtils.convertToDDFinderName(var2);
         } else {
            this.finderName = var2;
         }

         Class[] var3 = var1.getParameterTypes();
         this.finderParams = MethodUtils.classesToJavaSourceTypes(var3);
      }

      public FinderKey(WeblogicQueryBean var1) {
         QueryMethodBean var2 = var1.getQueryMethod();
         if (var2 == null) {
            this.finderName = "";
            this.finderParams = new String[0];
         } else {
            this.finderName = var2.getMethodName();
            if (this.finderName == null) {
               this.finderName = "";
            }

            MethodParamsBean var3 = var2.getMethodParams();
            if (var3 == null) {
               this.finderParams = new String[0];
            } else {
               this.finderParams = var3.getMethodParams();
               if (this.finderParams == null) {
                  this.finderParams = new String[0];
               }
            }
         }

      }

      public void setFinderName(String var1) {
         this.finderName = var1;
      }

      public String getFinderName() {
         return this.finderName;
      }

      public void setFinderParams(String[] var1) {
         this.finderParams = var1;
      }

      public String[] getFinderParams() {
         return this.finderParams;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof FinderKey)) {
            return false;
         } else {
            FinderKey var2 = (FinderKey)var1;
            if (!this.finderName.equals(var2.getFinderName())) {
               return false;
            } else {
               String[] var3 = var2.getFinderParams();
               if (this.finderParams.length != var3.length) {
                  return false;
               } else {
                  for(int var4 = 0; var4 < var3.length; ++var4) {
                     if (!this.finderParams[var4].equals(var3[var4])) {
                        return false;
                     }
                  }

                  return true;
               }
            }
         }
      }

      public int hashCode() {
         return this.finderName.hashCode();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("[FinderKey: " + this.getFinderName() + "(");
         if (this.finderParams != null) {
            for(int var2 = 0; var2 < this.finderParams.length; ++var2) {
               var1.append(this.finderParams[var2]);
               if (var2 < this.finderParams.length - 1) {
                  var1.append(", ");
               }
            }
         }

         var1.append(")]");
         return var1.toString();
      }
   }
}
