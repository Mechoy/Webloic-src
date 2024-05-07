package weblogic.xml.crypto.wss.policy;

public class BSTClaims implements Claims {
   private String subjectName;
   private String valueType;

   public BSTClaims(String var1, String var2) {
      this.subjectName = var1;
      this.valueType = var2;
   }

   public String getSubjectName() {
      return this.subjectName;
   }

   public String getValueType() {
      return this.valueType;
   }
}
