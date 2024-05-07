package weblogic.ejb.container.persistence;

import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.container.persistence.spi.Dependent;
import weblogic.ejb.container.persistence.spi.Dependents;
import weblogic.utils.Debug;

public final class DependentsImpl implements Dependents {
   private String description;
   private Map dependents = new HashMap();

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public Dependent getDependent(String var1) {
      Debug.assertion(this.dependents.get(var1) != null);
      return (Dependent)this.dependents.get(var1);
   }

   public void addDependent(Dependent var1) {
      this.dependents.put(var1.getName(), var1);
   }

   public Map getDependentMap() {
      return this.dependents;
   }
}
