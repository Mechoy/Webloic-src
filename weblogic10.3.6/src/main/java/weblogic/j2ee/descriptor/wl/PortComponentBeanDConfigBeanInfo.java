package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class PortComponentBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(PortComponentBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("PortComponentName", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getPortComponentName", "setPortComponentName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ServiceEndpointAddress", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getServiceEndpointAddress", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LoginConfig", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getLoginConfig", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TransportGuarantee", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getTransportGuarantee", "setTransportGuarantee");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DeploymentListenerList", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getDeploymentListenerList", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Wsdl", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getWsdl", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TransactionTimeout", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getTransactionTimeout", "setTransactionTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CallbackProtocol", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getCallbackProtocol", "setCallbackProtocol");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("StreamAttachments", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getStreamAttachments", "setStreamAttachments");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ValidateRequest", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "isValidateRequest", "setValidateRequest");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("HttpFlushResponse", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "isHttpFlushResponse", "setHttpFlushResponse");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("HttpResponseBuffersize", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getHttpResponseBuffersize", "setHttpResponseBuffersize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReliabilityConfig", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getReliabilityConfig", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistenceConfig", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getPersistenceConfig", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("BufferingConfig", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getBufferingConfig", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("WSATConfig", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getWSATConfig", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Operations", Class.forName("weblogic.j2ee.descriptor.wl.PortComponentBeanDConfig"), "getOperations", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for PortComponentBeanDConfigBeanInfo");
         }
      }
   }
}
