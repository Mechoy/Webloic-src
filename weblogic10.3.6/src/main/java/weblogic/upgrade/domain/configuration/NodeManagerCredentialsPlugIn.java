package weblogic.upgrade.domain.configuration;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.ia.DefaultCompositeInputAdapter;
import com.bea.plateng.plugin.ia.DefaultTextInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.nodemanager.server.DomainDir;
import weblogic.nodemanager.server.UserInfo;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class NodeManagerCredentialsPlugIn extends AbstractPlugIn {
   private static final String USERNAME_PROP = "username";
   private static final String PASSWORD_PROP = "password";

   public NodeManagerCredentialsPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      DomainMBean var2 = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      String var3 = (String)var1.get(DomainPlugInConstants.NM_CREDENTIALS_USERNAME_KEY);
      String var4 = (String)var1.get(DomainPlugInConstants.NM_CREDENTIALS_PASSWORD_KEY);
      SecurityConfigurationMBean var5 = var2.getSecurityConfiguration();
      if (this.isBlank(var3)) {
         var3 = var5.getNodeManagerUsername();
      }

      if (this.isBlank(var4)) {
         var4 = var5.getNodeManagerPassword();
      }

      File var6 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      File var7 = new File(var6, "config/nodemanager/nm_password.properties");
      if (this.isBlank(var3)) {
         var3 = "weblogic";
         if (var7.exists()) {
            Properties var8 = this.load(var7);
            var3 = var8.getProperty("username");
            var4 = var8.getProperty("password");
         }
      }

      if (this.isBlank(var4)) {
         byte[] var12 = new byte[4];
         (new SecureRandom()).nextBytes(var12);
         var4 = (new BigInteger(var12)).toString(16);
      }

      var1.put(DomainPlugInConstants.NM_CREDENTIALS_USERNAME_KEY, var3);
      var1.put(DomainPlugInConstants.NM_CREDENTIALS_PASSWORD_KEY, var4);
      DefaultCompositeInputAdapter var13 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultTextInputAdapter var9 = (DefaultTextInputAdapter)var13.getInputAdapter("UsernameIA");
      var9.setValue(var3);
      var9.setPrompt(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.UsernameIA.input.prompt.text"));
      var9.setDescription(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.UsernameIA.input.description.text"));
      DefaultTextInputAdapter var10 = (DefaultTextInputAdapter)var13.getInputAdapter("PasswordIA");
      var10.setAsPassword(true);
      var10.setValue(var4);
      var10.setPrompt(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.PasswordIA.input.prompt.text"));
      var10.setDescription(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.PasswordIA.input.description.text"));
      DefaultTextInputAdapter var11 = (DefaultTextInputAdapter)var13.getInputAdapter("PasswordConfirmIA");
      var11.setAsPassword(true);
      var11.setValue(var4);
      var11.setPrompt(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.PasswordConfirmIA.input.prompt.text"));
      var11.setDescription(UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.PasswordConfirmIA.input.description.text"));
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      ValidationStatus var2 = super.validateInputAdapter(var1);
      DefaultCompositeInputAdapter var3 = (DefaultCompositeInputAdapter)this._adapter;
      DefaultTextInputAdapter var4 = (DefaultTextInputAdapter)var3.getInputAdapter("UsernameIA");
      DefaultTextInputAdapter var5 = (DefaultTextInputAdapter)var3.getInputAdapter("PasswordIA");
      DefaultTextInputAdapter var6 = (DefaultTextInputAdapter)var3.getInputAdapter("PasswordConfirmIA");
      String var7 = var4.getValue();
      String var8 = var5.getValue();
      String var9 = var6.getValue();
      if (!var8.equals(var9)) {
         var2.setErrorMessage("Passwords supplied do not match");
         var2.setValid(false);
         return var2;
      } else {
         this._context.put(DomainPlugInConstants.NM_CREDENTIALS_USERNAME_KEY, var7);
         this._context.put(DomainPlugInConstants.NM_CREDENTIALS_PASSWORD_KEY, var8);
         var2.setValid(true);
         return var2;
      }
   }

   public void execute() throws PlugInException {
      DomainMBean var1 = (DomainMBean)this._context.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      SecurityConfigurationMBean var2 = var1.getSecurityConfiguration();
      String var3 = (String)this._context.get(DomainPlugInConstants.NM_CREDENTIALS_USERNAME_KEY);
      String var4 = (String)this._context.get(DomainPlugInConstants.NM_CREDENTIALS_PASSWORD_KEY);
      File var5 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      File var6 = new File(var5, "config/nodemanager/nm_password.properties");
      boolean var7 = var6.exists();
      boolean var8 = var3.equals(var2.getNodeManagerUsername()) && var4.equals(var2.getNodeManagerPassword());
      if (!var8) {
         UpgradeHelper.log(this, UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.execute.set_NM_cred_in_memory"));
         var2.setNodeManagerUsername(var3);
         var2.setNodeManagerPassword(var4);
      }

      if (!var8 || !var7) {
         try {
            UpgradeHelper.log(this, UpgradeHelper.i18n("NodeManagerCredentialsPlugIn.execute.writing_nm_password_file"));
            var6.getParentFile().mkdirs();
            OutputStreamWriter var9 = new OutputStreamWriter(new FileOutputStream(var6));
            String var10 = System.getProperty("line.separator");
            var9.write("username=" + var3 + var10 + "password=" + var4 + var10);
            var9.close();
            DomainDir var11 = new DomainDir(var5.getAbsolutePath());
            UserInfo var12 = new UserInfo(var11);
            var12.load(var6);
            var12.save(var6);
         } catch (IOException var13) {
            throw new PlugInException(this.getName(), "Execute Exception ... " + var13.toString(), var13);
         }
      }

   }

   private Properties load(File var1) {
      Properties var2 = new Properties();

      try {
         var2.load(new FileInputStream(var1));
      } catch (IOException var4) {
      }

      return var2;
   }

   private boolean isBlank(String var1) {
      return var1 == null || var1.trim().length() == 0;
   }
}
