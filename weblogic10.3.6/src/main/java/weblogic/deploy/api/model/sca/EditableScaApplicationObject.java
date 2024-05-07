package weblogic.deploy.api.model.sca;

import weblogic.deploy.api.model.EditableDeployableObject;

public interface EditableScaApplicationObject extends ScaApplicationObject, EditableDeployableObject {
   void addDeployableObject(EditableDeployableObject var1);
}
