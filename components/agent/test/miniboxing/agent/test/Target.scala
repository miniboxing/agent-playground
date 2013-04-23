package miniboxing.agent.test

/**********************************************************************************************************************
 * CLASS C()DE ********************************************************************************************************
 **********************************************************************************************************************/
trait Target[T] { def name: String }

class Target_J[T$sp](T_TypeTag: Byte) extends Target[T$sp] {
  def name = this.getClass().getSimpleName()
}

class Target_L[T$sp](val t: T$sp) extends Target[T$sp] {
  def name = this.getClass().getSimpleName()
}

// these guys are here just for the agent to pick them up
class Target_1[T$sp](T_TypeTag: Byte)
class Target_2[T$sp](T_TypeTag: Byte)
class Target_3[T$sp](T_TypeTag: Byte)
class Target_4[T$sp](T_TypeTag: Byte)
class Target_5[T$sp](T_TypeTag: Byte)
class Target_6[T$sp](T_TypeTag: Byte)
class Target_7[T$sp](T_TypeTag: Byte)
class Target_8[T$sp](T_TypeTag: Byte)
class Target_9[T$sp](T_TypeTag: Byte)
