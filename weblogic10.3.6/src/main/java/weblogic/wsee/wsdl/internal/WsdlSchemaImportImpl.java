package weblogic.wsee.wsdl.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.w3c.dom.Element;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlSchemaImport;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlSchemaBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaImportBuilder;

public class WsdlSchemaImportImpl extends WsdlBase implements WsdlSchemaImportBuilder {
   private static final boolean verbose = Verbose.isVerbose(WsdlSchemaImport.class);
   private WsdlSchemaBuilder schema;
   private WsdlSchemaBuilder parent;
   private String namespace;
   private String schemaLocation;
   private boolean relative;
   private boolean isInclude;
   private TransportInfo transportInfo;

   WsdlSchemaImportImpl(WsdlSchemaBuilder var1, TransportInfo var2) {
      this.parent = var1;
      this.transportInfo = var2;
   }

   public WsdlSchemaBuilder getSchema() {
      return this.schema;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getSchemaLocation() {
      return this.schemaLocation;
   }

   public boolean isRelative() {
      return this.relative;
   }

   public void parse(Element var1, Set<String> var2) throws WsdlException {
      this.addDocumentation(var1);
      this.namespace = var1.getAttributeNS((String)null, "namespace");
      this.schemaLocation = var1.getAttributeNS((String)null, "schemaLocation");
      this.isInclude = "include".equals(var1.getNodeName());
      if (verbose) {
         Verbose.log((Object)("import/include namespace: " + this.namespace));
         Verbose.log((Object)("import/include schemaLocation: " + this.schemaLocation));
      }

      if (this.schemaLocation != null && !"".equals(this.schemaLocation)) {
         String var3 = this.schemaLocation;

         try {
            URL var4 = new URL(this.schemaLocation);
            this.relative = false;
            if (var4.getProtocol().equals("servicebus")) {
               var3 = WsdlUtils.constructRelativeLocation(this.schemaLocation, this.parent.getLocationUrl(), this.parent.getWsdlDefinitions() == null ? null : this.parent.getWsdlDefinitions().getRelativeResourceResolver());
            }
         } catch (MalformedURLException var5) {
            this.relative = true;
            var3 = WsdlUtils.constructRelativeLocation(this.schemaLocation, this.parent.getLocationUrl(), this.parent.getWsdlDefinitions() == null ? null : this.parent.getWsdlDefinitions().getRelativeResourceResolver());
         }

         if (var2.contains(var3)) {
            if (verbose) {
               Verbose.log((Object)("Ignoring schema at " + this.schemaLocation + ": already processed"));
            }

         } else {
            var2.add(this.schemaLocation);
            var2.add(var3);
            this.schema = WsdlSchemaImpl.parse(this.transportInfo, var3, var2, this.parent.getWsdlDefinitions());
            var1.setAttributeNS((String)null, "schemaLocation", this.schemaLocation);
         }
      } else {
         this.relative = false;
      }
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, this.isInclude ? "include" : "import", "http://www.w3.org/2001/XMLSchema");
      if (this.namespace != null) {
         var2.setAttribute(var3, "namespace", "http://www.w3.org/2001/XMLSchema", this.namespace);
      }

      String var4 = var2.getImportPrefix();
      if (this.isRelative() && var4 != null) {
         var2.setAttribute(var3, "schemaLocation", "http://www.w3.org/2001/XMLSchema", var4 + "/" + this.schemaLocation);
      } else {
         var2.setAttribute(var3, "schemaLocation", "http://www.w3.org/2001/XMLSchema", this.schemaLocation);
      }

   }

   public void writeToFile(File var1, String var2) throws IOException, WsdlException {
      if (this.relative) {
         File var3 = new File(var1.getParentFile(), this.schemaLocation);
         if (verbose) {
            Verbose.log((Object)("Writing schema import to file " + var3.getAbsolutePath()));
         }

         this.schema.writeToFile(var3, var2);
      }
   }
}
