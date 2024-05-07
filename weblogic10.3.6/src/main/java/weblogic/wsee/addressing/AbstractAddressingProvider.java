package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public abstract class AbstractAddressingProvider implements AddressingProvider {
   protected abstract boolean isWSA09();

   public boolean isAnonymousReferenceURI(String var1) {
      return this.getAnonymousNamespaceURI().equals(var1);
   }

   public ActionHeader createActionHeader() {
      return this.isWSA09() ? new ActionHeader(WSAddressingConstants.WSA_HEADER_ACTION) : new ActionHeader(WSAddressingConstants.WSA_HEADER_ACTION_10);
   }

   public ActionHeader createActionHeader(String var1) {
      return this.isWSA09() ? new ActionHeader(var1, WSAddressingConstants.WSA_HEADER_ACTION) : new ActionHeader(var1, WSAddressingConstants.WSA_HEADER_ACTION_10);
   }

   public ActionHeader createFaultActionHeader() {
      return this.isWSA09() ? new ActionHeader(this.getFaultActionUri(), WSAddressingConstants.WSA_HEADER_ACTION) : new ActionHeader(this.getFaultActionUri(), WSAddressingConstants.WSA_HEADER_ACTION_10);
   }

   public String getFaultActionUri() {
      return this.getNamespaceURI() + "/fault";
   }

   public EndpointReference createAnonymousEndpointReference() {
      EndpointReference var1 = new EndpointReference(this.getAnonymousNamespaceURI());
      var1.setNamespaceURI(this.getNamespaceURI());
      return var1;
   }

   public EndpointReference createEndpointReference() {
      EndpointReference var1 = new EndpointReference();
      var1.setNamespaceURI(this.getNamespaceURI());
      return var1;
   }

   public EndpointReference createEndpointReference(String var1, String var2) {
      EndpointReference var3 = new EndpointReference(var1, var2);
      var3.setNamespaceURI(this.getNamespaceURI());
      return var3;
   }

   public EndpointReference createEndpointReference(String var1) {
      EndpointReference var2 = new EndpointReference(var1);
      var2.setNamespaceURI(this.getNamespaceURI());
      return var2;
   }

   public FaultToHeader createFaultToHeader() {
      return this.isWSA09() ? new FaultToHeader(WSAddressingConstants.WSA_HEADER_FAULT_TO) : new FaultToHeader(WSAddressingConstants.WSA_HEADER_FAULT_TO_10);
   }

   public FaultToHeader createFaultToHeader(EndpointReference var1) {
      if (var1 != null) {
         var1.setNamespaceURI(this.getNamespaceURI());
      }

      return this.isWSA09() ? new FaultToHeader(var1, WSAddressingConstants.WSA_HEADER_FAULT_TO) : new FaultToHeader(var1, WSAddressingConstants.WSA_HEADER_FAULT_TO_10);
   }

   public FromHeader createFromHeader() {
      return this.isWSA09() ? new FromHeader(WSAddressingConstants.WSA_HEADER_SOURCE) : new FromHeader(WSAddressingConstants.WSA_HEADER_SOURCE_10);
   }

   public FromHeader createFromHeader(EndpointReference var1) {
      if (var1 != null) {
         var1.setNamespaceURI(this.getNamespaceURI());
      }

      return this.isWSA09() ? new FromHeader(var1, WSAddressingConstants.WSA_HEADER_SOURCE) : new FromHeader(var1, WSAddressingConstants.WSA_HEADER_SOURCE_10);
   }

   public MessageIdHeader createMessageIdHeader() {
      return this.isWSA09() ? new MessageIdHeader(WSAddressingConstants.WSA_HEADER_MESSAGE_ID) : new MessageIdHeader(WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10);
   }

   public MessageIdHeader createMessageIdHeader(String var1) {
      return this.isWSA09() ? new MessageIdHeader(var1, WSAddressingConstants.WSA_HEADER_MESSAGE_ID) : new MessageIdHeader(var1, WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10);
   }

   public RelatesToHeader createRelatesToHeader() {
      return this.isWSA09() ? new RelatesToHeader(WSAddressingConstants.WSA_HEADER_RELATES_TO) : new RelatesToHeader(WSAddressingConstants.WSA_HEADER_RELATES_TO_10);
   }

   public RelatesToHeader createRelatesToHeader(String var1, QName var2) {
      return this.isWSA09() ? new RelatesToHeader(var1, var2, WSAddressingConstants.WSA_HEADER_RELATES_TO) : new RelatesToHeader(var1, var2, WSAddressingConstants.WSA_HEADER_RELATES_TO_10);
   }

   public ReplyToHeader createReplyToHeader() {
      return this.isWSA09() ? new ReplyToHeader(WSAddressingConstants.WSA_HEADER_REPLY_TO) : new ReplyToHeader(WSAddressingConstants.WSA_HEADER_REPLY_TO_10);
   }

   public ReplyToHeader createReplyToHeader(EndpointReference var1) {
      if (var1 != null) {
         var1.setNamespaceURI(this.getNamespaceURI());
      }

      return this.isWSA09() ? new ReplyToHeader(var1, WSAddressingConstants.WSA_HEADER_REPLY_TO) : new ReplyToHeader(var1, WSAddressingConstants.WSA_HEADER_REPLY_TO_10);
   }

   public ToHeader createToHeader() {
      return this.isWSA09() ? new ToHeader(WSAddressingConstants.WSA_HEADER_TO) : new ToHeader(WSAddressingConstants.WSA_HEADER_TO_10);
   }

   public ToHeader createToHeader(String var1) {
      return this.isWSA09() ? new ToHeader(var1, WSAddressingConstants.WSA_HEADER_TO) : new ToHeader(var1, WSAddressingConstants.WSA_HEADER_TO_10);
   }

   public FaultDetailHeader createFaultDetailHeader() {
      return this.isWSA09() ? new FaultDetailHeader(WSAddressingConstants.WSA_HEADER_FAULT_DETAIL) : new FaultDetailHeader(WSAddressingConstants.WSA_HEADER_FAULT_DETAIL_10);
   }

   public ProblemHeaderQNameHeader createProblemHeaderQNameHeader() {
      return this.isWSA09() ? new ProblemHeaderQNameHeader(WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME) : new ProblemHeaderQNameHeader(WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10);
   }

   public ProblemHeaderQNameHeader createProblemHeaderQNameHeader(String var1) {
      return this.isWSA09() ? new ProblemHeaderQNameHeader(WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME, var1) : new ProblemHeaderQNameHeader(WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10, var1);
   }

   public QName getMessageAddressingHeaderRequiredFaultQName() {
      return this.isWSA09() ? WSAddressingConstants.WSA_MESSAGE_HEADER_REQUIRED_FC : WSAddressingConstants.WSA_MESSAGE_HEADER_REQUIRED_FC_10;
   }

   public QName getInvalidAddressingHeaderFaultQName() {
      return this.isWSA09() ? WSAddressingConstants.WSA_INVALID_MESSAGE_HEADER_FC : WSAddressingConstants.WSA_INVALID_ADDRESSING_HEADER_FC;
   }

   public QName getActionNotSupportFaultQName() {
      return this.isWSA09() ? WSAddressingConstants.WSA_ACTION_NOT_SUPPORTED_FC : WSAddressingConstants.WSA_ACTION_NOT_SUPPORTED_FC_10;
   }

   public String getMessageAddressingHeaderRequiredReason() {
      return this.isWSA09() ? "A required message information header, To, MessageID, or Action, is not present." : "A required header representing a Message Addressing Property is not present.";
   }
}
