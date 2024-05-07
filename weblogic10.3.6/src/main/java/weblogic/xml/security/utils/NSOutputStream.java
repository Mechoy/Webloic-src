package weblogic.xml.security.utils;

import java.util.Map;
import weblogic.xml.stream.XMLOutputStream;

public interface NSOutputStream extends XMLOutputStream {
   void addPrefix(String var1, String var2);

   Map getNamespaces();
}
