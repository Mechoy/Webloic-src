package weblogic.entitlement.data;

import weblogic.entitlement.expression.EAuxiliary;
import weblogic.entitlement.expression.EExpression;

public class ERole extends BaseResource {
   private ERoleId mId;

   public ERole(ERoleId var1, EExpression var2) {
      this(var1, var2, false);
   }

   public ERole(ERoleId var1, EExpression var2, boolean var3) {
      super(var2, var3);
      if (var1 == null) {
         throw new NullPointerException("null role primary key");
      } else {
         this.mId = var1;
      }
   }

   public ERole(String var1, String var2, EExpression var3) {
      this(new ERoleId(var1, var2), var3, false);
   }

   public ERole(String var1, String var2, EExpression var3, boolean var4) {
      this(new ERoleId(var1, var2), var3, var4);
   }

   public ERole(ERoleId var1, EExpression var2, EAuxiliary var3) {
      this(var1, var2, var3, false);
   }

   public ERole(ERoleId var1, EExpression var2, EAuxiliary var3, boolean var4) {
      super(var2, var3, var4);
      if (var1 == null) {
         throw new NullPointerException("null role primary key");
      } else {
         this.mId = var1;
      }
   }

   public ERole(ERoleId var1, EExpression var2, EAuxiliary var3, boolean var4, String var5) {
      super(var2, var3, var4, var5);
      if (var1 == null) {
         throw new NullPointerException("null role primary key");
      } else {
         this.mId = var1;
      }
   }

   public ERole(String var1, String var2, EExpression var3, EAuxiliary var4) {
      this(new ERoleId(var1, var2), var3, var4, false);
   }

   public ERole(String var1, String var2, EExpression var3, EAuxiliary var4, boolean var5) {
      this(new ERoleId(var1, var2), var3, var4, var5);
   }

   public ERole(ERoleId var1, EExpression var2, boolean var3, String var4) {
      super(var2, var3, var4);
      if (var1 == null) {
         throw new NullPointerException("null role primary key");
      } else {
         this.mId = var1;
      }
   }

   public String getName() {
      return this.mId.getRoleName();
   }

   public String getResourceName() {
      return this.mId.getResourceName();
   }

   public Object getPrimaryKey() {
      return this.mId;
   }
}
