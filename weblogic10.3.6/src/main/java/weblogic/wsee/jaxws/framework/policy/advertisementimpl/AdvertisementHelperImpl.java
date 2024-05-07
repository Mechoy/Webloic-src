package weblogic.wsee.jaxws.framework.policy.advertisementimpl;

import com.sun.xml.ws.addressing.W3CAddressingConstants;
import com.sun.xml.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.ws.api.server.DocumentAddressResolver;
import com.sun.xml.ws.api.server.PortAddressResolver;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.server.SDDocumentImpl;
import com.sun.xml.ws.server.ServiceDefinitionImpl;
import com.sun.xml.ws.server.WSEndpointImpl;
import com.sun.xml.ws.transport.http.WSHTTPConnection;
import com.sun.xml.ws.util.ByteArrayBuffer;
import com.sun.xml.ws.util.JAXWSUtils;
import com.sun.xml.ws.util.RuntimeVersion;
import com.sun.xml.ws.util.ServiceFinder;
import com.sun.xml.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import com.sun.xml.ws.wsdl.parser.WSDLConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import org.xml.sax.InputSource;
import weblogic.wsee.jaxws.WLSServletAdapter;
import weblogic.wsee.jaxws.framework.policy.EnvironmentMetadataFactory;
import weblogic.wsee.jaxws.framework.policy.PolicyAdvertisementFilter;
import weblogic.wsee.jws.jaxws.owsm.PolicySubjectBindingFeature;

public class AdvertisementHelperImpl implements AdvertisementHelper {
   static final String WSDLFACTORY_CLASS_NAME = "javax.wsdl.factory.WSDLFactory";
   static final String ADDRESSINGEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.addressing.AddressingExtensionRegistry";
   static final String PARTNERLINKEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.partnerlink.PartnerLinkExtensionRegistry";
   static final String SCHEMAEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.oracle.schema.SchemaExtension";
   static final String JMSEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.jms.JMSExtensions";
   static final String DIMEEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.oracle.dime.DimeExtension";
   static final String STREAMATTACHMENTSEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.oracle.streaming.StreamAttachmentsExtension";
   static final String JAVAEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.wsif.java.JavaExtensionRegistry";
   static final String EJBEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.wsif.ejb.EJBExtensionRegistry";
   static final String FORMATEXTREG_CLASS_NAME = "oracle.j2ee.ws.wsdl.extensions.wsif.format.FormatExtensionRegistry";
   static final String POLICYEXTREG_CLASS_NAME = "oracle.wsm.policy.wsdl.extensions.PolicyExtensionRegistry";
   private static final String VERSION_COMMENT;
   private static final Logger logger;

   public boolean hasPolicyAdvertisementFilter() {
      PolicyAdvertisementFilter var1 = this.getPolicyAdvertisementFilter();
      return var1 != null;
   }

   public boolean handleAdvertisementRequest(SDDocument var1, WSEndpoint<?> var2, WSHTTPConnection var3, PortAddressResolver var4, DocumentAddressResolver var5) throws IOException {
      PolicyAdvertisementFilter var6 = this.getPolicyAdvertisementFilter();
      if (var6 != null) {
         PolicyAdvertisementFilter.MetadataType var7 = this.getMetadataType(var3.getQueryString());
         if (var7 != null) {
            PolicySubjectBindingFeature var8 = (PolicySubjectBindingFeature)var2.getBinding().getFeature(PolicySubjectBindingFeature.class);
            if (var8 != null && var8.hasPolicyReferences()) {
               var6.setEnvironmentMetadata(EnvironmentMetadataFactory.getEnvironmentMetadata());
               ArrayList var9 = new ArrayList();
               var9.add(var8.getPolicySubjectBinding());
               var6.setPolicySubjectBindings(var9);
               InputStream var10 = null;
               Object var11 = null;

               boolean var24;
               try {
                  InputSource var12 = null;
                  String var13 = null;
                  boolean var14 = false;
                  URL var15 = JAXWSUtils.getEncodedURL(((SDDocumentImpl)var1).getSystemId().toExternalForm());
                  var10 = this.getWsdlFrom(var15);
                  if (var10 != null) {
                     var12 = new InputSource(var10);
                     var13 = var15.toExternalForm();
                     var14 = true;
                  } else {
                     ByteArrayBuffer var16 = new ByteArrayBuffer();
                     var1.writeTo(var4, var5, var16);
                     var12 = new InputSource(var16.newInputStream());
                     var13 = var1.getURL().toExternalForm();
                  }

                  Map var23 = queryParametersToMap(var3.getQueryString());
                  var23.put("oracle.wsm.dont.remove.existing.policies", "true");
                  Object var17 = this.readWSDL(var12, var13);
                  Class var18 = this.getDefinitionClass(var6.getClass().getClassLoader());
                  this.invokeMethod(var6, "advertise", new Class[]{var18, String.class, Map.class}, new Object[]{var17, var7.name(), var23});
                  this.dumpDefinition(var17, "after WSM advertisement");
                  var3.setStatus(200);
                  var3.setContentTypeResponseHeader("text/xml;charset=\"utf-8\"");
                  var11 = var3.getProtocol().contains("1.1") ? var3.getOutput() : new Http10OutputStream(var3);
                  if (var14) {
                     ByteArrayOutputStream var19 = new ByteArrayOutputStream();
                     this.writeWSDL(var17, var19);
                     this.postProcessWSDL(var1, var2, var4, var5, new ByteArrayInputStream(var19.toByteArray()), (OutputStream)var11);
                  } else {
                     this.writeWSDL(var17, (OutputStream)var11);
                  }

                  var24 = true;
               } finally {
                  if (var10 != null) {
                     var10.close();
                  }

                  if (var11 != null) {
                     ((OutputStream)var11).close();
                  }

               }

               return var24;
            }
         }
      }

      return false;
   }

   public Object readWSDL(URL var1, InputSource var2) throws IOException {
      if (var2 != null) {
         return this.readWSDL(var2, var1.toExternalForm());
      } else {
         logger.warning("readWSDL; cannot proceed the wsdl parameter is null.");
         return null;
      }
   }

   private PolicyAdvertisementFilter getPolicyAdvertisementFilter() {
      PolicyAdvertisementFilter var1 = null;
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      Iterator var3 = ServiceFinder.find(PolicyAdvertisementFilter.class, var2).iterator();
      if (var3.hasNext()) {
         var1 = (PolicyAdvertisementFilter)var3.next();
         if (var3.hasNext()) {
            throw new WebServiceException("More than one Filter for OWSM Policy Advertisement found.");
         }
      }

      return var1;
   }

   private PolicyAdvertisementFilter.MetadataType getMetadataType(String var1) {
      try {
         String var2 = WLSServletAdapter.getRequestCmd(var1);
         if (var2 != null) {
            return PolicyAdvertisementFilter.MetadataType.valueOf(var2.toUpperCase(Locale.ENGLISH));
         }
      } catch (IllegalArgumentException var3) {
      }

      return null;
   }

   private Object readWSDL(InputSource var1, String var2) throws IOException {
      Object var3 = this.getWSDLFactory();
      Object var4 = this.invokeMethod(var3, "newPopulatedExtensionRegistry", (Class[])null, (Object[])null);
      this.registerExtension("oracle.j2ee.ws.wsdl.extensions.addressing.AddressingExtensionRegistry", var4);
      this.registerExtension("oracle.j2ee.ws.wsdl.extensions.partnerlink.PartnerLinkExtensionRegistry", var4);

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.oracle.schema.SchemaExtension", var4);
      } catch (Throwable var14) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.jms.JMSExtensions", var4);
      } catch (Throwable var13) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.oracle.dime.DimeExtension", var4);
      } catch (Throwable var12) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.oracle.streaming.StreamAttachmentsExtension", var4);
      } catch (Throwable var11) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.wsif.java.JavaExtensionRegistry", var4);
      } catch (Throwable var10) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.wsif.ejb.EJBExtensionRegistry", var4);
      } catch (Throwable var9) {
      }

      try {
         this.registerExtension("oracle.j2ee.ws.wsdl.extensions.wsif.format.FormatExtensionRegistry", var4);
      } catch (Throwable var8) {
      }

      try {
         this.registerExtension("oracle.wsm.policy.wsdl.extensions.PolicyExtensionRegistry", var4, Level.FINE);
      } catch (Throwable var7) {
      }

      Object var5 = this.invokeMethod(var3, "newWSDLReader", (Class[])null, (Object[])null);
      this.invokeMethod(var5, "setExtensionRegistry", new Class[]{var4.getClass()}, new Object[]{var4});
      this.invokeMethod(var5, "setFeature", new Class[]{String.class, Boolean.TYPE}, new Object[]{"javax.wsdl.verbose", false});
      this.invokeMethod(var5, "setFeature", new Class[]{String.class, Boolean.TYPE}, new Object[]{"javax.wsdl.importDocuments", false});
      Object var6 = this.invokeMethod(var5, "readWSDL", new Class[]{String.class, InputSource.class}, new Object[]{var2, var1});
      this.dumpDefinition(var6, "after reading from source");
      return var6;
   }

   private void dumpDefinition(Object var1, String var2) {
      if (logger.isLoggable(Level.FINE)) {
         if (var1 == null) {
            logger.fine("AdvertisementHelper: dumping Definition " + var2 + "; null");
         } else {
            try {
               ByteArrayOutputStream var3 = new ByteArrayOutputStream();
               this.writeWSDL(var1, var3);
               logger.fine("AdvertisementHelper: dumping Definition " + var2 + "; " + var3.toString());
            } catch (Throwable var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   private InputStream getWsdlFrom(URL var1) {
      InputStream var2 = null;

      try {
         var2 = var1.openStream();
      } catch (Throwable var4) {
         var2 = null;
      }

      return var2;
   }

   private void postProcessWSDL(SDDocument var1, WSEndpoint<?> var2, PortAddressResolver var3, DocumentAddressResolver var4, InputStream var5, OutputStream var6) throws IOException {
      XMLStreamReader var7 = XMLStreamReaderFactory.create((String)null, var5, "UTF-8", false);
      XMLStreamWriter var8 = null;
      boolean var20 = false;

      IOException var10;
      try {
         var20 = true;
         var8 = XMLStreamWriterFactory.create(var6, "UTF-8");
         var8.writeStartDocument("UTF-8", "1.0");
         var8.writeComment(VERSION_COMMENT);
         (new DefnPatcher((WSEndpointImpl)var2, (SDDocumentImpl)var1, var3, var4)).bridge(var7, var8);
         var8.writeEndDocument();
         var20 = false;
      } catch (XMLStreamException var23) {
         var10 = new IOException(var23.getMessage());
         var10.initCause(var23);
         throw var10;
      } finally {
         if (var20) {
            IOException var13;
            try {
               if (var7 != null) {
                  var7.close();
               }
            } catch (XMLStreamException var21) {
               var13 = new IOException(var21.getMessage());
               var13.initCause(var21);
               throw var13;
            }

            try {
               if (var8 != null) {
                  var8.close();
               }
            } catch (XMLStreamException var24) {
               var13 = new IOException(var24.getMessage());
               var13.initCause(var24);
               throw var13;
            }

         }
      }

      try {
         if (var7 != null) {
            var7.close();
         }
      } catch (XMLStreamException var26) {
         var10 = new IOException(var26.getMessage());
         var10.initCause(var26);
         throw var10;
      }

      try {
         if (var8 != null) {
            var8.close();
         }

      } catch (XMLStreamException var22) {
         var10 = new IOException(var22.getMessage());
         var10.initCause(var22);
         throw var10;
      }
   }

   private Class getDefinitionClass(ClassLoader var1) {
      Class var2 = null;

      try {
         if (var1 == null) {
            logger.fine("ClassLoader argument is null, will use my classloader.");
            var1 = this.getClass().getClassLoader();
         }

         if (var1 != null) {
            var2 = var1.loadClass("javax.wsdl.Definition");
         } else {
            logger.warning("Unable to load javax.wsdl.Definition, ClassLoader is null.");
         }

         return var2;
      } catch (ClassNotFoundException var4) {
         throw new WebServiceException(var4);
      }
   }

   private void registerExtension(String var1, Object var2) {
      this.registerExtension(var1, var2, Level.WARNING);
   }

   private void registerExtension(String var1, Object var2, Level var3) {
      try {
         ClassLoader var4 = Thread.currentThread().getContextClassLoader();
         Class var5 = var4.loadClass(var1);
         Method var6 = var5.getMethod("registerSerializersAndTypes", var2.getClass());
         var6.invoke((Object)null, var2);
      } catch (Exception var7) {
         logger.log(var3, "Registering " + var1 + " extension failed; " + var7.getClass().getName() + ": " + var7.getMessage());
         throw new WebServiceException(var7);
      }
   }

   public void writeWSDL(Object var1, OutputStream var2) throws IOException {
      Object var3 = this.getWSDLFactory();
      Object var4 = this.invokeMethod(var3, "newWSDLWriter", (Class[])null, (Object[])null);
      Class var5 = this.getDefinitionClass(var1.getClass().getClassLoader());
      if (var5 != null) {
         this.invokeMethod(var4, "writeWSDL", new Class[]{var5, OutputStream.class}, new Object[]{var1, var2});
      }

   }

   public String getDocumentBaseUriFromWSDL(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Class var2 = this.getDefinitionClass(var1.getClass().getClassLoader());
         if (var2 != null) {
            Class[] var3 = new Class[0];
            Object[] var4 = new Object[0];
            Object var5 = this.invokeMethod(var1, "getDocumentBaseURI", var3, var4);
            if (var5 != null) {
               return var5.toString();
            }
         }

         return null;
      }
   }

   private static Map<String, String> queryParametersToMap(String var0) {
      HashMap var1 = new HashMap();
      String[] var2 = var0.split("&");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         int var5 = var4.indexOf(61);
         String var6 = null;
         String var7 = null;
         if (var5 > -1) {
            var6 = var4.substring(0, var5);
            var7 = var4.substring(var5 + 1);
         } else {
            var6 = var4;
         }

         var1.put(var6, var7);
      }

      return var1;
   }

   private Object getWSDLFactory() {
      Object var1 = null;

      try {
         ClassLoader var2 = Thread.currentThread().getContextClassLoader();
         Class var3 = var2.loadClass("javax.wsdl.factory.WSDLFactory");
         Method var4 = var3.getMethod("newInstance", (Class[])null);
         var1 = var4.invoke((Object)null, (Object[])null);
         return var1;
      } catch (NoSuchMethodException var5) {
         throw new WebServiceException(var5);
      } catch (IllegalAccessException var6) {
         throw new WebServiceException(var6);
      } catch (InvocationTargetException var7) {
         throw new WebServiceException(var7);
      } catch (ClassNotFoundException var8) {
         throw new WebServiceException(var8);
      }
   }

   private Object invokeMethod(Object var1, String var2, Class[] var3, Object[] var4) {
      try {
         Method var5 = var1.getClass().getMethod(var2, var3);
         return var5.invoke(var1, var4);
      } catch (NoSuchMethodException var6) {
         throw new WebServiceException(var6);
      } catch (IllegalAccessException var7) {
         throw new WebServiceException(var7);
      } catch (InvocationTargetException var8) {
         throw new WebServiceException(var8);
      }
   }

   static {
      VERSION_COMMENT = " Published by JAX-WS RI at http://jax-ws.dev.java.net. RI's version is " + RuntimeVersion.VERSION + ". ";
      logger = Logger.getLogger(AdvertisementHelperImpl.class.getName());
   }

   private static final class DefnPatcher extends XMLStreamReaderToXMLStreamWriter {
      private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
      private static final QName SCHEMA_INCLUDE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "include");
      private static final QName SCHEMA_IMPORT_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "import");
      private static final QName SCHEMA_REDEFINE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
      private static final Logger logger = Logger.getLogger(DefnPatcher.class.getName());
      private final WSEndpointImpl<?> endpoint;
      private final SDDocumentImpl current;
      private final DocumentAddressResolver resolver;
      private final PortAddressResolver portAddressResolver;
      private String targetNamespace;
      private QName serviceName;
      private QName portName;
      private String portAddress;
      private EPR_ADDRESS_STATE eprAddressState;

      public DefnPatcher(WSEndpointImpl<?> var1, SDDocumentImpl var2, PortAddressResolver var3, DocumentAddressResolver var4) {
         this.eprAddressState = AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.OUT;
         this.endpoint = var1;
         this.current = var2;
         this.portAddressResolver = var3;
         this.resolver = var4;
      }

      protected void handleAttribute(int var1) throws XMLStreamException {
         QName var2 = this.in.getName();
         String var3 = this.in.getAttributeLocalName(var1);
         String var4;
         if ((!var2.equals(SCHEMA_INCLUDE_QNAME) || !var3.equals("schemaLocation")) && (!var2.equals(SCHEMA_IMPORT_QNAME) || !var3.equals("schemaLocation")) && (!var2.equals(SCHEMA_REDEFINE_QNAME) || !var3.equals("schemaLocation")) && (!var2.equals(WSDLConstants.QNAME_IMPORT) || !var3.equals("location"))) {
            if ((var2.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS) || var2.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS)) && var3.equals("location")) {
               this.portAddress = this.in.getAttributeValue(var1);
               var4 = this.getAddressLocation();
               if (var4 != null) {
                  logger.fine("Service:" + this.serviceName + " port:" + this.portName + " current address " + this.portAddress + " Patching it with " + var4);
                  this.writeAttribute(var1, var4);
                  return;
               }
            }

            super.handleAttribute(var1);
         } else {
            var4 = this.in.getAttributeValue(var1);
            String var5 = this.getPatchedImportLocation(var4);
            if (var5 != null) {
               logger.fine("Fixing the relative location:" + var4 + " with absolute location:" + var5);
               this.writeAttribute(var1, var5);
            }
         }
      }

      private void writeAttribute(int var1, String var2) throws XMLStreamException {
         String var3 = this.in.getAttributeNamespace(var1);
         if (var3 != null) {
            this.out.writeAttribute(this.in.getAttributePrefix(var1), var3, this.in.getAttributeLocalName(var1), var2);
         } else {
            this.out.writeAttribute(this.in.getAttributeLocalName(var1), var2);
         }

      }

      protected void handleStartElement() throws XMLStreamException {
         QName var1 = this.in.getName();
         String var2;
         if (var1.equals(WSDLConstants.QNAME_DEFINITIONS)) {
            var2 = this.in.getAttributeValue((String)null, "targetNamespace");
            if (var2 != null) {
               this.targetNamespace = var2;
            }
         } else if (var1.equals(WSDLConstants.QNAME_SERVICE)) {
            var2 = this.in.getAttributeValue((String)null, "name");
            if (var2 != null) {
               this.serviceName = new QName(this.targetNamespace, var2);
            }
         } else if (var1.equals(WSDLConstants.QNAME_PORT)) {
            var2 = this.in.getAttributeValue((String)null, "name");
            if (var2 != null) {
               this.portName = new QName(this.targetNamespace, var2);
            }
         } else if (var1.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME) || var1.equals(MemberSubmissionAddressingConstants.WSA_ADDRESS_QNAME)) {
            this.eprAddressState = AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.IN;
         }

         super.handleStartElement();
      }

      protected void handleEndElement() throws XMLStreamException {
         QName var1 = this.in.getName();
         if (var1.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME) || var1.equals(MemberSubmissionAddressingConstants.WSA_ADDRESS_QNAME)) {
            this.eprAddressState = AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.OUT;
         }

         super.handleEndElement();
      }

      protected void handleCharacters() throws XMLStreamException {
         if (this.eprAddressState == AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.IN) {
            String var1 = this.getAddressLocation();
            if (var1 != null) {
               logger.fine("Fixing EPR Address for service:" + this.serviceName + " port:" + this.portName + " address with " + var1);
               this.out.writeCharacters(var1);
               this.eprAddressState = AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.DONE;
            }
         }

         if (this.eprAddressState != AdvertisementHelperImpl.DefnPatcher.EPR_ADDRESS_STATE.DONE) {
            super.handleCharacters();
         }

      }

      private String getPatchedImportLocation(String var1) {
         try {
            ServiceDefinitionImpl var2 = this.endpoint.getServiceDefinition();

            assert var2 != null;

            URL var3 = new URL(this.current.getURL(), var1);
            SDDocument var4 = var2.getBySystemId(var3);
            return var4 == null ? var1 : this.resolver.getRelativeAddressFor(this.current, var4);
         } catch (MalformedURLException var5) {
            return null;
         }
      }

      private String getAddressLocation() {
         return this.portAddressResolver != null && this.portName != null ? this.portAddressResolver.getAddressFor(this.serviceName, this.portName.getLocalPart(), this.portAddress) : null;
      }

      private static enum EPR_ADDRESS_STATE {
         IN,
         OUT,
         DONE;
      }
   }

   private static final class Http10OutputStream extends ByteArrayBuffer {
      private final WSHTTPConnection con;

      Http10OutputStream(WSHTTPConnection var1) {
         this.con = var1;
      }

      public void close() throws IOException {
         super.close();
         this.con.setContentLengthResponseHeader(this.size());
         OutputStream var1 = this.con.getOutput();
         this.writeTo(var1);
         var1.close();
      }
   }
}
