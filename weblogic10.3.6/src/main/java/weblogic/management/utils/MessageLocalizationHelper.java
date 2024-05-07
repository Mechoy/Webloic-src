package weblogic.management.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageLocalizationHelper {
   private Locale locale_;
   private ResourceBundle resourceBundle_;

   public MessageLocalizationHelper(String var1, Locale var2) {
      this(var1, var2, (ClassLoader)null);
   }

   public MessageLocalizationHelper(String var1, Locale var2, ClassLoader var3) {
      this.locale_ = null;
      this.resourceBundle_ = null;
      this.locale_ = var2;
      if (var3 != null) {
         this.resourceBundle_ = ResourceBundle.getBundle(var1, var2, var3);
      } else {
         this.resourceBundle_ = ResourceBundle.getBundle(var1, var2, Thread.currentThread().getContextClassLoader());
      }

   }

   public MessageLocalizationHelper(String var1) {
      this(var1, Locale.getDefault());
   }

   public MessageLocalizationHelper(ResourceBundle var1, Locale var2) {
      this.locale_ = null;
      this.resourceBundle_ = null;
      this.locale_ = var2;
      this.resourceBundle_ = var1;
   }

   public MessageLocalizationHelper(ResourceBundle var1) {
      this(var1, Locale.getDefault());
   }

   public Locale getLocale() {
      return this.locale_;
   }

   public final String getLocalizedMessage(String var1) {
      return this.resourceBundle_.getString(var1);
   }

   public final String getLocalizedMessage(String var1, String var2) {
      String[] var3 = new String[]{var2};
      return this.getLocalizedMessage(var1, var3);
   }

   public final String getLocalizedMessage(String var1, String var2, String var3) {
      String[] var4 = new String[]{var2, var3};
      return this.getLocalizedMessage(var1, var4);
   }

   public final String getLocalizedMessage(String var1, String var2, String var3, String var4) {
      String[] var5 = new String[]{var2, var3, var4};
      return this.getLocalizedMessage(var1, var5);
   }

   public final String getLocalizedMessage(String var1, String[] var2) {
      String var3 = this.resourceBundle_.getString(var1);
      return MessageFormat.format(var3, (Object[])var2);
   }
}
