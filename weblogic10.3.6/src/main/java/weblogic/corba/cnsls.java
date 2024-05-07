package weblogic.corba;

import java.io.FileReader;
import java.util.Hashtable;
import java.util.Properties;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.corba.j2ee.naming.Utils;
import weblogic.utils.Getopt2;

public final class cnsls {
   private static String objRefFileName = null;
   private static String globalResolveName = null;
   private static boolean dumpStringRef = false;
   private static boolean dumpRecursive = false;
   private static ORB theORB = null;
   private static String insUrl = "iiop://localhost:7001";
   private static final String CONTEXT_DESIGNATOR = "[context] ";
   private static final String OBJECT_DESIGNATOR = "[object]  ";
   private static final String INDENT = "    ";
   private static final String PROGRAM = "weblogic.corba.cnsls";

   public static void main(String[] var0) {
      processCLOpts(var0);
      if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
         System.setProperty("weblogic.system.iiop.enableClient", "false");
      }

      NamingContextExt var1 = null;

      try {
         if (objRefFileName != null) {
            FileReader var2 = new FileReader(objRefFileName);
            StringBuffer var4 = new StringBuffer();

            int var3;
            while((var3 = var2.read()) >= 48) {
               var4.append((char)var3);
            }

            theORB = ORB.init(new String[0], (Properties)null);
            Object var5 = theORB.string_to_object(var4.toString());
            var1 = NamingContextExtHelper.narrow(var5);
         } else {
            theORB = ORBHelper.getORBHelper().getORB(insUrl, (Hashtable)null);
            Object var14 = theORB.resolve_initial_references("NameService");
            var1 = NamingContextExtHelper.narrow(var14);
         }
      } catch (Exception var13) {
         System.err.println("Error retrieving name service root object reference: " + var13.getMessage());
         System.exit(1);
      }

      NameComponent[] var15;
      try {
         var15 = var1.to_name("");
      } catch (Exception var8) {
         System.err.println("Error connecting to name service, please make sure the name service is running.");
         System.exit(1);
      }

      var15 = null;
      Object var16 = null;
      String var19;
      if (globalResolveName != null && globalResolveName.length() != 0) {
         try {
            var15 = var1.to_name(globalResolveName);
         } catch (Exception var7) {
            System.err.println("Error, invalid name: " + globalResolveName);
            System.exit(1);
         }

         try {
            var16 = var1.resolve(var15);
         } catch (NotFound var9) {
            var19 = "Error, ";
            switch (var9.why.value()) {
               case 0:
                  var19 = var19 + "missing node: ";
                  break;
               case 1:
                  var19 = var19 + "not context: ";
                  break;
               case 2:
                  var19 = var19 + "not object: ";
            }

            var19 = var19 + var9.rest_of_name;
            System.out.println(var19);
            System.exit(1);
         } catch (InvalidName var10) {
            System.out.println("Error, invalid name");
            System.exit(1);
         } catch (CannotProceed var11) {
            System.out.println("Error, cannot proceed: " + var11.rest_of_name);
            System.exit(1);
         } catch (Exception var12) {
            var12.printStackTrace();
            System.err.println("CORBA Exception: " + var12.getMessage());
            System.exit(1);
         }
      } else {
         var16 = var1._duplicate();
         var15 = new NameComponent[]{new NameComponent("<root>", "")};
      }

      try {
         if (var16._is_a(NamingContextHelper.id())) {
            NamingContext var17 = NamingContextHelper.narrow(var16);
            var19 = var1.to_string(var15);
            System.out.println("\n[context] " + var19);
            if (dumpStringRef) {
               dumpStringRef(theORB, var16);
            }

            dumpBindings(var17, 1);
         } else {
            String var18 = var1.to_string(var15);
            System.out.println("\n[object]  " + var18);
            if (dumpStringRef) {
               dumpStringRef(theORB, var16);
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         System.err.println("CORBA Exception: " + var6.getMessage());
         System.exit(1);
      }

   }

   private static void processCLOpts(String[] var0) {
      Getopt2 var1 = new Getopt2();

      try {
         var1.addFlag("h", "Help");
         var1.addFlag("s", "Dump stringified IORs");
         var1.addFlag("R", "Dump contexts recursively");
         var1.addOption("f", "filename", "IOR filename");
         var1.addOption("u", "url", "Server URL");
         var1.setUsageArgs("[name]");
         if (var0.length == 0) {
            var1.usageAndExit("weblogic.corba.cnsls");
         }

         var1.grok(var0);
         if (var1.args().length > 0) {
            globalResolveName = var1.args()[0];
         }

         if (var1.hasOption("h")) {
            var1.usageAndExit("weblogic.corba.cnsls");
         }

         objRefFileName = var1.getOption("f");
         dumpStringRef = var1.hasOption("s");
         dumpRecursive = var1.hasOption("R");
         if (var1.hasOption("u")) {
            insUrl = var1.getOption("u");
         }
      } catch (IllegalArgumentException var3) {
         System.out.println("Invalid option: " + var3.getMessage());
         var1.usageAndExit("weblogic.corba.cnsls");
      }

   }

   private static void dumpBindings(NamingContext var0, int var1) {
      BindingListHolder var2 = new BindingListHolder();
      BindingIteratorHolder var3 = new BindingIteratorHolder();
      var0.list(20, var2, var3);
      dumpBindingList(var2, var0, var1);
      if (var3.value != null) {
         int var4 = var2.value.length;

         boolean var5;
         do {
            try {
               var5 = var3.value.next_n(20, var2);
               var4 += var2.value.length;
               dumpBindingList(var2, var0, var1);
            } catch (OBJECT_NOT_EXIST var8) {
               var0.list(var4, var2, var3);
               var5 = true;
            }
         } while(var3.value != null && var5);

         try {
            if (var3.value != null) {
               var3.value.destroy();
            }
         } catch (Exception var7) {
         }
      }

   }

   private static void dumpBindingList(BindingListHolder var0, NamingContext var1, int var2) {
      for(int var3 = 0; var3 < var0.value.length; ++var3) {
         String var4 = "";

         for(int var5 = 0; var5 < var2; ++var5) {
            var4 = var4 + "    ";
         }

         String var12 = Utils.nameComponentToString(var0.value[var3].binding_name);
         Object var6 = null;
         if (dumpStringRef || dumpRecursive) {
            try {
               var6 = var1.resolve(var0.value[var3].binding_name);
            } catch (Exception var11) {
            }
         }

         if (var0.value[var3].binding_type.value() == 0) {
            System.out.println(var4 + "[object]  " + var12);
            if (dumpStringRef) {
               dumpStringRef(theORB, var6);
            }
         } else {
            System.out.println(var4 + "[context] " + var12);
            if (dumpStringRef) {
               dumpStringRef(theORB, var6);
            }

            if (dumpRecursive) {
               NamingContext var7 = null;

               try {
                  var7 = NamingContextHelper.narrow(var6);
               } catch (Exception var10) {
               }

               try {
                  dumpBindings(var7, var2 + 1);
               } catch (Exception var9) {
                  System.out.println("Error dumping bindings for " + var0.value[var3].binding_name[0].id);
               }
            }
         }
      }

   }

   private static void dumpStringRef(ORB var0, Object var1) {
      String var2;
      if (var1 != null) {
         var2 = var0.object_to_string(var1);
      } else {
         var2 = "<nil>";
      }

      System.out.println("\n" + var2);
   }
}
