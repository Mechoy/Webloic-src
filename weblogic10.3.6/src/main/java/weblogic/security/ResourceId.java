package weblogic.security;

import java.util.Map;
import weblogic.security.utils.ResourceUtils;

public class ResourceId {
   public static final String RESOURCE_TYPE = "ResourceType";

   public static String getResourceIdFromMap(Map var0) throws IllegalArgumentException {
      return ResourceUtils.getResourceIdFromMap(var0);
   }

   public static Map getMapFromResourceId(String var0) throws IllegalArgumentException {
      return ResourceUtils.getMapFromResourceId(var0);
   }

   public static String[] getResourceKeyNames(String var0) throws IllegalArgumentException {
      return ResourceUtils.getResourceKeyNames(var0);
   }

   public static String[] getParentResourceIds(String var0) throws IllegalArgumentException {
      return ResourceUtils.getParentResourceIds(var0);
   }
}
