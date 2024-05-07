package weblogic.wsee.wstx.wsc.v11.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _Register_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "Register");
   private static final QName _RegisterResponse_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "RegisterResponse");
   private static final QName _CreateCoordinationContext_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "CreateCoordinationContext");
   private static final QName _CreateCoordinationContextResponse_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wscoor/2006/06", "CreateCoordinationContextResponse");

   public CoordinationContextType createCoordinationContextType() {
      return new CoordinationContextType();
   }

   public CreateCoordinationContextType createCreateCoordinationContextType() {
      return new CreateCoordinationContextType();
   }

   public Expires createExpires() {
      return new Expires();
   }

   public RegisterType createRegisterType() {
      return new RegisterType();
   }

   public RegisterResponseType createRegisterResponseType() {
      return new RegisterResponseType();
   }

   public CreateCoordinationContextResponseType createCreateCoordinationContextResponseType() {
      return new CreateCoordinationContextResponseType();
   }

   public CoordinationContext createCoordinationContext() {
      return new CoordinationContext();
   }

   public CoordinationContextType.Identifier createCoordinationContextTypeIdentifier() {
      return new CoordinationContextType.Identifier();
   }

   public CreateCoordinationContextType.CurrentContext createCreateCoordinationContextTypeCurrentContext() {
      return new CreateCoordinationContextType.CurrentContext();
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
      name = "Register"
   )
   public JAXBElement<RegisterType> createRegister(RegisterType var1) {
      return new JAXBElement(_Register_QNAME, RegisterType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
      name = "RegisterResponse"
   )
   public JAXBElement<RegisterResponseType> createRegisterResponse(RegisterResponseType var1) {
      return new JAXBElement(_RegisterResponse_QNAME, RegisterResponseType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
      name = "CreateCoordinationContext"
   )
   public JAXBElement<CreateCoordinationContextType> createCreateCoordinationContext(CreateCoordinationContextType var1) {
      return new JAXBElement(_CreateCoordinationContext_QNAME, CreateCoordinationContextType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wscoor/2006/06",
      name = "CreateCoordinationContextResponse"
   )
   public JAXBElement<CreateCoordinationContextResponseType> createCreateCoordinationContextResponse(CreateCoordinationContextResponseType var1) {
      return new JAXBElement(_CreateCoordinationContextResponse_QNAME, CreateCoordinationContextResponseType.class, (Class)null, var1);
   }
}
