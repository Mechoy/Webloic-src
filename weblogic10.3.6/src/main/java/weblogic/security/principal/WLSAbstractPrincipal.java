package weblogic.security.principal;

import java.security.AccessController;

public abstract class WLSAbstractPrincipal implements WLSPrincipal {
   private byte[] signature = null;
   private byte[] salt = null;
   private String name = null;
   private static boolean useSignature;
   private static final long serialVersionUID = -5765092415154848004L;
   private String dn = null;
   private String guid = null;
   private boolean equalsCaseInsensitive = false;
   private boolean equalsCompareDnAndGuid = false;
   protected boolean principalFactoryCreated = false;

   public boolean isEqualsCaseInsensitive() {
      return this.equalsCaseInsensitive;
   }

   public boolean isEqualsCompareDnAndGuid() {
      return this.equalsCompareDnAndGuid;
   }

   public void setEqualsCaseInsensitive(boolean equalsCaseInsensitive) {
      this.equalsCaseInsensitive = equalsCaseInsensitive;
   }

   public void setEqualsCompareDnAndGuid(boolean equalsCompareDnAndGuid) {
      this.equalsCompareDnAndGuid = equalsCompareDnAndGuid;
   }

   protected WLSAbstractPrincipal() {
      this.salt = String.valueOf(System.currentTimeMillis()).getBytes();
   }

   protected WLSAbstractPrincipal(boolean createSalt) {
      if (createSalt) {
         this.salt = String.valueOf(System.currentTimeMillis()).getBytes();
      }

   }

   public String getName() {
      return this.name;
   }

   protected void setName(String name) {
      this.name = name;
   }

   public boolean equals(Object another) {
      if (another == null) {
         return false;
      } else if (this == another) {
         return true;
      } else if (!(another instanceof WLSAbstractPrincipal)) {
         return false;
      } else {
         WLSAbstractPrincipal anotherPrincipal = (WLSAbstractPrincipal)another;
         if (this.name != null && anotherPrincipal.name != null) {
            if (this.equalsCompareDnAndGuid) {
               if (this.guid != null && anotherPrincipal.guid != null) {
                  if (!this.guid.equalsIgnoreCase(anotherPrincipal.guid)) {
                     return false;
                  }
               } else if (this.dn != null && anotherPrincipal.dn != null) {
                  if (!this.dn.equalsIgnoreCase(anotherPrincipal.dn)) {
                     return false;
                  }
               } else if (this.equalsCaseInsensitive) {
                  if (!this.name.equalsIgnoreCase(anotherPrincipal.name)) {
                     return false;
                  }
               } else if (!this.name.equals(anotherPrincipal.name)) {
                  return false;
               }
            } else if (this.equalsCaseInsensitive) {
               if (!this.name.equalsIgnoreCase(anotherPrincipal.name)) {
                  return false;
               }
            } else if (!this.name.equals(anotherPrincipal.name)) {
               return false;
            }

            if (useSignature) {
               byte[] anotherSignature = anotherPrincipal.getSignature();
               if (this.signature == anotherSignature) {
                  return true;
               }

               if (this.signature == null || anotherSignature == null) {
                  return false;
               }

               if (this.signature.length != anotherSignature.length) {
                  return false;
               }

               for(int i = 0; i < this.signature.length; ++i) {
                  if (this.signature[i] != anotherSignature[i]) {
                     return false;
                  }
               }
            }

            return true;
         } else {
            return this.name == anotherPrincipal.name;
         }
      }
   }

   public String toString() {
      return this.name;
   }

   public int hashCode() {
      return this.equalsCaseInsensitive ? this.name.toLowerCase().hashCode() : this.name.hashCode();
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }

   public byte[] getSalt() {
      return this.salt;
   }

   public void setSalt(byte[] salt) {
      this.salt = salt;
   }

   public byte[] getSignedData() {
      return this.equalsCaseInsensitive ? this.name.toLowerCase().getBytes() : this.name.getBytes();
   }

   public String getGuid() {
      return this.guid;
   }

   protected void setGuid(String guid) {
      this.guid = guid;
   }

   public String getDn() {
      return this.dn;
   }

   protected void setDn(String dn) {
      this.dn = dn;
   }

   public boolean isPrincipalFactoryCreated() {
      return this.principalFactoryCreated;
   }

   static {
      try {
         GetSignatureProperty getSignatureProperty = new GetSignatureProperty((1)null);
         useSignature = (Boolean)AccessController.doPrivileged(getSignatureProperty);
      } catch (SecurityException var1) {
         useSignature = true;
      }

   }
}
