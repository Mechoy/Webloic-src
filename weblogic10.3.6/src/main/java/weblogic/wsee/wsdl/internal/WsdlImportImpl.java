package weblogic.wsee.wsdl.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.RelativeResourceResolver;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlImport;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlImportBuilder;

public class WsdlImportImpl extends WsdlBase implements WsdlImportBuilder {
   private static final boolean verbose = Verbose.isVerbose(WsdlImport.class);
   private WsdlDefinitionsBuilder definitions;
   private WsdlDefinitionsBuilder parent;
   private String namespace;
   private String location;
   private boolean relative;
   private boolean hasCircularImport = false;
   private boolean circularImportResovled;
   private TransportInfo transportInfo;

   WsdlImportImpl(WsdlDefinitionsBuilder var1, TransportInfo var2) {
      this.parent = var1;
      this.transportInfo = var2;
   }

   public WsdlDefinitionsBuilder getDefinitions() {
      return this.definitions;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getLocation() {
      return this.location;
   }

   public boolean isRelative() {
      return this.relative;
   }

   public boolean hasCirularImport() {
      return this.hasCircularImport;
   }

   public boolean cirularImportResovled() {
      return this.circularImportResovled;
   }

   public void setCirularImportResovled(boolean var1) {
      this.circularImportResovled = var1;
   }

   public void parse(Element var1, Map<String, WsdlDefinitions> var2) throws WsdlException {
      this.addDocumentation(var1);
      this.namespace = var1.getAttributeNS((String)null, "namespace");
      this.location = var1.getAttributeNS((String)null, "location");
      if (verbose) {
         Verbose.log((Object)("import namespace: " + this.namespace));
         Verbose.log((Object)("import location: " + this.location));
      }

      if (this.namespace == null) {
         throw new WsdlException("namespace not specified in import");
      } else if (this.location == null) {
         throw new WsdlException("location not specified in import");
      } else {
         String var3 = this.location;

         try {
            new URL(this.location);
            this.relative = false;
         } catch (MalformedURLException var5) {
            this.relative = true;
            var3 = WsdlUtils.constructRelativeLocation(this.location, this.parent.getWsdlLocation(), this.parent.getRelativeResourceResolver());
         }

         WsdlDefinitionsBuilder var4;
         if ((var4 = (WsdlDefinitionsBuilder)var2.get(var3)) != null) {
            if (verbose) {
               Verbose.log((Object)("ignoring previously imported wsdl at location: " + this.location));
            }

            this.hasCircularImport = true;
            this.definitions = var4;
         } else {
            var2.put(this.parent.getWsdlLocation(), this.parent);
            this.definitions = WsdlDefinitionsImpl.parse(this.transportInfo, var3, (RelativeResourceResolver)null, var2);
            this.definitions.setRelativeResourceResolver(this.parent.getRelativeResourceResolver());
            var2.put(var3, this.definitions);
         }
      }
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "import", WsdlConstants.wsdlNS);
      var2.setAttribute(var3, "namespace", WsdlConstants.wsdlNS, this.namespace);
      String var4 = var2.getImportPrefix();
      if (this.isRelative() && var4 != null) {
         var2.setAttribute(var3, "location", WsdlConstants.wsdlNS, var4 + "/" + this.location);
      } else {
         var2.setAttribute(var3, "location", WsdlConstants.wsdlNS, this.location);
      }

      this.writeDocumentation(var3, var2);
   }

   public void writeToFile(File var1, String var2) throws IOException, WsdlException {
      if (this.relative) {
         File var3 = new File(var1.getParentFile(), this.location);
         if (verbose) {
            Verbose.log((Object)("Writing import to file " + var3.getAbsolutePath()));
         }

         this.definitions.writeToFile(var3, var2);
      }
   }

   public void writeToFile(File var1) throws IOException, WsdlException {
      this.writeToFile(var1, (String)null);
   }
}
