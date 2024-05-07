package weblogic.wsee.security.wssc.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.ws.BindingProvider;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.WsrmSAFManager;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.WsrmSequenceContext;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownSourceSequenceException;
import weblogic.wsee.security.wssc.base.sct.ClientSCCredentialProviderBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wst.framework.WSTContext;

public class WSSCClientUtil {
   private static final Logger LOGGER = Logger.getLogger(WSSCClientUtil.class.getName());

   public static void terminateWssc(Stub var0) {
      RmHelper var1 = new RmHelper(var0);
      if (var1.checkSCCredentialInContext()) {
         ClientSCCredentialProviderBase.cancelSCToken(var0);
      } else {
         ClientSCCredentialProviderBase.cancelSCToken(var1.getMessageContextAdapter(false));
      }

   }

   public static void terminateWssc(BindingProvider var0) {
      RmHelper var1 = new RmHelper(var0);
      if (var1.checkSCCredentialInContext()) {
         ClientSCCredentialProviderBase.cancelSCToken(var0);
      } else {
         ClientSCCredentialProviderBase.cancelSCToken(var1.getMessageContextAdapter(false));
      }

   }

   public static void terminateWssc(MessageContext var0) {
      ClientSCCredentialProviderBase.cancelSCToken(var0);
   }

   public static void renewWssc(Stub var0) {
      RmHelper var1 = new RmHelper(var0);
      if (var1.checkSCCredentialInContext()) {
         ClientSCCredentialProviderBase.renewSCToken(var0);
      } else {
         ClientSCCredentialProviderBase.renewSCToken(var1.getMessageContextAdapter(false));
      }

   }

   public static void renewWssc(BindingProvider var0) {
      RmHelper var1 = new RmHelper(var0);
      if (var1.checkSCCredentialInContext()) {
         ClientSCCredentialProviderBase.renewSCToken(var0);
      } else {
         ClientSCCredentialProviderBase.renewSCToken(var1.getMessageContextAdapter(false));
      }

   }

   public static void renewWssc(MessageContext var0) {
      ClientSCCredentialProviderBase.renewSCToken(var0);
   }

   private static class RmHelper {
      private Map<String, Object> invokeProperties = null;

      public RmHelper(Stub var1) {
         this.invokeProperties = (Map)var1._getProperty("weblogic.wsee.invoke_properties");
      }

      public RmHelper(BindingProvider var1) {
         this.invokeProperties = var1.getRequestContext();
      }

      public boolean checkSCCredentialInContext() {
         Object var1 = this.invokeProperties.get("weblogic.wsee.wssc.sct");
         return var1 != null && var1 instanceof SCCredential;
      }

      private WsrmSecurityContext getRmSecurityContext() {
         String var1 = (String)this.invokeProperties.get("weblogic.wsee.sequenceid");
         if (var1 != null && !var1.equals("")) {
            SAFConversationInfo var2 = WsrmSAFManager.getConversationInfo(true, var1, true);
            if (var2 == null) {
               return null;
            } else {
               WsrmSequenceContext var3 = (WsrmSequenceContext)var2.getContext();
               return var3.getWsrmSecurityContext();
            }
         } else {
            return null;
         }
      }

      private WsrmSecurityContext getRm2SecurityContext() {
         WsrmInvocationPropertyBag var1 = (WsrmInvocationPropertyBag)this.invokeProperties.get(WsrmInvocationPropertyBag.key);
         if (var1 == null) {
            return null;
         } else {
            String var2 = var1.getSequenceId();
            if (var2 == null) {
               var2 = (String)this.invokeProperties.get("weblogic.wsee.reliability2.SequenceID");
            }

            if (var2 == null) {
               return null;
            } else {
               try {
                  SourceSequence var3 = SourceSequenceManager.getInstance().getSequence(var1.getWsrmVersion(), var2, true);
                  return var3 == null ? null : var3.getSecurityContext();
               } catch (UnknownSourceSequenceException var4) {
                  return null;
               }
            }
         }
      }

      public MessageContext getMessageContextAdapter(final boolean var1) {
         final WsrmSecurityContext var2 = this.getRmSecurityContext();
         if (var2 == null) {
            var2 = this.getRm2SecurityContext();
         }

         if (var2 == null) {
            throw new IllegalStateException("Can not find WSRM security context");
         } else {
            return new SoapMessageContext() {
               private Map properties = null;

               {
                  this.properties = new HashMap(RmHelper.this.invokeProperties);
                  if (var1) {
                     this.properties.put("weblogic.wsee.invoke_properties", RmHelper.this.invokeProperties);
                  }

                  if (var2.getSCCredential() != null) {
                     this.properties.put("weblogic.wsee.wssc.sct", var2.getSCCredential());
                  }

                  WSTContext var4 = var2.getWSTContext();
                  if (var4 != null) {
                     this.properties.put("weblogic.wsee.security.wst.framework.WSTContext", var4);
                  }

                  try {
                     Map var5 = var2.newInitializedMap();
                     MessageContext var6 = var4.getMessageContext();
                     Iterator var7 = var5.keySet().iterator();

                     while(var7.hasNext()) {
                        String var8 = (String)var7.next();
                        Object var9 = var5.get(var8);
                        var6.setProperty(var8, var9);
                        if (WSSCClientUtil.LOGGER.isLoggable(Level.FINEST)) {
                           WSSCClientUtil.LOGGER.log(Level.FINEST, "Has put additional security information into WST original message context: " + var8 + " = [" + var9 + "]");
                        }
                     }

                     if (WSSCClientUtil.LOGGER.isLoggable(Level.FINE)) {
                        WSSCClientUtil.LOGGER.log(Level.FINE, "Has put all additional security information into WST original message context form WSRM security context");
                     }

                  } catch (PolicyException var10) {
                     throw new IllegalStateException("Can not get properties from WSRM security context", var10);
                  }
               }

               private boolean isSCCredentialProperty(String var1x) {
                  return var1x != null && var1x.equals("weblogic.wsee.wssc.sct");
               }

               private boolean isWSTContextProperty(String var1x) {
                  return var1x != null && var1x.equals("weblogic.wsee.security.wst.framework.WSTContext");
               }

               public void setProperty(String var1x, Object var2x) {
                  this.properties.put(var1x, var2x);
                  if (this.isSCCredentialProperty(var1x)) {
                     var2.setSCCredential((SCCredential)var2x);
                     if (WSSCClientUtil.LOGGER.isLoggable(Level.FINE)) {
                        WSSCClientUtil.LOGGER.log(Level.FINE, "Sets the SC credential into WSRM security context, the SC credential identifier is: " + ((SCCredential)var2x).getIdentifier());
                     }
                  }

                  if (this.isWSTContextProperty(var1x)) {
                     var2.setWSTContext((WSTContext)var2x);
                  }

               }

               public void removeProperty(String var1x) {
                  this.properties.remove(var1x);
                  if (this.isSCCredentialProperty(var1x)) {
                     var2.setSCCredential((SCCredential)null);
                     if (WSSCClientUtil.LOGGER.isLoggable(Level.FINE)) {
                        WSSCClientUtil.LOGGER.log(Level.FINE, "Removes the SC credential from WSRM security context");
                     }
                  }

                  if (this.isWSTContextProperty(var1x)) {
                     var2.setWSTContext((WSTContext)null);
                  }

               }

               public Iterator getPropertyNames() {
                  return this.properties.keySet().iterator();
               }

               public Object getProperty(String var1x) {
                  return this.properties.get(var1x);
               }

               public boolean containsProperty(String var1x) {
                  return this.properties.containsKey(var1x);
               }

               public boolean isSoap12() {
                  boolean var1x = false;
                  WSTContext var2x = (WSTContext)this.properties.get("weblogic.wsee.security.wst.framework.WSTContext");
                  if (var2x != null) {
                     var1x = ((SoapMessageContext)var2x.getMessageContext()).isSoap12();
                  }

                  return var1x;
               }
            };
         }
      }
   }
}
