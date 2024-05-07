package weblogic.deployment.descriptors;

import java.util.EventListener;

public interface ValidationListener extends EventListener {
   void validationPerformed(ValidationEvent var1);
}
