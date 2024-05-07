package weblogic.tools.ui;

public class PropertyInfo {
   private boolean m_required;
   private Object[] m_constrainedObjects;
   private String[] m_constrainedStrings;
   private String m_name;
   private String m_label;
   private String m_tooltip;
   private String m_helpKey;
   private boolean m_emptyStringIsNull;
   private int numberMin;
   private int numberMax;
   private int numberInc;
   private boolean selectFirstListElement;
   private boolean allowListEditing;
   private boolean allowNullObject;

   public PropertyInfo(String var1, String var2, String var3, Object[] var4, boolean var5) {
      this.m_emptyStringIsNull = true;
      this.numberMin = -1;
      this.numberMax = -1;
      this.numberInc = -1;
      this.selectFirstListElement = false;
      this.allowListEditing = false;
      this.allowNullObject = false;
      this.m_name = var1;
      this.m_label = var2;
      this.m_tooltip = var3;
      if (var4 != null) {
         if (var4 instanceof String[]) {
            this.m_constrainedStrings = (String[])((String[])var4);
         } else {
            this.m_constrainedObjects = var4;
         }
      }

      this.m_required = var5;
   }

   public PropertyInfo(String var1, String var2, String var3) {
      this(var1, var2, var3, (Object[])null, false);
   }

   public PropertyInfo(String var1, String var2, String var3, boolean var4) {
      this(var1, var2, var3, (Object[])null, var4);
   }

   public String getName() {
      return this.m_name;
   }

   public String getLabel() {
      return this.m_label;
   }

   public String getTooltip() {
      return this.m_tooltip;
   }

   public Object[] getConstrainedObjects() {
      return this.m_constrainedObjects;
   }

   public String[] getConstrainedStrings() {
      return this.m_constrainedStrings;
   }

   public boolean isRequired() {
      return this.m_required;
   }

   public PropertyInfo setRequired(boolean var1) {
      this.m_required = var1;
      return this;
   }

   public PropertyInfo setConstrained(Object[] var1) {
      if (var1 == null) {
         this.m_constrainedStrings = null;
         this.m_constrainedObjects = null;
      } else if (var1 instanceof String[]) {
         this.m_constrainedStrings = (String[])((String[])var1);
         this.m_constrainedObjects = null;
      } else {
         this.m_constrainedObjects = var1;
         this.m_constrainedStrings = null;
      }

      return this;
   }

   public PropertyInfo setEmptyStringIsNull(boolean var1) {
      this.m_emptyStringIsNull = var1;
      return this;
   }

   public boolean isEmptyStringNull() {
      return this.m_emptyStringIsNull;
   }

   public PropertyInfo setNumberMin(int var1) {
      this.numberMin = var1;
      return this;
   }

   public int getNumberMin() {
      return this.numberMin;
   }

   public PropertyInfo setNumberMax(int var1) {
      this.numberMax = var1;
      return this;
   }

   public int getNumberMax() {
      return this.numberMax;
   }

   public PropertyInfo setNumberIncrement(int var1) {
      this.numberInc = var1;
      return this;
   }

   public int getNumberIncrement() {
      return this.numberInc;
   }

   public PropertyInfo setSelectFirstListElement(boolean var1) {
      this.selectFirstListElement = var1;
      return this;
   }

   public boolean getSelectFirstListElement() {
      return this.selectFirstListElement;
   }

   public PropertyInfo setAllowListEditing(boolean var1) {
      this.allowListEditing = var1;
      return this;
   }

   public boolean getAllowListEditing() {
      return this.allowListEditing;
   }

   public PropertyInfo setAllowNullObject(boolean var1) {
      this.allowNullObject = var1;
      return this;
   }

   public boolean getAllowNullObject() {
      return this.allowNullObject;
   }

   public static PropertyInfo fromArray(Object[] var0) {
      if (var0 == null) {
         throw new NullPointerException("null array");
      } else if (var0.length < 3) {
         throw new IllegalArgumentException("bad array");
      } else {
         String var1 = null;
         String var2 = null;
         String var3 = null;
         Object[] var4 = null;
         boolean var5 = false;
         var1 = (String)var0[0];
         var2 = (String)var0[1];
         var3 = (String)var0[2];
         if (var0.length > 3 && var0[3] != null && var0[3] instanceof Object[]) {
            var4 = (Object[])((Object[])var0[3]);
         }

         Object var6 = var0[var0.length - 1];
         if (var0.length > 3 && var6 != null && var6 instanceof Boolean) {
            Boolean var7 = (Boolean)var6;
            var5 = var7;
         }

         return new PropertyInfo(var1, var2, var3, var4, var5);
      }
   }
}
