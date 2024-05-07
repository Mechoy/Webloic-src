package weblogic.entitlement.expression;

public class GroupList extends UnionList {
   public GroupList(EExprRep[] var1) {
      super(var1);
   }

   public String getListTag() {
      return "Grp";
   }

   char getTypeId() {
      return 'G';
   }
}
