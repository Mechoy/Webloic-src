package weblogic.wsee.deploy;

import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class ClientDeployInfo {
   private JavaWsdlMappingBean mappingdd;
   private WsdlDefinitions wsdlDef;
   private ServiceRefBean serviceRef;
   private ServiceReferenceDescriptionBean wlServiceRef;
   private QName serviceName;
   private RuntimeBindings runtimeBindings;

   public JavaWsdlMappingBean getMappingdd() {
      return this.mappingdd;
   }

   public void setMappingdd(JavaWsdlMappingBean var1) {
      this.mappingdd = var1;
   }

   public WsdlDefinitions getWsdlDef() {
      return this.wsdlDef;
   }

   public void setWsdlDef(WsdlDefinitions var1) {
      this.wsdlDef = var1;
   }

   public ServiceRefBean getServiceRef() {
      return this.serviceRef;
   }

   public void setServiceRef(ServiceRefBean var1) {
      this.serviceRef = var1;
   }

   public ServiceReferenceDescriptionBean getWlServiceRef() {
      return this.wlServiceRef;
   }

   public void setWlServiceRef(ServiceReferenceDescriptionBean var1) {
      this.wlServiceRef = var1;
   }

   public QName getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(QName var1) {
      this.serviceName = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("serviceName", this.serviceName);
      var1.writeField("serviceRef", this.serviceRef);
      var1.writeField("wsdlDef", this.wsdlDef);
      var1.writeField("mapping", this.mappingdd);
      var1.end();
   }

   public void setRuntimeBindings(RuntimeBindings var1) {
      this.runtimeBindings = var1;
   }

   public RuntimeBindings getRuntimeBindings() {
      return this.runtimeBindings;
   }
}
