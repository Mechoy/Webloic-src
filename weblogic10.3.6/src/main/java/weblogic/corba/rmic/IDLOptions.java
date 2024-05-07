package weblogic.corba.rmic;

public final class IDLOptions {
   static boolean m_idlStrict = false;
   static boolean m_factories = false;
   static boolean m_noValueTypes = false;
   static boolean m_noAbstract = false;
   static boolean m_compatibility = false;
   static boolean m_visibroker = false;
   static boolean m_orbix = false;

   private IDLOptions() {
   }

   public static void setIDLStrict(boolean var0) {
      m_idlStrict = var0;
   }

   public static boolean getIDLStrict() {
      return m_idlStrict;
   }

   public static void setFactories(boolean var0) {
      m_factories = var0;
   }

   public static boolean getFactories() {
      return m_factories;
   }

   public static boolean getNoValueTypes() {
      return m_noValueTypes;
   }

   public static void setNoValueTypes(boolean var0) {
      m_noValueTypes = var0;
   }

   public static boolean getNoAbstract() {
      return m_noAbstract;
   }

   public static void setNoAbstract(boolean var0) {
      m_noAbstract = var0;
   }

   public static boolean getVisibroker() {
      return m_visibroker;
   }

   public static void setVisibroker(boolean var0) {
      m_visibroker = var0;
   }

   public static boolean getOrbix() {
      return m_orbix;
   }

   public static void setOrbix(boolean var0) {
      m_orbix = var0;
   }

   public static boolean getCompatibility() {
      return m_compatibility;
   }

   public static void setCompatibility(boolean var0) {
      m_compatibility = var0;
   }
}
