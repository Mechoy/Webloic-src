package weblogic.xml.registry;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import weblogic.xml.XMLLogger;
import weblogic.xml.jaxp.ReParsingStatus;
import weblogic.xml.util.InputSourceUtil;
import weblogic.xml.util.Tools;
import weblogic.xml.util.cache.entitycache.CX;

public class RegistryEntityResolver implements EntityResolver {
   XMLRegistry[] registryPath = null;
   private ReParsingStatus status;

   RegistryEntityResolver(XMLRegistry[] var1) {
      this.registryPath = var1;
   }

   public XMLRegistry[] getRegistryPath() {
      try {
         return XMLRegistry.getXMLRegistryPath();
      } catch (XMLRegistryException var2) {
         XMLLogger.logStackTrace("XMLRegistryException on the path.", var2);
         return this.registryPath;
      }
   }

   public RegistryEntityResolver() throws XMLRegistryException {
      this.registryPath = XMLRegistry.getXMLRegistryPath();
   }

   public boolean hasDocumentSpecificParserEntries() {
      for(int var1 = 0; var1 < this.registryPath.length; ++var1) {
         if (this.registryPath[var1].hasDocumentSpecificParserEntries()) {
            return true;
         }
      }

      return false;
   }

   public boolean hasDocumentSpecificEntityEntries() {
      for(int var1 = 0; var1 < this.registryPath.length; ++var1) {
         if (this.registryPath[var1].hasDocumentSpecificEntityEntries()) {
            return true;
         }
      }

      return false;
   }

   public TransformerFactory getTransformerFactory() throws XMLRegistryException {
      TransformerFactory var1 = null;

      for(int var2 = 0; var2 < this.registryPath.length; ++var2) {
         var1 = this.registryPath[var2].getTransformerFactory();
         if (var1 != null) {
            break;
         }
      }

      return var1;
   }

   public TransformerFactory getTransformerFactory(String var1, String var2, String var3) throws XMLRegistryException {
      if (var1 == null && var2 == null && var3 == null) {
         return null;
      } else {
         TransformerFactory var4 = null;

         for(int var5 = 0; var5 < this.registryPath.length; ++var5) {
            var4 = this.registryPath[var5].getTransformerFactory(var1, var2, var3);
            if (var4 != null) {
               break;
            }
         }

         return var4;
      }
   }

   public DocumentBuilderFactory getDocumentBuilderFactory() throws XMLRegistryException {
      DocumentBuilderFactory var1 = null;

      for(int var2 = 0; var2 < this.registryPath.length; ++var2) {
         var1 = this.registryPath[var2].getDocumentBuilderFactory();
         if (var1 != null) {
            break;
         }
      }

      return var1;
   }

   public DocumentBuilderFactory getDocumentBuilderFactory(String var1, String var2, String var3) throws XMLRegistryException {
      if (var1 == null && var2 == null && var3 == null) {
         return null;
      } else {
         DocumentBuilderFactory var4 = null;

         for(int var5 = 0; var5 < this.getRegistryPath().length; ++var5) {
            var4 = this.registryPath[var5].getDocumentBuilderFactory(var1, var2, var3);
            if (var4 != null) {
               break;
            }
         }

         return var4;
      }
   }

   public Parser getParser(String var1, String var2, String var3) throws XMLRegistryException {
      Parser var4 = null;

      for(int var5 = 0; var5 < this.registryPath.length; ++var5) {
         var4 = this.registryPath[var5].getParser(var1, var2, var3);
         if (var4 != null) {
            break;
         }
      }

      return var4;
   }

   public SAXParserFactory getSAXParserFactory(String var1, String var2, String var3) throws XMLRegistryException {
      if (var1 == null && var2 == null && var3 == null) {
         return null;
      } else {
         SAXParserFactory var4 = null;

         for(int var5 = 0; var5 < this.registryPath.length; ++var5) {
            var4 = this.registryPath[var5].getSAXParserFactory(var1, var2, var3);
            if (var4 != null) {
               break;
            }
         }

         return var4;
      }
   }

   public SAXParserFactory getSAXParserFactory() throws XMLRegistryException {
      SAXParserFactory var1 = null;

      for(int var2 = 0; var2 < this.registryPath.length; ++var2) {
         var1 = this.registryPath[var2].getSAXParserFactory();
         if (var1 != null) {
            break;
         }
      }

      return var1;
   }

   public String getHandleEntityInvalidation(String var1, String var2) throws XMLRegistryException {
      if (var1 == null && var2 == null) {
         return null;
      } else {
         String var3 = null;

         for(int var4 = 0; var4 < this.getRegistryPath().length; ++var4) {
            if (this.registryPath[var4].hasHandleEntityInvalidationSetSupport()) {
               var3 = this.registryPath[var4].getHandleEntityInvalidation(var1, var2);
               if (var3 != null) {
                  break;
               }
            }
         }

         return var3;
      }
   }

   public String getHandleEntityInvalidation() throws XMLRegistryException {
      String var1 = null;

      for(int var2 = 0; var2 < this.getRegistryPath().length; ++var2) {
         if (this.registryPath[var2].hasHandleEntityInvalidationSetSupport()) {
            var1 = this.registryPath[var2].getHandleEntityInvalidation();
            if (var1 != null) {
               break;
            }
         }
      }

      return var1;
   }

   public boolean hasHandleEntityInvalidationSetSupport() {
      for(int var1 = 0; var1 < this.getRegistryPath().length; ++var1) {
         if (this.registryPath[var1].hasHandleEntityInvalidationSetSupport()) {
            return true;
         }
      }

      return false;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      boolean var3 = this.status == null;
      if (var3) {
         this.status = new ReParsingStatus();
      }

      InputSource var4;
      try {
         this.status.usedRegistryResolver = true;
         if (this.status.lastEntity != null) {
            InputSourceUtil.resetInputSource(this.status.lastEntity);
         }

         var4 = this.status.lastEntity = this.resolveEntityI(var1, var2, true);
      } finally {
         if (var3) {
            this.status = null;
         }

      }

      return var4;
   }

   private InputSource resolveEntityI(String var1, String var2, boolean var3) throws SAXException, IOException {
      InputSource var4 = null;
      String var5 = Tools.getEntityDescriptor(var1, var2, (String)null);
      this.status.usedCache = true;

      for(int var6 = 0; var6 < this.registryPath.length; ++var6) {
         XMLRegistry var7 = this.registryPath[var6];
         ResolveContext var8 = new ResolveContext();
         var8.desc = var5;

         try {
            var4 = this.getEntityFromCache(var7, var1, var2, false, var8);
            if (var4 == null && var8.expired && this.status.isReParsing()) {
               var4 = this.getEntityFromCache(var7, var1, var2, true, var8);
            }

            if (!this.status.isReParsing() && var4 == null || this.status.isReParsing() && InputSourceUtil.isEqual(var4, this.status.lastEntity)) {
               RefreshCacheLock var9 = var7.getLock();

               try {
                  var9.lock(var1, var2);
                  var4 = this.getEntityFromCache(var7, var1, var2, false, var8);
                  if (var4 == null && var8.expired && this.status.isReParsing()) {
                     var4 = this.getEntityFromCache(var7, var1, var2, true, var8);
                  }

                  if (!this.status.isReParsing() && var4 == null || this.status.isReParsing() && InputSourceUtil.isEqual(var4, this.status.lastEntity)) {
                     XMLRegistry.ResolvedEntity var10 = this.getEntityFromNonCache(var7, var1, var2, var8);
                     var4 = var10 != null ? var10.inputSource() : null;
                     if (var4 != null) {
                        this.status.usedCache = false;
                        if (var3 && var10.isSubjectToCaching()) {
                           this.addToCache(var7, var10, var1, var2, var8);
                        }
                     }
                  }
               } finally {
                  var9.unlock(var1, var2);
               }
            }

            if (var4 == null && var8.expired) {
               var4 = this.getEntityFromCache(var7, var1, var2, true, var8);
            }

            if (var4 == null && var8.remoteAccessException != null) {
               throw var8.remoteAccessException;
            }
         } catch (XMLRegistryException var17) {
            throw new SAXException(var17);
         } catch (RuntimeException var18) {
            throw var18;
         }

         if (var4 != null) {
            break;
         }
      }

      if (this.status.isReParsing() && InputSourceUtil.isEqual(var4, this.status.lastEntity)) {
         throw new ReParsingEntityNotChangedException();
      } else {
         return var4;
      }
   }

   private InputSource getEntityFromCache(XMLRegistry var1, String var2, String var3, boolean var4, ResolveContext var5) throws XMLRegistryException, IOException {
      InputSource var6 = null;
      EntityCache var7 = var1.getCache();
      if (var7 == null) {
         return var6;
      } else {
         if (var4) {
            var7.renew(var2, var3);
         }

         try {
            var6 = var7.get(var2, var3);
            InputSourceUtil.transformInputSource(var6);
         } catch (XMLRegistryExceptionCacheEntryExpired var9) {
            var5.expired = true;
         } catch (XMLRegistryException var10) {
            XMLLogger.logEntityCacheBroken();
         }

         return var6;
      }
   }

   private XMLRegistry.ResolvedEntity getEntityFromNonCache(XMLRegistry var1, String var2, String var3, ResolveContext var4) throws XMLRegistryException, IOException {
      try {
         XMLRegistry.ResolvedEntity var5 = var1.getEntity(var2, var3);
         if (var5 != null) {
            InputSourceUtil.transformInputSource(var5.inputSource());
         }

         return var5;
      } catch (XMLRegistryRemoteAccessException var6) {
         var4.remoteAccessException = var6;
         return null;
      }
   }

   private void addToCache(XMLRegistry var1, XMLRegistry.ResolvedEntity var2, String var3, String var4, ResolveContext var5) throws IllegalArgumentException, IOException {
      try {
         EntityCache var6 = var1.getCache();
         if (var6 == null) {
            return;
         }

         InputSource var7 = var2.inputSource();
         int var8 = var1.getCacheTimeoutInterval(var2.getEntry());
         boolean var9 = true;
         if (var2.isLocal()) {
            var9 = false;
         } else {
            var9 = true;
         }

         try {
            var6.add(var7, var9, var8 * 1000);
         } catch (OutOfMemoryError var18) {
         } catch (CX.EntryTooLarge var19) {
         } catch (XMLRegistryException var20) {
            XMLLogger.logEntityCacheBroken();
         } finally {
            InputSourceUtil.resetInputSource(var7);
         }
      } catch (XMLRegistryException var22) {
      }

   }

   public void hookStatus(ReParsingStatus var1) {
      this.status = var1;
   }

   private class ResolveContext {
      public String desc;
      public boolean expired;
      public XMLRegistryRemoteAccessException remoteAccessException;

      private ResolveContext() {
         this.desc = null;
         this.expired = false;
         this.remoteAccessException = null;
      }

      // $FF: synthetic method
      ResolveContext(Object var2) {
         this();
      }
   }
}
