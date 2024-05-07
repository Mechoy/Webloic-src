package weblogic.wsee.jaxws;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.PortInfo;

public class PortInfoImpl implements PortInfo {
   private final String bindingId;
   private final QName portName;
   private final QName serviceName;

   public PortInfoImpl(String var1, QName var2, QName var3) {
      this.bindingId = var1;
      this.portName = var2;
      this.serviceName = var3;
   }

   public String getBindingID() {
      return this.bindingId;
   }

   public QName getPortName() {
      return this.portName;
   }

   public QName getServiceName() {
      return this.serviceName;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PortInfo)) {
         return false;
      } else {
         PortInfo var2 = (PortInfo)var1;
         boolean var3 = this.bindingId.equals(var2.getBindingID());
         var3 = var3 && this.portName.equals(var2.getPortName());
         var3 = var3 && this.serviceName.equals(var2.getServiceName());
         return var3;
      }
   }

   public int hashCode() {
      return this.bindingId.hashCode();
   }
}
