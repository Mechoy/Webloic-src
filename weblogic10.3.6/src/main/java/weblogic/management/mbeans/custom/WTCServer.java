package weblogic.management.mbeans.custom;

import weblogic.management.configuration.WTCExportMBean;
import weblogic.management.configuration.WTCImportMBean;
import weblogic.management.configuration.WTCLocalTuxDomMBean;
import weblogic.management.configuration.WTCPasswordMBean;
import weblogic.management.configuration.WTCRemoteTuxDomMBean;
import weblogic.management.configuration.WTCResourcesMBean;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.configuration.WTCtBridgeRedirectMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class WTCServer extends ConfigurationMBeanCustomizer {
   public WTCServer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public WTCImportMBean[] getImports() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCImports();
   }

   public WTCExportMBean[] getExports() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCExports();
   }

   public WTCLocalTuxDomMBean[] getLocalTuxDoms() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCLocalTuxDoms();
   }

   public WTCRemoteTuxDomMBean[] getRemoteTuxDoms() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCRemoteTuxDoms();
   }

   public WTCResourcesMBean getResources() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCResources();
   }

   public WTCPasswordMBean[] getPasswords() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCPasswords();
   }

   public WTCtBridgeRedirectMBean[] gettBridgeRedirects() {
      WTCServerMBean var1 = (WTCServerMBean)this.getMbean();
      return var1.getWTCtBridgeRedirects();
   }
}
