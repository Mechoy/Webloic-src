package weblogic.entitlement.expression;

public class UserList extends UnionList {
   public UserList(EExprRep[] var1) {
      super(var1);
   }

   public String getListTag() {
      return "Usr";
   }

   char getTypeId() {
      return 'U';
   }
}
