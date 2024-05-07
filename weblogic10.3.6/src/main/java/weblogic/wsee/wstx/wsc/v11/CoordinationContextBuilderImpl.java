package weblogic.wsee.wstx.wsc.v11;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.ws.api.message.Header;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.w3c.dom.Element;
import weblogic.wsee.wstx.wsc.common.CoordinationContextBuilder;
import weblogic.wsee.wstx.wsc.common.WSCUtil;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;
import weblogic.wsee.wstx.wsc.v11.types.CoordinationContext;
import weblogic.wsee.wstx.wsc.v11.types.CoordinationContextType;
import weblogic.wsee.wstx.wsc.v11.types.Expires;

public class CoordinationContextBuilderImpl extends CoordinationContextBuilder {
   public CoordinationContextIF build() {
      CoordinationContext var1 = this.buildContext();
      return XmlTypeAdapter.adapt(var1);
   }

   protected CoordinationContextIF _fromHeader(Header var1) {
      try {
         Unmarshaller var2 = XmlTypeAdapter.CoordinationContextImpl.jaxbContext.createUnmarshaller();
         CoordinationContext var3 = (CoordinationContext)var1.readAsJAXB(var2);
         return XmlTypeAdapter.adapt(var3);
      } catch (JAXBException var4) {
         throw new WebServiceException(var4);
      }
   }

   public JAXBRIContext getJAXBRIContext() {
      return XmlTypeAdapter.CoordinationContextImpl.jaxbContext;
   }

   private CoordinationContext buildContext() {
      CoordinationContext var1 = new CoordinationContext();
      if (this.mustUnderstand) {
         if (this.soapVersion == null) {
            throw new WebServiceException("SOAP version is not specified!");
         }

         var1.getOtherAttributes().put(new QName(this.soapVersion.nsUri, "mustUnderstand"), "1");
      }

      var1.setCoordinationType(this.coordinationType);
      CoordinationContextType.Identifier var2 = new CoordinationContextType.Identifier();
      var2.setValue(this.identifier);
      var1.setIdentifier(var2);
      Expires var3 = new Expires();
      var3.setValue(this.expires);
      var1.setExpires(var3);
      var1.setRegistrationService(this.getEPR());
      return var1;
   }

   private W3CEndpointReference getEPR() {
      Element var1 = WSCUtil.referenceElementTxId(this.txId);
      Element var2 = WSCUtil.referenceElementRoutingInfo();
      return (new W3CEndpointReferenceBuilder()).address(this.address).referenceParameter(var1).referenceParameter(var2).build();
   }
}
