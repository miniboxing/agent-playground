package miniboxing.agent
import java.lang.instrument.Instrumentation

object MiniboxingAgent {
  def premain(agentArguments: String, instrumentation: Instrumentation): Unit = {
    println("Miniboxing agent loaded")
    val transformer = new MiniboxingTransformer();
    instrumentation.addTransformer(transformer);
  }
}
