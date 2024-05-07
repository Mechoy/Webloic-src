package weblogic.ejb.container.cmp.rdbms;

public final class RelationshipRole {
   private String m_roleName = null;
   private String m_groupName = null;
   private ObjectLink m_columnMap = null;

   public RelationshipRole(String var1, String var2, ObjectLink var3) {
      this.m_roleName = var1;
      this.m_groupName = var2;
      this.m_columnMap = var3;
   }

   public String toString() {
      return "[RelationshipRole name:" + this.m_roleName + " group:" + this.m_groupName + " map:" + this.m_columnMap + "]";
   }
}
