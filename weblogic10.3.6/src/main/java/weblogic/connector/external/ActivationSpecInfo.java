package weblogic.connector.external;

import java.util.List;

public interface ActivationSpecInfo {
   String getActivationSpecClass();

   List getRequiredProps();
}
