package weblogic.wsee.util;

import com.bea.staxb.runtime.MarshalOptions;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.EditableDescriptorManager;

public class NamespaceSpecifyingDescriptorManager extends EditableDescriptorManager {
   protected MarshalOptions createMarshalOptions(Descriptor var1, String var2) {
      MarshalOptions var3 = super.createMarshalOptions(var1, var2);
      var3.setUseDefaultNamespaceForRootElement(false);
      return var3;
   }
}
