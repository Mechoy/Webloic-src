package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public interface AddressingProvider {
   String getNamespaceURI();

   WSAVersion getWSAVersion();

   String getAnonymousNamespaceURI();

   boolean isAnonymousReferenceURI(String var1);

   String getFaultActionUri();

   EndpointReference createAnonymousEndpointReference();

   EndpointReference createEndpointReference();

   EndpointReference createEndpointReference(String var1);

   EndpointReference createEndpointReference(String var1, String var2);

   ActionHeader createActionHeader(String var1);

   ActionHeader createActionHeader();

   ActionHeader createFaultActionHeader();

   FaultToHeader createFaultToHeader();

   FaultToHeader createFaultToHeader(EndpointReference var1);

   FromHeader createFromHeader();

   FromHeader createFromHeader(EndpointReference var1);

   MessageIdHeader createMessageIdHeader();

   MessageIdHeader createMessageIdHeader(String var1);

   RelatesToHeader createRelatesToHeader();

   RelatesToHeader createRelatesToHeader(String var1, QName var2);

   ReplyToHeader createReplyToHeader();

   ReplyToHeader createReplyToHeader(EndpointReference var1);

   ToHeader createToHeader();

   ToHeader createToHeader(String var1);

   ProblemHeaderQNameHeader createProblemHeaderQNameHeader();

   ProblemHeaderQNameHeader createProblemHeaderQNameHeader(String var1);

   FaultDetailHeader createFaultDetailHeader();

   QName getMessageAddressingHeaderRequiredFaultQName();

   QName getInvalidAddressingHeaderFaultQName();

   QName getActionNotSupportFaultQName();

   String getMessageAddressingHeaderRequiredReason();
}
