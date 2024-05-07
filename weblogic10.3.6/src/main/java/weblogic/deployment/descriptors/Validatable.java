package weblogic.deployment.descriptors;

import java.util.Collection;

public interface Validatable {
   boolean isValid();

   Collection getErrors();

   void validate();
}
