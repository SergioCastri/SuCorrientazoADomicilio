package co.com.corrientazoadomicilio.servicios
import java.util.concurrent.Executors
import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo.Cardinalidad.{E, N, O, S}
import co.com.corrientazoadomicilio.vo.{Coordenada, Posicion}
import scala.concurrent.ExecutionContext
import scala.util.Try


sealed trait algebraMovimientoDron{
  def moverDerecha(dron: Dron): Try[Dron]
  def moverIzquierda(dron: Dron):Try[Dron]
  def avanzar(dron: Dron):Try[Dron]


}

sealed trait interpreteDeAlgebraMovimientoDron extends algebraMovimientoDron {

  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  def moverDerecha(dron: Dron): Try[Dron] = {
    val res = dron.posicion.car match {
      case N() => E()
      case E() => S()
      case S() => O()
      case O() => N()
    }
    val respuesta = Dron.crearDron(dron.capacidad, Posicion(dron.posicion.coor, res), dron.identidad)
    respuesta
  }

  def moverIzquierda(dron: Dron): Try[Dron] = {
    val res = dron.posicion.car match {
      case N() => O()
      case E() => N()
      case S() => E()
      case O() => S()
    }
    val respuesta = Dron.crearDron(dron.capacidad, Posicion(dron.posicion.coor, res), dron.identidad)
    respuesta
  }


  def avanzar(dron: Dron): Try[Dron] = {
    val res = dron.posicion.car match {
      case N() => Coordenada(dron.posicion.coor.a, dron.posicion.coor.b + 1)
      case S() => Coordenada(dron.posicion.coor.a, dron.posicion.coor.b - 1)
      case E() => Coordenada(dron.posicion.coor.a + 1, dron.posicion.coor.b)
      case O() => Coordenada(dron.posicion.coor.a - 1, dron.posicion.coor.b)
    }
    val respuesta = Dron.crearDron(dron.capacidad, Posicion(res, dron.posicion.car), dron.identidad)
    respuesta
  }

}


object interpreteMovimientoDron extends interpreteDeAlgebraMovimientoDron
