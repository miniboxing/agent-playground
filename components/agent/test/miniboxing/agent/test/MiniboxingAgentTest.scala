package miniboxing.agent.test

import org.junit.Test
import scala.util.Random

class MiniboxingAgentTest {

  def setTParam(byte: Byte): Unit = ()

  @Test def testTargetName = {
    setTParam(2) // need to statically communicate the type parameter first, but this is not a real problem
    val target: Target[Int] = new Target_J()
    val targetName = target.name
    val expName = "Target_2"

    assert(targetName == expName, targetName + " != " + expName + " (expected equal)")
  }
}

/**********************************************************************************************************************
 * CLASS C()DE ********************************************************************************************************
 **********************************************************************************************************************/
// interface:
trait Target[T] { def name: String }

// template:
class Target_J[T$sp]() extends Target[T$sp] {
  def name = {
    val clazz1 = this.getClass().getSimpleName()
    val clazz2 = (new Target_J()).getClass().getSimpleName()

    // check new rewiring:
    assert(clazz1 == clazz2)

    clazz1
  }
}

// empty shells for specialization:
class Target_1 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_2 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_3 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_4 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_5 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_6 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_7 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_8 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
class Target_9 { /* NOTHING HERE, THIS IS JUST AN EMPTY SHELL */ }
