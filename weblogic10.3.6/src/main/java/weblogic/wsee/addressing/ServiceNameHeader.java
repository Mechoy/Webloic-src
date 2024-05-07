package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ServiceNameHeader extends MsgHeader {
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private QName name;
   private String serviceName;
   private String endpointName;

   public ServiceNameHeader(String var1, String var2) {
      this.name = WSAddressingConstants.WSA_HEADER_METADATA_SERVICE_NAME_10;
      this.serviceName = var1;
      this.endpointName = var2;
   }

   public ServiceNameHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_METADATA_SERVICE_NAME_10;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public String getEndpointName() {
      return this.endpointName;
   }

   public void setEndpointName(String var1) {
      this.endpointName = var1;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.serviceName = DOMUtils.getTextData(var1);
         this.endpointName = DOMUtils.getAttributeValueAsString(var1, WSAddressingConstants.WSA_HEADER_METADATA_SERVICE_ENDPOINT_ATTR);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get Interface name", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.serviceName);
      if (!StringUtil.isEmpty(this.endpointName)) {
         weblogic.wsee.policy.framework.DOMUtils.addPrefixedAttribute(var1, WSAddressingConstants.WSA_HEADER_METADATA_SERVICE_ENDPOINT_ATTR, "wsaw", this.endpointName);
      }

   }
}
