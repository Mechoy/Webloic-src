package weblogic.wsee.wsdl;

import org.w3c.dom.Element;
import weblogic.wsee.addressing.wsdl.EndpointReferenceWsdlParser;
import weblogic.wsee.callback.wsdl.CallbackWsdlExtensionParser;
import weblogic.wsee.conversation.wsdl.ConversationWsdlExtensionParser;
import weblogic.wsee.policy.deployment.PolicyWsdlExtensionParser;
import weblogic.wsee.tools.jws.UpgradedJwsWsdlExtensionParser;
import weblogic.wsee.wsdl.http.HttpExtensionParser;
import weblogic.wsee.wsdl.mime.MimeExtensionParser;
import weblogic.wsee.wsdl.soap11.SoapExtensionParser;
import weblogic.wsee.wsdl.soap12.Soap12ExtensionParser;

public final class WsdlExtensionRegistry implements WsdlExtensionParser {
   private WsdlExtensionParser[] parsers = createDefaultParsers();
   private static WsdlExtensionRegistry instance = new WsdlExtensionRegistry();

   private WsdlExtensionRegistry() {
   }

   public static WsdlExtensionParser getParser() {
      return instance;
   }

   private static WsdlExtensionParser[] createDefaultParsers() {
      WsdlExtensionParser[] var0 = new WsdlExtensionParser[]{new SoapExtensionParser(), new Soap12ExtensionParser(), new ConversationWsdlExtensionParser(), new CallbackWsdlExtensionParser(), new UpgradedJwsWsdlExtensionParser(), new PolicyWsdlExtensionParser(), new MimeExtensionParser(), new HttpExtensionParser(), new EndpointReferenceWsdlParser(), new WsdlPartnerLinkType(), new UnknownExtensionParser()};
      return var0;
   }

   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseMessage(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseOperation(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseBinding(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseBindingOperation(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseBindingMessage(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseService(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parsePort(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      for(int var3 = 0; var3 < this.parsers.length; ++var3) {
         WsdlExtension var4 = this.parsers[var3].parseDefinitions(var1, var2);
         if (var4 != null) {
            return var4;
         }
      }

      throw new WsdlException("Unable to parse extension " + var2);
   }

   public void cleanUp() {
      for(int var1 = 0; var1 < this.parsers.length; ++var1) {
         this.parsers[var1].cleanUp();
      }

   }
}
