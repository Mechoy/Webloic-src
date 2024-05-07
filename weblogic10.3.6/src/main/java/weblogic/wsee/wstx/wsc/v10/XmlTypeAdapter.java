package weblogic.wsee.wstx.wsc.v10;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.wstx.wsc.common.types.BaseExpires;
import weblogic.wsee.wstx.wsc.common.types.BaseIdentifier;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextTypeIF;
import weblogic.wsee.wstx.wsc.v10.types.CoordinationContext;
import weblogic.wsee.wstx.wsc.v10.types.CoordinationContextType;
import weblogic.wsee.wstx.wsc.v10.types.Expires;
import weblogic.wsee.wstx.wsc.v10.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.types.RegisterType;

public class XmlTypeAdapter {
   public static BaseExpires<Expires> adapt(Expires var0) {
      return var0 == null ? null : new ExpiresImpl(var0);
   }

   public static BaseIdentifier<CoordinationContextType.Identifier> adapt(CoordinationContextType.Identifier var0) {
      return var0 == null ? null : new IdentifierImpl(var0);
   }

   public static CoordinationContextIF<MemberSubmissionEndpointReference, Expires, CoordinationContextType.Identifier, CoordinationContextType> adapt(CoordinationContext var0) {
      return var0 == null ? null : new CoordinationContextImpl(var0);
   }

   public static BaseRegisterType<MemberSubmissionEndpointReference, RegisterType> adapt(RegisterType var0) {
      return var0 == null ? null : new RegisterTypeImpl(var0);
   }

   public static BaseRegisterType<MemberSubmissionEndpointReference, RegisterType> newRegisterType() {
      return new RegisterTypeImpl(new RegisterType());
   }

   public static BaseRegisterResponseType<MemberSubmissionEndpointReference, RegisterResponseType> adapt(RegisterResponseType var0) {
      return var0 == null ? null : new RegisterResponseTypeImpl(var0);
   }

   public static BaseRegisterResponseType newRegisterResponseType() {
      return new RegisterResponseTypeImpl(new RegisterResponseType());
   }

   static class RegisterResponseTypeImpl extends BaseRegisterResponseType<MemberSubmissionEndpointReference, RegisterResponseType> {
      RegisterResponseTypeImpl(RegisterResponseType var1) {
         super(var1);
      }

      public MemberSubmissionEndpointReference getCoordinatorProtocolService() {
         return ((RegisterResponseType)this.delegate).getCoordinatorProtocolService();
      }

      public void setCoordinatorProtocolService(MemberSubmissionEndpointReference var1) {
         ((RegisterResponseType)this.delegate).setCoordinatorProtocolService(var1);
      }

      public List<Object> getAny() {
         return ((RegisterResponseType)this.delegate).getAny();
      }

      public Map<QName, String> getOtherAttributes() {
         return ((RegisterResponseType)this.delegate).getOtherAttributes();
      }

      public RegisterResponseType getDelegate() {
         return (RegisterResponseType)this.delegate;
      }
   }

   public static class RegisterTypeImpl extends BaseRegisterType<MemberSubmissionEndpointReference, RegisterType> {
      RegisterTypeImpl(RegisterType var1) {
         super(var1);
      }

      public String getProtocolIdentifier() {
         return ((RegisterType)this.delegate).getProtocolIdentifier();
      }

      public void setProtocolIdentifier(String var1) {
         ((RegisterType)this.delegate).setProtocolIdentifier(var1);
      }

      public MemberSubmissionEndpointReference getParticipantProtocolService() {
         return ((RegisterType)this.delegate).getParticipantProtocolService();
      }

      public void setParticipantProtocolService(MemberSubmissionEndpointReference var1) {
         ((RegisterType)this.delegate).setParticipantProtocolService(var1);
      }

      public List<Object> getAny() {
         return ((RegisterType)this.delegate).getAny();
      }

      public Map<QName, String> getOtherAttributes() {
         return ((RegisterType)this.delegate).getOtherAttributes();
      }

      public boolean isDurable() {
         return "http://schemas.xmlsoap.org/ws/2004/10/wsat/Durable2PC".equals(((RegisterType)this.delegate).getProtocolIdentifier());
      }

      public boolean isVolatile() {
         return "http://schemas.xmlsoap.org/ws/2004/10/wsat/Volatile2PC".equals(((RegisterType)this.delegate).getProtocolIdentifier());
      }
   }

   public static class CoordinationContextImpl extends CoordinationContextTypeImpl implements CoordinationContextIF<MemberSubmissionEndpointReference, Expires, CoordinationContextType.Identifier, CoordinationContextType> {
      static final JAXBRIContext jaxbContext = getCoordinationContextJaxbContext();

      private static JAXBRIContext getCoordinationContextJaxbContext() {
         try {
            return (JAXBRIContext)JAXBRIContext.newInstance(new Class[]{CoordinationContext.class});
         } catch (JAXBException var1) {
            throw new WebServiceException("Error creating JAXBContext for CoordinationContext. ", var1);
         }
      }

      public CoordinationContextImpl(CoordinationContext var1) {
         super(var1);
      }

      public List<Object> getAny() {
         return this.getDelegate().getAny();
      }

      public JAXBRIContext getJAXBRIContext() {
         return jaxbContext;
      }

      public CoordinationContext getDelegate() {
         return (CoordinationContext)super.getDelegate();
      }
   }

   public static class CoordinationContextTypeImpl implements CoordinationContextTypeIF<MemberSubmissionEndpointReference, Expires, CoordinationContextType.Identifier, CoordinationContextType> {
      private CoordinationContextType delegate;

      public CoordinationContextTypeImpl(CoordinationContextType var1) {
         this.delegate = var1;
      }

      public BaseIdentifier<CoordinationContextType.Identifier> getIdentifier() {
         return XmlTypeAdapter.adapt(this.delegate.getIdentifier());
      }

      public void setIdentifier(BaseIdentifier<CoordinationContextType.Identifier> var1) {
         this.delegate.setIdentifier((CoordinationContextType.Identifier)var1.getDelegate());
      }

      public BaseExpires<Expires> getExpires() {
         return XmlTypeAdapter.adapt(this.delegate.getExpires());
      }

      public void setExpires(BaseExpires<Expires> var1) {
         this.delegate.setExpires((Expires)var1.getDelegate());
      }

      public String getCoordinationType() {
         return this.delegate.getCoordinationType();
      }

      public void setCoordinationType(String var1) {
         this.delegate.setCoordinationType(var1);
      }

      public MemberSubmissionEndpointReference getRegistrationService() {
         return this.delegate.getRegistrationService();
      }

      public void setRegistrationService(MemberSubmissionEndpointReference var1) {
         this.delegate.setRegistrationService(var1);
      }

      public Map<QName, String> getOtherAttributes() {
         return this.delegate.getOtherAttributes();
      }

      public CoordinationContextType getDelegate() {
         return this.delegate;
      }
   }

   static class IdentifierImpl extends BaseIdentifier<CoordinationContextType.Identifier> {
      protected IdentifierImpl(CoordinationContextType.Identifier var1) {
         super(var1);
      }

      public String getValue() {
         return ((CoordinationContextType.Identifier)this.delegate).getValue();
      }

      public void setValue(String var1) {
         ((CoordinationContextType.Identifier)this.delegate).setValue(var1);
      }

      public Map<QName, String> getOtherAttributes() {
         return ((CoordinationContextType.Identifier)this.delegate).getOtherAttributes();
      }

      public QName getQName() {
         return new QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "Identifier");
      }
   }

   static class ExpiresImpl extends BaseExpires<Expires> {
      protected ExpiresImpl(Expires var1) {
         super(var1);
      }

      public long getValue() {
         return ((Expires)this.delegate).getValue();
      }

      public void setValue(long var1) {
         ((Expires)this.delegate).setValue(var1);
      }

      public Map getOtherAttributes() {
         return ((Expires)this.delegate).getOtherAttributes();
      }
   }
}
