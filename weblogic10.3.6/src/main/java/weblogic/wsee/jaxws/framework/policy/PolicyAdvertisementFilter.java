package weblogic.wsee.jaxws.framework.policy;

import java.util.List;
import java.util.Map;
import javax.wsdl.Definition;
import javax.xml.ws.WebServiceException;

public interface PolicyAdvertisementFilter {
   String WSP_DEFAULT = "1.2";
   String WSSP_DEFAULT = "1.1";
   String OWSM_DONOT_REMOVE_POLICIES_FROM_SOURCE_WSDL = "oracle.wsm.dont.remove.existing.policies";

   void setPolicySubjectBindings(List<PolicySubjectBinding> var1);

   void setEnvironmentMetadata(EnvironmentMetadata var1);

   /** @deprecated */
   void advertise(Definition var1, String var2, String var3, String var4) throws WebServiceException;

   void advertise(Definition var1, String var2, Map<String, String> var3) throws WebServiceException;

   public static enum MetadataType {
      WSDL,
      ORAWSDL;
   }
}
