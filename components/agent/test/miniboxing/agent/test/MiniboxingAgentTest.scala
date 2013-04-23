package miniboxing.agent.test

import org.junit.Test

class Target_J(T$Type: Int)

class MiniboxingAgentTest {

  @Test def testTargetName = {
    val target = new Target_J(3)
    val targetName = target.getClass().getSimpleName()
    val expName = "Target_J"
    assert(targetName == expName, targetName + " != " + expName + " (expected equal)")
  }
}

