package co.com.corrientazoadomicilio.servicios

import java.io.{File, PrintWriter}
import java.util.concurrent.Executors

import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo.Cardinalidad.N
import co.com.corrientazoadomicilio.vo._
import javax.swing.text.AbstractWriter
import scala.concurrent.duration._


import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.language.postfixOps
import scala.util.{Failure, Try}



sealed trait AlgebraArchivo {
  def leerArchivo(path : String) :Ruta
  def escribirArchivo(ruta : Ruta): Unit
}
sealed trait  InterpreteAlgebraArchivo extends AlgebraArchivo {
  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def leerArchivo(nombre: String):Ruta={
    val path = s"/home/s4n/Documents/corrientazo-domicilio/src/test/resources/entradas/${nombre}"
    val archivo = Source.fromFile(path)
    val lineas: List[String] =archivo.getLines.toList
    val listaChar: List[List[Char]] = lineas.map(x=>x.toList)
    val listaInst: List[List[Try[Instruccion]]] = listaChar.map(x=>x.map(y => Instruccion.nuevaInstruccion(y)))
    val lisIns2: List[List[Instruccion]] = listaInst.map(y=>y.flatMap(x=> x.toOption))
    val entrega: List[Entrega] = lisIns2.map(x=> Entrega(x))
    val ruta = Ruta(entrega)


    //escribirArchivo(ruta)
    ruta
  }



  def escribirArchivo(ruta : Ruta):  Unit = {
    val posicionF = Posicion(Coordenada(0, 0), N())

    val a: Future[List[Future[Dron]]] = Future {
      val dron = Dron(10, posicionF, 1)
      //val id: Int = Thread.currentThread().getName.toInt
      val res: List[Future[Dron]] = interpreteHacerEntrega.seguirRuta(ruta, dron)
      res
    }(context)
    val retorno: Future[List[Posicion]] = a.map(x=> Future.sequence(x)).flatten
     .map(y=> y.map(z=> Posicion(z.posicion.coor, z.posicion.car)))
    retorno
    val queGonorrea: Seq[Posicion] = Await.result(retorno, 10 seconds)
    val escritor : PrintWriter = new PrintWriter(new File("src/test/resources/salidas/out1.txt"))
    queGonorrea.foreach(x=> escritor.write(s"${x}\n"))
    //retorno.map(x=>x.foreach(y => escritor.write(s"${y}\n")))
    escritor.close()
  }



}

object InterpreteAlgebraArchivo extends InterpreteAlgebraArchivo