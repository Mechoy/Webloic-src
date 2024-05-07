package weblogic.entitlement.expression;

public class RoleList extends UnionList {
   public RoleList(EExprRep[] var1) {
      super(var1);
   }

   public String getListTag() {
      return "Rol";
   }

   char getTypeId() {
      return 'R';
   }
}
