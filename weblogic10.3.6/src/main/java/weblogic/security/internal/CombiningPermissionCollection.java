package weblogic.security.internal;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import weblogic.security.SecurityLogger;
import weblogic.utils.enumerations.SequencingEnumerator;

public class CombiningPermissionCollection extends PermissionCollection {
   private PermissionCollection pc1;
   private PermissionCollection pc2;
   private boolean treatFirstReadOnly;
   private boolean treatSecondReadOnly;
   private static final boolean DEBUG = false;

   public CombiningPermissionCollection(PermissionCollection var1, boolean var2, PermissionCollection var3, boolean var4) {
      this.pc1 = var1;
      this.pc2 = var3;
      this.treatFirstReadOnly = var2;
      this.treatSecondReadOnly = var4;
   }

   public Enumeration elements() {
      SequencingEnumerator var1 = new SequencingEnumerator();
      var1.addEnumeration(this.pc1.elements());
      var1.addEnumeration(this.pc2.elements());
      return var1;
   }

   public boolean isReadOnly() {
      return (this.pc1.isReadOnly() || this.treatFirstReadOnly) && (this.pc2.isReadOnly() || this.treatSecondReadOnly);
   }

   public void setReadOnly() {
      this.pc1.setReadOnly();
      this.pc2.setReadOnly();
   }

   public String toString() {
      return this.pc1.toString() + " PLUS " + this.pc2.toString();
   }

   public boolean implies(Permission var1) {
      return this.pc1.implies(var1) || this.pc2.implies(var1);
   }

   public void add(Permission var1) {
      if (!this.pc1.isReadOnly() && !this.treatFirstReadOnly) {
         this.pc1.add(var1);
      } else {
         if (this.pc2.isReadOnly() || this.treatSecondReadOnly) {
            throw new SecurityException(SecurityLogger.getCantUpdateReadonlyPermColl());
         }

         this.pc2.add(var1);
      }

   }
}
