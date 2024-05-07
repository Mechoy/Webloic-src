package weblogic.wsee.jaxws.security;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.JavaMethod;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.message.saaj.SAAJMessage;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.WebServiceResource;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.saml.SAMLAttributeStatementData;
import weblogic.wsee.security.saml.SAMLAttributeStatementDataImpl;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.xml.crypto.wss.BinarySecurityTokenHandler;
import weblogic.xml.crypto.wss.SignatureInfo;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;

public class AuthorizationTube extends AbstractFilterTubeImpl {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Logger LOGGER = Logger.getLogger(AuthorizationTube.class.getName());
   private static final QName AUTHENTICATION_FAILURE_11 = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client.Authentication", "env");
   private static final QName AUTHENTICATION_FAILURE_12 = new QName("http://www.w3.org/2003/05/soap-envelope", "Client.Authentication", "env");
   public static final String WSS_SUBJECT_PROPERTY = "weblogic.wsee.wss.subject";
   public static final String OWSM_SUBJECT_PROPERTY = "weblogic.wsee.owsm.subject";
   public static final String TRANSPORT_SUBJECT_PROPERTY = "weblogic.wsee.subject";
   public static final String CURRENT_SUBJECT = "weblogic.wsee.jaxws.security.subject";
   public static final String RESOURCE = "weblogic.wsee.jaxws.security.resource";
   public static final String CONTEXT_HANDLER = "weblogic.wsee.jaxws.security.contexthandler";
   private SEIModel seiModel;
   private SOAPVersion sv;
   private SOAPFactory sf;
   private QName failureQName;
   private Map<JavaMethod, WebServiceResource> resources = new HashMap();
   private WebServiceResource providerResource = null;
   private AuthorizationManager am;

   public AuthorizationTube(ServerTubeAssemblerContext var1, Tube var2) {
      super(var2);
      EnvironmentFactory var3 = JAXRPCEnvironmentFeature.getFactory(var1.getEndpoint());
      String var4 = var3.getApplication();
      String var5 = var3.getContextPath();
      String var6 = var3.getSecurityRealmName();
      if (var6 == null) {
         var6 = SecurityServiceManager.getDefaultRealmName();
      }

      String var7 = var1.getWsdlModel() != null ? var1.getWsdlModel().getName().getLocalPart() : null;
      this.seiModel = var1.getSEIModel();
      if (this.seiModel != null) {
         Iterator var8 = this.seiModel.getJavaMethods().iterator();

         while(var8.hasNext()) {
            JavaMethod var9 = (JavaMethod)var8.next();
            Method var10 = var9.getMethod();
            String var11 = var10.getName();
            Class[] var12 = var10.getParameterTypes();
            String[] var13 = new String[var12.length];

            for(int var14 = 0; var14 < var12.length; ++var14) {
               var13[var14] = var12[var14].getCanonicalName();
            }

            this.resources.put(var9, new WebServiceResource(var4, var5, var7, var11, var13));
         }
      } else {
         try {
            Method var16 = Provider.class.getMethod("invoke", Object.class);
            Class[] var17 = var16.getParameterTypes();
            String[] var18 = new String[var17.length];

            for(int var19 = 0; var19 < var17.length; ++var19) {
               var18[var19] = var17[var19].getCanonicalName();
            }

            this.providerResource = new WebServiceResource(var4, var5, var7, var16.getName(), var18);
         } catch (NoSuchMethodException var15) {
            throw new RuntimeException(var15);
         }
      }

      this.am = ServerSecurityHelper.getAuthManager(var6);
      this.sv = var1.getEndpoint().getBinding().getSOAPVersion();
      this.sf = this.sv.getSOAPFactory();
      this.failureQName = this.sv.equals(SOAPVersion.SOAP_11) ? AUTHENTICATION_FAILURE_11 : AUTHENTICATION_FAILURE_12;
   }

   protected AuthorizationTube(AuthorizationTube var1, TubeCloner var2) {
      super(var1, var2);
      this.seiModel = var1.seiModel;
      this.sv = var1.sv;
      this.sf = var1.sf;
      this.failureQName = var1.failureQName;
      this.resources = var1.resources;
      this.providerResource = var1.providerResource;
      this.am = var1.am;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new AuthorizationTube(this, var1);
   }

   public NextAction processRequest(Packet var1) {
      AuthenticatedSubject var2 = setSubject(var1);
      WebServiceResource var3 = null;
      String var4 = null;
      if (this.seiModel != null) {
         Message var5 = var1.getMessage();
         if (var5 != null) {
            JavaMethod var6 = var5.getMethod(this.seiModel);
            if (var6 != null) {
               var3 = (WebServiceResource)this.resources.get(var6);
               var4 = var6.getOperationName();
            }
         }
      } else {
         var3 = this.providerResource;
      }

      if (var3 != null) {
         AuthenticatedSubject var11 = ServerSecurityHelper.getCurrentSubject();
         WebServiceContextHandler var12 = new WebServiceContextHandler(var1);
         var1.invocationProperties.put("weblogic.wsee.jaxws.security.subject", var11);
         var1.invocationProperties.put("weblogic.wsee.jaxws.security.resource", var3);
         var1.invocationProperties.put("weblogic.wsee.jaxws.security.contexthandler", var12);
         if (!this.am.isAccessAllowed(var11, var3, var12)) {
            if (LOGGER.isLoggable(Level.FINER)) {
               LOGGER.finer("** Access denied for subject " + var11 + " to resource " + var3);
            }

            try {
               String var7 = "Access denied to operation " + var4;
               SOAPFault var8 = this.sf.createFault();
               var8.setFaultCode(this.failureQName);
               var8.setFaultString(var7);
               Detail var9 = var8.addDetail();
               SOAPFaultUtil.fillDetail(new AccessException(var7), var9, this.sv.equals(SOAPVersion.SOAP_12));
               throw new SOAPFaultException(var8);
            } catch (SOAPException var10) {
               throw new WebServiceException(var10);
            }
         }

         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("** Access granted for subject " + var11 + " to resource " + var3);
         }
      }

      return this.doInvoke((Tube)(var2 != null ? new RunAsWrapperTube(var2, this.next) : this.next), var1);
   }

   public NextAction processException(Throwable var1) {
      Packet var2 = Fiber.current().getPacket();
      if (var2 != null) {
         resetSubject(var2);
      }

      return super.processException(var1);
   }

   public NextAction processResponse(Packet var1) {
      resetSubject(var1);
      return super.processResponse(var1);
   }

   protected static AuthenticatedSubject setSubject(Packet var0) {
      AuthenticatedSubject var1 = null;
      AuthenticatedSubject var2 = (AuthenticatedSubject)var0.invocationProperties.get("weblogic.wsee.wss.subject");
      if (var2 == null) {
         Subject var3 = (Subject)var0.invocationProperties.get("weblogic.wsee.owsm.subject");
         if (var3 != null) {
            var2 = AuthenticatedSubject.getFromSubject(var3);
            var1 = var2;
         }
      }

      if (var2 != null) {
         AuthenticatedSubject var4 = switchSubject(var2);
         var0.invocationProperties.put("weblogic.wsee.subject", var4);
      }

      return var1;
   }

   protected static void resetSubject(Packet var0) {
      AuthenticatedSubject var1 = (AuthenticatedSubject)var0.invocationProperties.get("weblogic.wsee.subject");
      if (var1 != null) {
         switchSubject(var1);
         var0.invocationProperties.remove("weblogic.wsee.subject");
      }

   }

   private static AuthenticatedSubject switchSubject(AuthenticatedSubject var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
      SecurityServiceManager.popSubject(kernelId);
      SecurityServiceManager.pushSubject(kernelId, var0);
      return var1;
   }

   private static class WebServiceContextHandler implements ContextHandler {
      private Map<String, Object> map = new HashMap();

      public WebServiceContextHandler(Packet var1) {
         WSSecurityContext var2 = (WSSecurityContext)var1.invocationProperties.get("weblogic.xml.crypto.wss.WSSecurityContext");
         if (var2 != null) {
            WssPolicyContext var3 = (WssPolicyContext)var1.invocationProperties.get("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
            if (var3 != null && var3.getWssConfiguration().isSignatureACLEnabled()) {
               try {
                  BinarySecurityTokenHandler var4 = (BinarySecurityTokenHandler)var3.getWssConfiguration().getTokenHandler("x509", "weblogic.xml.crypto.wss.BinarySecurityTokenHandler");
                  List var5 = var2.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
                  var5.addAll(var2.getSecurityTokens("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1"));
                  Iterator var6 = var5.iterator();

                  while(var6.hasNext()) {
                     BinarySecurityToken var7 = (BinarySecurityToken)var6.next();
                     Node var8 = null;
                     List var9 = var2.getSignatures(var7);
                     Iterator var10 = var9.iterator();

                     while(var10.hasNext()) {
                        SignatureInfo var11 = (SignatureInfo)var10.next();
                        var8 = getSignatureNode(var11, var1);
                        if (var8 != null) {
                           break;
                        }
                     }

                     if (var8 != null) {
                        String var15 = "{" + var8.getNamespaceURI() + "}" + var8.getLocalName();
                        String var16 = "Integrity{" + var15 + "}";
                        Subject var12 = var4.getSubject(var7, (WSSecurityContext)var2);
                        this.map.put(var16, var12);
                     }
                  }
               } catch (WssConfigurationException var13) {
                  throw new WebServiceException(var13);
               } catch (WSSecurityException var14) {
                  throw new WebServiceException(var14);
               }
            }

            SAMLAttributeStatementData var17 = (SAMLAttributeStatementData)var2.getMessageContext().getProperty("weblogic.wsee.security.saml.attributies");
            if (null != var17) {
               this.map.put("weblogic.wsee.security.saml.attributies", var17);
               if (AuthorizationTube.LOGGER.isLoggable(Level.FINER)) {
                  AuthorizationTube.LOGGER.finer("Save SAMLAttributeStatementData object with size =" + var17.size());
               }

               if (!var17.isEmpty()) {
                  this.map.putAll(((SAMLAttributeStatementDataImpl)var17).getNameValuePair());
                  if (AuthorizationTube.LOGGER.isLoggable(Level.FINER)) {
                     AuthorizationTube.LOGGER.finer("Added SAML Attributes to the map for XACML" + var17.toString());
                  }
               }
            }
         }

      }

      private static Node getSignatureNode(SignatureInfo var0, Packet var1) {
         Message var2 = var1.getMessage();
         Node var3 = null;
         if (var2 instanceof SAAJMessage) {
            try {
               SOAPMessage var4 = var2.readAsSOAPMessage(var1);
               var3 = getFirstSigNode(var0, var4.getSOAPBody());
               if (var3 == null) {
                  return getFirstSigNode(var0, var4.getSOAPHeader());
               }
            } catch (SOAPException var5) {
               throw new WebServiceException(var5);
            }
         }

         return var3;
      }

      private static Node getFirstSigNode(SignatureInfo var0, Node var1) {
         if (var0.containsNode(var1)) {
            return var1;
         } else {
            NodeList var2 = var1.getChildNodes();

            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               Node var4 = var2.item(var3);
               Node var5 = getFirstSigNode(var0, var4);
               if (var5 != null) {
                  return var5;
               }
            }

            return null;
         }
      }

      public String[] getNames() {
         return (String[])this.map.keySet().toArray(new String[this.map.size()]);
      }

      public Object getValue(String var1) {
         return this.map.get(var1);
      }

      public ContextElement[] getValues(String[] var1) {
         ContextElement[] var2 = new ContextElement[this.map.size()];
         int var3 = 0;

         Map.Entry var5;
         for(Iterator var4 = this.map.entrySet().iterator(); var4.hasNext(); var2[var3++] = new ContextElement((String)var5.getKey(), var5.getValue())) {
            var5 = (Map.Entry)var4.next();
         }

         return var2;
      }

      public int size() {
         return this.map.size();
      }
   }

   private static class RunAsWrapperTube extends AbstractTubeImpl {
      private AuthenticatedSubject subject;
      private Tube inner;

      public RunAsWrapperTube(AuthenticatedSubject var1, Tube var2) {
         this.subject = var1;
         this.inner = var2;
      }

      public RunAsWrapperTube(RunAsWrapperTube var1, TubeCloner var2) {
         super(var1, var2);
         this.subject = var1.subject;
         this.inner = var2.copy(var1.inner);
      }

      public RunAsWrapperTube copy(TubeCloner var1) {
         return new RunAsWrapperTube(this, var1);
      }

      public void preDestroy() {
      }

      public NextAction processException(Throwable var1) {
         return this.doThrow(var1);
      }

      public NextAction processRequest(final Packet var1) {
         return (NextAction)Subject.doAs(this.subject.getSubject(), new PrivilegedAction<NextAction>() {
            public NextAction run() {
               NextAction var1x = RunAsWrapperTube.this.inner.processRequest(var1);
               Tube var2 = var1x.getNext();
               if (var2 != null) {
                  var1x.setNext(new RunAsWrapperTube(RunAsWrapperTube.this.subject, var2));
               }

               return var1x;
            }
         });
      }

      public NextAction processResponse(final Packet var1) {
         return (NextAction)Subject.doAs(this.subject.getSubject(), new PrivilegedAction<NextAction>() {
            public NextAction run() {
               return RunAsWrapperTube.this.inner.processResponse(var1);
            }
         });
      }
   }
}
