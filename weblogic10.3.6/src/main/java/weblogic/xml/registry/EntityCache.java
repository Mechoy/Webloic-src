package weblogic.xml.registry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import org.xml.sax.InputSource;
import weblogic.xml.XMLLogger;
import weblogic.xml.util.InputSourceUtil;
import weblogic.xml.util.XMLConstants;
import weblogic.xml.util.cache.entitycache.CX;
import weblogic.xml.util.cache.entitycache.CacheEntry;

public final class EntityCache implements XMLConstants {
   XMLRegistry registry = null;
   weblogic.xml.util.cache.entitycache.EntityCache underlyingCache = null;
   private weblogic.xml.util.cache.entitycache.EntityCache cache = null;

   public EntityCache(XMLRegistry var1, weblogic.xml.util.cache.entitycache.EntityCache var2) {
      this.registry = var1;
      this.underlyingCache = var2;
   }

   private weblogic.xml.util.cache.entitycache.EntityCache getCache() throws XMLRegistryException {
      return this.underlyingCache;
   }

   public void setMemorySize(int var1) throws XMLRegistryException {
      if (this.underlyingCache != null) {
         try {
            this.underlyingCache.setMemoryFootprint((long)var1);
         } catch (CX.EntityCacheException var3) {
            throw new XMLRegistryException("Can't set Cache Memory Size", var3);
         }
      }
   }

   public void setDiskSize(int var1) throws XMLRegistryException {
      if (this.underlyingCache != null) {
         try {
            this.underlyingCache.setDiskFootprint((long)var1);
         } catch (CX.EntityCacheException var3) {
            throw new XMLRegistryException("Can't set Cache Disk Size", var3);
         }
      }
   }

   public void add(InputSource var1, boolean var2, int var3) throws XMLRegistryException, CX.EntryTooLarge {
      if (this.underlyingCache != null) {
         CacheEntry var4 = null;
         Object var5 = null;
         CacheKey var6 = null;

         try {
            byte[] var12 = InputSourceUtil.forceGetInputByteData(var1);
            var6 = new CacheKey(this.registry.getName(), var1.getPublicId(), var1.getSystemId());
            var4 = new CacheEntry(this.getCache(), var6, var12, (long)var12.length, (Object)null, (long)var3);
            this.getCache().addEntry(var6, var4, var2);
         } catch (IOException var8) {
            throw new XMLRegistryException(var8);
         } catch (CX.EntryTooLarge var9) {
            XMLLogger.logEntityCacheRejection("" + var6, String.valueOf(((Object[])var5).length), String.valueOf(this.getCache().getMemoryFootprint()));
            throw var9;
         } catch (CX.EntityCacheException var10) {
            throw new XMLRegistryException(var10);
         } catch (Exception var11) {
            throw new XMLRegistryException(var11);
         }
      }
   }

   public InputSource get(String var1, String var2) throws XMLRegistryException, XMLRegistryExceptionCacheEntryExpired {
      if (this.underlyingCache == null) {
         return null;
      } else {
         Object var3 = null;
         byte[] var4 = null;
         CacheKey var5 = null;

         try {
            if (var1 != null) {
               var5 = new CacheKey(this.registry.getName(), var1, (String)null);
               var4 = (byte[])((byte[])this.getCache().getData(var5));
            }

            if (var4 == null && var2 != null) {
               var5 = new CacheKey(this.registry.getName(), (String)null, var2);
               var4 = (byte[])((byte[])this.getCache().getData(var5));
            }
         } catch (CX.EntryExpired var7) {
            throw new XMLRegistryExceptionCacheEntryExpired(var7);
         } catch (CX.EntityCacheException var8) {
            throw new XMLRegistryException("Failure getting: " + var5 + " from cache.", var8);
         }

         return var4 == null ? null : this.getInputSource(var1, var2, var4);
      }
   }

   public void renew(String var1, String var2) throws XMLRegistryException {
      if (this.underlyingCache != null) {
         CacheKey var3 = null;

         try {
            CacheEntry var4 = null;
            if (var1 != null) {
               var3 = new CacheKey(this.registry.getName(), var1, (String)null);
               var4 = this.getCache().renewLease(var3);
            }

            if (var4 == null && var2 != null) {
               var3 = new CacheKey(this.registry.getName(), (String)null, var2);
               this.getCache().renewLease(var3);
            }

         } catch (CX.EntityCacheException var5) {
            throw new XMLRegistryException("Failure renewing: " + var3, var5);
         }
      }
   }

   public void remove(String var1, String var2) throws XMLRegistryException {
      if (this.underlyingCache != null) {
         CacheKey var3 = null;

         try {
            CacheEntry var4 = null;
            if (var1 != null) {
               var3 = new CacheKey(this.registry.getName(), var1, (String)null);
               var4 = this.getCache().removeEntry(var3);
            }

            if (var4 == null && var2 != null) {
               var3 = new CacheKey(this.registry.getName(), (String)null, var2);
               this.getCache().removeEntry(var3);
            }

         } catch (CX.EntityCacheException var5) {
            throw new XMLRegistryException("Failure removing: " + var3, var5);
         }
      }
   }

   public void putrify(String var1, String var2) throws XMLRegistryException {
      if (this.underlyingCache != null) {
         CacheKey var3 = null;

         try {
            CacheEntry var4 = null;
            if (var1 != null) {
               var3 = new CacheKey(this.registry.getName(), var1, (String)null);
               var4 = this.getCache().putrify(var3);
            }

            if (var4 == null && var2 != null) {
               var3 = new CacheKey(this.registry.getName(), (String)null, var2);
               this.getCache().putrify(var3);
            }

         } catch (CX.EntityCacheException var5) {
            throw new XMLRegistryException("Failure putrifying: " + var3, var5);
         }
      }
   }

   protected InputSource getInputSource(String var1, String var2, byte[] var3) {
      InputSource var4 = new InputSource();
      var4.setPublicId(var1);
      var4.setSystemId(var2);
      var4.setByteStream(new ByteArrayInputStream(var3));
      return var4;
   }

   String getDescription(Serializable var1) {
      if (var1 == null) {
         return "UNKNOWN (KEY NOT SET)";
      } else {
         String var2 = var1.toString();
         return var2.substring(var2.indexOf(":"));
      }
   }
}
