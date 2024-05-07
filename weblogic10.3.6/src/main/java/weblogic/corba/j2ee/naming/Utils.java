package weblogic.corba.j2ee.naming;

import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
import weblogic.corba.cos.naming.NamingContextAnyHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WNameComponent;

public final class Utils {
   private static final boolean DEBUG = false;

   public static NamingException wrapNamingException(Exception var0, String var1) throws NamingException {
      Object var2 = null;
      if (var0 instanceof InvalidName) {
         var2 = new InvalidNameException(var1);
      } else if (var0 instanceof org.omg.CORBA.ORBPackage.InvalidName) {
         var2 = new InvalidNameException(var1);
      } else if (var0 instanceof NotFound) {
         NotFound var3 = (NotFound)var0;
         var1 = var1 + ": `" + nameComponentToString(var3.rest_of_name) + "'" + notFoundReasonToString(var3.why);
         var2 = new NameNotFoundException(var1);
      } else if (var0 instanceof weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound) {
         weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound var4 = (weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound)var0;
         var1 = var1 + ": `" + nameComponentToString(var4.rest_of_name) + "'" + notFoundReasonToString(var4.why);
         var2 = new NameNotFoundException(var1);
      } else if (var0 instanceof CannotProceed) {
         CannotProceed var5 = (CannotProceed)var0;
         var1 = var1 + ": unresolved name `" + nameComponentToString(var5.rest_of_name) + "'";
         var2 = new CannotProceedException(var1);
      } else if (var0 instanceof weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed) {
         weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed var6 = (weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed)var0;
         var1 = var1 + ": unresolved name `" + nameComponentToString(var6.rest_of_name) + "'";
         var2 = new CannotProceedException(var1);
      } else if (var0 instanceof AlreadyBound) {
         var2 = new NameAlreadyBoundException(var1);
      } else {
         var2 = new NamingException(var1);
      }

      ((NamingException)var2).setRootCause(var0);
      return (NamingException)var2;
   }

   public static SystemException unwrapNamingException(SystemException var0, NamingException var1) {
      if (var1.getRootCause() instanceof SystemException) {
         return (SystemException)var1.getRootCause();
      } else {
         var0.initCause(var1);
         return var0;
      }
   }

   public static SystemException initCORBAExceptionWithCause(SystemException var0, Throwable var1) {
      var0.initCause(var1);
      return var0;
   }

   public static final NamingContext narrowContext(org.omg.CORBA.Object var0) {
      return (NamingContext)(var0._is_a(NamingContextAnyHelper.id()) ? NamingContextAnyHelper.narrow(var0) : NamingContextHelper.narrow(var0));
   }

   public static final NameComponent[] stringToNameComponent(String var0) throws NamingException {
      return nameToName(stringToName(var0));
   }

   public static final Name stringToName(String var0) throws NamingException {
      var0 = NameParser.getNameString(var0);

      CompositeName var1;
      for(var1 = new CompositeName(); var0.length() > 0; var0 = getSuffix(var0)) {
         var1.add(getPrefix(var0));
      }

      return var1;
   }

   public static final WNameComponent[] stringToWNameComponent(String var0) throws NamingException {
      return nameToWName(stringToNameComponent(var0));
   }

   public static final WNameComponent[] nameToWName(NameComponent[] var0) throws NamingException {
      WNameComponent[] var1 = new WNameComponent[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = new WNameComponent(var0[var2].id, var0[var2].kind);
      }

      return var1;
   }

   public static final WNameComponent[] nameToWName(Name var0) throws NamingException {
      WNameComponent[] var1 = new WNameComponent[var0.size()];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1[var2] = new WNameComponent(var0.get(var2), "");
      }

      return var1;
   }

   public static final NameComponent[] nameToName(Name var0) throws NamingException {
      NameComponent[] var1 = new NameComponent[var0.size()];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1[var2] = new NameComponent(var0.get(var2), "");
      }

      return var1;
   }

   public static final String nameComponentToString(NameComponent[] var0) {
      StringBuffer var1 = new StringBuffer("");
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var2 > 0) {
               var1.append('/');
            }

            String var3 = var0[var2].id;

            for(int var4 = 0; var4 < var3.length(); ++var4) {
               if (var4 != 0 && var4 != var3.length() - 1 && isQuote(var3.charAt(var4)) || isSeparator(var3.charAt(var4))) {
                  var1.append('\\');
               }

               var1.append(var3.charAt(var4));
            }

            if (var0[var2].kind.length() > 0) {
               var1.append('/').append(var0[var2].kind);
            }
         }
      }

      return var1.toString();
   }

   private static boolean isSeparator(char var0) {
      return var0 == '.' || var0 == '/';
   }

   private static boolean isQuote(char var0) {
      return var0 == '"' || var0 == '\'';
   }

   public static final String nameComponentToString(WNameComponent[] var0) {
      StringBuffer var1 = new StringBuffer("");
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var2 > 0) {
               var1.append('/');
            }

            String var3 = var0[var2].id;

            for(int var4 = 0; var4 < var3.length(); ++var4) {
               if (var4 != 0 && var4 != var3.length() - 1 && isQuote(var3.charAt(var4)) || isSeparator(var3.charAt(var4))) {
                  var1.append('\\');
               }

               var1.append(var3.charAt(var4));
            }

            if (var0[var2].kind.length() > 0) {
               var1.append('/').append(var0[var2].kind);
            }
         }
      }

      return var1.toString();
   }

   private static final String notFoundReasonToString(NotFoundReason var0) {
      switch (var0.value()) {
         case 0:
            return " could not be found.";
         case 1:
            return " is not a naming context.";
         case 2:
            return " is not a remote object.";
         default:
            return null;
      }
   }

   private static String getPrefix(String var0) throws NamingException {
      if (var0.length() == 0) {
         return var0;
      } else {
         int var3;
         if (var0.charAt(0) == '"') {
            var3 = var0.indexOf(34, 1);
            if (var3 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var3 < var0.length() - 1 && !isSeparator(var0.charAt(var3 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               return var0.substring(1, var3);
            }
         } else if (var0.charAt(0) == '\'') {
            var3 = var0.indexOf(39, 1);
            if (var3 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var3 < var0.length() - 1 && !isSeparator(var0.charAt(var3 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               return var0.substring(1, var3);
            }
         } else {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < var0.length(); ++var2) {
               switch (var0.charAt(var2)) {
                  case '"':
                  case '\'':
                     throw new InvalidNameException("Unescaped quote in a component");
                  case '\\':
                     ++var2;
                     if (var2 == var0.length()) {
                        throw new InvalidNameException("An escape at the end of a name must be escaped");
                     }

                     var1.append(var0.charAt(var2));
                     break;
                  default:
                     if (isSeparator(var0.charAt(var2))) {
                        return var1.toString();
                     }

                     var1.append(var0.charAt(var2));
               }
            }

            return var1.toString();
         }
      }
   }

   private static String getSuffix(String var0) throws NamingException {
      if (var0.length() == 0) {
         return var0;
      } else {
         int var1;
         if (var0.charAt(0) == '"') {
            var1 = var0.indexOf(34, 1);
            if (var1 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var1 < var0.length() - 1 && !isSeparator(var0.charAt(var1 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               ++var1;
               if (var1 < var0.length() && isSeparator(var0.charAt(var1))) {
                  ++var1;
               }

               return var0.substring(var1);
            }
         } else if (var0.charAt(0) == '\'') {
            var1 = var0.indexOf(39, 1);
            if (var1 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var1 < var0.length() - 1 && !isSeparator(var0.charAt(var1 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               ++var1;
               if (var1 < var0.length() && isSeparator(var0.charAt(var1))) {
                  ++var1;
               }

               return var0.substring(var1);
            }
         } else {
            for(var1 = 0; var1 < var0.length(); ++var1) {
               switch (var0.charAt(var1)) {
                  case '"':
                  case '\'':
                     throw new InvalidNameException("Unescaped quote in a component");
                  case '\\':
                     ++var1;
                     if (var1 == var0.length()) {
                        throw new InvalidNameException("An escape at the end of a name must be escaped");
                     }
                     break;
                  default:
                     if (isSeparator(var0.charAt(var1))) {
                        return var0.substring(var1 + 1);
                     }
               }
            }

            return "";
         }
      }
   }
}
