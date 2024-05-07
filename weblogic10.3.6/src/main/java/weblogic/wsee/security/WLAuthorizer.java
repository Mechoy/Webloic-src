package weblogic.wsee.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.WebServiceResource;
import weblogic.utils.Debug;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.xml.crypto.wss.BinarySecurityTokenHandler;
import weblogic.xml.crypto.wss.SignatureInfo;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;

public class WLAuthorizer implements Authorizer {
   private final ResourceMap resourceMap = new ResourceMap();
   private AuthorizationManager am;
   private AuthorizationContext authContext;
   private static final String VERBOSE_PROPERTY = "weblogic.wsee.security.verbose";
   private static final String DEBUG_PROPERTY = "weblogic.wsee.security.debug";
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.wsee.security.debug");
   private static final boolean VERBOSE = Boolean.getBoolean("weblogic.wsee.security.verbose");

   public WLAuthorizer(AuthorizationContext var1) {
      this.authContext = var1;
      this.am = ServerSecurityHelper.getAuthManager(this.authContext.getSecurityRealm());
   }

   public boolean isAccessAllowed(WlMessageContext var1) {
      AuthenticatedSubject var2 = ServerSecurityHelper.getCurrentSubject();
      Dispatcher var3 = var1.getDispatcher();
      WsMethod var4 = var3.getWsMethod();
      String var5 = var3.getWsdlPort().getName().getLocalPart();
      WebServiceContextHandler var6 = new WebServiceContextHandler(var1);
      WebServiceResource var7 = this.getResource(var1, var4, var5, var6);
      if (VERBOSE) {
         Debug.say("** Authorizer got Operation " + var4.getMethodName() + " and user " + var2);
         Debug.say("** Authorizer using Resource " + var7);
      }

      if (DEBUG) {
         Debug.assertion(var7 != null, "Failed to retrieve Resource for Operation " + var4);
         Debug.assertion(var2 != null, "Failed to retrieve subject for invoke");
      }

      boolean var8 = this.am.isAccessAllowed(var2, var7, var6);
      if (VERBOSE) {
         if (var8) {
            Debug.say("** Access granted for subject " + var2 + " to Resource " + var7);
         } else {
            Debug.say("** Access denied for subject " + var2 + " to Resource " + var7);
         }
      }

      return var8;
   }

   public WebServiceResource getResource(WlMessageContext var1, WsMethod var2, String var3, WebServiceContextHandler var4) {
      WebServiceResource var5 = this.lookupResource(var2);
      if (var5 == null) {
         if (VERBOSE) {
            Debug.say("** Missed on cache for Operation " + var2.getMethodName());
         }

         var5 = createResource(var1, this.authContext, var3, var2, var4);
         this.cacheResource(var2, var5);
      } else if (VERBOSE) {
         Debug.say("** Cache hit for Operation " + var2.getMethodName());
      }

      return var5;
   }

   private WebServiceResource lookupResource(WsMethod var1) {
      return this.resourceMap.get(var1);
   }

   private WebServiceResource cacheResource(WsMethod var1, WebServiceResource var2) {
      this.resourceMap.put(var1, var2);
      return var2;
   }

   private static WebServiceResource createResource(WlMessageContext var0, AuthorizationContext var1, String var2, WsMethod var3, WebServiceContextHandler var4) {
      if (VERBOSE) {
         Debug.say("** Creating resource for " + var3.getMethodName());
      }

      if (DEBUG) {
         Debug.assertion(var3 != null, "Operation provided WLAuthorizer was null");
      }

      String var5 = var3.getMethodName();
      Iterator var8 = var3.getParameters();
      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();

      String var12;
      while(var8.hasNext()) {
         WsParameterType var11 = (WsParameterType)var8.next();
         var10.add(var11.getJavaType().getCanonicalName());
         var12 = var11.getName();
         var9.add(var12);
         var4.addParameter(var12, var0.getDispatcher());
      }

      int var13 = var10.size();
      String[] var6 = new String[var13];
      var10.toArray(var6);
      String[] var7 = new String[var13];
      var9.toArray(var7);
      var12 = null;
      if (VERBOSE) {
         Debug.say("** Args to WebServiceResource");
         Debug.say("**   methodName = " + var5);
         Debug.say("**   methodParams = " + var6);
         Debug.say("**   paramNames = " + var7);
      }

      WebServiceResource var14 = new WebServiceResource(var1.getApplicationName(), var1.getContextPath(), var2, var5, var6);
      if (DEBUG) {
         Debug.assertion(var14 != null, "Failed to create WebServiceResource for " + var5);
      }

      if (VERBOSE) {
         Debug.say("** Created resource " + var14);
      }

      return var14;
   }

   String getSecurityRealm() {
      return this.authContext.getSecurityRealm();
   }

   private static class WebServiceContextHandler implements ContextHandler {
      private ArrayList<String> names = new ArrayList();
      private ArrayList<Object> values = new ArrayList();

      WebServiceContextHandler(WlMessageContext var1) {
         WSSecurityContext var2 = WSSecurityContext.getSecurityContext(var1);
         if (var2 != null) {
            WssPolicyContext var3 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
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
                        Subject var12 = var4.getSubject(var7, (MessageContext)var1);
                        if (WLAuthorizer.VERBOSE) {
                           Debug.say("** Add Signature ACL name: " + var16);
                           Debug.say("** Add Signature ACL value: " + var12);
                        }

                        this.names.add(var16);
                        this.values.add(var12);
                     }
                  }
               } catch (WssConfigurationException var13) {
               } catch (WSSecurityException var14) {
               }
            }
         }

      }

      public int size() {
         return this.values.size();
      }

      public String[] getNames() {
         return (String[])this.names.toArray(new String[0]);
      }

      public final Object getValue(String var1) {
         for(int var2 = 0; var2 < this.names.size(); ++var2) {
            String var3 = (String)this.names.get(var2);
            if (var3.equals(var1)) {
               return this.values.get(var2);
            }
         }

         return null;
      }

      public ContextElement[] getValues(String[] var1) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            Object var5 = this.getValue(var4);
            if (var5 != null) {
               var2.add(new ContextElement(var4, var5));
            }
         }

         return (ContextElement[])var2.toArray(new ContextElement[0]);
      }

      public void addParameter(String var1, Dispatcher var2) {
         Map var3 = var2.getInParams();
         Object var4 = var3.get(var1);
         if (var4 != null) {
            if (WLAuthorizer.VERBOSE) {
               Debug.say("** Args to ContextHandler");
               Debug.say("**   paramName = " + var1);
               Debug.say("**   paramValue = " + var4);
            }

            this.names.add(var1);
            this.values.add(var4);
         } else if (WLAuthorizer.VERBOSE) {
            Debug.say("** Can not find param value for param name: " + var1);
         }

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

      private static Node getSignatureNode(SignatureInfo var0, WlMessageContext var1) {
         Node var2 = null;
         if (var1 instanceof SOAPMessageContext) {
            SOAPMessageContext var3 = (SOAPMessageContext)var1;
            SOAPMessage var4 = var3.getMessage();

            try {
               var2 = getFirstSigNode(var0, var4.getSOAPBody());
               if (var2 == null) {
                  return getFirstSigNode(var0, var4.getSOAPHeader());
               }
            } catch (SOAPException var6) {
            }
         }

         return var2;
      }
   }

   protected static class ResourceMap {
      private Map resourceMap = null;

      protected ResourceMap() {
         this.resourceMap = Collections.synchronizedMap(new HashMap());
      }

      protected WebServiceResource get(WsMethod var1) {
         if (WLAuthorizer.DEBUG) {
            Debug.assertion(var1 != null, "WebServiceResource lookup got a null operation");
         }

         return (WebServiceResource)this.resourceMap.get(var1);
      }

      protected WebServiceResource put(WsMethod var1, WebServiceResource var2) {
         if (WLAuthorizer.DEBUG) {
            Debug.assertion(var2 != null, "WebServiceResource cache got a null resource");
            Debug.assertion(var1 != null, "WebServiceResource cache got a null operation");
         }

         this.resourceMap.put(var1, var2);
         return var2;
      }
   }
}
