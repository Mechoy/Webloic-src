package weblogic.xml.jaxp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.EmptyStackException;
import java.util.ListIterator;
import java.util.Stack;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.util.XMLConstants;

public class ChainingEntityResolver implements EntityResolver, XMLConstants {
   private Stack resolvers = new Stack();
   private Stack clonedResolvers;
   private boolean clone = true;
   private RegistryEntityResolver registryResolver;

   public void pushEntityResolver(EntityResolver var1) {
      this.resolvers.push(var1);
      this.clone = true;
      if (var1 instanceof RegistryEntityResolver) {
         this.registryResolver = (RegistryEntityResolver)var1;
      }

   }

   public EntityResolver popEntityResolver() {
      EntityResolver var1 = null;

      try {
         var1 = (EntityResolver)this.resolvers.pop();
         if (var1 instanceof RegistryEntityResolver && var1 == this.registryResolver) {
            this.registryResolver = null;
         }
      } catch (EmptyStackException var3) {
      }

      this.clone = true;
      return var1;
   }

   public int getResolverCount() {
      return this.resolvers.size();
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      InputSource var3 = null;
      if (this.clone) {
         this.clonedResolvers = (Stack)this.resolvers.clone();
      }

      ListIterator var4 = this.clonedResolvers.listIterator(this.resolvers.size());

      while(var4.hasPrevious()) {
         EntityResolver var5 = (EntityResolver)var4.previous();

         try {
            var3 = var5.resolveEntity(var1, var2);
         } catch (IOException var7) {
            throw new SAXException(var7);
         }

         if (var3 != null) {
            return this.getValidInputSource(var5, var3);
         }
      }

      return var3;
   }

   public static String getEntityDescriptor(String var0, String var1) {
      return getEntityDescriptor(var0, var1, (String)null);
   }

   public static String getEntityDescriptor(String var0, String var1, String var2) {
      if (var2 == null) {
         var2 = "";
      }

      String var3 = "publicId = " + var0 + ", systemId = " + var1;
      if (var2 != null && var2.length() > 0) {
         var3 = var3 + ", root = " + var2;
      }

      return var3;
   }

   public EntityResolver peekEntityResolver() {
      return (EntityResolver)this.resolvers.peek();
   }

   private InputSource getValidInputSource(EntityResolver var1, InputSource var2) {
      if (!(var1 instanceof RegistryEntityResolver) && !(var1 instanceof ChainingEntityResolver)) {
         if (this.registryResolver == null) {
            return var2;
         } else if (var2.getByteStream() == null && var2.getCharacterStream() == null) {
            if (var2.getSystemId() == null) {
               return var2;
            } else {
               InputStream var3 = null;

               try {
                  URL var4 = new URL(var2.getSystemId());
                  if (var4.getProtocol().equalsIgnoreCase("http")) {
                     URLConnection var5 = var4.openConnection();
                     var3 = var5.getInputStream();
                     var3.read();
                  }

                  InputSource var25 = var2;
                  return var25;
               } catch (MalformedURLException var21) {
               } catch (IOException var22) {
               } finally {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (IOException var18) {
                     }
                  }

               }

               try {
                  InputSource var24 = this.registryResolver.resolveEntity(var2.getPublicId(), var2.getSystemId());
                  if (var24 != null) {
                     return var24;
                  }
               } catch (SAXException var19) {
               } catch (IOException var20) {
               }

               return var2;
            }
         } else {
            return var2;
         }
      } else {
         return var2;
      }
   }
}
