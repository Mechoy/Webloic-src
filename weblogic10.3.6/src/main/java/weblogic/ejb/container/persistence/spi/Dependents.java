package weblogic.ejb.container.persistence.spi;

import java.util.Map;

public interface Dependents {
   String getDescription();

   Dependent getDependent(String var1);

   Map getDependentMap();
}
