package weblogic.ejb.container.cache;

import weblogic.ejb.container.InternalException;

public class MultiValueQueryCacheElement extends QueryCacheElement {
   public MultiValueQueryCacheElement(QueryCacheElement[] var1) {
      super((Object)var1);
   }

   public void setInvalidatable(boolean var1) {
   }

   public Object getReturnValue(Object var1, boolean var2) throws InternalException {
      QueryCacheElement[] var3 = (QueryCacheElement[])((QueryCacheElement[])this.value);
      Object[] var4 = new Object[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         Object var6 = var3[var5].getReturnValue(var1, var2);
         var4[var5] = var6 == weblogic.ejb.container.interfaces.QueryCache.NULL_VALUE ? null : var6;
      }

      return var4;
   }

   public boolean enroll(Object var1) throws InternalException {
      QueryCacheElement[] var2 = (QueryCacheElement[])((QueryCacheElement[])this.value);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].enroll(var1)) {
            return false;
         }
      }

      return true;
   }
}
