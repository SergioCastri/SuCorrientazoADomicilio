package co.com.corrientazoadomicilio.vo

import scala.util.{Failure, Try}

sealed trait Instruccion

object Instruccion {

  def nuevaInstruccion(c:Char):Try[Instruccion] ={
    c match {
      case 'A' => Try(A())
      case 'D' => Try(D())
      case 'I' => Try(I())
      case _ => Failure(new IllegalArgumentException(s"Caracter invalido para creacion de instruccion: $c"))
    }
  }
}

case class A() extends Instruccion
case class I() extends Instruccion
case class D() extends Instruccion
