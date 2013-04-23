package miniboxing.agent.test

import org.junit.Test

class MiniboxingAgentTest {

  @Test def testTargetName = {
    val target = new Target_J(3)
    val targetName = target.name
    val expName = "Target_J"
    assert(targetName == expName, targetName + " != " + expName + " (expected equal)")
  }
}

