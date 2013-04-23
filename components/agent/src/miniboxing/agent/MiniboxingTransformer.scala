package miniboxing.agent

import java.lang.instrument._
import java.security.ProtectionDomain
import java.io.InputStream
import org.objectweb.asm._
import org.objectweb.asm.tree._
import java.lang.{ClassLoader => JClassLoader}
import java.util.{List => JList}
import scala.collection.JavaConverters.asScalaBufferConverter
import java.util.ListIterator
import scala.collection.mutable.Map
import java.io.PrintWriter
import org.objectweb.asm.util.TraceClassVisitor


class MiniboxingTransformer extends ClassFileTransformer {

  def transform(loader: ClassLoader,
                className: String,
                classBeingRedefined: Class[_],
                protectionDomain: ProtectionDomain,
                classfileBuffer: Array[Byte]): Array[Byte] ={

    if (className.endsWith("_1")) {
      System.err.println("  creating miniboxed class: " + className)
      transformMiniboxedClass(loader, className)
    } else {
//      System.err.println("Normal-transforming: " + className)
      transformNormalClass(classfileBuffer)
    }
  }

  def prepareNormalClass(classfileBuffer: Array[Byte]): Array[Byte] = {
    val cr = new ClassReader(classfileBuffer)
    val classNode = new ClassNode()
    cr.accept(classNode, 0)

    var different = false

    // Patch all the methods
    val methodNodes = classNode.methods.asInstanceOf[JList[MethodNode]].asScala
    for (methodNode <- methodNodes) {
      val insnNodes = methodNode.instructions.iterator().asInstanceOf[ListIterator[AbstractInsnNode]]
      while (insnNodes.hasNext) {
        insnNodes.next match {
          case tinst: TypeInsnNode if tinst.getOpcode() == Opcodes.NEW =>
            if (tinst.desc.endsWith("_J")) {
              different = true
              System.err.println("  rewired NEW: " + tinst.desc + " => " + tinst.desc.replaceAll("_J", "_1"))
              insnNodes.set(new TypeInsnNode(Opcodes.NEW, tinst.desc.replaceAll("_J", "_1")))
            }
          case minst: MethodInsnNode =>
            // patch up constructor call
            if (minst.name == "<init>")
              if (minst.owner.endsWith("_J")) {
                different = true
                System.err.println("  rewired <init>: " + minst.desc + " => " + minst.desc.replaceAll("_J", "_1"))
                minst.owner = minst.owner.replaceAll("_J$", "_1")
              }
          case _ =>
        }
      }
    }

//    if (different) {
//      // Debugging:
//      System.err.println("================================AFTER================================")
//      val printWriter = new PrintWriter(System.err)
//      val traceClassVisitor = new TraceClassVisitor(printWriter)
//      classNode.accept(traceClassVisitor)
//    }

    if (different) {
      val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
      classNode.accept(cw);
      cw.toByteArray
    } else
      classfileBuffer
  }


  def transformMiniboxedClass(loader: ClassLoader,
                              className: String): Array[Byte] ={
    val encodedName = className.replace('.', '/')
    val encodedTplName = encodedName.replaceAll("_[0-9]$", "_J")
    val templateBytes = loader.getResourceAsStream(encodedTplName + ".class");
    prepareMiniboxedClass(templateBytes, encodedTplName, encodedName);
  }

  def transformNormalClass(classfileBuffer: Array[Byte]): Array[Byte] ={
    prepareNormalClass(classfileBuffer)
  }

  def prepareMiniboxedClass(in: InputStream, oldname: String, newname: String): Array[Byte] = {
    val tparam = newname.last.toInt - '0'.toInt
    val cr = new ClassReader(in)
    val classNode = new ClassNode()
    cr.accept(classNode, 0)

    classNode.name = newname

    // Make the type tags static final
    val fieldNodes = classNode.fields.asInstanceOf[JList[FieldNode]].asScala
    for (fieldNode <- fieldNodes if fieldNode.name.endsWith("_TypeTag")) {
      fieldNode.access |= Opcodes.ACC_FINAL;
      // TODO: Extend to more parameters, this only supports one
      fieldNode.value = new Integer(tparam)
    }

    // Patch all the methods
    val methodNodes = classNode.methods.asInstanceOf[JList[MethodNode]].asScala
    for (methodNode <- methodNodes) {
      val insnNodes = methodNode.instructions.iterator().asInstanceOf[ListIterator[AbstractInsnNode]]
      while (insnNodes.hasNext) {
        insnNodes.next match {
          case finst: FieldInsnNode =>
            // update owner to the new class
            finst.owner = finst.owner.replace(oldname, newname) // update names everywhere
            // and patch the code for the static final value
            if (finst.name.endsWith("_TypeTag")) {
              finst.getOpcode match {
                case Opcodes.GETFIELD =>
                  insnNodes.set(new InsnNode(Opcodes.POP));
                  val replNode = tparam match {
                    case 0 => new InsnNode(Opcodes.ICONST_0)
                    case 1 => new InsnNode(Opcodes.ICONST_1)
                    case 2 => new InsnNode(Opcodes.ICONST_2)
                    case 3 => new InsnNode(Opcodes.ICONST_3)
                    case 4 => new InsnNode(Opcodes.ICONST_4)
                    case _ => new IntInsnNode(Opcodes.BIPUSH, tparam)
                  }
                  insnNodes.add(replNode) // Full expansion
                case Opcodes.PUTFIELD =>
                  insnNodes.set(new InsnNode(Opcodes.POP2));
              }
            }
          case tinst: TypeInsnNode if tinst.getOpcode() == Opcodes.NEW =>
            // patch up NEW calls
            if (tinst.desc.endsWith("_J"))
              insnNodes.set(new TypeInsnNode(Opcodes.NEW, tinst.desc.replaceAll("_J$", "_" + tparam)))
          case minst: MethodInsnNode =>
            // update owner to the new class
            minst.owner = minst.owner.replace(oldname, newname) // update names everywhere
            // patch up constructor call
            if (minst.name == "<init>")
              if (minst.owner.endsWith("_J"))
                minst.owner = minst.owner.replaceAll("_J$", "_" + tparam)
          case _ =>
        }
      }
    }

//    // Debugging:
//    System.err.println("================================AFTER================================")
//    val printWriter = new PrintWriter(System.err);
//    val traceClassVisitor = new TraceClassVisitor(printWriter);
//    classNode.accept(traceClassVisitor);

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    classNode.accept(cw);
    var classBytes = cw.toByteArray

    classBytes
  }
}
