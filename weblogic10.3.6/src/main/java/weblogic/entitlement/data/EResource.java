package weblogic.entitlement.data;

import weblogic.entitlement.expression.EExpression;

public class EResource extends BaseResource {
   private String mName;

   public EResource(String var1, EExpression var2) {
      this(var1, var2, false, (String)null);
   }

   public EResource(String var1, EExpression var2, boolean var3) {
      this(var1, var2, var3, (String)null);
   }

   public EResource(String var1, EExpression var2, boolean var3, String var4) {
      super(var2, var3, var4);
      if (var1 == null) {
         throw new NullPointerException("null resource name");
      } else {
         this.mName = var1;
      }
   }

   public Object getPrimaryKey() {
      return this.mName;
   }

   public String getName() {
      return this.mName;
   }
}
