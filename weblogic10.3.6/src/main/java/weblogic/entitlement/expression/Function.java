package weblogic.entitlement.expression;

public abstract class Function extends EExprRep {
   protected EExprRep[] mArgs;

   public Function(EExprRep var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null function argument");
      } else {
         this.mArgs = new EExprRep[1];
         this.mArgs[0] = var1;
      }
   }

   public Function(EExprRep var1, EExprRep var2) {
      if (var1 != null && var2 != null) {
         this.mArgs = new EExprRep[2];
         this.mArgs[0] = var1;
         this.mArgs[1] = var2;
      } else {
         throw new IllegalArgumentException("Null " + (var1 == null ? "first" : "second") + " function argument");
      }
   }

   public Function(EExprRep[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] == null) {
            throw new IllegalArgumentException("Null function argument " + var2);
         }
      }

      this.mArgs = new EExprRep[var1.length];
      System.arraycopy(var1, 0, this.mArgs, 0, var1.length);
   }

   protected int getDependsOnInternal() {
      int var1 = DEPENDS_ON_UNKNOWN;

      for(int var2 = 0; var2 < this.mArgs.length; ++var2) {
         var1 |= this.mArgs[var2].getDependsOn();
      }

      return var1;
   }

   protected void setArg(int var1, EExprRep var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("EExprRep argument is null");
      } else {
         this.mArgs[var1] = var2;
      }
   }

   void outForPersist(StringBuffer var1) {
      this.writeTypeId(var1);
      var1.append((char)this.mArgs.length);

      for(int var2 = 0; var2 < this.mArgs.length; ++var2) {
         this.mArgs[var2].outForPersist(var1);
      }

   }

   protected void writeExternalForm(StringBuffer var1) {
      if (this.Enclosed) {
         var1.append('{');
      }

      if (this.mArgs.length > 0) {
         for(int var2 = 0; var2 < this.mArgs.length; ++var2) {
            if (var2 > 0) {
               var1.append(this.getTypeId());
            }

            this.mArgs[var2].writeExternalForm(var1);
         }
      }

      if (this.Enclosed) {
         var1.append('}');
      }

   }
}
