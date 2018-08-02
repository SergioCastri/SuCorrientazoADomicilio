package co.com.corrientazoadomicilio.entidades

import co.com.corrientazoadomicilio.vo

import scala.util.{Failure, Try}

case class Dron(capacidad: Int, posicion : vo.Posicion, identidad:Int)


/*
object Dron{
  def crearDron(capacidad: Int, posicion : vo.Posicion, identidad:Int): Either[String, Dron] ={
      if (posicion.coor.a > 10 || posicion.coor.b > 10) Left("Esta coordenada supera el margen")
      else Right(Dron(capacidad, posicion, identidad)
    )
  }
}*/

object Dron{
  def crearDron(capacidad: Int, posicion : vo.Posicion, identidad:Int): Try[Dron] ={
    if (posicion.coor.a > 10 || posicion.coor.b > 10) Failure(new Exception ("EL DRON NO SE PUEDE CREAR EN ESTA COORDENADA"))
    else Try(Dron(capacidad, posicion, identidad))

  }
}