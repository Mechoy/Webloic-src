package weblogic.wsee.wsdl;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.internal.WsdlDefinitionsImpl;
import weblogic.wsee.wsdl.validation.WsdlValidationRegistry;

public class WsdlFactory {
   private WsdlFactory() {
   }

   public static WsdlFactory getInstance() {
      return new WsdlFactory();
   }

   public WsdlDefinitions parse(String var1) throws WsdlException {
      return this.parse(var1, true, (RelativeResourceResolver)null);
   }

   public WsdlDefinitions parse(String var1, RelativeResourceResolver var2) throws WsdlException {
      return this.parse(var1, true, var2);
   }

   public WsdlDefinitions parse(TransportInfo var1, String var2, boolean var3) throws WsdlException {
      return this.parse(var1, var2, var3, (RelativeResourceResolver)null);
   }

   public WsdlDefinitions parse(TransportInfo var1, String var2, boolean var3, RelativeResourceResolver var4) throws WsdlException {
      WsdlDefinitions var5 = WsdlDefinitionsImpl.parse(var1, var2, var4);
      if (var3) {
         WsdlValidationRegistry.getInstance().validate(var5);
      }

      WsdlExtensionRegistry.getParser().cleanUp();
      return var5;
   }

   public WsdlDefinitions parse(InputSource var1) throws WsdlException {
      return this.parse(var1, true);
   }

   public WsdlDefinitions parse(String var1, boolean var2) throws WsdlException {
      return this.parse((TransportInfo)null, var1, var2, (RelativeResourceResolver)null);
   }

   public WsdlDefinitions parse(String var1, boolean var2, RelativeResourceResolver var3) throws WsdlException {
      return this.parse((TransportInfo)null, var1, var2, var3);
   }

   public WsdlDefinitions parse(InputSource var1, boolean var2) throws WsdlException {
      WsdlDefinitions var3 = WsdlDefinitionsImpl.parse(var1);
      if (var2) {
         WsdlValidationRegistry.getInstance().validate(var3);
      }

      WsdlExtensionRegistry.getParser().cleanUp();
      return var3;
   }

   public WsdlDefinitions parse(Document var1, String var2) throws WsdlException {
      return this.parse(var1, var2, true);
   }

   public WsdlDefinitions parse(Document var1, String var2, boolean var3) throws WsdlException {
      WsdlDefinitions var4 = WsdlDefinitionsImpl.parse(var1, var2);
      if (var3) {
         WsdlValidationRegistry.getInstance().validate(var4);
      }

      WsdlExtensionRegistry.getParser().cleanUp();
      return var4;
   }

   public WsdlDefinitionsBuilder create() {
      return new WsdlDefinitionsImpl();
   }
}
