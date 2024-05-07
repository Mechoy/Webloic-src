package weblogic.wsee.policy.deployment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

public class WseePolicyReferenceInfo {
   private static final String[] ITEM_NAMES = new String[]{"category", "policyReferenceURI", "enabled", "configOverrides", "direction"};
   private static CompositeType COMPOSITE_TYPE = null;
   private static TabularType TABULAR_TYPE = null;
   private static final String[] MAP_ITEM_NAMES = new String[]{"key", "value"};
   private static final TabularType STRING_MAP_TYPE;
   private static final CompositeType STRING_MAP_ENTRY_TYPE;
   public static final String CATEGORY_OWSM_SECURITY = "owsm-security";
   public static final String CATEGORY_WS_POLICY = "ws-policy";
   public static final String STATUS_ENABLED = "enabled";
   public static final String STATUS_DISABLED = "disabled";
   public static final String STATUS_DELETED = "deleted";
   public static final String DIRECTION_BOTH = "both";
   public static final String DIRECTION_INBOUND = "inbound";
   public static final String DIRECTION_OUTBOUND = "outbound";
   private static HashSet<String> CATEGORIES;
   private static HashSet<String> STATUSES;
   private static HashSet<String> DIRECTIONS;
   private String category;
   private String uri;
   private String status;
   private String direction;
   private Map<String, String> overrides;

   public WseePolicyReferenceInfo() {
      this.category = null;
      this.uri = null;
      this.status = "enabled";
      this.direction = "both";
      this.overrides = null;
   }

   public WseePolicyReferenceInfo(String var1, String var2) {
      this.category = null;
      this.uri = null;
      this.status = "enabled";
      this.direction = "both";
      this.overrides = null;
      this.setCategory(var1);
      this.setUri(var2);
   }

   public WseePolicyReferenceInfo(String var1, String var2, String var3) {
      this(var1, var2);
      this.setStatus(var3);
   }

   public WseePolicyReferenceInfo(String var1, String var2, String var3, String var4) {
      this(var1, var2, var3);
      this.setDirection(var4);
   }

   public WseePolicyReferenceInfo(String var1, String var2, String var3, Map<String, String> var4) {
      this(var1, var2, var3);
      this.setOverrides(var4);
   }

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String var1) {
      this.validateCategory(var1);
      this.category = var1;
   }

   public String getUri() {
      return this.uri;
   }

   public void setUri(String var1) {
      this.uri = var1;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String var1) {
      this.validateStatus(var1);
      this.status = var1;
   }

   public String getDirection() {
      return this.direction;
   }

   public void setDirection(String var1) {
      this.validateDirection(var1);
      this.direction = var1;
   }

   public Map<String, String> getOverrides() {
      return this.overrides;
   }

   public void setOverrides(Map<String, String> var1) {
      if ("ws-policy".equals(this.category) && var1 != null) {
         throw new IllegalArgumentException("Overrides invalid for category " + this.category);
      } else {
         this.overrides = var1;
      }
   }

   private void validateCategory(String var1) {
      if (!CATEGORIES.contains(var1)) {
         throw new IllegalArgumentException("Invalid policy category: " + var1);
      } else if ("owsm-security".equals(var1) && !"both".equals(this.direction)) {
         throw new IllegalArgumentException("Invalid policy direction " + this.direction + " for category " + var1);
      } else if ("ws-policy".equals(var1) && this.overrides != null) {
         throw new IllegalArgumentException("Invalid policy category " + var1 + " for existing overrides.");
      }
   }

   private void validateStatus(String var1) {
      if (!STATUSES.contains(var1)) {
         throw new IllegalArgumentException("Invalid policy status: " + var1);
      } else if ("disabled".equals(var1) && !"both".equals(this.direction)) {
         throw new IllegalArgumentException("Invalid policy status " + var1 + " for direction " + this.direction);
      }
   }

   private void validateDirection(String var1) {
      if (!DIRECTIONS.contains(var1)) {
         throw new IllegalArgumentException("Invalid policy direction: " + var1);
      } else if ("owsm-security".equals(this.category) && !"both".equals(var1)) {
         throw new IllegalArgumentException("Invalid policy direction " + var1 + " for category " + this.category);
      } else if ("disabled".equals(this.status) && !"both".equals(var1)) {
         throw new IllegalArgumentException("Invalid policy direction " + var1 + " for status " + this.status);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null && var1.getClass() != this.getClass()) {
         return false;
      } else {
         WseePolicyReferenceInfo var2 = (WseePolicyReferenceInfo)var1;
         if (!this.getUri().equals(var2.getUri())) {
            return false;
         } else if (!this.getDirection().equals(var2.getDirection())) {
            return false;
         } else if (!this.getStatus().equals(var2.getStatus())) {
            return false;
         } else {
            return this.getCategory().equals(var2.getCategory());
         }
      }
   }

   public int hashCode() {
      return this.getUri().hashCode();
   }

   public static WseePolicyReferenceInfo from(CompositeData var0) {
      if (var0 == null) {
         return null;
      } else {
         TabularData var1 = (TabularData)var0.get(ITEM_NAMES[3]);
         HashMap var2 = null;
         if (var1 != null && !var1.isEmpty()) {
            var2 = new HashMap();
            Iterator var3 = var1.values().iterator();

            while(var3.hasNext()) {
               CompositeData var4 = (CompositeData)var3.next();
               var2.put((String)var4.get(MAP_ITEM_NAMES[0]), (String)var4.get(MAP_ITEM_NAMES[1]));
            }
         }

         WseePolicyReferenceInfo var5 = new WseePolicyReferenceInfo((String)var0.get(ITEM_NAMES[0]), (String)var0.get(ITEM_NAMES[1]), (String)var0.get(ITEM_NAMES[2]), var2);
         var5.setDirection((String)var0.get(ITEM_NAMES[4]));
         return var5;
      }
   }

   public static CompositeData from(WseePolicyReferenceInfo var0) {
      if (var0 == null) {
         return null;
      } else {
         Map var1 = var0.getOverrides();
         if (var1 != null && var1.isEmpty()) {
            var0.setOverrides((Map)null);
         }

         TabularDataSupport var2 = new TabularDataSupport(STRING_MAP_TYPE);
         var1 = var0.getOverrides();

         try {
            if (var1 != null) {
               Iterator var3 = var0.getOverrides().keySet().iterator();

               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  var2.put(new CompositeDataSupport(STRING_MAP_ENTRY_TYPE, MAP_ITEM_NAMES, new Object[]{var4, var0.getOverrides().get(var4)}));
               }
            }

            Object[] var6 = new Object[]{var0.getCategory(), var0.getUri(), var0.getStatus(), var2, var0.getDirection()};
            return new CompositeDataSupport(COMPOSITE_TYPE, ITEM_NAMES, var6);
         } catch (OpenDataException var5) {
            throw new RuntimeException(var5);
         }
      }
   }

   public static TabularData from(WseePolicyReferenceInfo[] var0) {
      if (var0 == null) {
         return null;
      } else {
         TabularDataSupport var1 = new TabularDataSupport(TABULAR_TYPE);
         WseePolicyReferenceInfo[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WseePolicyReferenceInfo var5 = var2[var4];
            var1.put(from(var5));
         }

         return var1;
      }
   }

   public static WseePolicyReferenceInfo[] from(TabularData var0) {
      if (var0 == null) {
         return null;
      } else {
         WseePolicyReferenceInfo[] var1 = new WseePolicyReferenceInfo[var0.size()];
         int var2 = 0;

         for(Iterator var3 = var0.values().iterator(); var3.hasNext(); ++var2) {
            CompositeData var4 = (CompositeData)var3.next();
            var1[var2] = from(var4);
         }

         return var1;
      }
   }

   static {
      try {
         STRING_MAP_ENTRY_TYPE = new CompositeType("StringMapEntry", "A Map entry whose value type is String", MAP_ITEM_NAMES, MAP_ITEM_NAMES, new OpenType[]{SimpleType.STRING, SimpleType.STRING});
         STRING_MAP_TYPE = new TabularType("Map", "A Map of String keys to String values", STRING_MAP_ENTRY_TYPE, new String[]{MAP_ITEM_NAMES[0]});
         OpenType[] var0 = new OpenType[]{SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, STRING_MAP_TYPE, SimpleType.STRING};
         COMPOSITE_TYPE = new CompositeType("weblogic.management.runtime.WseePolicyReferenceInfo", "weblogic.management.runtime.WseePolicyReferenceInfo", ITEM_NAMES, ITEM_NAMES, var0);
         TABULAR_TYPE = new TabularType("weblogic.management.runtime.WseePolicyReferenceInfo[]", "weblogic.management.runtime.WseePolicyReferenceInfo[]", COMPOSITE_TYPE, ITEM_NAMES);
      } catch (OpenDataException var1) {
         throw new RuntimeException(var1);
      }

      CATEGORIES = new HashSet();
      STATUSES = new HashSet();
      DIRECTIONS = new HashSet();
      CATEGORIES.add("owsm-security");
      CATEGORIES.add("ws-policy");
      STATUSES.add("enabled");
      STATUSES.add("disabled");
      STATUSES.add("deleted");
      DIRECTIONS.add("both");
      DIRECTIONS.add("inbound");
      DIRECTIONS.add("outbound");
   }
}
