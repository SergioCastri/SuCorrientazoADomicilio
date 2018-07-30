package co.com.corrientazoadomicilio.entidades

trait Cardinalidad

object Cardinalidad {

  def nuevaCardinalidad(c:Char):Cardinalidad = {
    c match {
      case 'N' => N()
      case 'S' => S()
      case 'E' => E()
      case 'O' => O()
      case _ => throw new Exception(s"Caracter invalido: $c")
    }
  }
}

case class N() extends Cardinalidad
case class S() extends Cardinalidad
case class E() extends Cardinalidad
case class O() extends Cardinalidad
