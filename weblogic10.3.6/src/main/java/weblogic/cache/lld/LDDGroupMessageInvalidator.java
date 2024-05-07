package weblogic.cache.lld;

import java.util.Map;

class LDDGroupMessageInvalidator extends LLDGroupMessageInvalidator implements ChangeListener {
   public LDDGroupMessageInvalidator(String var1, Map var2) {
      super(var1, var2);
   }

   public void onUpdate(CacheEntry var1, Object var2) {
      this.onDelete(var1);
   }
}
