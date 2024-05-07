package weblogic.wsee.jaxws.tubeline;

import java.util.Set;

public class TubelineAssemblerItem {
   private String name;
   private TubeFactory factory;
   private Set<String> goAfter;
   private Set<String> goBefore;
   private Set<String> required;

   public TubelineAssemblerItem(String var1, TubeFactory var2) {
      this(var1, var2, (Set)null, (Set)null, (Set)null);
   }

   public TubelineAssemblerItem(String var1, TubeFactory var2, Set<String> var3, Set<String> var4, Set<String> var5) {
      this.name = var1;
      this.factory = var2;
      this.goAfter = var3;
      this.goBefore = var4;
      this.required = var5;
   }

   public TubeFactory getFactory() {
      return this.factory;
   }

   public void setFactory(TubeFactory var1) {
      this.factory = var1;
   }

   public Set<String> getGoAfter() {
      return this.goAfter;
   }

   public void setGoAfter(Set<String> var1) {
      this.goAfter = var1;
   }

   public Set<String> getGoBefore() {
      return this.goBefore;
   }

   public void setGoBefore(Set<String> var1) {
      this.goBefore = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Set<String> getRequired() {
      return this.required;
   }

   public void setRequired(Set<String> var1) {
      this.required = var1;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.required == null ? 0 : this.required.hashCode());
      var2 = 31 * var2 + (this.goAfter == null ? 0 : this.goAfter.hashCode());
      var2 = 31 * var2 + (this.goBefore == null ? 0 : this.goBefore.hashCode());
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         TubelineAssemblerItem var2 = (TubelineAssemblerItem)var1;
         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         if (this.required == null) {
            if (var2.required != null) {
               return false;
            }
         } else if (!this.required.equals(var2.required)) {
            return false;
         }

         if (this.goAfter == null) {
            if (var2.goAfter != null) {
               return false;
            }
         } else if (!this.goAfter.equals(var2.goAfter)) {
            return false;
         }

         if (this.goBefore == null) {
            if (var2.goBefore != null) {
               return false;
            }
         } else if (!this.goBefore.equals(var2.goBefore)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("TAI: ");
      var1.append("name = ");
      var1.append(this.name);
      if (this.required != null) {
         var1.append(" required = ");
         var1.append(this.required);
      }

      if (this.goBefore != null) {
         var1.append(" goBefore = ");
         var1.append(this.goBefore);
      }

      if (this.goAfter != null) {
         var1.append(" goAfter = ");
         var1.append(this.goAfter);
      }

      return var1.toString();
   }
}
