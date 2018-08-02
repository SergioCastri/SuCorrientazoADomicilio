package co.com.corrientazoadomicilio

import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.servicios.InterpreteAlgebraArchivo
import co.com.corrientazoadomicilio.vo.{Posicion, Ruta}

import scala.concurrent.Future

object Main {
  def main(args: Array[String]): Unit = {
    val lista = List("in1.txt", "in2.txt")
    val ruta = InterpreteAlgebraArchivo.leerArchivo(lista)

  }


}
