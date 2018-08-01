package co.com.corrientazoadomicilio.test
import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo._
import co.com.corrientazoadomicilio.servicios.{InterpreteAlgebraArchivo, interpreteDeAlgebraSeguirRuta, interpreteHacerEntrega}
import co.com.corrientazoadomicilio.vo
import org.scalatest.FunSuite
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class testSuite extends FunSuite {
  test("El dron puede moverse a la derecha") {
    val coordenada =  Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('N')
    val posicion = Posicion(coordenada, car)
    val dron =  Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.moverDerecha(dron)
    val res = Await.result(ruta, 10 seconds)
    assert(res == Dron(10,Posicion(Coordenada(0, 0), E()),1))
  }

  test("El dron puede moverse a la izquierda") {
    val coordenada = Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = Posicion(coordenada, car)
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.moverIzquierda(dron)
    val res = Await.result(ruta, 10 seconds)
    assert(res == Dron(10,Posicion(Coordenada(0, 0), E()),1))
  }

  test("El dron puede moverse hacia adelante") {
    val coordenada = Coordenada(0, 0)
    val car = Cardinalidad.nuevaCardinalidad('S')
    val posicion = Posicion(coordenada, car)
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteDeAlgebraSeguirRuta.avanzar(dron)
    val res = Await.result(ruta, 10 seconds)
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

  test ("un dron debe hacer todo el recorrido") {
    val posicionF = Posicion(Coordenada(0, 0), Cardinalidad.nuevaCardinalidad('N'))
    val dron = Dron(10, posicionF, 1)
    val ruta: Ruta = InterpreteAlgebraArchivo.leerArchivo("prueba.txt")
    val res3: List[Future[Dron]] = interpreteHacerEntrega.seguirRuta(ruta, dron)
    val res = res3.map(x=> Await.result(x, 10 seconds))
    assert(res == List(Dron(10,Posicion(Coordenada(-2,4),N()),1), Dron(10,Posicion(Coordenada(-1,3),S()),1), Dron(10,Posicion(Coordenada(0,0),O()),1), Dron(10,Posicion(Coordenada(-4,1),O()),1), Dron(10,Posicion(Coordenada(-5,1),S()),1), Dron(10,Posicion(Coordenada(-5,-1),O()),1), Dron(10,Posicion(Coordenada(-7,-1),E()),1), Dron(10,Posicion(Coordenada(-7,-3),E()),1), Dron(10,Posicion(Coordenada(-7,-2),N()),1), Dron(10,Posicion(Coordenada(-5,2),N()),1)) )
  }


  /*
  test ("el programa debe leer un archivo y convertirlo a ruta"){
    val ruta= InterpreteAlgebraArchivo.leerArchivo("prueba.txt")
    println(ruta)
    assert(ruta == Ruta(List(Entrega(List(A(), A(), A(), A(), I(), A(), A(), D())), Entrega(List(D(), D(), A(), I(), A(), D())), Entrega(List(A(), A(), I(), A(), D(), A(), D())), Entrega(List(A(), A(), A(), D(), A(), I(), A())), Entrega(List(D(), A(), A(), I(), A(), I(), A(), A())), Entrega(List(A(), A(), I(), D(), I(), A(), D(), D(), A())), Entrega(List(A(), A(), A(), D(), I(), D(), D(), A())), Entrega(List(D(), A(), A(), D(), A(), I(), I(), A())), Entrega(List(I(), I(), I(), I(), D(), A(), D(), D(), A(), A())), Entrega(List(A(), A(), D(), A(), A(), I(), A(), A())))))
  } */



}


