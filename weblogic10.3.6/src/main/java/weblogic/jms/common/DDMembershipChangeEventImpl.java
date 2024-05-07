package weblogic.jms.common;

public class DDMembershipChangeEventImpl {
   private String ddConfigName;
   private String ddJndiName;
   private boolean isDD;
   private DDMemberInformation[] addedDDMemberInformation;
   private DDMemberInformation[] removedDDMemberInformation;

   public DDMembershipChangeEventImpl(boolean var1, String var2, String var3, DDMemberInformation[] var4, DDMemberInformation[] var5) {
      this.isDD = var1;
      this.ddConfigName = var2;
      this.ddJndiName = var3;
      this.addedDDMemberInformation = var4;
      this.removedDDMemberInformation = var5;
   }

   public String getDDConfigName() {
      return this.ddConfigName;
   }

   public String getDDJndiName() {
      return this.ddJndiName;
   }

   public DDMemberInformation[] getAddedDDMemberInformation() {
      return this.addedDDMemberInformation;
   }

   public DDMemberInformation[] getRemovedDDMemberInformation() {
      return this.removedDDMemberInformation;
   }

   public void setAdded(DDMemberInformation[] var1) {
      this.addedDDMemberInformation = var1;
   }

   public void setRemoved(DDMemberInformation[] var1) {
      this.removedDDMemberInformation = var1;
   }

   public boolean isDD() {
      return this.isDD;
   }
}
