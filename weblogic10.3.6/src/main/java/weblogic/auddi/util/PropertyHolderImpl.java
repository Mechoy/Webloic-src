package weblogic.auddi.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyHolderImpl implements PropertyHolder {
   protected Properties m_props;

   public PropertyHolderImpl() {
      this.m_props = new Properties();
   }

   public PropertyHolderImpl(Properties var1) {
      this.m_props = new Properties(var1);
   }

   public String getProperty(String var1) {
      return this.m_props.getProperty(var1);
   }

   public String getProperty(String var1, String var2) {
      return this.m_props.getProperty(var1, var2);
   }

   public void list(PrintStream var1) {
      this.m_props.list(var1);
   }

   public void list(PrintWriter var1) {
      this.m_props.list(var1);
   }

   public Enumeration propertyNames() {
      return this.m_props.propertyNames();
   }

   public String setProperty(String var1, String var2) {
      return var2 == null ? (String)this.m_props.remove(var1) : (String)this.m_props.setProperty(var1, var2);
   }

   public boolean isEmpty() {
      return this.m_props.isEmpty();
   }

   public int size() {
      return this.m_props.size();
   }

   public boolean isProperty(String var1) {
      return this.m_props.getProperty(var1) != null;
   }

   public void addProperties(Properties var1, boolean var2) {
      Enumeration var3 = var1.propertyNames();

      while(true) {
         String var4;
         do {
            if (!var3.hasMoreElements()) {
               return;
            }

            var4 = (String)var3.nextElement();
         } while(!var2 && this.m_props.getProperty(var4) != null);

         this.m_props.setProperty(var4, var1.getProperty(var4));
      }
   }
}
