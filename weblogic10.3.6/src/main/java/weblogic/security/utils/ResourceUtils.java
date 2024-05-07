package weblogic.security.utils;

import com.bea.common.security.SecurityLogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import weblogic.management.security.ResourceIdInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.spi.Resource;

public final class ResourceUtils {
   public static String[] listRegisteredResourceTypes() {
      return weblogic.security.providers.utils.ResourceUtils.listRegisteredResourceTypes();
   }

   public static void registerResourceType(AuthenticatedSubject var0, ResourceIdInfo var1) throws IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException(SecurityLogger.getNoResourceType());
      } else {
         Class var2 = weblogic.security.providers.utils.ResourceIdInfo.class;
         weblogic.security.providers.utils.ResourceIdInfo var3 = (weblogic.security.providers.utils.ResourceIdInfo)Proxy.newProxyInstance(var2.getClassLoader(), new Class[]{ResourceIdInfo.class, var2}, new InvocationHandlerImpl(var1));
         weblogic.security.providers.utils.ResourceUtils.registerResourceType(var0, var3);
      }
   }

   public static String getResourceIdFromMap(Map var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getResourceIdFromMap(var0);
   }

   public static Map getMapFromResourceId(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getMapFromResourceId(var0);
   }

   public static String[] getResourceKeyNames(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getResourceKeyNames(var0);
   }

   public static String[] getParentResourceIds(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getParentResourceIds(var0);
   }

   public static String getResourceTypeNameFilter(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getResourceTypeNameFilter(var0);
   }

   public static SearchHelper getApplicationSearchHelper(String var0) throws IllegalArgumentException {
      return (SearchHelper)Proxy.newProxyInstance(SearchHelper.class.getClassLoader(), new Class[]{weblogic.security.providers.utils.ResourceUtils.SearchHelper.class, SearchHelper.class}, new InvocationHandlerImpl(weblogic.security.providers.utils.ResourceUtils.getApplicationSearchHelper(var0)));
   }

   public static SearchHelper getComponentSearchHelper(String var0, String var1, String var2) throws IllegalArgumentException {
      return (SearchHelper)Proxy.newProxyInstance(SearchHelper.class.getClassLoader(), new Class[]{weblogic.security.providers.utils.ResourceUtils.SearchHelper.class, SearchHelper.class}, new InvocationHandlerImpl(weblogic.security.providers.utils.ResourceUtils.getComponentSearchHelper(var0, var1, var2)));
   }

   public static SearchHelper getChildSearchHelper(String var0) throws IllegalArgumentException {
      return (SearchHelper)Proxy.newProxyInstance(SearchHelper.class.getClassLoader(), new Class[]{weblogic.security.providers.utils.ResourceUtils.SearchHelper.class, SearchHelper.class}, new InvocationHandlerImpl(weblogic.security.providers.utils.ResourceUtils.getChildSearchHelper(var0)));
   }

   public static ResourceIdInfo getResourceIdInfo(String var0) {
      weblogic.security.providers.utils.ResourceIdInfo var1 = weblogic.security.providers.utils.ResourceUtils.getResourceIdInfo(var0);
      return var1 == null ? null : (ResourceIdInfo)Proxy.newProxyInstance(ResourceIdInfo.class.getClassLoader(), new Class[]{weblogic.security.providers.utils.ResourceIdInfo.class, ResourceIdInfo.class}, new InvocationHandlerImpl(var1));
   }

   public static SearchHelper getRepeatingActionsSearchHelper(String var0) throws IllegalArgumentException {
      weblogic.security.providers.utils.ResourceUtils.SearchHelper var1 = weblogic.security.providers.utils.ResourceUtils.getRepeatingActionsSearchHelper(var0);
      return var1 == null ? null : (SearchHelper)Proxy.newProxyInstance(SearchHelper.class.getClassLoader(), new Class[]{weblogic.security.providers.utils.ResourceUtils.SearchHelper.class, SearchHelper.class}, new InvocationHandlerImpl(var1));
   }

   public static String getResourceIdNameFilter(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getResourceIdNameFilter(var0);
   }

   public static Resource getScopedResource(String var0) throws IllegalArgumentException {
      return weblogic.security.providers.utils.ResourceUtils.getScopedResource(var0);
   }

   public static String escapeSearchChars(String var0) {
      return weblogic.security.providers.utils.ResourceUtils.escapeSearchChars(var0);
   }

   public static String unescapeChars(String var0) {
      return weblogic.security.providers.utils.ResourceUtils.unescapeChars(var0);
   }

   public interface SearchHelper {
      String getNameFilter();

      boolean isValid(String var1);
   }

   private static class InvocationHandlerImpl implements InvocationHandler {
      private Object delegate;

      private InvocationHandlerImpl(Object var1) {
         this.delegate = var1;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         return var2.invoke(this.delegate, var3);
      }

      // $FF: synthetic method
      InvocationHandlerImpl(Object var1, Object var2) {
         this(var1);
      }
   }
}
