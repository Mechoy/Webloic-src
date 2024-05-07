package weblogic.management.provider.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import weblogic.descriptor.DescriptorReader;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.ManagementLogger;
import weblogic.management.VersionConstants;

public class ConfigReader extends DescriptorReader {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   public static final String EXECUTE_CONFIG_TRANSLATORS_PROP = "weblogic.configuration.executeConfigurationTranslators";
   private static final boolean executeConfigurationTranslators = getBooleanProperty("weblogic.configuration.executeConfigurationTranslators", false);
   private static final boolean convertSecurityExtensionSchema = getBooleanProperty("weblogic.management.convertSecurityExtensionSchema", false);

   public static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }

   public ConfigReader(InputStream var1) throws XMLStreamException {
      this(var1, (ConfigReaderContext)null);
   }

   public ConfigReader(InputStream var1, ConfigReaderContext var2) throws XMLStreamException {
      super(convert(var1, var2));
      if (var2 != null && var2.isStreamModifed()) {
         this.setModified(true);
      }

      int var3;
      for(var3 = 0; var3 < VersionConstants.NAMESPACE_MAPPING.length; ++var3) {
         this.addNamespaceMapping(VersionConstants.NAMESPACE_MAPPING[var3][0], VersionConstants.NAMESPACE_MAPPING[var3][1]);
      }

      if (convertSecurityExtensionSchema) {
         for(var3 = 0; var3 < VersionConstants.EXTENSION_NAMESPACE_MAPPING.length; ++var3) {
            this.addNamespaceMapping(VersionConstants.EXTENSION_NAMESPACE_MAPPING[var3][0], VersionConstants.EXTENSION_NAMESPACE_MAPPING[var3][1]);
         }
      }

   }

   public static InputStream convert(InputStream var0, ConfigReaderContext var1) throws XMLStreamException {
      try {
         TransformerFactory var2 = TransformerFactory.newInstance();
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         var3.setNamespaceAware(true);
         DocumentBuilder var4 = var3.newDocumentBuilder();
         Document var5 = var4.parse(var0);
         Element var6 = var5.getDocumentElement();
         String var7 = var6.getNamespaceURI();
         if ("http://www.bea.com/ns/weblogic/90/domain".equals(var7) || "http://www.bea.com/ns/weblogic/920/domain".equals(var7)) {
            if (var1 != null) {
               var1.setStreamModified(true);
            }

            convert90(var5);
         }

         if (executeConfigurationTranslators) {
            if (var1 != null) {
               var1.setStreamModified(true);
            }

            convertPluggable(var5);
         }

         ByteArrayOutputStream var8 = new ByteArrayOutputStream();
         DOMSource var9 = new DOMSource(var5);
         StreamResult var10 = new StreamResult(var8);
         Transformer var11 = var2.newTransformer();
         var11.transform(var9, var10);
         byte[] var12 = var8.toByteArray();
         return new ByteArrayInputStream(var12);
      } catch (ParserConfigurationException var13) {
         throw new XMLStreamException(var13.getMessage(), var13);
      } catch (SAXException var14) {
         throw new XMLStreamException(var14.getMessage(), var14);
      } catch (TransformerConfigurationException var15) {
         throw new XMLStreamException(var15.getMessage(), var15);
      } catch (IOException var16) {
         throw new XMLStreamException(var16.getMessage(), var16);
      } catch (TransformerException var17) {
         throw new XMLStreamException(var17.getMessage(), var17);
      }
   }

   private static void convert90(Document var0) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Convert config from 90 to current version");
      }

      NodeList var1 = var0.getElementsByTagName("domain");
      if (var1 != null && var1.getLength() > 0) {
         Node var2 = var1.item(0);
         Node var3 = null;
         Node var4 = null;
         var1 = var2.getChildNodes();

         for(int var5 = 0; var5 < var1.getLength(); ++var5) {
            Node var6 = var1.item(var5);
            if (("name".equals(var6.getLocalName()) || "notes".equals(var6.getLocalName())) && var1.getLength() > var5 + 1) {
               var4 = var1.item(var5 + 1);
            }

            if ("domain-version".equals(var6.getLocalName())) {
               var3 = var6;
            }
         }

         NodeList var15 = var0.getElementsByTagName("log");
         if (var15 != null && var15.getLength() > 0) {
            for(int var16 = 0; var16 < var1.getLength(); ++var16) {
               Node var7 = var15.item(var16);
               Node var8 = null;
               Node var9 = null;
               Node var10 = null;
               Node var11 = null;
               if (var7 != null) {
                  NodeList var12 = var7.getChildNodes();
                  if (var12 != null && var12.getLength() > 0) {
                     for(int var13 = 0; var13 < var12.getLength(); ++var13) {
                        Node var14 = var12.item(var13);
                        if (var14.getLocalName() != null) {
                           if (var14.getLocalName().equals("stdout-severity") && var9 == null) {
                              var9 = var14;
                           }

                           if (var14.getLocalName().equals("log-file-filter") && var8 == null) {
                              var8 = var14;
                           }

                           if (var14.getLocalName().equals("stdout-format") && var10 == null) {
                              var10 = var14;
                           }

                           if (var14.getLocalName().equals("stdout-log-stack") && var11 == null) {
                              var11 = var14;
                           }
                        }
                     }

                     if (var9 != null & var8 != null) {
                        var7.insertBefore(var9, var8);
                     }

                     if (var10 != null & var8 != null) {
                        var7.insertBefore(var10, var8);
                     }

                     if (var11 != null & var8 != null) {
                        var7.insertBefore(var11, var8);
                     }
                  }
               }
            }
         }

         if (var3 == null) {
            Element var17 = var0.createElement("domain-version");
            Text var18 = var0.createTextNode("10.3.0.0");
            Text var19 = var0.createTextNode("\n  ");
            var17.appendChild(var18);
            if (var4 != null) {
               var2.insertBefore(var17, var4);
               var2.insertBefore(var19, var17);
            } else {
               var2.appendChild(var17);
               var2.appendChild(var19);
            }
         }
      }

   }

   private static void convertPluggable(Document var0) {
      ConfigurationTranslator var1 = null;
      Element var2 = var0.getDocumentElement();
      String var3 = var2.getNamespaceURI();
      if (var3 != null) {
         ClassLoader var4 = Thread.currentThread().getContextClassLoader();
         if (var4 == null) {
            var4 = ConfigReader.class.getClassLoader();
         }

         Enumeration var5 = null;

         try {
            var5 = var4.getResources("META-INF/wls-configuration-translators");

            while(var5.hasMoreElements()) {
               URL var6 = (URL)var5.nextElement();
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Run configuration translator " + var6);
               }

               BufferedReader var7 = new BufferedReader(new InputStreamReader(var6.openStream()));

               for(String var8 = var7.readLine(); var8 != null; var8 = var7.readLine()) {
                  Loggable var10;
                  try {
                     var1 = (ConfigurationTranslator)Class.forName(var8).newInstance();
                  } catch (ClassNotFoundException var11) {
                     var10 = ManagementLogger.logErrorLoadingConfigTranslatorLoggable(var8, var11.toString());
                     var10.log();
                     continue;
                  } catch (InstantiationException var12) {
                     var10 = ManagementLogger.logErrorLoadingConfigTranslatorLoggable(var8, var12.toString());
                     var10.log();
                     continue;
                  } catch (IllegalAccessException var13) {
                     var10 = ManagementLogger.logErrorLoadingConfigTranslatorLoggable(var8, var13.toString());
                     var10.log();
                     continue;
                  } catch (ClassCastException var14) {
                     var10 = ManagementLogger.logErrorLoadingConfigTranslatorLoggable(var8, var14.toString());
                     var10.log();
                     continue;
                  }

                  if (var3.equals(var1.sourceNamespace()) && "http://www.bea.com/ns/weblogic/920/domain".equals(var1.targetNamespace())) {
                     var1.translate(var0);
                  }
               }
            }
         } catch (IOException var15) {
            ManagementLogger.logExceptionDuringConfigTranslation(var15);
         }

      }
   }
}
