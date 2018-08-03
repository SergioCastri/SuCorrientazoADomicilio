package co.com.corrientazoadomicilio.servicios
import java.io.{File, PrintWriter}
import java.util.concurrent.Executors

import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo.Cardinalidad.{E, N, O, S}
import co.com.corrientazoadomicilio.vo._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.language.postfixOps
import scala.util.Try


sealed trait AlgebraArchivo {
   def leerArchivo(archivos : List[String]):List[Ruta]
  protected def cardinalidadAString(cardinalidad: Cardinalidad):String
  def escribirArchivo(listaRuta : List[String]): Boolean
}

sealed trait InterpreteAlgebraArchivo extends AlgebraArchivo {

  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  protected def cardinalidadAString(cardinalidad: Cardinalidad):String = {
    cardinalidad match {
      case N() => "direccion Norte"
      case O() => "direccion Oeste"
      case S() => "direccion sur"
      case E() => "direccion Este"
    }
  }

   def leerArchivo(archivos : List[String]):List[Ruta]={
    val path= archivos.flatMap(x=>List(s"/home/s4n/Documents/corrientazo-domicilio/src/test/resources/entradas/${x}"))
    val archivo = path.map(x=>Source.fromFile(x))
    val lineas = archivo.map(x=>x.getLines.toList)
    val listaChar = lineas.map(x=>x.map(x=>x.toList))
    val listaInst= listaChar.map(x=>x.map(z=>z.map(y => Instruccion.nuevaInstruccion(y))))
    val lisIns2= listaInst.map(y=>y.map(z=>z.flatMap(x=> x.toOption)))
    val entrega = lisIns2.map(x=> x.map(y=> Entrega(y)))
    val ruta = entrega.map(x=>Ruta(x))
    ruta
  }

  def escribirArchivo(listaRuta : List[String]): Boolean = {
    val listaRutaDron = leerArchivo(listaRuta)
    val ide = Range(1, listaRuta.size +1).toList
    val dron= ide.map(x=> Dron(10,  Posicion(Coordenada(0, 0), N()), x))
    val tupla= dron.zip(listaRutaDron)
    val futuro = tupla.map(x=> Future{
      interpreteHacerDomicilio.seguirRuta(x._2, x._1)
    }(context))
    val a: Future[List[List[Try[Dron]]]] = Future.sequence(futuro)
    val id: List[Int] = Range(1, listaRuta.size +1).toList
    val escritor: List[PrintWriter] = id.map(x=> new PrintWriter(new File(s"src/test/resources/salidas/out${x}.txt")))
    val c: Future[List[(List[Try[Dron]], PrintWriter)]] = a.map(x=> x.zip(escritor))
    escritor.map(x=> x.write("== Reporte de entregas ==\n"))
    c.map(q=> q.map(t => {
      t._1.map(linea => linea.map(dron => t._2.write(s"(${dron.posicion.coor.a}, ${dron.posicion.coor.b}) ${cardinalidadAString(dron.posicion.car)}\n")))
      t._2.close()
    }))
    true
  }


}

object InterpreteAlgebraArchivo extends InterpreteAlgebraArchivo