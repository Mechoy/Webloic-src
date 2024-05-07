package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.common.internal.VersionInfo;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.diagnostics.logging.LogVariablesImpl;
import weblogic.diagnostics.query.Query;
import weblogic.diagnostics.query.QueryException;
import weblogic.diagnostics.query.QueryFactory;
import weblogic.logging.LoggingConfigurationProcessor;
import weblogic.logging.Severities;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class LogFilter extends ConfigurationMBeanCustomizer {
   private int severityLevel = 16;
   private String[] userIds;
   private String[] subSystems;
   private String filterExpr = "";
   private Query query;
   private static final VersionInfo diabloVersion = new VersionInfo("9.0.0.0");

   public LogFilter(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public int getSeverityLevel() {
      return this.severityLevel;
   }

   public void setSeverityLevel(int var1) {
      this.severityLevel = var1;
      LogFilterMBean var2 = (LogFilterMBean)this.getMbean();
      if (this.isDelegateModeEnabled()) {
         String var3 = var2.getName();
         DomainMBean var4 = (DomainMBean)((DomainMBean)this.getMbean().getParent());
         if (var4 != null) {
            ServerMBean[] var5 = var4.getServers();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               ServerMBean var7 = var5[var6];
               LogFilterMBean var8 = var7.getLog().getDomainLogBroadcastFilter();
               if (var8 != null && var8.getName().equals(var3)) {
                  var7.getLog().setDomainLogBroadcastSeverity(Severities.severityNumToString(this.severityLevel));
               }
            }

         }
      }
   }

   public String[] getSubsystemNames() {
      return this.subSystems;
   }

   public void setSubsystemNames(String[] var1) throws InvalidAttributeValueException {
      this.subSystems = var1;
      if (this.isDelegateModeEnabled()) {
         this.computeFilterExpression();
      }

   }

   public String[] getUserIds() {
      return this.userIds;
   }

   public void setUserIds(String[] var1) throws InvalidAttributeValueException {
      this.userIds = var1;
      if (this.isDelegateModeEnabled()) {
         this.computeFilterExpression();
      }

   }

   protected boolean isDelegateModeEnabled() {
      DomainMBean var1 = (DomainMBean)this.getMbean().getDescriptor().getRootBean();
      String var2 = var1.getConfigurationVersion();
      if (var2 == null) {
         return false;
      } else {
         VersionInfo var3 = new VersionInfo(var2);
         return !var3.earlierThan(diabloVersion);
      }
   }

   public String getFilterExpression() {
      return this.filterExpr == null ? "" : this.filterExpr;
   }

   public void setFilterExpression(String var1) throws InvalidAttributeValueException {
      try {
         this.query = createQuery(var1);
         this.filterExpr = var1;
      } catch (QueryException var4) {
         String var3 = DiagnosticsTextTextFormatter.getInstance().getInvalidQueryExpressionText(var1);
         throw new InvalidAttributeValueException(var3);
      }
   }

   public Query getQuery() {
      return this.query;
   }

   private void computeFilterExpression() throws InvalidAttributeValueException {
      String var1 = LoggingConfigurationProcessor.convertOldAttrsToFilterExpression(this.userIds, this.subSystems);
      ((LogFilterMBean)this.getMbean()).setFilterExpression(var1);
   }

   private static Query createQuery(String var0) throws QueryException {
      if (var0 != null && var0.length() > 0) {
         LogVariablesImpl var1 = LogVariablesImpl.getInstance();
         return QueryFactory.createQuery(var1, var1, var0);
      } else {
         return null;
      }
   }
}
