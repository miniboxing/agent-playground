package miniboxing.agent.test

import org.junit.Test

class MiniboxingAgentTest {

  @Test def testTargetName = {
    val target: Target[Int] = new Target_J(1)
    val targetName = target.name
    val expName = "Target_1"
    assert(targetName == expName, targetName + " != " + expName + " (expected equal)")
  }
}

/**********************************************************************************************************************
 * CLASS C()DE ********************************************************************************************************
 **********************************************************************************************************************/
// interface:
trait Target[T] { def name: String }

// template:
class Target_J[T$sp](T_TypeTag: Byte) extends Target[T$sp] {
  def name = {
    val clazz = this.getClass().getSimpleName()
    System.err.println("Target_J class: " + clazz)
    clazz
  }
}

// empty shells for specialization:
class Target_1[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_2[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_3[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_4[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_5[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_6[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_7[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_8[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_9[T$sp](T_TypeTag: Byte) { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
