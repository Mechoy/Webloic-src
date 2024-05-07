package weblogic.ejb.container.deployer;

import com.bea.objectweb.asm.ClassAdapter;
import com.bea.objectweb.asm.ClassReader;
import com.bea.objectweb.asm.ClassVisitor;
import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;
import weblogic.utils.classloaders.ClassPreProcessor;

/** @deprecated */
@Deprecated
public class RemoteBizIntfClassPreProcessor extends EJBClassEnhancer implements ClassPreProcessor {
   private String remoteBiName;
   private String biName;
   private ClassWriter ricw = null;
   private byte[] result = null;
   private List<String> neededMethods = new ArrayList();

   public RemoteBizIntfClassPreProcessor(String var1, String var2) {
      this.remoteBiName = var1;
      this.biName = var2;
   }

   public void initialize(Hashtable var1) {
   }

   public boolean isContainMethod(String var1) {
      return this.neededMethods.contains(var1);
   }

   public void addMethod(String var1) {
      this.neededMethods.add(var1);
   }

   public byte[] preProcess(String var1, byte[] var2) {
      if (var1.indexOf("_WLStub") > 0) {
         return var2;
      } else {
         ClassReader var3 = new ClassReader(var2);
         ClassWriter var4 = new ClassWriter(var3, 0);
         boolean var5 = false;
         if (var1.equals(this.biName)) {
            this.ricw = new ClassWriter(var3, 0);
            var5 = true;
         }

         Local2RemoteTransformer var6;
         if (var5) {
            var6 = new Local2RemoteTransformer(var4, this.ricw, false);
         } else {
            var6 = new Local2RemoteTransformer(var4, this.ricw, true);
         }

         var3.accept(var6, 0);
         return var4.toByteArray();
      }
   }

   public byte[] postProcess() {
      if (this.result != null) {
         return this.result;
      } else {
         this.ricw.visitEnd();
         this.result = this.ricw.toByteArray();
         this.writeEnhancedClassBack(this.remoteBiName, this.result);
         return this.result;
      }
   }

   public String getPseudoname(String var1) {
      String var2 = "_pseudo_";
      if (var1.contains(".")) {
         var2 = var2 + var1.substring(var1.lastIndexOf(".") + 1, var1.length());
         var2 = var1.substring(0, var1.lastIndexOf(".") + 1) + var2;
      } else {
         var2 = var2 + var1;
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public class Local2RemoteTransformer extends ClassAdapter {
      private static final String REMOTE = "java/rmi/Remote";
      private static final String REMOTE_EXCEPTION = "java/rmi/RemoteException";
      private final String[] EXCEPTIONS = new String[]{"java/rmi/RemoteException"};
      private final ClassVisitor riClassVisitor;
      private final boolean onlyTransferMethod;
      private String currentCName;

      public Local2RemoteTransformer(ClassVisitor var2, ClassVisitor var3, boolean var4) {
         super(var2);
         this.riClassVisitor = var3;
         this.onlyTransferMethod = var4;
      }

      public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
         this.currentCName = var3;
         if (this.onlyTransferMethod) {
            this.cv.visit(var1, var2, var3, var4, var5, var6);
         }

         if (!this.onlyTransferMethod) {
            ArrayList var7 = new ArrayList(1);
            var7.add("java/rmi/Remote");
            String[] var8 = new String[var7.size()];
            var8 = (String[])var7.toArray(var8);
            this.riClassVisitor.visit(var1, var2, RemoteBizIntfClassPreProcessor.this.remoteBiName.replace('.', '/'), var4, var5, var8);
            this.currentCName = RemoteBizIntfClassPreProcessor.this.getPseudoname(RemoteBizIntfClassPreProcessor.this.remoteBiName).replace('.', '/');
            this.cv.visit(var1, var2, this.currentCName, var4, var5, var6);
         }

      }

      public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
         String var6 = var2 + var3;
         if (!var2.equals("<init>") && !var2.equals("<clinit>") && !RemoteBizIntfClassPreProcessor.this.isContainMethod(var6)) {
            RemoteBizIntfClassPreProcessor.this.addMethod(var6);
            return this.riClassVisitor.visitMethod(var1, var2, var3, var4, this.getExceptions(var5));
         } else {
            return this.cv.visitMethod(var1, var2, var3, var4, var5);
         }
      }

      public void visitEnd() {
         super.visitEnd();
         RemoteBizIntfClassPreProcessor.this.writeEnhancedClassBack(this.currentCName, ((ClassWriter)this.cv).toByteArray());
      }

      private final String[] getExceptions(String[] var1) {
         if (var1 != null && var1.length > 0) {
            TreeSet var2 = new TreeSet();
            String[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               var2.add(var6);
            }

            if (!var2.contains("java/rmi/RemoteException")) {
               var2.add("java/rmi/RemoteException");
            }

            var3 = new String[var2.size()];
            var2.toArray(var3);
            return var3;
         } else {
            return this.EXCEPTIONS;
         }
      }
   }
}
