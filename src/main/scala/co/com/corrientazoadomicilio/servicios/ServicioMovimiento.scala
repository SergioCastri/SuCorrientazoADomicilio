package co.com.corrientazoadomicilio.servicios

import java.util.concurrent.Executors

import co.com.corrientazoadomicilio.entidades.{Coordenada, Posicion}
import co.com.corrientazoadomicilio.entidades.{E, N, O, S}

import scala.concurrent.{ExecutionContext, Future}


sealed trait algebraSeguirRuta{
  /*
  def moverDerecha(pos: Posicion):Future[Posicion]
  def moverIzquierda(pos: Posicion):Future[Posicion]
  def avanzar(pos: Posicion):Future[Posicion]*/
  def moverDerecha(pos: Posicion):Posicion
  def moverIzquierda(pos: Posicion):Posicion
  def avanzar(pos: Posicion):Posicion

}

sealed trait interpreteDeAlgebraSeguirRuta extends algebraSeguirRuta{

 /* implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(3))

  def moverDerecha(pos: Posicion): Future[Posicion] = {
    val md = Future {
      val res  = pos.car match{
        case  N() =>  E()
        case  E() =>  S()
        case  S() =>  O()
        case  O() =>  N()
      }
      val respuesta: Posicion = Posicion(pos.coor, res)
      respuesta
    }(context)
    md
  }

  def moverIzquierda(pos: Posicion): Future[Posicion]= {
    val mi = Future {
      val res  = pos.car match{
        case  N() =>  O()
        case  E() =>  N()
        case  S() =>  E()
        case  O() =>  S()
      }
      val respuesta: Posicion = Posicion(pos.coor, res)
      respuesta
    }(context)
    mi
  }

  def avanzar(pos: Posicion): Future[Posicion] = {
    val aa = Future {
      val res = pos.car match {
        case N() => Coordenada(pos.coor.a, pos.coor.b +1)
        case S() => Coordenada(pos.coor.a, pos.coor.b -1)
        case E() => Coordenada(pos.coor.a +1, pos.coor.b)
        case O() => Coordenada(pos.coor.a -1, pos.coor.b)
      }
      val respuesta = Posicion(res, pos.car)
      respuesta
    }(context)
    aa
  } */

  def moverDerecha(pos: Posicion): Posicion = {

      val res  = pos.car match{
        case  N() =>  E()
        case  E() =>  S()
        case  S() =>  O()
        case  O() =>  N()
      }
      val respuesta: Posicion = Posicion(pos.coor, res)
      respuesta

  }

  def moverIzquierda(pos: Posicion): Posicion= {
      val res  = pos.car match{
        case  N() =>  O()
        case  E() =>  N()
        case  S() =>  E()
        case  O() =>  S()
      }
      val respuesta: Posicion = Posicion(pos.coor, res)
      respuesta

  }

  def avanzar(pos: Posicion): Posicion = {

      val res = pos.car match {
        case N() => Coordenada(pos.coor.a, pos.coor.b +1)
        case S() => Coordenada(pos.coor.a, pos.coor.b -1)
        case E() => Coordenada(pos.coor.a +1, pos.coor.b)
        case O() => Coordenada(pos.coor.a -1, pos.coor.b)
      }
      val respuesta = Posicion(res, pos.car)
      respuesta

  }

}

object interpreteDeAlgebraSeguirRuta extends interpreteDeAlgebraSeguirRuta