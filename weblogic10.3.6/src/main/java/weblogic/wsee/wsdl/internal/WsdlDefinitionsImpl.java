package weblogic.wsee.wsdl.internal;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.util.IOUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.RelativeResourceResolver;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlImportBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.builder.WsdlTypesBuilder;
import weblogic.xml.domimpl.DocumentImpl;
import weblogic.xml.domimpl.Saver;
import weblogic.xml.domimpl.SaverOptions;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public final class WsdlDefinitionsImpl extends WsdlExtensibleImpl implements WsdlDefinitionsBuilder {
   private static final String ENC_UTF8 = "UTF-8";
   private WsdlTypesImpl alltypes;
   private WsdlTypesImpl localtypes;
   private Map<QName, WsdlMessageBuilder> messageList = new LinkedHashMap();
   private Map<QName, WsdlPortTypeBuilder> portTypeList = new LinkedHashMap();
   private Map<QName, WsdlBindingBuilder> bindingList = new LinkedHashMap();
   private Map<QName, WsdlServiceBuilder> serviceList = new LinkedHashMap();
   private List<WsdlImportBuilder> importList = new ArrayList();
   private Map<QName, WsdlMessageBuilder> localMessageList = new LinkedHashMap();
   private Map<QName, WsdlPortTypeBuilder> localPortTypeList = new LinkedHashMap();
   private Map<QName, WsdlBindingBuilder> localBindingList = new LinkedHashMap();
   private Map<QName, WsdlServiceBuilder> localServiceList = new LinkedHashMap();
   private String name;
   private String targetNamespace;
   private static final String DEFINITIONS = "definitions";
   private static final String NAME = "name";
   private static final String TARGET_NAMESPACE = "targetNamespace";
   private String wsdlLocation;
   private String encoding = "UTF-8";
   private WsdlSchemaBuilder theOnlySchema;
   private RelativeResourceResolver resolver;
   private Map<String, WsdlDefinitions> knownWsdlLocations = new HashMap();
   private static final boolean verbose = Verbose.isVerbose(WsdlDefinitions.class);
   public boolean specialModeForCallback81Wsdl;
   private TransportInfo transportInfo;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   void setKnownWsdlLocations(Map<String, WsdlDefinitions> var1) {
      if (var1 != null) {
         this.knownWsdlLocations = var1;
      }

   }

   Map<String, WsdlDefinitions> getKnownWsdlLocations() {
      return this.knownWsdlLocations;
   }

   public Set<String> getKnownImportedWsdlLocations() {
      HashSet var1 = new HashSet();
      var1.addAll(this.knownWsdlLocations.keySet());
      var1.remove(this.getWsdlLocation());
      return var1;
   }

   public WsdlSchemaBuilder getTheOnlySchema() {
      return this.theOnlySchema;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public WsdlTypesBuilder addTypes() {
      if (this.localtypes == null) {
         this.localtypes = new WsdlTypesImpl(this.transportInfo);
      }

      return this.localtypes;
   }

   public WsdlTypesBuilder getTypes() {
      return this.alltypes != null ? this.alltypes : this.localtypes;
   }

   public WsdlServiceBuilder getService(QName var1) {
      return (WsdlServiceBuilder)this.serviceList.get(var1);
   }

   public Map<QName, WsdlServiceBuilder> getServices() {
      return this.serviceList;
   }

   public WsdlServiceBuilder addService(QName var1) {
      WsdlServiceImpl var2 = new WsdlServiceImpl(this, var1);
      this.serviceList.put(var1, var2);
      this.localServiceList.put(var1, var2);
      return var2;
   }

   public WsdlPortBuilder getPort(QName var1) {
      Iterator var2 = this.getServices().values().iterator();

      WsdlPortBuilder var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         WsdlServiceBuilder var3 = (WsdlServiceBuilder)var2.next();
         var4 = (WsdlPortBuilder)var3.getPorts().get(var1);
      } while(var4 == null);

      return var4;
   }

   public Map<QName, WsdlPortBuilder> getPorts() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.getServices().values().iterator();

      while(var2.hasNext()) {
         WsdlServiceBuilder var3 = (WsdlServiceBuilder)var2.next();
         var1.putAll(var3.getPorts());
      }

      return var1;
   }

   public WsdlBindingBuilder getBinding(QName var1) {
      return (WsdlBindingBuilder)this.bindingList.get(var1);
   }

   public WsdlBindingBuilder addBinding(QName var1, WsdlPortTypeBuilder var2) {
      WsdlBindingImpl var3 = new WsdlBindingImpl(var1, var2.getName(), this);
      this.bindingList.put(var1, var3);
      this.localBindingList.put(var1, var3);
      return var3;
   }

   public Map<QName, WsdlBindingBuilder> getBindings() {
      return this.bindingList;
   }

   public WsdlPortTypeBuilder getPortType(QName var1) {
      WsdlPortTypeBuilder var2 = (WsdlPortTypeBuilder)this.portTypeList.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("portType not found '" + var1 + "'");
      } else {
         return var2;
      }
   }

   public WsdlPortTypeBuilder addPortType(QName var1) {
      WsdlPortTypeImpl var2 = new WsdlPortTypeImpl(var1, this);
      this.portTypeList.put(var1, var2);
      this.localPortTypeList.put(var1, var2);
      return var2;
   }

   public Map<QName, WsdlPortTypeBuilder> getPortTypes() {
      return this.portTypeList;
   }

   public WsdlMessageBuilder getMessage(QName var1) {
      return (WsdlMessageBuilder)this.messageList.get(var1);
   }

   public WsdlMessageBuilder addMessage(QName var1) {
      WsdlMessageImpl var2 = new WsdlMessageImpl(var1);
      this.messageList.put(var1, var2);
      this.localMessageList.put(var1, var2);
      return var2;
   }

   public Map<QName, WsdlMessageBuilder> getMessages() {
      return this.messageList;
   }

   public String getWsdlLocation() {
      return this.wsdlLocation;
   }

   public void setRelativeResourceResolver(RelativeResourceResolver var1) {
      this.resolver = var1;
   }

   public RelativeResourceResolver getRelativeResourceResolver() {
      return this.resolver;
   }

   public static WsdlDefinitions parse(String var0) throws WsdlException {
      return parse((TransportInfo)null, var0);
   }

   static WsdlDefinitions parse(TransportInfo var0, String var1) throws WsdlException {
      return parse(var0, var1, (RelativeResourceResolver)null);
   }

   public static WsdlDefinitions parse(TransportInfo var0, String var1, RelativeResourceResolver var2) throws WsdlException {
      return parse(var0, var1, var2, new HashMap());
   }

   static WsdlDefinitionsBuilder parse(TransportInfo var0, String var1, RelativeResourceResolver var2, Map<String, WsdlDefinitions> var3) throws WsdlException {
      if (verbose) {
         Verbose.log((Object)("Parsing WSDL :" + var1));
      }

      WsdlDefinitionsImpl var4 = new WsdlDefinitionsImpl();
      var4.setKnownWsdlLocations(var3);
      var4.setRelativeResourceResolver(var2);
      var4.wsdlLocation = var1;
      var4.setTransportInfo(var0);
      var4.parse(WsdlReader.getDocument(var0, var1));
      return var4;
   }

   void setTransportInfo(TransportInfo var1) {
      this.transportInfo = var1;
   }

   public static WsdlDefinitions parse(InputSource var0) throws WsdlException {
      if (verbose) {
         Verbose.log((Object)("Parsing WSDL :" + var0.getSystemId()));
      }

      WsdlDefinitionsImpl var1 = new WsdlDefinitionsImpl();
      if (var0.getSystemId() == null) {
         throw new WsdlException("SystemID must be set on InputSource");
      } else {
         var1.wsdlLocation = var0.getSystemId();
         var1.parse(WsdlReader.getDocument(var0));
         return var1;
      }
   }

   public static WsdlDefinitions parse(Document var0, String var1) throws WsdlException {
      if (verbose) {
         Verbose.log((Object)("Parsing WSDL :" + var1));
      }

      WsdlDefinitionsImpl var2 = new WsdlDefinitionsImpl();
      if (var1 == null) {
         throw new WsdlException("location must not be null");
      } else {
         var2.wsdlLocation = var1;
         var2.parse(var0);
         return var2;
      }
   }

   void parse(Document var1) throws WsdlException {
      Element var2 = var1.getDocumentElement();
      if (var2 == null) {
         throw new WsdlException("Invalid Wsdl file found. ");
      } else {
         if (var1 instanceof DocumentImpl) {
            DocumentImpl var3 = (DocumentImpl)var1;
            this.encoding = var3.getXmlEncoding();
            if (this.encoding == null) {
               this.encoding = "UTF-8";
            }
         }

         if (var2.getLocalName().equals("schema")) {
            this.theOnlySchema = new WsdlSchemaImpl(this, this.transportInfo);
            this.theOnlySchema.parse(var2, this.wsdlLocation);
            this.localtypes = new WsdlTypesImpl(this.transportInfo);
            this.localtypes.addSchema(this.theOnlySchema);
            this.alltypes = this.localtypes;
         } else {
            WsdlReader.checkWsdlDefinitions("definitions", var2);
            WsdlReader.checkWsdlNamespace(var2);
            super.parse(var2, (String)null);
         }

      }
   }

   private void importDefinition() {
      this.alltypes = new WsdlTypesImpl(this.transportInfo);
      this.alltypes.addTypes(this.localtypes);
      Iterator var1 = this.importList.iterator();

      while(var1.hasNext()) {
         WsdlImportBuilder var2 = (WsdlImportBuilder)var1.next();
         this.importDefinition((WsdlDefinitionsImpl)var2.getDefinitions());
      }

   }

   private void importDefinition(WsdlDefinitionsImpl var1) {
      if (var1.alltypes != null) {
         if (var1.theOnlySchema != null) {
            this.alltypes.addSchemaAsFirst(var1.theOnlySchema);
         } else {
            this.alltypes.addTypes(var1.alltypes);
         }
      }

      Iterator var2 = var1.messageList.entrySet().iterator();

      Map.Entry var3;
      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         this.messageList.put(var3.getKey(), var3.getValue());
      }

      var2 = var1.portTypeList.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         this.portTypeList.put(var3.getKey(), var3.getValue());
      }

      var2 = var1.bindingList.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         this.bindingList.put(var3.getKey(), var3.getValue());
      }

      var2 = var1.serviceList.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Map.Entry)var2.next();
         this.serviceList.put(var3.getKey(), var3.getValue());
      }

   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      String var3 = var1.getLocalName();
      if ("types".equals(var3)) {
         this.localtypes = new WsdlTypesImpl(this.transportInfo);
         this.localtypes.parse(var1, this.wsdlLocation);
      } else if ("import".equals(var3)) {
         WsdlImportImpl var4 = new WsdlImportImpl(this, this.transportInfo);
         var4.parse(var1, this.knownWsdlLocations);
         this.importList.add(var4);
      } else if ("message".equals(var3)) {
         WsdlMessageImpl var5 = new WsdlMessageImpl();
         var5.parse(var1, this.targetNamespace);
         this.localMessageList.put(var5.getName(), var5);
         this.messageList.put(var5.getName(), var5);
      } else if ("portType".equals(var3)) {
         WsdlPortTypeImpl var6 = new WsdlPortTypeImpl(this);
         var6.parse(var1, this.targetNamespace);
         this.localPortTypeList.put(var6.getName(), var6);
         this.portTypeList.put(var6.getName(), var6);
      } else if ("binding".equals(var3)) {
         WsdlBindingImpl var7 = new WsdlBindingImpl(this);
         var7.parse(var1, this.targetNamespace);
         this.localBindingList.put(var7.getName(), var7);
         this.bindingList.put(var7.getName(), var7);
      } else if ("service".equals(var3)) {
         WsdlServiceImpl var8 = new WsdlServiceImpl(this);
         var8.parse(var1, this.targetNamespace);
         this.localServiceList.put(var8.getName(), var8);
         this.serviceList.put(var8.getName(), var8);
      } else if (!"documentation".equals(var3)) {
         WsdlExtensionParser var9 = WsdlExtensionRegistry.getParser();
         return var9.parseDefinitions(this, var1);
      }

      this.importDefinition();
      return null;
   }

   protected void parseAttributes(Element var1, String var2) {
      this.name = var1.getAttributeNS((String)null, "name");
      this.targetNamespace = var1.getAttributeNS((String)null, "targetNamespace");
   }

   public void write(OutputStream var1) throws IOException, WsdlException {
      this.write((OutputStream)var1, (WsdlAddressInfo)null);
   }

   /** @deprecated */
   public void writeToFile(File var1) throws IOException, WsdlException {
      this.writeToFile(var1, (String)null);
   }

   public void writeToFile(File var1, String var2) throws IOException, WsdlException {
      if (var1.getParentFile() != null) {
         var1.getParentFile().mkdirs();
      }

      OutputStream var3 = IOUtil.createEncodedFileOutputStream(var1, var2);

      try {
         this.write(var3, (WsdlAddressInfo)null, var2);
         Iterator var4 = this.importList.iterator();

         while(var4.hasNext()) {
            WsdlImportBuilder var5 = (WsdlImportBuilder)var4.next();
            if (!var5.hasCirularImport()) {
               var5.writeToFile(var1, var2);
            }
         }

         if (this.localtypes != null) {
            this.localtypes.writeImportToFile(var1, var2);
         }

         if (StringUtil.isEmpty(this.wsdlLocation)) {
            this.wsdlLocation = var1.getAbsolutePath();
         }
      } finally {
         var3.flush();

         try {
            var3.close();
         } catch (IOException var12) {
         }

      }

   }

   /** @deprecated */
   public void write(OutputStream var1, WsdlAddressInfo var2) throws IOException, WsdlException {
      this.write(var1, var2, this.encoding);
   }

   public void write(OutputStream var1, WsdlAddressInfo var2, String var3) throws IOException, WsdlException {
      Document var4 = this.toDocument(var2);
      SaverOptions var5 = SaverOptions.getDefaults();
      var5.setPrettyPrint(true);
      var5.setWriteXmlDeclaration(true);
      if (var3 != null) {
         var5.setEncoding(var3);
      }

      Saver.save(var1, var4, var5);
   }

   private Document toDocument(WsdlAddressInfo var1) throws WsdlException {
      DocumentBuilderFactory var2 = WebLogicDocumentBuilderFactory.newInstance();
      var2.setNamespaceAware(true);

      try {
         Document var3 = var2.newDocumentBuilder().newDocument();
         this.write(var3, var1);
         return var3;
      } catch (ParserConfigurationException var4) {
         throw new WsdlException("Failed to create XML document", var4);
      }
   }

   private void write(Document var1, WsdlAddressInfo var2) throws WsdlException {
      if (this.theOnlySchema != null) {
         WsdlWriter var11 = new WsdlWriter();
         var11.setWsdlAddressInfo(var2);
         this.theOnlySchema.write(var1, var11);
      } else {
         Element var3 = var1.createElementNS(WsdlConstants.wsdlNS, "definitions");
         var1.appendChild(var3);
         WsdlWriter var4 = new WsdlWriter(var1, var3, "");
         Iterator var6;
         if (this.getExtension("unknown") != null && this.localtypes != null) {
            HashMap var5 = this.localtypes.getNameSpaceDefs();
            var6 = var5.keySet().iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               Object var8 = var5.get(var7);
               var4.addPrefix((String)var7, (String)var8);
            }
         }

         var4.setWsdlAddressInfo(var2);
         if (!StringUtil.isEmpty(this.name)) {
            var4.setAttribute(var3, "name", (String)null, (String)this.name);
         }

         var4.setAttribute(var3, "targetNamespace", (String)null, (String)this.targetNamespace);
         var4.setTargetNS(this.targetNamespace);
         Iterator var12 = this.importList.iterator();

         while(var12.hasNext()) {
            WsdlImportBuilder var14 = (WsdlImportBuilder)var12.next();
            var14.write(var3, var4);
         }

         this.writeExtensions(var3, var4);
         if (this.localtypes != null) {
            this.localtypes.write(var3, var4);
         }

         Collection var13 = this.specialModeForCallback81Wsdl ? this.messageList.values() : this.localMessageList.values();
         var6 = var13.iterator();

         while(var6.hasNext()) {
            WsdlMessageBuilder var16 = (WsdlMessageBuilder)var6.next();
            var16.write(var3, var4);
         }

         Collection var15 = this.specialModeForCallback81Wsdl ? this.portTypeList.values() : this.localPortTypeList.values();
         Iterator var17 = var15.iterator();

         while(var17.hasNext()) {
            WsdlPortTypeBuilder var19 = (WsdlPortTypeBuilder)var17.next();
            var19.write(var3, var4);
         }

         Collection var18 = this.specialModeForCallback81Wsdl ? this.bindingList.values() : this.localBindingList.values();
         Iterator var20 = var18.iterator();

         while(var20.hasNext()) {
            WsdlBindingBuilder var9 = (WsdlBindingBuilder)var20.next();
            var9.write(var3, var4);
         }

         Collection var21 = this.specialModeForCallback81Wsdl ? this.serviceList.values() : this.localServiceList.values();
         Iterator var22 = var21.iterator();

         while(var22.hasNext()) {
            WsdlServiceBuilder var10 = (WsdlServiceBuilder)var22.next();
            var10.write(var3, var4);
         }

      }
   }

   public List<WsdlDefinitionsBuilder> getImportedWsdlDefinitions() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.importList.iterator();

      while(var2.hasNext()) {
         WsdlImportBuilder var3 = (WsdlImportBuilder)var2.next();
         WsdlDefinitionsImpl var4 = (WsdlDefinitionsImpl)var3.getDefinitions();
         if (var3.hasCirularImport()) {
            if (!var3.cirularImportResovled()) {
               var4.importDefinition();
               this.importDefinition(var4);
               ((WsdlImportImpl)var3).setCirularImportResovled(true);
            }
         } else {
            var1.add(var4);
         }
      }

      return var1;
   }

   public WsdlDefinitionsBuilder findImport(String var1) {
      Iterator var2 = this.importList.iterator();

      while(var2.hasNext()) {
         WsdlImportBuilder var3 = (WsdlImportBuilder)var2.next();
         if (var3.isRelative() && var1.equals(var3.getLocation())) {
            return var3.getDefinitions();
         }

         if (!var3.hasCirularImport()) {
            WsdlDefinitionsBuilder var4 = var3.getDefinitions().findImport(var1);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public WsdlDefinitionsBuilder findAbsoluteImport(String var1) {
      Iterator var2 = this.importList.iterator();

      WsdlDefinitionsBuilder var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         WsdlImportBuilder var3 = (WsdlImportBuilder)var2.next();
         if (!var3.isRelative() && var1.equals(var3.getLocation())) {
            return var3.getDefinitions();
         }

         var4 = var3.getDefinitions().findAbsoluteImport(var1);
      } while(var4 == null);

      return var4;
   }

   public List<WsdlImportBuilder> getImports() {
      return this.importList;
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("targetNamespace", this.targetNamespace);
      var1.writeField("wsdlLocation", this.wsdlLocation);
      var1.writeArray("message", this.messageList.keySet().iterator());
      var1.writeArray("portType", this.portTypeList.keySet().iterator());
      var1.writeArray("binding", this.bindingList.keySet().iterator());
      var1.writeArray("service", this.serviceList.keySet().iterator());
      var1.end();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }
}
