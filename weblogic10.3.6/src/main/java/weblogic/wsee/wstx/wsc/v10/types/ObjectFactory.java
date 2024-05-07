package weblogic.wsee.wstx.wsc.v10.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _Register_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "Register");
   private static final QName _RegisterResponse_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegisterResponse");
   private static final QName _CreateCoordinationContext_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CreateCoordinationContext");
   private static final QName _CreateCoordinationContextResponse_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CreateCoordinationContextResponse");

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
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
      name = "Register"
   )
   public JAXBElement<RegisterType> createRegister(RegisterType var1) {
      return new JAXBElement(_Register_QNAME, RegisterType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
      name = "RegisterResponse"
   )
   public JAXBElement<RegisterResponseType> createRegisterResponse(RegisterResponseType var1) {
      return new JAXBElement(_RegisterResponse_QNAME, RegisterResponseType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
      name = "CreateCoordinationContext"
   )
   public JAXBElement<CreateCoordinationContextType> createCreateCoordinationContext(CreateCoordinationContextType var1) {
      return new JAXBElement(_CreateCoordinationContext_QNAME, CreateCoordinationContextType.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wscoor",
      name = "CreateCoordinationContextResponse"
   )
   public JAXBElement<CreateCoordinationContextResponseType> createCreateCoordinationContextResponse(CreateCoordinationContextResponseType var1) {
      return new JAXBElement(_CreateCoordinationContextResponse_QNAME, CreateCoordinationContextResponseType.class, (Class)null, var1);
   }
}
