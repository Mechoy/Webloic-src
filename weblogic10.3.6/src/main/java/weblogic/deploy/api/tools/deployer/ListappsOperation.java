package weblogic.deploy.api.tools.deployer;

import java.util.HashSet;
import java.util.Iterator;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;

public class ListappsOperation extends Jsr88Operation {
   private HashSet apps;
   private AppRuntimeStateRuntimeMBean appRT;

   public ListappsOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() {
   }

   public void validate() throws DeployerException {
      super.validate();
   }

   protected void prepareTmids() {
   }

   public void execute() throws Exception {
      this.apps = new HashSet();
      TargetModuleID[] var1 = null;
      Target[] var2 = this.dm.getTargets();

      for(int var3 = 0; var3 < WebLogicModuleType.getModuleTypes(); ++var3) {
         ModuleType var4 = WebLogicModuleType.getModuleType(var3);
         if (var4 != null) {
            var1 = this.dm.getAvailableModules(var4, var2);
            if (var1 != null) {
               for(int var5 = 0; var5 < var1.length; ++var5) {
                  if (var1[var5].getParentTargetModuleID() == null) {
                     this.apps.add(var1[var5].getModuleID());
                  }
               }
            }
         }
      }

   }

   public int report() {
      if (this.apps.isEmpty()) {
         System.out.println(cat.noAppToList());
      } else {
         Iterator var1 = this.apps.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            String var3 = " " + ApplicationVersionUtils.getDisplayName(var2) + this.getActiveState(var2);
            System.out.println(var3);
         }

         System.out.println(cat.appsFound() + " " + this.apps.size());
      }

      return 0;
   }

   public String getOperation() {
      return "listapps";
   }

   private String getActiveState(String var1) {
      if (ApplicationVersionUtils.getVersionId(var1) == null) {
         return "";
      } else {
         AppRuntimeStateRuntimeMBean var2 = this.getAppRuntimeStateMBean();
         if (var2 == null) {
            return "";
         } else {
            Target[] var3 = this.dm.getTargets();
            if (var3 != null && var3.length != 0 && var3[0] != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  String var5 = var2.getCurrentState(var1, var3[var4].getName());
                  if (var5.equals("STATE_ACTIVE")) {
                     return " <ACTIVE VERSION>";
                  }
               }

               return "";
            } else {
               return "";
            }
         }
      }
   }

   private AppRuntimeStateRuntimeMBean getAppRuntimeStateMBean() {
      if (this.appRT == null) {
         try {
            this.appRT = this.dm.getHelper().getAppRuntimeStateMBean();
         } catch (Exception var2) {
         }
      }

      return this.appRT;
   }
}
