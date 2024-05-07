package weblogic.wsee.tools.clientgen.stubgen;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.rpc.JAXRPCException;
import weblogic.jws.Conversation;
import weblogic.wsee.jws.CallbackInterface;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsParameterType;
import weblogic.wsee.util.JamUtil;

public abstract class StubBase extends JavaFileGenBase {
   protected boolean generateAsyncMethod = false;
   protected boolean onlyConvenienceMethod = false;
   protected JsClass endpoint;
   protected String portTypeName;
   protected String serviceClassName;
   private String callbackInterface;
   protected Map portNameMap;
   private Boolean hasCallbackApi = null;
   private JClass callback = null;
   private static String callbackInterfaceMethods = null;

   public void setOnlyConvenienceMethod(boolean var1) {
      this.onlyConvenienceMethod = var1;
   }

   public void setGenerateAsyncMethod(boolean var1) {
      this.generateAsyncMethod = var1;
   }

   public void setEndpoint(JsClass var1) {
      this.endpoint = var1;
   }

   public void setPortTypeName(String var1) {
      this.portTypeName = var1;
   }

   public void setServiceClassName(String var1) {
      this.serviceClassName = var1;
   }

   public void setCallback(JClass var1) {
      this.callback = var1;
   }

   public void setCallbackInterface(String var1) {
      this.callbackInterface = var1;
   }

   protected String getCallbackInterface() {
      return "";
   }

   public void setPortNameMap(Map var1) {
      this.portNameMap = var1;
   }

   protected String firstLetterCap(String var1) {
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1, var1.length());
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

   public String convertInnerClassCallbackName(String var1) {
      String var2 = "";
      if (var1 != null || !var1.equals("")) {
         var2 = var1.replace('$', '.');
      }

      return var2;
   }

   public boolean hasCallbackApi() {
      if (this.hasCallbackApi != null) {
         return this.hasCallbackApi;
      } else {
         this.hasCallbackApi = new Boolean(this.callbackInterface != null);
         return this.hasCallbackApi;
      }
   }

   public String callbackApiPreInvoke() {
      return this.hasCallbackApi() ? "cbapistub.invokePrep();" : "";
   }

   private Class getType(String var1) {
      try {
         if (var1.indexOf("int") != -1) {
            return Integer.TYPE;
         } else if (var1.indexOf("float") != -1) {
            return Float.TYPE;
         } else if (var1.indexOf("long") != -1) {
            return Long.TYPE;
         } else if (var1.indexOf("double") != -1) {
            return Double.TYPE;
         } else if (var1.indexOf("char") != -1) {
            return Character.TYPE;
         } else {
            return var1.indexOf("boolean") != -1 ? Boolean.TYPE : Thread.currentThread().getContextClassLoader().loadClass(var1);
         }
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         throw new IllegalStateException(var3);
      }
   }

   private boolean compareJMethod(JMethod var1, String var2, String[] var3) {
      if (!var2.equals(var1.getSimpleName())) {
         return false;
      } else {
         JParameter[] var4 = var1.getParameters();
         if (var3.length != var4.length) {
            return false;
         } else {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (!var3[var5].equals(var4[var5].getType().getQualifiedName())) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public String finishCallbackStubConversation(JsMethod var1) {
      if (this.callback == null) {
         return "";
      } else {
         String var2 = var1.getMethodName();
         JsParameterType[] var3 = var1.getArguments();
         String[] var4 = new String[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = var3[var5].getType();
         }

         JMethod[] var11 = this.callback.getMethods();
         JMethod[] var6 = var11;
         int var7 = var11.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            JMethod var9 = var6[var8];
            if (this.compareJMethod(var9, var2, var4)) {
               JAnnotation var10 = var9.getAnnotation(Conversation.class);
               if (var10 != null && JamUtil.getAnnotationEnumValue(var10, "value", Conversation.Phase.class, Conversation.Phase.CONTINUE) == Conversation.Phase.FINISH) {
                  return "weblogic.wsee.util.ControlAPIUtil.finishCallbackStubConversation(this);";
               }

               return "";
            }
         }

         return "";
      }
   }

   public String generateCallbackInterfaceMethods() {
      if (!this.hasCallbackApi()) {
         return "";
      } else if (callbackInterfaceMethods != null) {
         return callbackInterfaceMethods;
      } else {
         Class var1 = CallbackInterface.class;
         Method[] var2 = var1.getDeclaredMethods();
         StringBuilder var3 = new StringBuilder();
         var3.append("private weblogic.wsee.callback.CallbackStubImpl cbapistub = new weblogic.wsee.callback.CallbackStubImpl(this);\n");
         if (var2 != null) {
            Method[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Method var7 = var4[var6];
               StringBuilder var8 = new StringBuilder();
               String var9 = var7.toGenericString();
               Class var10 = var7.getReturnType();
               if (var10 != null && !"void".equals(var10.getCanonicalName())) {
                  var8.append("return ");
               }

               var8.append("cbapistub.");
               var8.append(var7.getName());
               var8.append("(");
               var9.indexOf(var1.getName());
               var9 = var9.replaceFirst("abstract", "");
               var9 = var9.replaceFirst(var1.getName() + ".", "");
               int var12 = var9.indexOf("throws");
               String var13 = "";
               if (var12 >= 0) {
                  var13 = var9.substring(var12);
                  var9 = var9.substring(0, var12);
               }

               int var14 = 1;
               StringTokenizer var15 = new StringTokenizer(var9, ",");
               StringBuilder var16 = new StringBuilder();

               String var17;
               while(var15.hasMoreTokens()) {
                  var17 = var15.nextToken();
                  var16.append(var17);
                  if (var15.hasMoreTokens()) {
                     String var18 = "v" + var14++ + ", ";
                     var8.append(var18);
                     var16.append(" ");
                     var16.append(var18);
                  }
               }

               var9 = var16.toString();
               if (var9.indexOf("()") < 0) {
                  var17 = "v" + var14++;
                  var8.append(var17);
                  var9 = var9.replaceAll("\\)", " " + var17 + ")");
               }

               var9 = var9 + var13;
               var8.append(");");
               var3.append(var9);
               var3.append("\n{\n   ");
               var3.append(var8.toString());
               var3.append("\n}\n");
            }
         }

         callbackInterfaceMethods = var3.toString();
         return callbackInterfaceMethods;
      }
   }

   public Class<? extends Throwable> getRemoteExceptionClass() {
      return this.callbackInterface == null ? super.getRemoteExceptionClass() : JAXRPCException.class;
   }
}
