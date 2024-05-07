package weblogic.iiop.csi;

import java.util.Arrays;
import org.omg.CORBA.MARSHAL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.ArrayUtils;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.Hex;

public class IdentityToken {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private int identityType;
   private boolean absent;
   private boolean anonymous;
   private byte[] principalName;
   private byte[] certChain;
   private byte[] distinguishedName;
   private int hash;

   public IdentityToken() {
   }

   public IdentityToken(int var1, boolean var2, byte[] var3) {
      this.identityType = var1;
      switch (this.identityType) {
         case 0:
            this.absent = var2;
            break;
         case 1:
            this.anonymous = var2;
            break;
         case 2:
            this.principalName = var3;
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new MARSHAL("Unsupported Identity Type.");
         case 4:
            this.certChain = var3;
            break;
         case 8:
            this.distinguishedName = var3;
      }

      this.hash = var1 ^ ArrayUtils.hashCode(var3);
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("created " + this);
      }

   }

   protected IdentityToken(IIOPInputStream var1) {
      this.identityType = var1.read_long();
      long var3;
      switch (this.identityType) {
         case 0:
            this.absent = var1.read_boolean();
            this.hash = this.identityType ^ 0;
            break;
         case 1:
            this.anonymous = var1.read_boolean();
            this.hash = this.identityType ^ 0;
            break;
         case 2:
            var3 = var1.startEncapsulation();
            this.principalName = var1.read_octet_sequence();
            var1.endEncapsulation(var3);
            this.hash = this.identityType ^ ArrayUtils.hashCode(this.principalName);
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new MARSHAL("Unsupported Identity Type.");
         case 4:
            var3 = var1.startEncapsulation();
            this.certChain = var1.read_octet_sequence();
            var1.endEncapsulation(var3);
            this.hash = this.identityType ^ ArrayUtils.hashCode(this.certChain);
            break;
         case 8:
            var3 = var1.startEncapsulation();
            this.distinguishedName = var1.read_octet_sequence();
            var1.endEncapsulation(var3);
            this.hash = this.identityType ^ ArrayUtils.hashCode(this.distinguishedName);
      }

      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("read " + this);
      }

   }

   public int getIdentityType() {
      return this.identityType;
   }

   public boolean getAbsent() {
      return this.absent;
   }

   public boolean getAnonymous() {
      return this.anonymous;
   }

   public byte[] getPrincipalName() {
      return this.principalName;
   }

   public byte[] getCertChain() {
      return this.certChain;
   }

   public byte[] getDistinguishedName() {
      return this.distinguishedName;
   }

   public void write(IIOPOutputStream var1) {
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("writing " + this);
      }

      var1.write_long(this.identityType);
      long var2;
      switch (this.identityType) {
         case 0:
            var1.write_boolean(this.absent);
            break;
         case 1:
            var1.write_boolean(this.anonymous);
            break;
         case 2:
            var2 = var1.startEncapsulation();
            var1.write_octet_sequence(this.principalName);
            var1.endEncapsulation(var2);
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new MARSHAL("Unsupported Identity Type.");
         case 4:
            var2 = var1.startEncapsulation();
            var1.write_octet_sequence(this.certChain);
            var1.endEncapsulation(var2);
            break;
         case 8:
            var2 = var1.startEncapsulation();
            var1.write_octet_sequence(this.distinguishedName);
            var1.endEncapsulation(var2);
      }

   }

   public boolean equals(Object var1) {
      if (var1 instanceof IdentityToken) {
         IdentityToken var2 = (IdentityToken)var1;
         if (this.identityType != var2.identityType) {
            return false;
         } else {
            switch (this.identityType) {
               case 0:
               case 1:
                  return true;
               case 2:
                  return Arrays.equals(this.principalName, var2.principalName);
               case 3:
               case 5:
               case 6:
               case 7:
               default:
                  return false;
               case 4:
                  return Arrays.equals(this.certChain, var2.certChain);
               case 8:
                  return Arrays.equals(this.distinguishedName, var2.distinguishedName);
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   public String toString() {
      String var1 = "IdentityToken (IdentityType = " + this.identityType;
      switch (this.identityType) {
         case 0:
            var1 = var1 + ", absent = " + this.absent;
            break;
         case 1:
            var1 = var1 + ", anonymous = " + this.anonymous;
            break;
         case 2:
            var1 = var1 + ", principal = " + Hex.dump(this.principalName);
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            var1 = var1 + "Unsupported Identity Type.";
            break;
         case 4:
            var1 = var1 + ", certChain = " + Hex.dump(this.certChain);
            break;
         case 8:
            var1 = var1 + ", distinguished = " + Hex.dump(this.distinguishedName);
      }

      return var1;
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<IdentityToken>: " + var0);
   }
}
