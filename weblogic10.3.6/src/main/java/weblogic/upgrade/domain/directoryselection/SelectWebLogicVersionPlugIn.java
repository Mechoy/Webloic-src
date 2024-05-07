package weblogic.upgrade.domain.directoryselection;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.util.Arrays;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class SelectWebLogicVersionPlugIn extends AbstractPlugIn {
   private static String DOMAIN_VERSION_SPECIFIED_BY_USER = "8.1";
   private static boolean DOMAIN_VERSION_HAS_BEEN_SPECIFIED_BY_USER = false;
   private static boolean DOMAIN_VERSION_9_0_OR_HIGHER_SPECIFIED_BY_USER = false;
   private static String DOMAIN_VERSION_9_0 = "9.0";

   public SelectWebLogicVersionPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      UpgradeHelper.addSummaryMessageForOutputLocation(var1, this.getName());
      DefaultChoiceInputAdapter var2 = (DefaultChoiceInputAdapter)this._adapter;
      var2.setGroupDescription(UpgradeHelper.i18n("SelectWebLogicVersionPlugIn.ChoiceIA.description.text"));
      var2.setMultipleSelectionPermitted(false);
      Choice[] var3 = new Choice[4];
      String[] var4 = new String[]{"6.1", "7.0", "8.1", UpgradeHelper.i18n("SelectWebLogicVersionPlugIn.ChoiceIA.domain.90orhigher.text")};
      String var5 = System.getProperty("weblogic.upgrade.domainversion");
      if (!Arrays.asList(var4).contains(var5)) {
         var5 = "8.1";
      }

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var3[var6] = new Choice(var4[var6], false);
         var3[var6].setPrompt(var4[var6]);
         if (var5.equals(var4[var6])) {
            var3[var6].setSelected(true);
         }
      }

      var2.setChoices(var3);
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      DefaultChoiceInputAdapter var2 = (DefaultChoiceInputAdapter)this._adapter;
      Choice var3 = var2.getSelectedChoices()[0];
      String var4 = var3.getId();
      String var5 = UpgradeHelper.i18n("SelectWebLogicVersionPlugIn.ChoiceIA.domain.90orhigher.text");
      if (var5.equals(var4)) {
         var4 = DOMAIN_VERSION_9_0;
         DOMAIN_VERSION_9_0_OR_HIGHER_SPECIFIED_BY_USER = true;
      }

      this._context.put(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY, var4);
      return new ValidationStatus(true);
   }

   public void execute() throws PlugInException {
      String var1 = (String)this._context.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
      DOMAIN_VERSION_SPECIFIED_BY_USER = var1;
      DOMAIN_VERSION_HAS_BEEN_SPECIFIED_BY_USER = true;
      UpgradeHelper.log(this, UpgradeHelper.i18n("SelectWebLogicVersionPlugIn.execute.weblogic_version_set", (Object)var1));
   }

   public static String getUserSpecifiedDomainVersion() {
      return DOMAIN_VERSION_SPECIFIED_BY_USER;
   }

   public static boolean getDomainVersionHasBeenSpecifiedByUser() {
      return DOMAIN_VERSION_HAS_BEEN_SPECIFIED_BY_USER;
   }

   public static void validateDomainVersion(String var0) {
      if (var0 == null || var0.trim().length() <= 0 || !DOMAIN_VERSION_9_0_OR_HIGHER_SPECIFIED_BY_USER || var0.startsWith("6.") || var0.startsWith("7.") || var0.startsWith("8.")) {
         if (var0 != null && var0.trim().length() > 0 && DOMAIN_VERSION_HAS_BEEN_SPECIFIED_BY_USER && !var0.startsWith(DOMAIN_VERSION_SPECIFIED_BY_USER)) {
            if (var0.length() >= 3) {
               var0 = var0.substring(0, 3);
            }

            String var1 = UpgradeHelper.i18n("SelectWebLogicVersionPlugIn.validateDomainVersion.error", DOMAIN_VERSION_SPECIFIED_BY_USER, var0);
            throw new VersionException(var1);
         }
      }
   }
}
