package miniboxing.agent

import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.IllegalClassFormatException
import java.security.ProtectionDomain

class MiniboxingTransformer extends ClassFileTransformer {

  def transform(loader: ClassLoader,
                className: String,
                classBeingRedefined: Class[_],
                protectionDomain: ProtectionDomain,
                classfileBuffer: Array[Byte]): Array[Byte] ={

    println("Transforming: " + className)
    classfileBuffer
  }
}
