package weblogic.wsee.jaxws.management;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.model.JavaMethod;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.ws.api.wsdl.parser.XMLEntityResolver;
import com.sun.xml.ws.model.RuntimeModeler;
import com.sun.xml.ws.model.wsdl.WSDLModelImpl;
import com.sun.xml.ws.model.wsdl.WSDLPortImpl;
import com.sun.xml.ws.resources.ServerMessages;
import com.sun.xml.ws.server.ServerRtException;
import com.sun.xml.ws.util.ServiceConfigurationError;
import com.sun.xml.ws.util.ServiceFinder;
import com.sun.xml.ws.wsdl.parser.RuntimeWSDLParser;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServiceProvider;
import org.xml.sax.SAXException;
import weblogic.security.service.WebServiceResource;
import weblogic.wsee.jaxws.handler.BindingIdTranslator;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlImport;
import weblogic.wsee.wsdl.WsdlSchema;

public class ManagementUtils {
   static boolean verbose = Verbose.isVerbose(ManagementUtils.class);

   public static WSDLPort generateWSDL(WsdlDefinitions var0, @NotNull QName var1, @NotNull QName var2) {
      String var3 = null;
      Object var4 = new ArrayList();
      if (var0 != null) {
         var3 = var0.getWsdlLocation();
         var4 = getImportedWsdlsAndSchemas(var0);
      }

      SDDocumentSource var5 = createSDDocumentSource(var3);
      return generateWSDL(var5, (List)var4, var1, var2);
   }

   public static WSDLPort generateWSDL(SDDocumentSource var0, List<? extends SDDocumentSource> var1, @NotNull QName var2, @NotNull QName var3) {
      URL var4 = var0.getSystemId();

      try {
         WSDLModelImpl var5 = RuntimeWSDLParser.parse(new XMLEntityResolver.Parser(var0), new EntityResolverImpl(var1), false, ContainerResolver.getInstance().getContainer(), (WSDLParserExtension[])ServiceFinder.find(WSDLParserExtension.class).toArray());
         WSDLPortImpl var6 = var5.getService(var2).get(var3);
         if (var6 == null) {
            throw new ServerRtException(ServerMessages.localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICEPORT(var2, var3, var4));
         } else {
            return var6;
         }
      } catch (IOException var7) {
         throw new ServerRtException("runtime.parser.wsdl", new Object[]{var4, var7});
      } catch (XMLStreamException var8) {
         throw new ServerRtException("runtime.saxparser.exception", new Object[]{var8.getMessage(), var8.getLocation(), var8});
      } catch (SAXException var9) {
         throw new ServerRtException("runtime.parser.wsdl", new Object[]{var4, var9});
      } catch (ServiceConfigurationError var10) {
         throw new ServerRtException("runtime.parser.wsdl", new Object[]{var4, var10});
      }
   }

   public static BindingID parseBindingId(String var0) {
      var0 = BindingIdTranslator.translate(var0);
      if (StringUtil.isEmpty(var0)) {
         var0 = "http://schemas.xmlsoap.org/wsdl/soap/http";
      }

      return BindingID.parse(var0);
   }

   public static SEIModel generateModel(WSDLPort var0, Class<?> var1, @NotNull QName var2, @NotNull QName var3, BindingID var4) {
      if (var1.getAnnotation(WebService.class) != null && var1.getAnnotation(WebServiceProvider.class) == null) {
         RuntimeModeler var5;
         if (var0 == null) {
            var5 = new RuntimeModeler(var1, var2, var4);
         } else {
            var5 = new RuntimeModeler(var1, var2, (WSDLPortImpl)var0, new WebServiceFeature[0]);
         }

         var5.setPortName(var3);
         var5.setClassLoader(var1.getClassLoader());
         return var5.buildRuntimeModel();
      } else {
         return null;
      }
   }

   public static Map<JavaMethod, WebServiceResource> generateResources(String var0, String var1, SEIModel var2) {
      HashMap var3 = new HashMap();
      if (var2 != null) {
         String var4 = var2.getPortName().getLocalPart();
         Iterator var5 = var2.getJavaMethods().iterator();

         while(var5.hasNext()) {
            JavaMethod var6 = (JavaMethod)var5.next();
            Method var7 = var6.getMethod();
            String var8 = var7.getName();
            Class[] var9 = var7.getParameterTypes();
            String[] var10 = new String[var9.length];

            for(int var11 = 0; var11 < var9.length; ++var11) {
               var10[var11] = var9[var11].getCanonicalName();
            }

            var3.put(var6, new WebServiceResource(var0, var1, var4, var8, var10));
         }
      }

      return var3;
   }

   private static List<SDDocumentSource> getImportedWsdlsAndSchemas(WsdlDefinitions var0) {
      HashSet var1 = new HashSet();
      buildImportedWsdls(var0, var1);
      buildImportedSchemas(var0, var1);
      return new ArrayList(var1);
   }

   private static void buildImportedSchemas(WsdlDefinitions var0, Collection<SDDocumentSource> var1) {
      if (var0.getTypes() != null && var0.getTypes().getImportedWsdlSchemas() != null) {
         List var2 = var0.getTypes().getImportedWsdlSchemas();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            WsdlSchema var4 = (WsdlSchema)var3.next();
            SDDocumentSource var5 = createSDDocumentSource(var4.getLocationUrl());
            if (var5 != null) {
               var1.add(var5);
            }
         }
      }

   }

   private static void buildImportedWsdls(WsdlDefinitions var0, Collection<SDDocumentSource> var1) {
      List var2 = var0.getImports();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         WsdlImport var4 = (WsdlImport)var3.next();
         String var5 = var4.getDefinitions().getWsdlLocation();
         SDDocumentSource var6 = createSDDocumentSource(var5);
         if (var6 != null) {
            var1.add(var6);
         }
      }

   }

   private static SDDocumentSource createSDDocumentSource(String var0) {
      SDDocumentSource var1 = null;
      URL var2 = null;
      if (var0 != null) {
         try {
            var2 = new URL(var0);
         } catch (MalformedURLException var4) {
            throw new WebServiceException(var4);
         }
      }

      if (verbose) {
         Verbose.log((Object)("Constructing SDDocumentSource :" + var2));
      }

      if (var2 != null) {
         var1 = SDDocumentSource.create(var2);
      }

      return var1;
   }

   private static final class EntityResolverImpl implements XMLEntityResolver {
      private Map<String, SDDocumentSource> metadata = new HashMap();

      public EntityResolverImpl(List<? extends SDDocumentSource> var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            SDDocumentSource var3 = (SDDocumentSource)var2.next();
            this.metadata.put(var3.getSystemId().toExternalForm(), var3);
         }

      }

      public XMLEntityResolver.Parser resolveEntity(String var1, String var2) throws IOException, XMLStreamException {
         if (var2 != null) {
            SDDocumentSource var3 = (SDDocumentSource)this.metadata.get(var2);
            if (var3 != null) {
               return new XMLEntityResolver.Parser(var3);
            }
         }

         return null;
      }
   }
}
