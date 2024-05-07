package weblogic.descriptor.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import weblogic.application.utils.IOUtils;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;

public class DescriptorUtils {
   private static final EditableDescriptorManager edm = new EditableDescriptorManager();

   private DescriptorUtils() {
   }

   public static void writeAsXML(DescriptorBean var0) {
      Descriptor var1 = var0.getDescriptor();

      try {
         edm.writeDescriptorAsXML(var1, new BufferedOutputStream(System.out) {
            public void close() {
            }
         });
      } catch (IOException var3) {
      }

   }

   public static void writeDescriptor(DescriptorManager var0, DescriptorBean var1, File var2) throws IOException {
      BufferedOutputStream var3 = null;

      try {
         IOUtils.checkCreateParent(var2);
         var3 = new BufferedOutputStream(new FileOutputStream(var2));
         var0.writeDescriptorBeanAsXML(var1, var3);
         var3.flush();
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   public static Object getOrCreateFirstChild(Object var0, Object var1, String var2) {
      Object var3 = getFirstChild(var1);
      if (var3 != null) {
         return var3;
      } else if (var0 == null) {
         throw new NullPointerException("Parent Bean object null");
      } else {
         Method var4 = getMethod(var0, "create" + var2, new Class[0]);
         return invokeMethod(var0, var4, new Object[0]);
      }
   }

   public static Object getFirstChildOrDefaultBean(Object var0, Object var1, String var2) {
      Object var3 = getFirstChild(var1);
      if (var3 != null) {
         return var3;
      } else if (var0 == null) {
         throw new NullPointerException("Parent Bean object null");
      } else {
         Method var4 = getMethod(var0, "create" + var2, new Class[0]);
         var3 = invokeMethod(var0, var4, new Object[0]);
         if (var3 == null) {
            throw new IllegalArgumentException("Error creating bean " + var2);
         } else {
            var4 = getMethod(var0, "destroy" + var2, new Class[]{var4.getReturnType()});
            invokeMethod(var0, var4, new Object[]{var3});
            return var3;
         }
      }
   }

   private static Object getFirstChild(Object var0) {
      if (var0 == null) {
         throw new NullPointerException("Array of children is null");
      } else if (!var0.getClass().isArray()) {
         throw new IllegalArgumentException("childArray is not of type array");
      } else {
         return Array.getLength(var0) > 0 ? Array.get(var0, 0) : null;
      }
   }

   private static Method getMethod(Object var0, String var1, Class[] var2) {
      try {
         return var0.getClass().getMethod(var1, var2);
      } catch (NoSuchMethodException var4) {
         throw new IllegalArgumentException(var1 + "() method does not exist on " + var0.getClass());
      }
   }

   private static Object invokeMethod(Object var0, Method var1, Object[] var2) {
      try {
         return var1.invoke(var0, var2);
      } catch (IllegalAccessException var4) {
         throw new IllegalArgumentException(var1 + " method  not accessible on " + var0.getClass());
      } catch (InvocationTargetException var5) {
         throw new IllegalArgumentException(var1 + " method on " + var0.getClass() + " threw an exception: " + var5.getTargetException().getMessage());
      }
   }

   public static void main(String[] var0) throws IOException, XMLStreamException {
      if (var0.length > 0) {
         if (var0[0].equals("load") && var0.length > 1) {
            DescriptorManager var1 = new DescriptorManager();
            Descriptor var2 = var1.createDescriptor(XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(var0[1])));
            var1.writeDescriptorAsXML(var2, System.out);
         }
      } else {
         System.out.println("Arguments: [load <descriptor_file>]");
      }

   }
}
