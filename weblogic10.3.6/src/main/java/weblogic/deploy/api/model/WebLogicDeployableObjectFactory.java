package weblogic.deploy.api.model;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.utils.LibrarySpec;
import weblogic.deploy.api.model.sca.EditableScaApplicationObject;

public interface WebLogicDeployableObjectFactory {
   EditableJ2eeApplicationObject createApplicationObject() throws IOException;

   EditableScaApplicationObject createScaApplicationObject() throws IOException;

   EditableDeployableObject createDeployableObject(String var1, String var2, ModuleType var3) throws IOException;

   WebLogicDeployableObject createDeployableObject(File var1) throws IOException, InvalidModuleException;

   WebLogicDeployableObject createDeployableObject(File var1, File var2) throws IOException, InvalidModuleException;

   WebLogicDeployableObject createDeployableObject(File var1, File var2, File var3, File var4, LibrarySpec[] var5) throws IOException, InvalidModuleException;

   WebLogicDeployableObject createLazyDeployableObject(File var1, File var2, File var3, File var4, LibrarySpec[] var5) throws IOException, InvalidModuleException;
}
