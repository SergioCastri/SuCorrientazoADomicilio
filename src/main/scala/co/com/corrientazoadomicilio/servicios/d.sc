import co.com.corrientazoadomicilio.vo.{Entrega, Instruccion, Ruta}

import scala.io.Source

  val archivo = Source.fromFile("/home/s4n/Documents/corrientazo-domicilio/src/test/resources/prueba.txt")
  val lineas: List[String] =archivo.getLines.toList
  val listaChar = lineas.map(x=>x.toList)
  val listaInst = listaChar.map(x=>x.map(y => Instruccion.nuevaInstruccion(y)))
  val entrega = listaInst.map(x=> Entrega(x))
  val ruta = Ruta(entrega)



