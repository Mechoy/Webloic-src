package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import weblogic.upgrade.PluginActionDelegatePlugIn;

public class DomainDirectoryFileReorgPlugIn extends PluginActionDelegatePlugIn {
   private static String[] RESTRUCTURERS = new String[]{"weblogic.upgrade.domain.directoryrestructure.MoveLogFilesPlugin", "weblogic.upgrade.domain.directoryrestructure.MoveSecurityFilesPlugin", "weblogic.upgrade.domain.directoryrestructure.MoveDeploymentFilesPlugin", "weblogic.upgrade.domain.directoryrestructure.DirectoryRestructureCleanupPlugin", "weblogic.upgrade.PluginActionDelegate"};

   public DomainDirectoryFileReorgPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1, RESTRUCTURERS);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
   }

   public void execute() throws PlugInException {
      super.execute();
   }
}
