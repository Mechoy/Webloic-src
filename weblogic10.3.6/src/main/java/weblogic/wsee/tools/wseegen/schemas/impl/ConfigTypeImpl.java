package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.ConfigType;
import weblogic.wsee.tools.wseegen.schemas.DeploymentListenersType;

public class ConfigTypeImpl extends XmlComplexContentImpl implements ConfigType {
   private static final long serialVersionUID = 1L;
   private static final QName DEPLOYMENTLISTENERS$0 = new QName("http://www.bea.com/wsee/ns/config", "deployment-listeners");

   public ConfigTypeImpl(SchemaType var1) {
      super(var1);
   }

   public DeploymentListenersType getDeploymentListeners() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         DeploymentListenersType var2 = null;
         var2 = (DeploymentListenersType)this.get_store().find_element_user(DEPLOYMENTLISTENERS$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public boolean isSetDeploymentListeners() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(DEPLOYMENTLISTENERS$0) != 0;
      }
   }

   public void setDeploymentListeners(DeploymentListenersType var1) {
      this.generatedSetterHelperImpl(var1, DEPLOYMENTLISTENERS$0, 0, (short)1);
   }

   public DeploymentListenersType addNewDeploymentListeners() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         DeploymentListenersType var2 = null;
         var2 = (DeploymentListenersType)this.get_store().add_element_user(DEPLOYMENTLISTENERS$0);
         return var2;
      }
   }

   public void unsetDeploymentListeners() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(DEPLOYMENTLISTENERS$0, 0);
      }
   }
}
