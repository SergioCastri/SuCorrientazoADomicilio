package co.com.corrientazoadomicilio.servicios

import co.com.corrientazoadomicilio.vo.{Entrega, Instruccion, Ruta}

import scala.io.Source

sealed trait AlgebraArchivo {
  def leerArchivo(path : String) :Ruta
}
sealed trait  InterpreteAlgebraArchivo extends AlgebraArchivo {

  def leerArchivo(path: String):Ruta={
    val archivo = Source.fromFile(path)
    val lineas: List[String] =archivo.getLines.toList
    val listaChar = lineas.map(x=>x.toList)
    val listaInst = listaChar.map(x=>x.map(y => Instruccion.nuevaInstruccion(y)))
    val entrega = listaInst.map(x=> Entrega(x))
    val ruta = Ruta(entrega)
    ruta
  }

}

object InterpreteAlgebraArchivo extends InterpreteAlgebraArchivo