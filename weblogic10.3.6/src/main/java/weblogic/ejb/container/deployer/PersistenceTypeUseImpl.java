package weblogic.ejb.container.deployer;

import weblogic.ejb.container.interfaces.PersistenceTypeUse;
import weblogic.ejb.container.persistence.PersistenceType;

class PersistenceTypeUseImpl implements PersistenceTypeUse {
   private String typeIdentifier = null;
   private String typeVersion = null;

   public PersistenceTypeUseImpl() {
   }

   public PersistenceTypeUseImpl(String var1, String var2) {
      this.setIdentifier(var1);
      this.setVersion(var2);
   }

   public String getIdentifier() {
      return this.typeIdentifier;
   }

   public void setIdentifier(String var1) {
      this.typeIdentifier = var1;
   }

   public String getVersion() {
      return this.typeVersion;
   }

   public void setVersion(String var1) {
      this.typeVersion = var1;
   }

   public boolean matchesType(PersistenceType var1) {
      if (!this.typeIdentifier.equals(var1.getIdentifier())) {
         return false;
      } else {
         return this.typeVersion.equals(var1.getVersion());
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[PersistenceDescriptor.PersistenceTypeUse: ");
      var1.append(" identifier = " + this.typeIdentifier);
      var1.append(" version = " + this.typeVersion);
      var1.append("]");
      return var1.toString();
   }
}
