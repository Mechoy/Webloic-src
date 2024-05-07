package weblogic.j2ee;

public class DeploymentInfo {
   public static final int UNKNOWN = 0;
   public static final int ARCHIVED_EAR = 1;
   public static final int EXPLODED_EAR = 2;
   public static final int ARCHIVED_WEB = 3;
   public static final int EXPLODED_WEB = 4;
   public static final int ARCHIVED_RAR = 5;
   public static final int EXPLODED_RAR = 6;
   public static final int ARCHIVED_EJB = 7;
   public static final int EXPLODED_EJB = 8;
   public static final int ARCHIVED_WEBSERVICE = 9;
   public static final int EXPLODED_WEBSERVICE = 10;
   public static final int ARCHIVED_CAR = 11;
   public static final int EXPLODED_CAR = 12;
   public static final String UNKNOWN_STRING = "UNKNOWN";
   public static final String ARCHIVED_EAR_STRING = "j2ee.ARCHIVED_EAR";
   public static final String EXPLODED_EAR_STRING = "j2ee.EXPLODED_EAR";
   public static final String ARCHIVED_WEB_STRING = "j2ee.ARCHIVED_WEB";
   public static final String EXPLODED_WEB_STRING = "j2ee.EXPLODED_WEB";
   public static final String ARCHIVED_RAR_STRING = "j2ee.ARCHIVED_RAR";
   public static final String EXPLODED_RAR_STRING = "j2ee.EXPLODED_RAR";
   public static final String ARCHIVED_EJB_STRING = "j2ee.ARCHIVED_EJB";
   public static final String EXPLODED_EJB_STRING = "j2ee.EXPLODED_EJB";
   public static final String ARCHIVED_CAR_STRING = "j2ee.ARCHIVED_CAR";
   public static final String EXPLODED_CAR_STRING = "j2ee.EXPLODED_CAR";
   public static final String ARCHIVED_WEBSERVICE_STRING = "ARCHIVED_WEBSERVICE";
   public static final String EXPLODED_WEBSERVICE_STRING = "EXPLODED_WEBSERVICE";
   public static String TYPE_EAR = "EAR";
   public static String TYPE_EXPLODED_EAR = "EXPLODED EAR";
   public static String TYPE_COMPONENT = "COMPONENT";
   public static String TYPE_EXPLODED_COMPONENT = "EXPLODED COMPONENT";
   public static String TYPE_UNKNOWN = "UNKNOWN";
   private int type;
   private String uri;
   private boolean exploded;

   DeploymentInfo(int var1, String var2) {
      this.type = var1;
      this.uri = var2;
      switch (var1) {
         case 2:
         case 4:
         case 6:
         case 8:
         case 12:
            this.exploded = true;
            break;
         case 3:
         case 5:
         case 7:
         case 9:
         case 10:
         case 11:
         default:
            this.exploded = false;
      }

   }

   public boolean isExploded() {
      return this.exploded;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      this.uri = var1;
   }

   public String toString() {
      String var1 = asString(this.type);
      return "DeploymentInfo{" + this.uri + ", " + var1 + "}";
   }

   public static String asString(int var0) {
      switch (var0) {
         case 1:
            return "j2ee.ARCHIVED_EAR";
         case 2:
            return "j2ee.EXPLODED_EAR";
         case 3:
            return "j2ee.ARCHIVED_WEB";
         case 4:
            return "j2ee.EXPLODED_WEB";
         case 5:
            return "j2ee.ARCHIVED_RAR";
         case 6:
            return "j2ee.EXPLODED_RAR";
         case 7:
            return "j2ee.ARCHIVED_EJB";
         case 8:
            return "j2ee.EXPLODED_EJB";
         case 9:
            return "ARCHIVED_WEBSERVICE";
         case 10:
            return "EXPLODED_WEBSERVICE";
         case 11:
            return "j2ee.ARCHIVED_CAR";
         case 12:
            return "j2ee.EXPLODED_CAR";
         default:
            return "UNKNOWN";
      }
   }

   public static String asStringCategory(int var0) {
      switch (var0) {
         case 1:
            return TYPE_EAR;
         case 2:
            return TYPE_EXPLODED_EAR;
         case 3:
         case 5:
         case 7:
         case 9:
         case 11:
            return TYPE_COMPONENT;
         case 4:
         case 6:
         case 8:
         case 10:
         case 12:
            return TYPE_EXPLODED_COMPONENT;
         default:
            return "UNKNOWN";
      }
   }

   public static int asType(String var0) {
      if ("j2ee.ARCHIVED_EAR".equals(var0)) {
         return 1;
      } else if ("j2ee.EXPLODED_EAR".equals(var0)) {
         return 2;
      } else if ("j2ee.ARCHIVED_WEB".equals(var0)) {
         return 3;
      } else if ("j2ee.EXPLODED_WEB".equals(var0)) {
         return 4;
      } else if ("j2ee.ARCHIVED_RAR".equals(var0)) {
         return 5;
      } else if ("j2ee.EXPLODED_RAR".equals(var0)) {
         return 6;
      } else if ("j2ee.ARCHIVED_EJB".equals(var0)) {
         return 7;
      } else if ("j2ee.EXPLODED_EJB".equals(var0)) {
         return 8;
      } else if ("j2ee.ARCHIVED_CAR".equals(var0)) {
         return 11;
      } else if ("j2ee.EXPLODED_CAR".equals(var0)) {
         return 12;
      } else if ("ARCHIVED_WEBSERVICE".equals(var0)) {
         return 9;
      } else {
         return "EXPLODED_WEBSERVICE".equals(var0) ? 10 : 0;
      }
   }
}
