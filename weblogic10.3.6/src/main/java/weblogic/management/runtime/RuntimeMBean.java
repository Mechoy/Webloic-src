package weblogic.management.runtime;

import java.beans.PropertyChangeListener;
import weblogic.management.WebLogicMBean;

public interface RuntimeMBean extends WebLogicMBean {
   void preDeregister() throws Exception;

   void addPropertyChangeListener(PropertyChangeListener var1);

   void removePropertyChangeListener(PropertyChangeListener var1);
}
