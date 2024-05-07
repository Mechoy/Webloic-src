package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIList;

public class ServiceInfos extends UDDIList {
   public void add(ServiceInfo var1) throws UDDIException {
      super.add(var1);
   }

   public ServiceInfo getFirst() {
      return (ServiceInfo)super.getVFirst();
   }

   public ServiceInfo getNext() {
      return (ServiceInfo)super.getVNext();
   }

   public String toXML() {
      return super.toXML("serviceInfos");
   }
}
