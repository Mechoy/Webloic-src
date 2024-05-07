package weblogic.logging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.management.configuration.DomainLogFilterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.provider.AccessCallback;
import weblogic.utils.ArrayUtils;

public final class LogEditObserver implements AccessCallback, PropertyChangeListener, ArrayUtils.DiffHandler {
   private static final String DOMAIN_LOG_FILTERS_PROP = "DomainLogFilters";
   private DomainMBean domain;

   public void accessed(DomainMBean var1) {
      this.domain = var1;
      LogFilterMBean[] var2 = var1.getLogFilters();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         DomainLogFilterMBean var4 = var1.createDomainLogFilter(var2[var3].getName());
         var4.setDelegate(var2[var3]);
      }

      var1.addPropertyChangeListener(this);
   }

   public void shutdown() {
      this.domain.removePropertyChangeListener(this);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if (var2.equals("DomainLogFilters")) {
         DomainLogFilterMBean[] var3 = (DomainLogFilterMBean[])((DomainLogFilterMBean[])var1.getOldValue());
         DomainLogFilterMBean[] var4 = (DomainLogFilterMBean[])((DomainLogFilterMBean[])var1.getNewValue());
         ArrayUtils.computeDiff(var3, var4, this);
      }

   }

   public void addObject(Object var1) {
      DomainLogFilterMBean var2 = (DomainLogFilterMBean)var1;
      if (this.domain.lookupLogFilter(var2.getName()) == null) {
         LogFilterMBean var3 = this.domain.createLogFilter(var2.getName());
         var2.setDelegate(var3);
      }

   }

   public void removeObject(Object var1) {
      DomainLogFilterMBean var2 = (DomainLogFilterMBean)var1;
      if (this.domain.lookupLogFilter(var2.getName()) != null) {
         this.domain.destroyLogFilter(this.domain.lookupLogFilter(var2.getName()));
      }

   }
}
