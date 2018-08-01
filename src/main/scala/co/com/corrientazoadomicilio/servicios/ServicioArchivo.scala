package co.com.corrientazoadomicilio.servicios

import java.util.concurrent.Executors

import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo.Cardinalidad.N
import co.com.corrientazoadomicilio.vo._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Try}
sealed trait AlgebraArchivo {
 // def leerArchivo(path : String) :Try[Ruta]
  def escribirArchivo(ruta : Ruta):Future[List[Posicion]]
}
sealed trait  InterpreteAlgebraArchivo extends AlgebraArchivo {
  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def leerArchivo(nombre: String):Ruta={
    val path = s"/home/s4n/Documents/corrientazo-domicilio/src/test/resources/${nombre}"
    val archivo = Source.fromFile(path)
    val lineas: List[String] =archivo.getLines.toList
    val listaChar: List[List[Char]] = lineas.map(x=>x.toList)
    val listaInst: List[List[Try[Instruccion]]] = listaChar.map(x=>x.map(y => Instruccion.nuevaInstruccion(y)))
    val lisIns2: List[List[Instruccion]] = listaInst.map(y=>y.flatMap(x=> x.toOption))
    val entrega: List[Entrega] = lisIns2.map(x=> Entrega(x))
    val ruta = Ruta(entrega)
    ruta
  }



  def escribirArchivo(ruta : Ruta):Future[List[Posicion]] = {
    val posicionF = Posicion(Coordenada(0, 0), N())
    val dron = Dron(10, posicionF, 1)
    val a: Future[List[Future[Dron]]] = Future {
      val res: List[Future[Dron]] = interpreteHacerEntrega.seguirRuta(ruta, dron)
      res
    }(context)
    val retorno: Future[List[Posicion]] = a.map(x=> Future.sequence(x)).flatten
     .map(y=> y.map(z=> Posicion(z.posicion.coor, z.posicion.car)))
    retorno

  }



}

object InterpreteAlgebraArchivo extends InterpreteAlgebraArchivo