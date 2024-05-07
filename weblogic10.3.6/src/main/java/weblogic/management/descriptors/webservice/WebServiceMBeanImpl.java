package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;
import weblogic.xml.xmlnode.XMLNode;
import weblogic.xml.xmlnode.XMLNodeSet;

public class WebServiceMBeanImpl extends XMLElementMBeanDelegate implements WebServiceMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_webServiceName = false;
   private String webServiceName;
   private boolean isSet_security = false;
   private XMLNode security;
   private boolean isSet_style = false;
   private String style;
   private boolean isSet_protocol = false;
   private String protocol;
   private boolean isSet_portName = false;
   private String portName;
   private boolean isSet_jmsURI = false;
   private String jmsURI;
   private boolean isSet_typeMapping = false;
   private TypeMappingMBean typeMapping;
   private boolean isSet_types = false;
   private XMLNodeSet types;
   private boolean isSet_targetNamespace = false;
   private String targetNamespace;
   private boolean isSet_useSOAP12 = false;
   private boolean useSOAP12;
   private boolean isSet_exposeWSDL = false;
   private boolean exposeWSDL = true;
   private boolean isSet_exposeHomePage = false;
   private boolean exposeHomePage = true;
   private boolean isSet_portTypeName = false;
   private String portTypeName;
   private boolean isSet_charset = false;
   private String charset;
   private boolean isSet_uri = false;
   private String uri;
   private boolean isSet_components = false;
   private ComponentsMBean components;
   private boolean isSet_responseBufferSize = false;
   private int responseBufferSize;
   private boolean isSet_operations = false;
   private OperationsMBean operations;
   private boolean ignoreAuthHeader;
   private boolean isSet_ignoreAuthHeader = false;
   private boolean handleAllActors = true;
   private boolean isSet_handleAllActors = false;

   public String getWebServiceName() {
      return this.webServiceName;
   }

   public void setWebServiceName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.webServiceName;
      this.webServiceName = var1;
      this.isSet_webServiceName = var1 != null;
      this.checkChange("webServiceName", var2, this.webServiceName);
   }

   public XMLNode getSecurity() {
      return this.security;
   }

   public void setSecurity(XMLNode var1) {
      XMLNode var2 = this.security;
      this.security = var1;
      this.isSet_security = var1 != null;
      this.checkChange("security", var2, this.security);
   }

   public String getStyle() {
      return this.style;
   }

   public void setStyle(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.style;
      this.style = var1;
      this.isSet_style = var1 != null;
      this.checkChange("style", var2, this.style);
   }

   public String getProtocol() {
      return this.protocol;
   }

   public void setProtocol(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.protocol;
      this.protocol = var1;
      this.isSet_protocol = var1 != null;
      this.checkChange("protocol", var2, this.protocol);
   }

   public String getPortName() {
      return this.portName;
   }

   public void setPortName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.portName;
      this.portName = var1;
      this.isSet_portName = var1 != null;
      this.checkChange("portName", var2, this.portName);
   }

   public String getJmsURI() {
      return this.jmsURI;
   }

   public void setJmsURI(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.jmsURI;
      this.jmsURI = var1;
      this.isSet_jmsURI = var1 != null;
      this.checkChange("jmsURI", var2, this.jmsURI);
   }

   public TypeMappingMBean getTypeMapping() {
      return this.typeMapping;
   }

   public void setTypeMapping(TypeMappingMBean var1) {
      TypeMappingMBean var2 = this.typeMapping;
      this.typeMapping = var1;
      this.isSet_typeMapping = var1 != null;
      this.checkChange("typeMapping", var2, this.typeMapping);
   }

   public XMLNodeSet getTypes() {
      return this.types;
   }

   public void setTypes(XMLNodeSet var1) {
      XMLNodeSet var2 = this.types;
      this.types = var1;
      this.isSet_types = var1 != null;
      this.checkChange("types", var2, this.types);
   }

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   public void setTargetNamespace(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.targetNamespace;
      this.targetNamespace = var1;
      this.isSet_targetNamespace = var1 != null;
      this.checkChange("targetNamespace", var2, this.targetNamespace);
   }

   public boolean getExposeWSDL() {
      return this.exposeWSDL;
   }

   public void setExposeWSDL(boolean var1) {
      boolean var2 = this.exposeWSDL;
      this.exposeWSDL = var1;
      this.isSet_exposeWSDL = true;
      this.checkChange("exposeWSDL", var2, this.exposeWSDL);
   }

   public boolean getExposeHomePage() {
      return this.exposeHomePage;
   }

   public void setExposeHomePage(boolean var1) {
      boolean var2 = this.exposeHomePage;
      this.exposeHomePage = var1;
      this.isSet_exposeWSDL = true;
      this.checkChange("exposeHomePage", var2, this.exposeHomePage);
   }

   public boolean getUseSOAP12() {
      return this.useSOAP12;
   }

   public void setUseSOAP12(boolean var1) {
      boolean var2 = this.useSOAP12;
      this.useSOAP12 = var1;
      this.isSet_useSOAP12 = true;
      this.checkChange("useSOAP12", var2, this.useSOAP12);
   }

   public boolean getHandleAllActors() {
      return this.handleAllActors;
   }

   public void setHandleAllActors(boolean var1) {
      boolean var2 = this.handleAllActors;
      this.handleAllActors = var1;
      this.isSet_handleAllActors = true;
      this.checkChange("handleAllActors", var2, this.handleAllActors);
   }

   public String getPortTypeName() {
      return this.portTypeName;
   }

   public void setPortTypeName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.portTypeName;
      this.portTypeName = var1;
      this.isSet_portTypeName = var1 != null;
      this.checkChange("portTypeName", var2, this.portTypeName);
   }

   public boolean getIgnoreAuthHeader() {
      return this.ignoreAuthHeader;
   }

   public void setIgnoreAuthHeader(boolean var1) {
      boolean var2 = this.ignoreAuthHeader;
      this.ignoreAuthHeader = var1;
      this.isSet_ignoreAuthHeader = true;
      this.checkChange("ignroeAuthHeader", var2, this.ignoreAuthHeader);
   }

   public String getCharset() {
      return this.charset;
   }

   public void setCharset(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.charset;
      this.charset = var1;
      this.isSet_charset = var1 != null;
      this.checkChange("charset", var2, this.charset);
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.uri;
      this.uri = var1;
      this.isSet_uri = var1 != null;
      this.checkChange("uri", var2, this.uri);
   }

   public ComponentsMBean getComponents() {
      return this.components;
   }

   public void setComponents(ComponentsMBean var1) {
      ComponentsMBean var2 = this.components;
      this.components = var1;
      this.isSet_components = var1 != null;
      this.checkChange("components", var2, this.components);
   }

   public int getResponseBufferSize() {
      return this.responseBufferSize;
   }

   public void setResponseBufferSize(int var1) {
      int var2 = this.responseBufferSize;
      this.responseBufferSize = var1;
      this.isSet_responseBufferSize = var1 != -1;
      this.checkChange("responseBufferSize", var2, this.responseBufferSize);
   }

   public OperationsMBean getOperations() {
      return this.operations;
   }

   public void setOperations(OperationsMBean var1) {
      OperationsMBean var2 = this.operations;
      this.operations = var1;
      this.isSet_operations = var1 != null;
      this.checkChange("operations", var2, this.operations);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<web-service");
      if (this.isSet_protocol) {
         var2.append(" protocol=\"").append(String.valueOf(this.getProtocol())).append("\"");
      }

      if (this.isSet_charset) {
         var2.append(" jmsUri=\"").append(String.valueOf(this.getCharset())).append("\"");
      }

      if (this.isSet_useSOAP12) {
         var2.append(" useSOAP12=\"").append(String.valueOf(this.getUseSOAP12())).append("\"");
      }

      if (this.isSet_exposeWSDL) {
         var2.append(" exposeWSDL=\"").append(String.valueOf(this.getExposeWSDL())).append("\"");
      }

      if (this.isSet_exposeHomePage) {
         var2.append(" exposeHomePage=\"").append(String.valueOf(this.getExposeHomePage())).append("\"");
      }

      if (this.isSet_targetNamespace) {
         var2.append(" targetNamespace=\"").append(String.valueOf(this.getTargetNamespace())).append("\"");
      }

      if (this.isSet_webServiceName) {
         var2.append(" name=\"").append(String.valueOf(this.getWebServiceName())).append("\"");
      }

      if (this.isSet_responseBufferSize) {
         var2.append(" responseBufferSize=\"").append(String.valueOf(this.getResponseBufferSize())).append("\"");
      }

      if (this.isSet_style) {
         var2.append(" style=\"").append(String.valueOf(this.getStyle())).append("\"");
      }

      if (this.isSet_portTypeName) {
         var2.append(" portTypeName=\"").append(String.valueOf(this.getPortTypeName())).append("\"");
      }

      if (this.isSet_jmsURI) {
         var2.append(" jmsUri=\"").append(String.valueOf(this.getJmsURI())).append("\"");
      }

      if (this.isSet_uri) {
         var2.append(" uri=\"").append(String.valueOf(this.getURI())).append("\"");
      }

      if (this.isSet_portName) {
         var2.append(" portName=\"").append(String.valueOf(this.getPortName())).append("\"");
      }

      if (this.isSet_ignoreAuthHeader) {
         var2.append(" ignoreAuthHeader=\"").append(String.valueOf(this.getIgnoreAuthHeader())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getSecurity()) {
         var2.append(ToXML.indent(var1 + 2)).append(this.getSecurity()).append("\n");
      }

      if (null != this.getTypes()) {
         var2.append(ToXML.indent(var1 + 2)).append("\n<types>").append(this.getTypes()).append("\n</types>\n");
      }

      if (null != this.getTypeMapping()) {
         var2.append(this.getTypeMapping().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getComponents()) {
         var2.append(this.getComponents().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getOperations()) {
         var2.append(this.getOperations().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</web-service>\n");
      return var2.toString();
   }
}
