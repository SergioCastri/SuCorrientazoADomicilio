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
  def leerArchivo(archivos : List[String]):List[Ruta]
  def escribirArchivo(listaRuta : List[Ruta]):  Unit
}
sealed trait  InterpreteAlgebraArchivo extends AlgebraArchivo {
  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def leerArchivo(archivos : List[String]):List[Ruta]={

    val path: List[String] = archivos.flatMap(x=>List(s"/home/s4n/Documents/corrientazo-domicilio/src/test/resources/entradas/${x}"))
    val archivo = path.map(x=>Source.fromFile(x))
    val lineas: List[List[String]] = archivo.map(x=>x.getLines.toList)
    val listaChar: List[List[List[Char]]] = lineas.map(x=>x.map(x=>x.toList))
    val listaInst: List[List[List[Try[Instruccion]]]] = listaChar.map(x=>x.map(z=>z.map(y => Instruccion.nuevaInstruccion(y))))
    val lisIns2: List[List[List[Instruccion]]] = listaInst.map(y=>y.map(z=>z.flatMap(x=> x.toOption)))
    val entrega: List[List[Entrega]] = lisIns2.map(x=> x.map(y=> Entrega(y)))
    val ruta: List[Ruta] = entrega.map(x=>Ruta(x))

    escribirArchivo(ruta)
    ruta

    //escribirArchivo(ruta)

  }



  def escribirArchivo(listaRuta : List[Ruta]):  Unit = {
    val ide: List[Int] = Range(1, listaRuta.size +1).toList
    val dron: List[Dron] = ide.map(x=> Dron(10,  Posicion(Coordenada(0, 0), N()), x))
    val tupla: List[(Dron, Ruta)] = dron.zip(listaRuta)

      val futuro: List[Future[List[Future[Dron]]]] = tupla.map(x=> Future  {
        interpreteHacerEntrega.seguirRuta(x._2, x._1)
      }(context)
      )

    val re: Future[List[List[Future[Dron]]]] = Future.sequence(futuro)
    val re2: Future[List[Future[Posicion]]] = re.map(x=> x.flatten.map(y=> y.map(z=> Posicion(z.posicion.coor,z.posicion.car))))
    val ret: Future[List[Posicion]] = re2.flatMap(x=> Future.sequence(x))
    val queGonorrea = Await.result(ret, 10 seconds)
    val id: List[Int] = Range(1, listaRuta.size +1).toList

    val escritor: List[PrintWriter] = id.map(x=> new PrintWriter(new File( s"src/test/resources/salidas/out${x}.txt")))

    queGonorrea.foreach(y=> escritor.map(x=>x.write(s"${y}\n")))
   // ret.map(x=>x.foreach(y => escritor.map(z=> z.write(s"${y}\n"))))
    escritor.map(x=>x.close())
  }



}

object InterpreteAlgebraArchivo extends InterpreteAlgebraArchivo