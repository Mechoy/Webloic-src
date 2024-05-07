package weblogic.entitlement.expression;

public abstract class Identifier extends EExprRep {
   private Object mId = null;

   protected Identifier(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("null id");
      } else {
         this.mId = var1;
      }
   }

   protected int getDependsOnInternal() {
      return 1;
   }

   public final Object getId() {
      return this.mId;
   }

   void outForPersist(StringBuffer var1) {
      this.writeTypeId(var1);
      writeStr(this.mId.toString(), var1);
   }

   protected void writeExternalIdentifier(StringBuffer var1) {
      var1.append(this.mId.toString());
   }

   protected void writeExternalForm(StringBuffer var1) {
      if (this.Enclosed) {
         var1.append('{');
      }

      var1.append(this.getIdTag());
      var1.append('(');
      this.writeExternalIdentifier(var1);
      var1.append(')');
      if (this.Enclosed) {
         var1.append('}');
      }

   }

   protected abstract String getIdTag();

   public String toString() {
      return this.mId.toString();
   }
}
