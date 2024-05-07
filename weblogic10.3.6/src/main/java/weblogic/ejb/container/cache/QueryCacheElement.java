package weblogic.ejb.container.cache;

import javax.ejb.EntityBean;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.manager.TTLManager;

public class QueryCacheElement {
   protected TTLManager beanManager;
   protected Object value;
   protected boolean isInvalidatable = true;
   protected boolean isIncludable = true;
   protected boolean isPrimaryKey = true;
   private int hashcode;

   public QueryCacheElement(Object var1) {
      this.value = var1 == null ? weblogic.ejb.container.interfaces.QueryCache.NULL_VALUE : var1;
      this.isInvalidatable = false;
      this.isPrimaryKey = false;
   }

   public QueryCacheElement(Object var1, TTLManager var2) {
      this.value = var1;
      this.beanManager = var2;
      this.hashcode = this.value.hashCode() ^ this.beanManager.hashCode();
   }

   protected QueryCacheElement(CacheKey var1) {
      this.value = var1.getPrimaryKey();
      this.beanManager = (TTLManager)var1.getCallback();
      this.hashcode = this.value.hashCode() ^ this.beanManager.hashCode();
   }

   public Object getReturnValue(Object var1, boolean var2) throws InternalException {
      if (this.isPrimaryKey) {
         EntityBean var3 = this.beanManager.enrollIfNotTimedOut(var1, new CacheKey(this.value, this.beanManager));
         return var3 != null ? this.beanManager.finderGetEoFromBeanOrPk(var3, (Object)null, var2) : null;
      } else {
         return this.value;
      }
   }

   public boolean enroll(Object var1) throws InternalException {
      if (this.isPrimaryKey) {
         return this.beanManager.enrollIfNotTimedOut(var1, new CacheKey(this.value, this.beanManager)) != null;
      } else {
         return true;
      }
   }

   public void setInvalidatable(boolean var1) {
      this.isInvalidatable = var1;
   }

   public void setIncludable(boolean var1) {
      this.isIncludable = var1;
   }

   public boolean isInvalidatable() {
      return this.isInvalidatable;
   }

   public boolean isIncludable() {
      return this.isIncludable;
   }

   public int hashCode() {
      return this.hashcode;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof QueryCacheElement)) {
         return false;
      } else {
         QueryCacheElement var2 = (QueryCacheElement)var1;
         return this.hashcode == var2.hashcode && this.eq(this.value, var2.value) && this.eq(this.beanManager, var2.beanManager);
      }
   }

   private boolean eq(Object var1, Object var2) {
      return var1 == var2 || var1.equals(var2);
   }

   public String toString() {
      return this.beanManager != null ? this.value.toString() + "#" + this.beanManager.toString() : this.value.toString();
   }
}
