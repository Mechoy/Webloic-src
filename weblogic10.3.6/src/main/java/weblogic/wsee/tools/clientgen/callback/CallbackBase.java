package weblogic.wsee.tools.clientgen.callback;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.source.JsFault;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsParameterType;
import weblogic.wsee.tools.source.JsReturnType;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.tools.wsdlc.jaxrpc.JwsBase;

public abstract class CallbackBase extends JwsBase {
   private ProcessInfo processInfo;

   public void setup(Object var1) {
      if (var1 == null) {
         throw new JAXRPCException("No setup object found");
      } else if (!(var1 instanceof ProcessInfo)) {
         throw new JAXRPCException("Incorrect type of setup object: " + var1.getClass().getName());
      } else {
         this.processInfo = (ProcessInfo)var1;
      }
   }

   protected String wrap(JsParameterType var1) {
      String var2 = var1.getType();
      String var3 = var1.getParamName();
      String var4;
      if (var2.equals("float")) {
         var4 = "new Float(" + var3 + ")";
      } else if (var2.equals("int")) {
         var4 = "new Integer(" + var3 + ")";
      } else if (var2.equals("long")) {
         var4 = "new Long(" + var3 + ")";
      } else if (var2.equals("boolean")) {
         var4 = "new Boolean(" + var3 + ")";
      } else if (var2.equals("short")) {
         var4 = "new Short(" + var3 + ")";
      } else if (var2.equals("double")) {
         var4 = "new Double(" + var3 + ")";
      } else if (var2.equals("byte")) {
         var4 = "new Byte(" + var3 + ")";
      } else {
         var4 = var3;
      }

      return var4;
   }

   public String arguments(JsMethod var1) {
      StringBuffer var2 = new StringBuffer();
      JsParameterType[] var3 = var1.getArguments();
      if (var3 != null && var3.length > 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            JsParameterType var5 = var3[var4];
            var2.append(var5.getType());
            var2.append(" " + var5.getParamName());
            if (var4 < var3.length - 1) {
               var2.append(", ");
            }
         }
      }

      return var2.toString();
   }

   public String exceptions(JsMethod var1) {
      StringBuffer var2 = new StringBuffer("");
      JsFault[] var3 = var1.getFaults();
      if (var3 != null && var3.length > 0) {
         var2.append(", ");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var2.append(var3[var4].getExceptionClass());
            if (var4 < var3.length - 1) {
               var2.append(", ");
            }
         }
      }

      return var2.toString();
   }

   public String getReturnType(JsMethod var1) {
      JsReturnType var2 = var1.getReturnType();
      return var2.getType();
   }

   public String getCallbackQueue() {
      return "@weblogic.wsee.CallbackQueue@";
   }

   public String getServiceUri(String var1) {
      JsService var2 = this.processInfo.getJsCallbackService();
      String var3 = var1;
      if (var2 == null && CallbackUtils.has81StyleCallback(this.processInfo.getJsService())) {
         var2 = this.processInfo.getJsService();
         var3 = var1 + "Callback";
      }

      if (var2 == null) {
         return null;
      } else {
         QName var4 = var2.getWsdlService().getName();
         return CallbackUtils.getServiceUri(var4, var3);
      }
   }

   public String methodName(JsMethod var1) {
      return var1.getMethodName();
   }

   public String argTypes(JsMethod var1) {
      JsParameterType[] var2 = var1.getArguments();
      StringBuffer var4 = new StringBuffer("");
      if (var2 != null && var2.length > 0) {
         var4.append("new Class[]{");

         for(int var5 = 0; var5 < var2.length; ++var5) {
            String var3 = var2[var5].getType();
            var4.append(this.getType(var3));
            if (var5 < var2.length - 1) {
               var4.append(", ");
            }
         }

         var4.append("}");
      } else {
         var4.append("new Class[]{}");
      }

      return var4.toString();
   }

   public String argValues(JsMethod var1) {
      JsParameterType[] var2 = var1.getArguments();
      StringBuffer var3 = new StringBuffer("");
      if (var2 != null && var2.length > 0) {
         var3.append("new Object[]{");

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4].getType();
            var3.append(this.wrapType(var5, var2[var4].getParamName()));
            if (var4 < var2.length - 1) {
               var3.append(", ");
            }
         }

         var3.append("}");
      } else {
         var3.append("new Class[]{}");
      }

      return var3.toString();
   }

   public String returnValue(JsMethod var1) {
      JsReturnType var2 = var1.getReturnType();
      String var3 = "";
      if (!var2.getType().equals("void")) {
         String var4 = var2.getType();
         if (var4.indexOf("int") != -1) {
            var3 = "return ((Integer)_retVal).intValue();";
         } else if (var4.indexOf("float") != -1) {
            var3 = "return ((Float)_retVal).floatValue();";
         } else if (var4.indexOf("long") != -1) {
            var3 = "return ((Long)_retVal).longValue();";
         } else if (var4.indexOf("double") != -1) {
            var3 = "return ((Double)_retVal).doubleValue();";
         } else if (var4.indexOf("char") != -1) {
            var3 = "return ((Character)_retVal).charValue();";
         } else {
            if (var4.indexOf("boolean") == -1) {
               return "return (" + var4 + ")_retVal;";
            }

            var3 = "return ((Boolean)_retVal).booleanValue();";
         }
      }

      return var3;
   }

   private String getType(String var1) {
      String var2 = "";
      if (var1.indexOf("int") != -1) {
         var2 = "Integer.TYPE";
      } else if (var1.indexOf("float") != -1) {
         var2 = "Float.TYPE";
      } else if (var1.indexOf("long") != -1) {
         var2 = "Long.TYPE";
      } else if (var1.indexOf("double") != -1) {
         var2 = "Double.TYPE";
      } else if (var1.indexOf("char") != -1) {
         var2 = "Character.TYPE";
      } else if (var1.indexOf("boolean") != -1) {
         var2 = "Boolean.TYPE";
      } else {
         var2 = var1 + ".class";
      }

      return var2;
   }

   private String wrapType(String var1, String var2) {
      String var3 = "";
      if (var1.indexOf("int") != -1) {
         var3 = "new Integer(" + var2 + ")";
      } else if (var1.indexOf("float") != -1) {
         var3 = "new Float(" + var2 + ")";
      } else if (var1.indexOf("long") != -1) {
         var3 = "new Long(" + var2 + ")";
      } else if (var1.indexOf("double") != -1) {
         var3 = "new Double(" + var2 + ")";
      } else if (var1.indexOf("char") != -1) {
         var3 = "new Character(" + var2 + ")";
      } else if (var1.indexOf("boolean") != -1) {
         var3 = "new Boolean(" + var2 + ")";
      } else {
         var3 = var2;
      }

      return var3;
   }
}
