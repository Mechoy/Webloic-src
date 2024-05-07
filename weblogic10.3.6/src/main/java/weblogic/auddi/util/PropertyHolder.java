package weblogic.auddi.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

public interface PropertyHolder {
   String getProperty(String var1);

   String getProperty(String var1, String var2);

   String setProperty(String var1, String var2);

   Enumeration propertyNames();

   boolean isProperty(String var1);

   boolean isEmpty();

   int size();

   void list(PrintStream var1);

   void list(PrintWriter var1);

   void addProperties(Properties var1, boolean var2);
}
