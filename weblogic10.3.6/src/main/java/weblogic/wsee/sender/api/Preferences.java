package weblogic.wsee.sender.api;

import java.io.Serializable;

public class Preferences implements Serializable {
   private static final long serialVersionUID = 1L;
   private String _logicalStoreName = null;

   public String getLogicalStoreName() {
      return this._logicalStoreName;
   }

   public void setLogicalStoreName(String var1) {
      this._logicalStoreName = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Preferences)) {
         return false;
      } else {
         Preferences var2 = (Preferences)var1;
         return var2._logicalStoreName == null && this._logicalStoreName == null || var2._logicalStoreName != null && var2._logicalStoreName.equals(this._logicalStoreName);
      }
   }

   public int hashCode() {
      return this._logicalStoreName == null ? 0 : this._logicalStoreName.hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("Preferences: ");
      var1.append("LogicalStoreName=").append(this._logicalStoreName);
      return var1.toString();
   }
}
