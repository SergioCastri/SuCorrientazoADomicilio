package co.com.corrientazoadomicilio.test
import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo._
import co.com.corrientazoadomicilio.servicios.{InterpreteAlgebraArchivo, interpreteDeAlgebraSeguirRuta, interpreteHacerEntrega}
import co.com.corrientazoadomicilio.vo
import org.scalatest.FunSuite

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source


class testSuite extends FunSuite {
  test("El dron puede moverse a la derecha") {
    val coordenada =  Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('N')
    val posicion = Posicion(coordenada, car)
    val dron =  Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.moverDerecha(dron)
    val res = Await.result(ruta, 10 seconds)
    //  println(res)
    assert(res == Dron(10,Posicion(Coordenada(0, 0), E()),1))
  }

  test("El dron puede moverse a la izquierda") {
    val coordenada = Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = Posicion(coordenada, car)
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.moverIzquierda(dron)
    val res = Await.result(ruta, 10 seconds)
    //  println(res)
    assert(res == Dron(10,Posicion(Coordenada(0, 0), E()),1))
  }

  test("El dron puede moverse hacia adelante") {
    val coordenada = Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = Posicion(coordenada, car)
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.avanzar(dron)
    val res = Await.result(ruta, 10 seconds)
    //println(res)
    assert(res == Dron(10,Posicion(Coordenada(0, -1), S()),1))
  }

  test("un dron debe moverse dependiendo de la intruccion") {
    val ins = Instruccion.nuevaInstruccion('A')
    val posicion = new Posicion(Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N'))
    val dron = new Dron(10, posicion, 1)
    val res = interpreteHacerEntrega.recorrerPorInstruccion(dron, ins)
    val res2 = Await.result(res, 10 seconds)
    assert(res2 == Dron(10,Posicion(Coordenada(0, 1), N()),1))
  }

  test("un dron debe moverse dependiendo de la intruccion 2") {
    val ins = Instruccion.nuevaInstruccion('I')
    val posicion = Posicion(vo.Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('O'))
    val dron = Dron(10, posicion, 1)
    val res = interpreteHacerEntrega.recorrerPorInstruccion(dron, ins)
    val res2 = Await.result(res, 10 seconds)

    assert(res2 == Dron(10,Posicion(Coordenada(0, 0), S()),1))
  }


  test("Un dron debe hacer toda una entrega") {
    val posicionF = Posicion(Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N'))
    val dron = Dron(10, posicionF, 1)
    val entrega: Entrega = Entrega(List(A(),A(),A(),A(),I(),A(),A(),D()))
    val res = interpreteHacerEntrega.seguirEntrega(entrega, dron)
    val res2 = Await.result(res, 10 seconds)
    assert(res2 == Dron(10,Posicion(vo.Coordenada(-2, 4), N()),1))

  }

  test ("un dron debe hacer todo el recorrido"){
    val posicionF = Posicion(Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N'))
    val dron = Dron(10, posicionF, 1)
    val ruta = InterpreteAlgebraArchivo.leerArchivo("/home/s4n/Documents/corrientazo-domicilio/src/test/resources/prueba.txt")
    val res: List[Future[Dron]] = interpreteHacerEntrega.seguirRuta(ruta,dron)
    val res3 = res.map(x=>Await.result(x, 10 seconds)).tail
    assert(res3 == List(Dron(10,Posicion(Coordenada(-2,4),N()),1), Dron(10,Posicion(Coordenada(-1,3),S()),1), Dron(10,Posicion(Coordenada(0,0),O()),1)))
  }
  /*

  test("Un dron debe hacer toda una ruta, partiendo desde el origen para cada entrega") {
    val listaPosicionF = List(vo.Posicion(vo.Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N')))
    val entrega1 = "AAAAIAAD"
    val entrega2 = "DDAIAD"
    val entrega3 = "AAIADAD"
    val lista1 = entrega1.toList
    val lista2 = entrega2.toList
    val lista3 = entrega3.toList
    val listaRuta: List[List[Char]] = List(lista1, lista2, lista3)
    val r = interpreteHacerEntrega.r
    //println(r)
    assert(posicionFinal == List(vo.Posicion(vo.Coordenada(-2, 4), N()), vo.Posicion(vo.Coordenada(1, -1), S()), vo.Posicion(vo.Coordenada(-1, 3), E())))

  }










  test("Un dron debe hacer toda una ruta, partiendo desde la posicion final para cada entrega") {
    val listaPosicionI = List(Posicion(Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N')))
    val entrega1 = "AAAAIAAD"
    val entrega2 = "DDAIAD"
    val entrega3 = "AAIADAD"

    val listaRuta = List(entrega1, entrega2, entrega3)
    val res = interpreteHacerEntrega.seguirRuta(listaRuta)
    assert(res == List(Posicion(Coordenada(-1,3),S()), Posicion(Coordenada(-3,7),E())))

  }
*/
  test ("el programa debe leer un y convertirlo a ruta"){
    val ruta= InterpreteAlgebraArchivo.leerArchivo("/home/s4n/Documents/corrientazo-domicilio/src/test/resources/prueba.txt")
    assert(ruta == Ruta(List(Entrega(List(A(), A(), A(), A(), I(), A(), A(), D())), Entrega(List(D(), D(), A(), I(), A(), D())), Entrega(List(A(), A(), I(), A(), D(), A(), D())))))

  }


}


