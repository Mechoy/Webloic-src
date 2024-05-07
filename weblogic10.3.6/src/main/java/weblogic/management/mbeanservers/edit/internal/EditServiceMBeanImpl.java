package weblogic.management.mbeanservers.edit.internal;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.edit.RecordingManagerMBean;
import weblogic.management.mbeanservers.internal.ServiceImpl;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditFailedException;

public class EditServiceMBeanImpl extends ServiceImpl implements EditServiceMBean {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXEdit");
   private EditAccess edit;
   private ConfigurationManagerMBean configurationManager;
   private RecordingManagerMBean recordingManager;

   EditServiceMBeanImpl(EditAccess var1, WLSModelMBeanContext var2) {
      super("EditService", EditServiceMBean.class.getName(), (Service)null);
      this.edit = var1;
      this.configurationManager = new ConfigurationManagerMBeanImpl(var1, var2);
      this.recordingManager = new RecordingManagerMBeanImpl();
   }

   public DomainMBean getDomainConfiguration() {
      try {
         return this.edit.getDomainBeanWithoutLock();
      } catch (EditFailedException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception getting domain configuration ", var2);
         }

         throw new RuntimeException(var2);
      }
   }

   public ConfigurationManagerMBean getConfigurationManager() {
      return this.configurationManager;
   }

   public RecordingManagerMBean getRecordingManager() {
      return this.recordingManager;
   }
}
