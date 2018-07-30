package co.com.corrientazoadomicilio.test
import co.com.corrientazoadomicilio.entidades._
import co.com.corrientazoadomicilio.servicios.{interpreteDeAlgebraSeguirRuta, interpreteHacerEntrega}
import org.scalatest.FunSuite

import scala.concurrent.duration._
import scala.concurrent.Await


class testSuite extends FunSuite {
  test("El dron puede moverse a la derecha"){
    val coordenada = new Coordenada(0,0)
    val car = Cardinalidad.nuevaCardinalidad('N')
    val posicion = new Posicion(coordenada, car)
    val ruta = interpreteDeAlgebraSeguirRuta.moverDerecha(posicion)
    // val res = Await.result(ruta, 10 seconds)
  //  println(res)
    assert(ruta == Posicion(Coordenada(0,0), E()))
  }

  test("El dron puede moverse a la izquierda"){
    val coordenada = new Coordenada(0,0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = new Posicion(coordenada, car)
    val ruta = interpreteDeAlgebraSeguirRuta.moverIzquierda(posicion)
    //val res = Await.result(ruta, 10 seconds)
  //  println(res)
    assert(ruta == Posicion(Coordenada(0,0), E()))
  }

  test("El dron puede moverse hacia adelante"){
    val coordenada = new Coordenada(0,0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = new Posicion(coordenada, car)
    val ruta = interpreteDeAlgebraSeguirRuta.avanzar(posicion)
   // val res = Await.result(ruta, 10 seconds)
    //println(res)
    assert(ruta == Posicion(Coordenada(0,-1), S()))
  }

  test ("un dron debe moverse dependiendo de la intruccion"){
    val ins = Instruccion.nuevaInstruccion('A')
    val posicion = new Posicion(Coordenada(0,0), Cardinalidad.nuevaCardinalidad('N'))
    val res: Posicion = interpreteHacerEntrega.recorrerPorInstruccion(Movimiento(posicion, ins))
    assert(res == Posicion(Coordenada(0,1),N()) )
  }

  test ("un dron debe moverse dependiendo de la intruccion 2"){
    val ins = Instruccion.nuevaInstruccion('I')
    val posicion = new Posicion(Coordenada(0,0), Cardinalidad.nuevaCardinalidad('O'))
    val res: Posicion = interpreteHacerEntrega.recorrerPorInstruccion(Movimiento(posicion, ins))
    assert(res == Posicion(Coordenada(0,0),S()) )
  }

  test("Un dron debe hacer toda una entrega"){
    val listaPosicionF = List(Posicion(Coordenada(0,0), Cardinalidad.nuevaCardinalidad('N')))
    val entrega = "AAAAIAAD"
    val lista = entrega.toList
    val r: List[Posicion] = lista.foldLeft(listaPosicionF){ (resultado, item) =>
      resultado :+ interpreteHacerEntrega.recorrerPorInstruccion(Movimiento(resultado.last, Instruccion.nuevaInstruccion(item)))
    }
    val posicionFinal: Posicion = r.last
    println(posicionFinal)
    println(r)
    assert(posicionFinal == Posicion(Coordenada(-2,4), N()))

  }



}


