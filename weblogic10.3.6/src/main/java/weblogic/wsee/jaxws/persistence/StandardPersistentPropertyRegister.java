package weblogic.wsee.jaxws.persistence;

import java.util.Set;

public interface StandardPersistentPropertyRegister {
   Set<String> getStandardProperties();

   Set<String> getStandardPropertyBagClassNames();
}
