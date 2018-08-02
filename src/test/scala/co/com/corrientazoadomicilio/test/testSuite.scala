package co.com.corrientazoadomicilio.test
import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.servicios._
import co.com.corrientazoadomicilio.vo.Cardinalidad.{E, N, O, S}
import co.com.corrientazoadomicilio.vo._
import org.scalatest.FunSuite

import scala.language.postfixOps
import scala.util.Try

class testSuite extends FunSuite {
  test("El dron puede moverse a la derecha") {
    val coordenada =  Coordenada(0, 0)

    val posicion = Posicion(coordenada, N())
    val dron =  Dron(10, posicion, 1)
    val ruta: Try[Dron] = interpreteMovimientoDron.moverDerecha(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, 0), E()),1)))
  }

  test("El dron puede moverse a la izquierda") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada,  S())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.moverIzquierda(dron)
    assert(ruta  == Try(Dron(10,Posicion(Coordenada(0, 0), E()),1)))
  }

  test("El dron puede moverse hacia adelante") {
    val coordenada = Coordenada(0, 0)
    val posicion = Posicion(coordenada, S())
    val dron = Dron(10, posicion, 1)
    val ruta = interpreteMovimientoDron.avanzar(dron)
    assert(ruta == Try(Dron(10,Posicion(Coordenada(0, -1), S()),1)))
  }

  test("un dron debe moverse dependiendo de la intruccion") {
    val ins: Try[Instruccion] = Instruccion.nuevaInstruccion('A')
    val posicion = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicion, 1)
    val res: Try[Dron] = ins.map(x=>interpreteHacerDomicilio.recorrerPorInstruccion(dron, x)).flatten
    assert(res  == Try(Dron(10,Posicion(Coordenada(0, 1), N()),1)))
  }

  test("un dron debe moverse dependiendo de la intruccion 2") {
    val ins = Instruccion.nuevaInstruccion('I')
    val posicion = Posicion(Coordenada(0, 0), O())
    val dron = Dron(10, posicion, 1)
    val res: Try[Dron] = ins.map(x=>interpreteHacerDomicilio.recorrerPorInstruccion(dron, x)).flatten
    assert(res == Try(Dron(10,Posicion(Coordenada(0, 0), S()),1)))
  }


  test("debe ocurrir un failure si la instruccion es incorrecta") {
    val ins: Try[Instruccion] = Instruccion.nuevaInstruccion('f')
    val posicion = Posicion(Coordenada(0, 0), O())
    val dron = Dron(10, posicion, 1)
    val res: Try[Dron] = ins.map(x=>interpreteHacerDomicilio.recorrerPorInstruccion(dron, x)).flatten
    assert(res.isFailure)
  }

    test("Un dron debe hacer toda una entrega") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val lisIns: List[Try[Instruccion]] = List(Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('I'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('D'))
      val lisIns2: List[Instruccion] = lisIns.flatMap(x=> x.toOption)
      val dron = Dron(10, posicionF, 1)
      val entrega: Entrega = Entrega(lisIns2)
      val res: Try[Dron] = interpreteHacerDomicilio.seguirEntrega(entrega, dron)
      assert(res == Try(Dron(10,Posicion(Coordenada(-2, 4), N()),1)))

    }


    test("Un dron debe hacer toda una entrega y si el archivo tiene caracteres malos, descarta dichos caracteres") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val lisIns: List[Try[Instruccion]] = List(Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('I'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('t'))
      val lisIns2: List[Instruccion] = lisIns.flatMap(x=> x.toOption)
      val dron = Dron(10, posicionF, 1)
      val entrega: Entrega = Entrega(lisIns2)
      val res = interpreteHacerDomicilio.seguirEntrega(entrega, dron)
      assert(res == Try(Dron(10,Posicion(Coordenada(-2, 4), O()),1)))

    }



    test ("un dron debe hacer todo el recorrido con archivo perfecto") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val dron = Dron(10, posicionF, 1)
      val archivos = List("in1.txt")
      val ruta: List[Ruta] = InterpreteAlgebraArchivo.leerArchivo(archivos)
      val res3: List[Try[Dron]] = interpreteHacerDomicilio.seguirRuta(ruta.head, dron)
      assert(res3 == List(Try(Dron(10,Posicion(Coordenada(-2,4),N()),1)), Try(Dron(10,Posicion(Coordenada(-1,3), S()),1)), Try(Dron(10,Posicion(Coordenada(0,0),O()),1))))
    }


    test ("un dron debe hacer todo el recorrido con archivo no perfecto") {
      val posicionF = Posicion(Coordenada(0, 0), N())
      val dron = Dron(10, posicionF, 1)
      val archivos = List("in2.txt")
      val ruta = InterpreteAlgebraArchivo.leerArchivo(archivos)
      val res3: List[Try[Dron]] = interpreteHacerDomicilio.seguirRuta(ruta.head, dron)
      assert(res3 == List(Try(Dron(10,Posicion(Coordenada(-2,4),N()),1)), Try(Dron(10,Posicion(Coordenada(-1,3), S()),1)), Try(Dron(10,Posicion(Coordenada(0,0),O()),1))))
    }

  test("no se debe crear un dron si se sale de la coordenada"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val archivos = List("in3.txt")
    val ruta = InterpreteAlgebraArchivo.leerArchivo(archivos)
    val res3: List[Try[Dron]] = interpreteHacerDomicilio.seguirRuta(ruta.head, dron)
    println(res3)
    assert(res3.last.isFailure)
  }

  test("se debe retornar la posicion final de las entregas sin  salirse de la grilla"){
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val archivos = List("in1.txt","in2.txt", "in3.txt", "in4.txt", "in5.txt", "in6.txt")
    val ruta = InterpreteAlgebraArchivo.escribirArchivo(archivos)
    println(ruta)
  }


  test ("el programa debe leer un archivo y convertirlo a ruta"){
    val archivos = List("in6.txt")
    val ruta = InterpreteAlgebraArchivo.leerArchivo(archivos)
     assert(ruta == List(Ruta(List(Entrega(List(A(), A(), D()))))))
  }
}


