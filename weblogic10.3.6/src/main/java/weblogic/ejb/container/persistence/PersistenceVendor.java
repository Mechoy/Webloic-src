package weblogic.ejb.container.persistence;

import java.util.Iterator;
import java.util.Set;
import weblogic.utils.Debug;

public final class PersistenceVendor {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private String vendorName = null;
   private Set persistenceTypes = null;

   public String getName() {
      return this.vendorName;
   }

   public void setName(String var1) {
      if (debug) {
         Debug.assertion(var1 != null);
      }

      this.vendorName = var1;
   }

   public void addType(PersistenceType var1) {
      this.persistenceTypes.add(var1);
   }

   public Set getTypes() {
      return this.persistenceTypes;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[PersistenceVendor: ");
      var1.append("\n\tvendorName : " + this.vendorName);
      Iterator var2 = this.getTypes().iterator();

      while(var2.hasNext()) {
         var1.append("\n\t" + var2.next());
      }

      var1.append("\n PersistenceVendor]");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (null == var1) {
         return false;
      } else if (!(var1 instanceof PersistenceVendor)) {
         return false;
      } else {
         PersistenceVendor var2 = (PersistenceVendor)var1;
         if (this.getName() == null != (var2.getName() == null)) {
            return false;
         } else {
            return this.getName() == null && var2.getName() == null || this.getName().equals(var2.getName());
         }
      }
   }

   public int hashCode() {
      return this.vendorName.hashCode();
   }
}
