package co.com.corrientazoadomicilio.test
import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.servicios._
import co.com.corrientazoadomicilio.vo.Cardinalidad.{E, N, O, S}
import co.com.corrientazoadomicilio.vo._
import org.scalatest.FunSuite
import scala.language.postfixOps
import scala.util.Try

class testSuite extends FunSuite {

  test("El dron puede moverse a la derecha desde el Norte") {
    val coordenada =  Coordenada(0, 0)
    val posicion = Posicion(coordenada, N())
    val dron =  Dron(10, posicion, 1)
    val ruta: Try[Dron] = interpreteMovimientoDron.moverDerecha(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 0), E()),1)))
  }


  test("El dron puede moverse a la izquierda desde el norte") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada,  N())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.moverIzquierda(dron)
    assert(ruta  == Try(Dron(10,Posicion(Coordenada(0, 0), O()),1)))
  }

  test("El dron puede moverse hacia adelante desde el norte") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, N())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.avanzar(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 1), N()),1)))
  }
  test("El dron puede moverse a la derecha desde el sur") {
    val coordenada =  Coordenada(0, 0)
    val posicion = Posicion(coordenada, S())
    val dron =  Dron(10, posicion, 1)
    val ruta: Try[Dron] = interpreteMovimientoDron.moverDerecha(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 0), O()),1)))
  }


  test("El dron puede moverse a la izquierda desde el sur") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada,  S())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.moverIzquierda(dron)
    assert(ruta  == Try(Dron(10,Posicion(Coordenada(0, 0), E()),1)))
  }

  test("El dron puede moverse hacia adelante desde el sur") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, S())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.avanzar(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, -1), S()),1)))
  }

  test("El dron puede moverse a la derecha desde el oeste") {
    val coordenada =  Coordenada(0, 0)
    val posicion = Posicion(coordenada, O())
    val dron =  Dron(10, posicion, 1)
    val ruta: Try[Dron] = interpreteMovimientoDron.moverDerecha(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 0), N()),1)))
  }


  test("El dron puede moverse a la izquierda desde el oeste") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, O())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.moverIzquierda(dron)
    assert(ruta  == Try(Dron(10,Posicion(Coordenada(0, 0), S()),1)))
  }

  test("El dron puede moverse hacia adelante desde el oeste") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, O())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.avanzar(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(-1, 0), O()),1)))
  }

  test("El dron puede moverse a la derecha desde el este") {
    val coordenada =  Coordenada(0, 0)
    val posicion = Posicion(coordenada, E())
    val dron =  Dron(10, posicion, 1)
    val ruta: Try[Dron] = interpreteMovimientoDron.moverDerecha(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 0), S()),1)))
  }


  test("El dron puede moverse a la izquierda desde el este") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, E())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.moverIzquierda(dron)
    assert(ruta  == Try(Dron(10,Posicion(Coordenada(0, 0), N()),1)))
  }

  test("El dron puede moverse hacia adelante desde el este") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, E())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.avanzar(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(1, 0), E()),1)))
  }

    test ("un dron debe hacer todo el recorrido con archivo perfecto") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val dron = Dron(10, posicionF, 1)
      val ruta: List[Ruta] = List(Ruta(List(Entrega(List(A(), A(), A(), A(), I(), A(), A(), D())), Entrega(List(D(), D(), A(), I(), A(), D())), Entrega(List(A(), A(), I(), A(), D(), A(), D())))))
      val res3: List[Try[Dron]] = interpreteHacerDomicilio.seguirRuta(ruta.head, dron)
      assert(res3 == List(Try(Dron(10,Posicion(Coordenada(-2,4),N()),1)), Try(Dron(10,Posicion(Coordenada(-1,3), S()),1)), Try(Dron(10,Posicion(Coordenada(0,0),O()),1))))
    }


    test ("un dron debe hacer todo el recorrido con archivo no perfecto") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val dron = Dron(10, posicionF, 1)
      val archivos = List("in2.txt")
      val ruta = InterpreteAlgebraArchivo.escribirArchivo(archivos)
      assert(ruta)
    }



  test("no se debe crear un dron si se sale de la coordenada"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val ruta: List[Ruta] = List(Ruta(List(Entrega(List(A(), A(), A(), A(), I(), A(), A(), D(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A())))))
    val res3: List[Try[Dron]] = interpreteHacerDomicilio.seguirRuta(ruta.head, dron)
    assert(res3.last.isFailure)
  }

  test("EL PROGRAMA DEBE EJECUTAR ADECUDAMENTE CUALQUEIR CANTIDAD DE ARCHIVOS"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val archivos = List("in1.txt","in2.txt","in3.txt","in4.txt","in5.txt","in6.txt","in7.txt","in8.txt","in9.txt","in10.txt","in11.txt","in12.txt","in13.txt","in14.txt","in15.txt","in16.txt","in17.txt","in18.txt","in19.txt","in20.txt","in21.txt","in22.txt","in23.txt","in24.txt")
    val ruta = InterpreteAlgebraArchivo.escribirArchivo(archivos)
    assert(ruta)
  }

  test("El programa debe leer archivos vacios y no sacar error"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val archivos = List("in8.txt")
    val ruta = InterpreteAlgebraArchivo.escribirArchivo(archivos)
    assert(ruta)
  }


  test("si un archivo viene sin instrcciones se genera un reporte vacio"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val archivos = List("in9.txt")
    val ruta = InterpreteAlgebraArchivo.escribirArchivo(archivos)
    assert(ruta)
  }

}


