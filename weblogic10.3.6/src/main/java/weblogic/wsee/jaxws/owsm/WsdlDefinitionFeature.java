package weblogic.wsee.jaxws.owsm;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSDLLocator;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.wsdl.parser.MetaDataResolver;
import com.sun.xml.ws.api.wsdl.parser.MetadataResolverFactory;
import com.sun.xml.ws.api.wsdl.parser.ServiceDescriptor;
import com.sun.xml.ws.resources.ClientMessages;
import com.sun.xml.ws.util.ServiceFinder;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelper;
import weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelperFactory;

public class WsdlDefinitionFeature extends WebServiceFeature {
   private static final String ID = WsdlDefinitionFeature.class.getName();
   private Object definition = null;

   public WsdlDefinitionFeature(Object var1) {
      this.definition = var1;
      this.enabled = true;
   }

   public String getID() {
      return ID;
   }

   public Object getDefinition() {
      return this.definition;
   }

   public static boolean required() {
      AdvertisementHelper var0 = AdvertisementHelperFactory.getAdvertisementHelper();
      return var0 != null;
   }

   public static WsdlDefinitionFeature create(Object var0, Class<? extends Service> var1, EntityResolver var2) {
      assert var2 != null;

      return var0 != null ? new WsdlDefinitionFeature(var0) : null;
   }

   public static WsdlDefinitionFeature create(URL var0, Source var1, Class<? extends Service> var2, EntityResolver var3) throws IOException, SAXException {
      assert var3 != null;

      Object var4 = null;
      var4 = readDefinition(var0, var1, var2, var3);
      return var4 != null ? new WsdlDefinitionFeature(var4) : null;
   }

   public static Object readDefinition(URL var0, Source var1, Class<? extends Service> var2, EntityResolver var3) throws IOException, SAXException {
      Object var4 = null;
      AdvertisementHelper var5 = AdvertisementHelperFactory.getAdvertisementHelper();
      if (var5 != null) {
         try {
            InputSource var6 = resolveWSDL(var0, var1, var2, var3);
            if (var6.getSystemId() != null) {
               String var10 = var6.getSystemId();
               URL var8 = new URL(var10);
               var4 = var5.readWSDL(var8, var6);
            } else {
               var4 = var5.readWSDL(var0, var6);
            }
         } catch (Exception var9) {
            if (var0 == null) {
               throw new WebServiceException(var9);
            }

            InputSource var7 = tryWithMex(var0, var3, var9, var2);
            var4 = var5.readWSDL(var0, var7);
         }
      }

      return var4;
   }

   private static InputSource resolveWSDL(@Nullable URL var0, @NotNull Source var1, Class var2, EntityResolver var3) throws IOException, SAXException {
      String var4 = var1.getSystemId();
      InputSource var5 = var3.resolveEntity((String)null, var4);
      if (var5 == null && var0 != null) {
         String var6 = var0.toExternalForm();
         var5 = var3.resolveEntity((String)null, var6);
         if (var5 == null && var2 != null) {
            URL var7 = var2.getResource(".");
            if (var7 != null) {
               String var8 = var7.toExternalForm();
               if (var6.startsWith(var8)) {
                  var5 = var3.resolveEntity((String)null, var6.substring(var8.length()));
               }
            }
         }
      }

      if (var5 == null) {
         InputStream var9 = null;
         if (var1 != null) {
            var9 = createReader(var1);
         } else if (var0 != null) {
            var9 = createReader(var0, var2);
         }

         if (var9 != null) {
            var5 = new InputSource(var9);
         }
      }

      return var5;
   }

   private static InputStream createReader(@NotNull Source var0) throws MalformedURLException, IOException {
      InputStream var1 = null;
      if (var0 instanceof StreamSource) {
         StreamSource var2 = (StreamSource)var0;
         var1 = var2.getInputStream();
         if (var1 == null) {
            var1 = (new URL(var0.getSystemId())).openStream();
         }
      }

      return var1;
   }

   private static InputStream createReader(URL var0, Class<Service> var1) throws IOException {
      InputStream var2 = null;

      try {
         var2 = var0.openStream();
      } catch (IOException var9) {
         if (var1 != null) {
            WSDLLocator var4 = (WSDLLocator)ContainerResolver.getInstance().getContainer().getSPI(WSDLLocator.class);
            if (var4 != null) {
               String var5 = var0.toExternalForm();
               URL var6 = var1.getResource(".");
               String var7 = var0.getPath();
               if (var6 != null) {
                  String var8 = var6.toExternalForm();
                  if (var5.startsWith(var8)) {
                     var7 = var5.substring(var8.length());
                  }
               }

               var0 = var4.locateWSDL(var1, var7);
               if (var0 != null) {
                  var2 = var0.openStream();
                  return var2;
               }
            }
         }

         throw var9;
      }

      return var2;
   }

   private static InputSource tryWithMex(@NotNull URL var0, @NotNull EntityResolver var1, Throwable var2, Class var3) {
      ArrayList var4 = new ArrayList();

      try {
         InputSource var5 = parseUsingMex(var0, var1, var3);
         if (var5 == null) {
            throw new WebServiceException(ClientMessages.FAILED_TO_PARSE(var0.toExternalForm(), var2.getMessage()), var2);
         } else {
            return var5;
         }
      } catch (Exception var6) {
         var4.add(var2);
         var4.add(var6);
         throw new InaccessibleWSDLException(var4);
      }
   }

   private static InputSource parseUsingMex(@NotNull URL var0, @NotNull EntityResolver var1, Class var2) throws IOException, SAXException, URISyntaxException {
      MetaDataResolver var3 = null;
      ServiceDescriptor var4 = null;
      InputSource var5 = null;
      Iterator var6 = ServiceFinder.find(MetadataResolverFactory.class).iterator();

      while(var6.hasNext()) {
         MetadataResolverFactory var7 = (MetadataResolverFactory)var6.next();
         var3 = var7.metadataResolver(var1);
         var4 = var3.resolve(var0.toURI());
         if (var4 != null) {
            break;
         }
      }

      if (var4 != null) {
         var6 = var4.getWSDLs().iterator();

         while(var6.hasNext()) {
            Source var9 = (Source)var6.next();
            var5 = resolveWSDL((URL)null, var9, (Class)null, var1);
            if (var5 != null) {
               break;
            }
         }
      }

      if ((var3 == null || var4 == null) && (var0.getProtocol().equals("http") || var0.getProtocol().equals("https")) && var0.getQuery() == null) {
         String var8 = var0.toExternalForm();
         var8 = var8 + "?wsdl";
         var0 = new URL(var8);
         var5 = resolveWSDL(var0, new StreamSource(var0.toExternalForm()), var2, var1);
      }

      return var5;
   }

   public static void writeWSDL(Object var0, OutputStream var1) throws IOException {
      AdvertisementHelper var2 = AdvertisementHelperFactory.getAdvertisementHelper();
      if (var2 != null) {
         var2.writeWSDL(var0, var1);
      }

   }

   public static String getDocumentBaseUriFromWSDL(Object var0) {
      AdvertisementHelper var1 = AdvertisementHelperFactory.getAdvertisementHelper();
      return var1 != null ? var1.getDocumentBaseUriFromWSDL(var0) : null;
   }
}
