package weblogic.management.mbeanservers.compatibility.internal;

import javax.management.MBeanException;
import weblogic.management.AttributeAddNotification;
import weblogic.management.AttributeRemoveNotification;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.utils.ArrayUtils;

public class PropertyChangeNotificationTranslator extends weblogic.management.jmx.modelmbean.PropertyChangeNotificationTranslator {
   public PropertyChangeNotificationTranslator(WLSModelMBeanContext var1, Object var2, NotificationGenerator var3) {
      super(var1, var2, var3);
   }

   protected void generateNotification(String var1, Class var2, Object var3, Object var4) {
      if (var2.isArray()) {
         Object[] var5 = (Object[])((Object[])var3);
         Object[] var6 = (Object[])((Object[])var4);
         ArrayUtils.computeDiff(var5, var6, this.createNewDiffHandler(var1, var2.getName()));
      } else {
         super.generateNotification(var1, var2, var3, var4);
      }

   }

   private ArrayUtils.DiffHandler createNewDiffHandler(final String var1, final String var2) {
      return new ArrayUtils.DiffHandler() {
         public void addObject(Object var1x) {
            AttributeAddNotification var2x = new AttributeAddNotification(PropertyChangeNotificationTranslator.this.generator.getObjectName(), PropertyChangeNotificationTranslator.this.generator.incrementSequenceNumber(), System.currentTimeMillis(), "add", var1, var2, var1x);

            try {
               PropertyChangeNotificationTranslator.this.generator.sendNotification(var2x);
            } catch (MBeanException var4) {
               JMXLogger.logErrorGeneratingAttributeChangeNotification(PropertyChangeNotificationTranslator.this.generator.getObjectName().toString(), var1);
            }

         }

         public void removeObject(Object var1x) {
            AttributeRemoveNotification var2x = new AttributeRemoveNotification(PropertyChangeNotificationTranslator.this.generator.getObjectName(), PropertyChangeNotificationTranslator.this.generator.incrementSequenceNumber(), System.currentTimeMillis(), "remove", var1, var2, var1x);

            try {
               PropertyChangeNotificationTranslator.this.generator.sendNotification(var2x);
            } catch (MBeanException var4) {
               JMXLogger.logErrorGeneratingAttributeChangeNotification(PropertyChangeNotificationTranslator.this.generator.getObjectName().toString(), var1);
            }

         }
      };
   }
}
