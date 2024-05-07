package weblogic.corba.rmic;

import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import javax.rmi.CORBA.Stub;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA_2_3.portable.InputStream;
import weblogic.corba.utils.RemoteInfo;
import weblogic.iiop.IDLUtils;
import weblogic.iiop.Utils;
import weblogic.kernel.Kernel;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.utils.AssertionError;
import weblogic.utils.Getopt2;
import weblogic.utils.classfile.ClassFile;
import weblogic.utils.classfile.CodeAttribute;
import weblogic.utils.classfile.CodeGenHelper;
import weblogic.utils.classfile.MethodInfo;
import weblogic.utils.classfile.Scope;
import weblogic.utils.classfile.Type;
import weblogic.utils.classfile.cp.CPFieldref;
import weblogic.utils.classfile.cp.CPMethodref;
import weblogic.utils.classfile.cp.ConstantPool;
import weblogic.utils.classfile.expr.AssignStatement;
import weblogic.utils.classfile.expr.CastExpression;
import weblogic.utils.classfile.expr.CatchExceptionExpression;
import weblogic.utils.classfile.expr.CompoundStatement;
import weblogic.utils.classfile.expr.Const;
import weblogic.utils.classfile.expr.ConstStringExpression;
import weblogic.utils.classfile.expr.Expression;
import weblogic.utils.classfile.expr.ExpressionStatement;
import weblogic.utils.classfile.expr.IfStatement;
import weblogic.utils.classfile.expr.InvokeSpecialExpression;
import weblogic.utils.classfile.expr.LocalVariableExpression;
import weblogic.utils.classfile.expr.MemberVarExpression;
import weblogic.utils.classfile.expr.NewArrayExpression;
import weblogic.utils.classfile.expr.NewExpression;
import weblogic.utils.classfile.expr.ReturnStatement;
import weblogic.utils.classfile.expr.Statement;
import weblogic.utils.classfile.expr.ThrowStatement;
import weblogic.utils.classfile.expr.TryCatchStatement;
import weblogic.utils.reflect.MethodSignatureBuilder;

public class StubGenerator extends ClassFile {
   private static final String STUB_PACKAGE_PREFIX = "org.omg.stub.";
   public static final String IIOP = "iiop";
   public static final String IIOP_DIRECTORY = "iiopDirectory";
   private static final String TYPE_IDS_FIELD = "_type_ids";
   private final Class remoteInterface;
   private final RuntimeDescriptor descriptor;
   private final Class[] allInterfaces;
   private static boolean initialized;
   private static Method REQUEST_METHOD;
   private static Method INVOKE_METHOD;
   private static Method RELEASE_REPLY_METHOD;
   private Scope scope;
   private static final Expression[] VOIDPARAMS = new Expression[0];
   private static Class INPUT_STREAM_23 = InputStream.class;

   public StubGenerator(Class var1) {
      init();
      this.remoteInterface = var1;
      this.descriptor = RemoteInfo.findRemoteInfo(var1).getDescriptor();
      this.setClassName(this.generateClassName());
      this.setSuperClassName(Stub.class.getName());
      this.allInterfaces = getAllInterfaces(this.remoteInterface);

      for(int var2 = 0; var2 < this.allInterfaces.length; ++var2) {
         this.addInterface(this.allInterfaces[var2].getName());
      }

      this.addDefaultConstructor();
      this.addIds();
      this.addGetIdsMethod();
      this.addMethods();
   }

   private String generateClassName() {
      String var1 = this.remoteInterface.getName();
      if (var1.startsWith("javax.") || var1.startsWith("java.")) {
         var1 = "org.omg.stub." + var1;
      }

      int var2 = var1.lastIndexOf(".");
      if (var2 == -1) {
         return "_" + this.remoteInterface.getName() + "_Stub";
      } else {
         String var3 = var1.substring(0, var2 + 1);
         String var4 = var1.substring(var2 + 1, var1.length());
         return var3 + "_" + var4 + "_Stub";
      }
   }

   static synchronized void init() {
      if (!initialized) {
         try {
            Class[] var0 = new Class[]{String.class, Boolean.TYPE};
            REQUEST_METHOD = ObjectImpl.class.getMethod("_request", var0);
            var0 = new Class[]{OutputStream.class};
            INVOKE_METHOD = ObjectImpl.class.getMethod("_invoke", var0);
            var0 = new Class[]{org.omg.CORBA.portable.InputStream.class};
            RELEASE_REPLY_METHOD = ObjectImpl.class.getMethod("_releaseReply", var0);
         } catch (NoSuchMethodException var1) {
            throw new AssertionError(var1);
         }

         initialized = true;
      }
   }

   private static Class[] getAllInterfaces(Class[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         addSuperRemoteInterfaces(var0[var2], var1);
      }

      return (Class[])((Class[])var1.toArray(new Class[var1.size()]));
   }

   public static Class[] getAllInterfaces(Class var0) {
      HashSet var1 = new HashSet();
      addSuperRemoteInterfaces(var0, var1);
      return (Class[])((Class[])var1.toArray(new Class[var1.size()]));
   }

   private static void addSuperRemoteInterfaces(Class var0, HashSet var1) {
      if (isRemoteInterface(var0) && !var1.contains(var0)) {
         var1.add(var0);
      }

      if (var0.getSuperclass() != null) {
         addSuperRemoteInterfaces(var0.getSuperclass(), var1);
      }

      Class[] var2 = var0.getInterfaces();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (isRemoteInterface(var2[var3]) && !var1.contains(var2[var3])) {
            var1.add(var2[var3]);
            addSuperRemoteInterfaces(var2[var3], var1);
         }
      }

   }

   private static boolean isRemoteInterface(Class var0) {
      return var0.isInterface() && Remote.class.isAssignableFrom(var0) && !var0.equals(Remote.class);
   }

   private void addDefaultConstructor() {
      MethodInfo var1 = this.addMethod("<init>", "()V", 1);
      CodeAttribute var2 = var1.getCodeAttribute();
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/CORBA/Stub", "<init>", "()V");
      var2.setCode(new ReturnStatement(new InvokeSpecialExpression(var3, Const.THIS, VOIDPARAMS)));
   }

   private void addIds() {
      this.addField("_type_ids", "[Ljava/lang/String;", 10);
      MethodInfo var1 = this.addMethod("<clinit>", "()V", 8);
      CodeAttribute var2 = var1.getCodeAttribute();
      CPFieldref var3 = this.cp.getFieldref(this.getClassName(), "_type_ids", "[Ljava/lang/String;");
      int var4 = isRemoteInterface(this.remoteInterface) ? 0 : 1;
      Expression[] var5 = new Expression[this.allInterfaces.length + var4];
      if (var4 > 0) {
         var5[0] = Const.get(IDLUtils.getTypeID(this.remoteInterface));
      }

      for(int var6 = 0; var6 < this.allInterfaces.length; ++var6) {
         var5[var6 + var4] = Const.get(IDLUtils.getTypeID(this.allInterfaces[var6]));
      }

      CompoundStatement var7 = new CompoundStatement();
      var7.add(new AssignStatement(new MemberVarExpression(var3), new NewArrayExpression(String.class, var5)));
      var7.add(new ReturnStatement());
      var2.setCode(var7);
   }

   private void addGetIdsMethod() {
      MethodInfo var1 = this.addMethod("_ids", "()[Ljava/lang/String;", 1);
      CodeAttribute var2 = var1.getCodeAttribute();
      ConstantPool var3 = var1.getConstantPool();
      CPFieldref var4 = var3.getFieldref(this.getClassName(), "_type_ids", "[Ljava/lang/String;");
      new ReturnStatement(new MemberVarExpression(var4));
      var2.setCode(new ReturnStatement(new MemberVarExpression(var4)));
   }

   public void addMethods() {
      if (this.remoteInterface.isInterface()) {
         Method[] var1 = this.remoteInterface.getMethods();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.addMethod(var1[var2]);
         }
      } else {
         HashSet var6 = new HashSet();
         Class[] var7 = this.remoteInterface.getInterfaces();

         for(int var3 = 0; var3 < var7.length; ++var3) {
            if (isRemoteInterface(var7[var3])) {
               Method[] var4 = var7[var3].getMethods();

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (!var6.contains(var4[var5])) {
                     var6.add(var4[var5]);
                     this.addMethod(var4[var5]);
                  }
               }
            }
         }
      }

   }

   private void addMethod(Method var1) {
      int var2 = var1.getModifiers();
      var2 &= 7;
      var2 |= 16;
      MethodInfo var3 = this.addMethod(var1, var2);
      this.scope = var3.getScope();
      var3.addException(this.cp.getClass("java/rmi/RemoteException"));
      var3.getCodeAttribute().setCode(this.getCodeForMethod(var1));
   }

   private Statement getCodeForMethod(Method var1) {
      CompoundStatement var2 = new CompoundStatement();
      var2.add(this.generateOuterTryCatch(var1));
      var2.add(new ReturnStatement());
      return var2;
   }

   private Statement generateOuterTryCatch(Method var1) {
      TryCatchStatement var2 = new TryCatchStatement();
      var2.setBody(this.generateInnerTryCatch(var1));
      var2.addHandler("org/omg/CORBA/SystemException", this.generateHandleSystemException());
      return var2;
   }

   private Statement generateInnerTryCatch(Method var1) {
      LocalVariableExpression var2 = this.scope.createLocalVar(Type.OBJECT);
      CompoundStatement var3 = new CompoundStatement();
      var3.add(new AssignStatement(var2, Const.NULL));
      TryCatchStatement var4 = new TryCatchStatement();
      var4.setBody(this.genMarshalingCode(var1, var2));
      var4.addHandler("org/omg/CORBA/portable/ApplicationException", this.generateHandleApplicationException(var1, var2));
      var4.addHandler("org/omg/CORBA/portable/RemarshalException", this.generateHandleRemarshalException(var1));
      var4.setFinally(this.generateReleaseReply(var2));
      var3.add(var4);
      return var3;
   }

   private boolean isOneway(Method var1) {
      return this.descriptor == null ? false : this.descriptor.getClientMethodDescriptor(MethodSignatureBuilder.compute(var1)).isOneway();
   }

   private boolean hasNonPrimitiveParam(Class[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!var1[var2].isPrimitive()) {
            return true;
         }
      }

      return false;
   }

   private Statement genMarshalingCode(Method var1, LocalVariableExpression var2) {
      Class[] var3 = var1.getParameterTypes();
      Expression[] var4 = this.scope.getArgs();
      CompoundStatement var5 = new CompoundStatement();
      LocalVariableExpression var6 = this.scope.createLocalVar(Type.OBJECT);
      Object var7 = this.getRequest(var1);
      if (this.hasNonPrimitiveParam(var3)) {
         var7 = new CastExpression(org.omg.CORBA_2_3.portable.OutputStream.class, (Expression)var7);
      }

      var5.add(new AssignStatement(var6, (Expression)var7));

      for(int var8 = 0; var8 < var3.length; ++var8) {
         var5.add(new ExpressionStatement(this.genWriteParam(var6, var3[var8], var4[var8])));
      }

      Object var10 = this.getInvoke(var6);
      Class var9 = var1.getReturnType();
      if (var9 != Void.TYPE && !this.isOneway(var1)) {
         if (!var9.isPrimitive()) {
            var10 = new CastExpression(INPUT_STREAM_23, (Expression)var10);
         }

         var5.add(new AssignStatement(var2, (Expression)var10));
         var5.add(new ReturnStatement(this.genReadParam(var2, var9)));
         return var5;
      } else {
         var5.add(new ExpressionStatement((Expression)var10));
         return var5;
      }
   }

   private Expression genWriteParam(Expression var1, Class var2, Expression var3) {
      if (var2.isPrimitive()) {
         return this.genWritePrimitive(var1, var2, var3);
      } else if (!Remote.class.isAssignableFrom(var2) && !org.omg.CORBA.Object.class.isAssignableFrom(var2)) {
         if (!var2.equals(Object.class) && !var2.equals(Serializable.class) && !var2.equals(Externalizable.class)) {
            return Utils.isAbstractInterface(var2) ? this.genWriteAbstractInterface(var1, var3) : this.genWriteValue(var1, var2, var3);
         } else {
            return this.genWriteAny(var1, var3);
         }
      } else {
         return this.genWriteRemote(var1, var3);
      }
   }

   private Expression genReadParam(Expression var1, Class var2) {
      if (var2.isPrimitive()) {
         return this.genReadPrimitive(var1, var2);
      } else if (!var2.equals(Object.class) && !var2.equals(Serializable.class) && !var2.equals(Externalizable.class)) {
         if (!Remote.class.isAssignableFrom(var2) && !org.omg.CORBA.Object.class.isAssignableFrom(var2)) {
            return Utils.isAbstractInterface(var2) ? this.genReadAbstractInterface(var1, var2) : this.genReadValue(var1, var2);
         } else {
            return this.genReadRemote(var1, var2);
         }
      } else {
         return this.genReadAny(var1, var2);
      }
   }

   private Expression genWritePrimitive(Expression var1, Class var2, Expression var3) {
      CPMethodref var4 = null;
      if (var2 == Integer.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_long", "(I)V");
      } else if (var2 == Byte.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_octet", "(B)V");
      } else if (var2 == Boolean.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_boolean", "(Z)V");
      } else if (var2 == Short.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_short", "(S)V");
      } else if (var2 == Long.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_longlong", "(J)V");
      } else if (var2 == Float.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_float", "(F)V");
      } else if (var2 == Character.TYPE) {
         var4 = this.cp.getMethodref(OutputStream.class, "write_wchar", "(C)V");
      } else {
         if (var2 != Double.TYPE) {
            throw new AssertionError("Unknown primitive: " + var2);
         }

         var4 = this.cp.getMethodref(OutputStream.class, "write_double", "(D)V");
      }

      return var4.invoke(var1, new Expression[]{var3});
   }

   private Expression genReadPrimitive(Expression var1, Class var2) {
      CPMethodref var3 = null;
      if (var2 == Integer.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_long", "()I");
      } else if (var2 == Byte.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_octet", "()B");
      } else if (var2 == Boolean.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_boolean", "()Z");
      } else if (var2 == Short.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_short", "()S");
      } else if (var2 == Long.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_longlong", "()J");
      } else if (var2 == Float.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_float", "()F");
      } else if (var2 == Character.TYPE) {
         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_wchar", "()C");
      } else {
         if (var2 != Double.TYPE) {
            throw new AssertionError("Unknown primitive: " + var2);
         }

         var3 = this.cp.getMethodref(org.omg.CORBA.portable.InputStream.class, "read_double", "()D");
      }

      return var3.invoke(var1, VOIDPARAMS);
   }

   private Expression genWriteRemote(Expression var1, Expression var2) {
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/CORBA/Util", "writeRemoteObject", "(Lorg/omg/CORBA/portable/OutputStream;Ljava/lang/Object;)V");
      return var3.invokeStatic(new Expression[]{var1, var2});
   }

   private Expression genReadRemote(Expression var1, Class var2) {
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/PortableRemoteObject", "narrow", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
      return new CastExpression(var2, var3.invokeStatic(new Expression[]{this.genReadObject(var1, var2), Const.get(var2)}));
   }

   private Expression genWriteAny(Expression var1, Expression var2) {
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/CORBA/Util", "writeAny", "(Lorg/omg/CORBA/portable/OutputStream;Ljava/lang/Object;)V");
      return var3.invokeStatic(new Expression[]{var1, var2});
   }

   private Expression genWriteAbstractInterface(Expression var1, Expression var2) {
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/CORBA/Util", "writeAbstractObject", "(Lorg/omg/CORBA/portable/OutputStream;Ljava/lang/Object;)V");
      return var3.invokeStatic(new Expression[]{var1, var2});
   }

   private Expression genReadAny(Expression var1, Class var2) {
      CPMethodref var3 = this.cp.getMethodref("javax/rmi/CORBA/Util", "readAny", "(Lorg/omg/CORBA/portable/InputStream;)Ljava/lang/Object;");
      return new CastExpression(var2, var3.invokeStatic(new Expression[]{var1}));
   }

   private Expression genReadAbstractInterface(Expression var1, Class var2) {
      CPMethodref var3 = this.cp.getMethodref("org/omg/CORBA_2_3/portable/InputStream", "read_abstract_interface", "()Ljava/lang/Object;");
      return new CastExpression(var2, var3.invoke(var1, VOIDPARAMS));
   }

   private Expression genReadObject(Expression var1, Class var2) {
      CPMethodref var3 = this.cp.getMethodref("org/omg/CORBA/portable/InputStream", "read_Object", "()Lorg/omg/CORBA/Object;");
      return new CastExpression(var2, var3.invoke(var1, VOIDPARAMS));
   }

   private Expression genWriteValue(Expression var1, Class var2, Expression var3) {
      CPMethodref var4 = this.cp.getMethodref("org/omg/CORBA_2_3/portable/OutputStream", "write_value", "(Ljava/io/Serializable;Ljava/lang/Class;)V");
      return var4.invoke(var1, new Expression[]{var3, Const.get(var2)});
   }

   private Expression genReadValue(Expression var1, Class var2) {
      CPMethodref var3 = this.cp.getMethodref("org/omg/CORBA_2_3/portable/InputStream", "read_value", "(Ljava/lang/Class;)Ljava/io/Serializable;");
      return new CastExpression(var2, var3.invoke(var1, new Expression[]{Const.get(var2)}));
   }

   private Statement generateHandleSystemException() {
      CPMethodref var1 = this.cp.getMethodref("javax/rmi/CORBA/Util", "mapSystemException", "(Lorg/omg/CORBA/SystemException;)Ljava/rmi/RemoteException;");
      CompoundStatement var2 = new CompoundStatement();
      LocalVariableExpression var3 = this.scope.createLocalVar(Type.OBJECT);
      var2.add(new AssignStatement(var3, new CatchExceptionExpression()));
      var2.add(new ThrowStatement(var1.invokeStatic(new Expression[]{var3})));
      this.scope.freeLocalVar(var3);
      return var2;
   }

   private Statement generateHandleApplicationException(Method var1, LocalVariableExpression var2) {
      CPMethodref var3 = this.cp.getMethodref("org/omg/CORBA/portable/ApplicationException", "getInputStream", "()Lorg/omg/CORBA/portable/InputStream;");
      CPMethodref var4 = this.cp.getMethodref("org/omg/CORBA/portable/InputStream", "read_string", "()Ljava/lang/String;");
      CPMethodref var5 = this.cp.getMethodref("java/rmi/UnexpectedException", "<init>", "(Ljava/lang/String;)V");
      CompoundStatement var6 = new CompoundStatement();
      LocalVariableExpression var7 = this.scope.createLocalVar(Type.OBJECT);
      var6.add(new AssignStatement(var7, new CatchExceptionExpression()));
      var6.add(new AssignStatement(var2, new CastExpression(INPUT_STREAM_23, var3.invoke(var7, VOIDPARAMS))));
      LocalVariableExpression var8 = this.scope.createLocalVar(Type.OBJECT);
      var6.add(new AssignStatement(var8, var4.invoke(var2, VOIDPARAMS)));
      Class[] var9 = this.getCheckedExceptions(var1);
      var6.add(this.genCheckedExceptions(var9, var2, var8));
      var6.add(new ThrowStatement(new NewExpression(var5, new Expression[]{var8})));
      this.scope.freeLocalVar(var7);
      return var6;
   }

   private Statement genCheckedExceptions(Class[] var1, Expression var2, Expression var3) {
      CompoundStatement var4 = new CompoundStatement();
      CPMethodref var5 = this.cp.getMethodref("java/lang/Object", "equals", "(Ljava/lang/Object;)Z");

      for(int var6 = 0; var6 < var1.length; ++var6) {
         ConstStringExpression var7 = Const.get(Utils.getIDFromException(var1[var6]));
         var4.add(new IfStatement(var5.invoke(var3, new Expression[]{var7}), new ThrowStatement(new CastExpression(var1[var6], this.genReadValue(var2, var1[var6])))));
      }

      return var4;
   }

   private Class[] getCheckedExceptions(Method var1) {
      Class[] var2 = var1.getExceptionTypes();
      int var3 = 0;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (this.isCheckedException(var2[var4])) {
            ++var3;
         } else {
            var2[var4] = null;
         }
      }

      Class[] var6 = new Class[var3];
      var3 = 0;

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (var2[var5] != null) {
            var6[var3++] = var2[var5];
         }
      }

      return var6;
   }

   private boolean isCheckedException(Class var1) {
      return !RemoteException.class.isAssignableFrom(var1);
   }

   private Statement generateHandleRemarshalException(Method var1) {
      CPMethodref var2 = this.cp.getMethodref(this.getClassName(), var1.getName(), CodeGenHelper.getMethodDescriptor(var1));
      CompoundStatement var3 = new CompoundStatement();
      var3.add(new ExpressionStatement(new CatchExceptionExpression()));
      if (var1.getReturnType() == Void.TYPE) {
         var3.add(new ExpressionStatement(var2.invoke(this.scope.getArgs())));
      } else {
         var3.add(new ReturnStatement(var2.invoke(this.scope.getArgs())));
      }

      return var3;
   }

   private Statement generateReleaseReply(LocalVariableExpression var1) {
      CPMethodref var2 = this.cp.getMethodref(RELEASE_REPLY_METHOD);
      Expression[] var3 = new Expression[]{var1};
      return new ExpressionStatement(var2.invoke(var3));
   }

   private Expression getRequest(Method var1) {
      CPMethodref var2 = this.cp.getMethodref(REQUEST_METHOD);
      Expression[] var3 = new Expression[]{this.getMangledName(var1), Const.get(!this.isOneway(var1))};
      return var2.invoke(var3);
   }

   private Expression getMangledName(Method var1) {
      return Const.get(IDLMangler.getMangledMethodName(var1, this.remoteInterface));
   }

   private Expression getInvoke(Expression var1) {
      CPMethodref var2 = this.cp.getMethodref(INVOKE_METHOD);
      Expression[] var3 = new Expression[]{var1};
      return var2.invoke(var3);
   }

   public static void addOpts(Getopt2 var0) {
      var0.addFlag("iiop", "Generate iiop stubs from servers");
      var0.addOption("iiopDirectory", "directory", "Specify the directory where IIOP proxy classes will be written (overrides target directory)");
   }

   private static void ensureDirectoryExists(String var0, String var1) {
      String var2 = "";
      int var3 = var1.lastIndexOf(File.separatorChar);
      if (var3 != -1) {
         var2 = var1.substring(0, var3);
      }

      String var4 = var0 + File.separatorChar + var2;
      File var5 = new File(var4);
      var5.mkdirs();
   }

   public static void generate(Getopt2 var0) throws Exception {
      String var1 = var0.getOption("iiopDirectory", (String)null);
      if (var1 == null) {
         var1 = var0.getOption("d", (String)null);
      }

      if (var1 == null) {
         var1 = ".";
      }

      Kernel.ensureInitialized();
      String[] var2 = var0.args();
      Class[] var3 = new Class[var2.length];
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      if (null == var4) {
         var4 = StubGenerator.class.getClassLoader();
      }

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var3[var5] = Class.forName(var2[var5], true, var4);
      }

      Class[] var9 = getAllInterfaces(var3);

      for(int var6 = 0; var6 < var9.length; ++var6) {
         StubGenerator var7 = new StubGenerator(var9[var6]);
         String var8 = var7.getClassName().replace('.', File.separatorChar) + ".class";
         ensureDirectoryExists(var1, var8);
         var7.write(new FileOutputStream(var1 + File.separatorChar + var8));
      }

   }

   public static void main(String[] var0) {
      try {
         Getopt2 var1 = new Getopt2();
         var1.grok(var0);
         generate(var1);
      } catch (Exception var2) {
         System.err.println("ERROR: " + var2);
         var2.printStackTrace();
      }

   }
}
