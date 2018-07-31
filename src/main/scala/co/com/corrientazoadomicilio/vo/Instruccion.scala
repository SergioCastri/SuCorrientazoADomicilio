package co.com.corrientazoadomicilio.vo

sealed trait Instruccion

object Instruccion {

  def nuevaInstruccion(c:Char):Instruccion ={
    c match {
      case 'A' => A()
      case 'D' => D()
      case 'I' => I()
      case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $c")
    }
  }
}

case class A() extends Instruccion
case class I() extends Instruccion
case class D() extends Instruccion
